package com.liangxunwang.unimanager.service.app;

import com.liangxunwang.unimanager.alipay.AlipayConfig;
import com.liangxunwang.unimanager.alipay.OrderInfoUtil2_0;
import com.liangxunwang.unimanager.dao.AppOrderMakeDao;
import com.liangxunwang.unimanager.dao.EmpDao;
import com.liangxunwang.unimanager.dao.HyrzDao;
import com.liangxunwang.unimanager.dao.MessagesDao;
import com.liangxunwang.unimanager.model.*;
import com.liangxunwang.unimanager.service.FindService;
import com.liangxunwang.unimanager.service.SaveService;
import com.liangxunwang.unimanager.service.ServiceException;
import com.liangxunwang.unimanager.service.UpdateService;
import com.liangxunwang.unimanager.util.*;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/8/14.
 */
@Service("appOrderMakeService")
public class AppOrderMakeService implements SaveService,UpdateService,FindService {
    @Autowired
    @Qualifier("appOrderMakeDao")
    private AppOrderMakeDao appOrderMakeSaveDao;

    @Autowired
    @Qualifier("empDao")
    private EmpDao empDao;

    //保存订单
    @Override
    public Object save(Object object) throws ServiceException {
        Order order = (Order) object;

        //商品总额，用于插入订单金额
        String out_trade_no = UUIDFactory.random();//订单总金额的id
        ShoppingTrade shoppingTrade = new ShoppingTrade();
        shoppingTrade.setOut_trade_no(out_trade_no);
        shoppingTrade.setPay_status("0");
        shoppingTrade.setDateline(System.currentTimeMillis() + "");
        shoppingTrade.setTrade_prices(order.getPayable_amount());

        //保存总订单--和支付宝一致
        appOrderMakeSaveDao.saveTrade(shoppingTrade);

        order.setOrder_no(UUIDFactory.random());
        order.setCreate_time(System.currentTimeMillis() + "");
        order.setOut_trade_no(out_trade_no);

        //保存订单
        appOrderMakeSaveDao.saveList(order);

        String notify_url = "";
        if("0".equals(order.getIs_dxk_order())){
//                private String is_dxk_order;//0认证服务费  1诚信保证金
            notify_url =  Constants.ZFB_NOTIFY_URL_HY;
        }else{
            notify_url =  Constants.ZFB_NOTIFY_URL_CX;
        }

        boolean rsa2 = (AlipayConfig.RSA2_PRIVATE.length() > 0);
        Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap(AlipayConfig.APPID, rsa2, order.getPayable_amount(), out_trade_no , notify_url);
        String orderParam = OrderInfoUtil2_0.buildOrderParam(params);

        String privateKey = rsa2 ? AlipayConfig.RSA2_PRIVATE : AlipayConfig.RSA_PRIVATE;
        String sign = OrderInfoUtil2_0.getSign(params, privateKey, rsa2);
        final String orderInfo = orderParam + "&" + sign;

        try {
            // 仅需对sign 做URL编码
            sign = URLEncoder.encode(sign, "UTF-8");
            return new OrderInfoAndSign(orderInfo, sign, out_trade_no);
        } catch (UnsupportedEncodingException e) {
            throw new ServiceException("ISWRONG");
        }
    }


    @Autowired
    @Qualifier("hyrzDao")
    private HyrzDao hyrzDao;

    @Autowired
    @Qualifier("messagesDao")
    private MessagesDao messagesDao;

    //更新订单状态
    @Override
    public Object update(Object object) {
        if (object instanceof Order){
            //跟新主订单和分订单状态
            Order order = (Order) object;
            List<Order> orders = appOrderMakeSaveDao.findOrderByTradeNo(order.getOut_trade_no());
            if(orders != null && orders.size() > 0){
                Order order1 = orders.get(0);
                if(order1 != null){
                    if(!"2".equals(order1.getStatus())){
                        //更新主订单
                        appOrderMakeSaveDao.updateTradeById(order.getOut_trade_no());
                        order.setPay_time(System.currentTimeMillis() + "");
                        //更新分订单
                        appOrderMakeSaveDao.updateOrderById(order);
                        //更新会员到期日期和会员认证状态
                        empDao.updateRzstate2(order1.getEmpid(), "1");

                        Map<String, Object> maphyrz = new HashMap<>();
                        maphyrz.put("empid", order1.getEmpid());
                        maphyrz.put("index", 0);
                        maphyrz.put("size", 10);
                        List<HappyHandHyrz> lists = hyrzDao.lists(maphyrz);
                        if(lists != null && lists.size()>0)
                        {
                            //说明数据库有认证的数据  更新
                            HappyHandHyrz happyHandHyrz =lists.get(0);
                            Object[] ob = DateUtil.getDayInterval(System.currentTimeMillis(), -1);
                            long endtime = (long) ob[0];
                            happyHandHyrz.setEndtime(String.valueOf(endtime));

                            happyHandHyrz.setIs_use("1");
                            hyrzDao.update(happyHandHyrz);
                        }else{
                            //添加会员认证数据
                            HappyHandHyrz happyHandHyrz = new HappyHandHyrz();
                            Object[] ob = DateUtil.getDayInterval(System.currentTimeMillis(), -1);
                            long endtime = (long) ob[0];
                            happyHandHyrz.setEndtime(String.valueOf(endtime));

                            happyHandHyrz.setIs_use("1");
                            happyHandHyrz.setStarttime(System.currentTimeMillis() + "");
                            happyHandHyrz.setEmpid(order1.getEmpid());
                            happyHandHyrz.setHyrzid(UUIDFactory.random());
                            hyrzDao.save(happyHandHyrz);
                        }
                        //会员认证成功之后，发送系统消息
                        //todo
                        Emp emp = empDao.findById(order1.getEmpid());
                        if(emp != null){
                            HappyHandMessage happyHandMessage = new HappyHandMessage();
                            happyHandMessage.setMsgid(UUIDFactory.random());
                            happyHandMessage.setDateline(System.currentTimeMillis() + "");
                            happyHandMessage.setTitle("恭喜你成为认证会员，快去寻找幸福吧!");
                            happyHandMessage.setEmpid(order1.getEmpid());
                            messagesDao.save(happyHandMessage);

                            if(!StringUtil.isNullOrEmpty(emp.getChannelId())){
                                BaiduPush.PushMsgToSingleDevice(Integer.parseInt(emp.getDeviceType()), "系统消息", "恭喜你成为认证会员，快去寻找幸福吧!", "1", emp.getChannelId());
                            }
                        }
                    }
                }
            }

        }
        return null;
    }


    @Override
    public Object findById(Object object) throws ServiceException {
        String order_no = (String) object;
        Order record = appOrderMakeSaveDao.findOrderByOrderNo(order_no);

        if(!StringUtil.isNullOrEmpty(record.getCreate_time())){
            record.setCreate_time(RelativeDateFormat.format(Long.parseLong(record.getCreate_time())));
        }
        if(!StringUtil.isNullOrEmpty(record.getPay_time())){
            record.setPay_time(RelativeDateFormat.format(Long.parseLong(record.getPay_time())));
        }
        return record;
    }
}
