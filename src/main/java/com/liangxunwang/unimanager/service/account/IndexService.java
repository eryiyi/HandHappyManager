package com.liangxunwang.unimanager.service.account;

import com.liangxunwang.unimanager.dao.EmpDao;
import com.liangxunwang.unimanager.dao.HyrzDao;
import com.liangxunwang.unimanager.dao.LoginEmpDao;
import com.liangxunwang.unimanager.dao.OrderDao;
import com.liangxunwang.unimanager.service.ListService;
import com.liangxunwang.unimanager.service.ServiceException;
import com.liangxunwang.unimanager.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhl on 2015/3/3.
 */
@Service("indexService")
public class IndexService implements ListService {
    @Autowired
    @Qualifier("empDao")
    private EmpDao empDao;

    @Autowired
    @Qualifier("orderDao")
    private OrderDao orderDao;

    @Autowired
    @Qualifier("hyrzDao")
    private HyrzDao hyrzDao;

    @Autowired
    @Qualifier("loginEmpDao")
    private LoginEmpDao loginEmpDao;

    @Override
    public Object list(Object object) throws ServiceException {
        Map<String, Object> mapEmpCountAll = new HashMap<String, Object>();

        //总会员数数量
        long empCountAll = empDao.count(mapEmpCountAll);
        //今日注册会员数量
        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("startTime",  DateUtil.getStartDay());
        map1.put("endTime",  DateUtil.getEndDay());
        long empCountDay = empDao.countDay(map1);

        //查询总输入
        Map<String, Object> mapOrderCountAll = new HashMap<String, Object>();
        mapOrderCountAll.put("status",  "2");
        Float orderAmountAll = orderDao.income(mapOrderCountAll);
        //查询今天的收入
        Map<String, Object> mapOrderCountDay = new HashMap<String, Object>();
        mapOrderCountDay.put("status",  "2");
        mapOrderCountDay.put("startTime",  DateUtil.getStartDay());
        mapOrderCountDay.put("endTime",  DateUtil.getEndDay());
        Float orderAmountDay = orderDao.incomeDay(mapOrderCountDay);
        //查询会员费
        Map<String, Object> mapOrderCountAllHy = new HashMap<String, Object>();
        mapOrderCountAllHy.put("status",  "2");
        mapOrderCountAllHy.put("is_dxk_order",  "0");
        Float orderAmountAllHy = orderDao.income(mapOrderCountAllHy);
        //查询诚信费
        Map<String, Object> mapOrderCountAllCx = new HashMap<String, Object>();
        mapOrderCountAllCx.put("status",  "2");
        mapOrderCountAllCx.put("is_dxk_order",  "1");
        Float orderAmountAllCx = orderDao.income(mapOrderCountAllCx);

        //体验会员人数统计
        Map<String, Object> mapEmpCountAll1 = new HashMap<String, Object>();
        mapEmpCountAll1.put("rzstate2", "2");
        //体验会员总数量
        long empCountAllTy = empDao.count(mapEmpCountAll1);
        //今日体验会员数量
        Map<String, Object> map11 = new HashMap<String, Object>();
        map11.put("startTime",  DateUtil.getStartDay());
        map11.put("endTime",  DateUtil.getEndDay());
        map11.put("rzstate2",  "2");
        long empCountDayTy = empDao.countDay(map11);

        //会员认证人数统计
        Map<String, Object> mapEmpCountAll12 = new HashMap<String, Object>();
        mapEmpCountAll12.put("rzstate2", "1");
        //认证会员总数量
        long empCountAllRz = empDao.count(mapEmpCountAll12);
        //今日认证会员数量
        Map<String, Object> map111 = new HashMap<String, Object>();
        map111.put("startTime",  DateUtil.getStartDay());
        map111.put("endTime",  DateUtil.getEndDay());
        map111.put("status",  "2");
        map111.put("is_dxk_order",  "0");
//        long empCountDayRz = hyrzDao.countDay(map111);
        long empCountDayRz = orderDao.countDay(map111);

        //在线会员统计
        Map<String, Object> mapon1 = new HashMap<String, Object>();
        mapon1.put("is_login", "0");
        long lon1 = loginEmpDao.count(mapon1);
        //昨日会员登录人数统计
        Map<String, Object> mapon11 = new HashMap<String, Object>();
        mapon11.put("logindate",  DateUtil.getDateAndTimeTwo2());//昨天的日期
        long lon11 = loginEmpDao.count(mapon11);

        //诚信会员总数
        Map<String, Object> mapEmpCountAll13 = new HashMap<String, Object>();
        mapEmpCountAll13.put("rzstate3", "1");
        //诚信会员总数量
        long empCountAllCx = empDao.count(mapEmpCountAll13);

        List<String> list = new ArrayList<String>();
        list.add(String.valueOf(empCountAll));
        list.add(String.valueOf(empCountDay));

        list.add(String.valueOf(orderAmountAll==null?0:orderAmountAll));
        list.add(String.valueOf(orderAmountDay==null?0:orderAmountDay));
        list.add(String.valueOf(orderAmountAllHy==null?0:orderAmountAllHy));
        list.add(String.valueOf(orderAmountAllCx==null?0:orderAmountAllCx));
        list.add(String.valueOf(empCountAllTy));
        list.add(String.valueOf(empCountDayTy));
        list.add(String.valueOf(empCountAllRz));
        list.add(String.valueOf(empCountDayRz));
        list.add(String.valueOf(lon1));
        list.add(String.valueOf(lon11));
        list.add(String.valueOf(empCountAllCx));



        return list;
    }


}
