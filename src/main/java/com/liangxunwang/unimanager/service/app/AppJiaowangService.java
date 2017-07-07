package com.liangxunwang.unimanager.service.app;


import com.liangxunwang.unimanager.chat.impl.EasemobChatGroup;
import com.liangxunwang.unimanager.dao.EmpDao;
import com.liangxunwang.unimanager.dao.EmpGroupsDao;
import com.liangxunwang.unimanager.dao.JiaowangDao;
import com.liangxunwang.unimanager.dao.MessagesDao;
import com.liangxunwang.unimanager.model.Emp;
import com.liangxunwang.unimanager.model.EmpGroups;
import com.liangxunwang.unimanager.model.HappyHandJw;
import com.liangxunwang.unimanager.model.HappyHandMessage;
import com.liangxunwang.unimanager.query.JiaowangQuery;
import com.liangxunwang.unimanager.service.*;
import com.liangxunwang.unimanager.util.BaiduPush;
import com.liangxunwang.unimanager.util.Constants;
import com.liangxunwang.unimanager.util.StringUtil;
import com.liangxunwang.unimanager.util.UUIDFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhl on 2015/3/3.
 */
@Service("appJiaowangService")
public class AppJiaowangService implements SaveService,ListService,UpdateService,DeleteService , ExecuteService{
    @Autowired
    @Qualifier("jiaowangDao")
    private JiaowangDao jiaowangDao;

    @Autowired
    @Qualifier("empDao")
    private EmpDao empDao;

    @Autowired
    @Qualifier("messagesDao")
    private MessagesDao messagesDao;

    @Autowired
    @Qualifier("empGroupsDao")
    private EmpGroupsDao empGroupsDao;

    @Override
    public Object save(Object object) throws ServiceException {
        HappyHandJw happyHandJw = (HappyHandJw) object;
        if(StringUtil.isNullOrEmpty(happyHandJw.getEmpid1())){
            throw new ServiceException("empId1Null");
        }
        if(StringUtil.isNullOrEmpty(happyHandJw.getEmpid2())){
            throw new ServiceException("empId2Null");
        }
        //先判断是否已经有两个人的交往申请了
        Map<String, Object> map = new HashMap<>();
        map.put("empid1", happyHandJw.getEmpid1() );
        map.put("empid2", happyHandJw.getEmpid2() );
        map.put("is_check", "0" );
        List<HappyHandJw> list1 = jiaowangDao.lists(map);
        if(list1 != null && list1.size() > 0){
            throw new ServiceException("hasApply");//对方尚未确认，请耐心等待
        }
        //第二层判断
        Emp emp1 = empDao.findById(happyHandJw.getEmpid1());
        if(emp1 != null && "2".equals(emp1.getState())){
            throw new ServiceException("youHasJwdx");//您已经有交往对象了
        }
        Emp emp2= empDao.findById(happyHandJw.getEmpid2());
        if(emp2 != null && "2".equals(emp2.getState())){
            throw new ServiceException("sheHasJwdx");//对方已经有交往对象了
        }

        happyHandJw.setApplytime(System.currentTimeMillis() + "");
        happyHandJw.setIs_check("0");
        happyHandJw.setJwid(UUIDFactory.random());
        jiaowangDao.save(happyHandJw);
        //通知对方 有人想与您交往

        if(!StringUtil.isNullOrEmpty(emp2.getChannelId())){
            BaiduPush.PushMsgToSingleDevice(Integer.parseInt(emp2.getDeviceType()), "交往消息", emp1.getNickname()+"请求与您交往", "4", emp2.getChannelId());
        }
        return null;
    }

    @Override
    public Object list(Object object) throws ServiceException {
        JiaowangQuery query = (JiaowangQuery) object;
        Map<String, Object> map = new HashMap<>();
        if(!StringUtil.isNullOrEmpty(query.getEmpid1())){
            map.put("empid1", query.getEmpid1());
        }
        if(!StringUtil.isNullOrEmpty(query.getEmpid2())){
            map.put("empid2", query.getEmpid2());
        }
        if(!StringUtil.isNullOrEmpty(query.getIs_check())){
            map.put("is_check", query.getIs_check());
        }
        List<HappyHandJw> list = jiaowangDao.lists(map);
        for(HappyHandJw member:list){
            if(member != null){
                if(!StringUtil.isNullOrEmpty(member.getEmpid1Cover())){
                    if (member.getEmpid1Cover().startsWith("upload")) {
                        member.setEmpid1Cover(Constants.URL + member.getEmpid1Cover());
                    }else {
                        member.setEmpid1Cover(Constants.QINIU_URL + member.getEmpid1Cover());
                    }
                }
                if(!StringUtil.isNullOrEmpty(member.getEmpid2Cover())){
                    if (member.getEmpid2Cover().startsWith("upload")) {
                        member.setEmpid2Cover(Constants.URL + member.getEmpid2Cover());
                    }else {
                        member.setEmpid2Cover(Constants.QINIU_URL + member.getEmpid2Cover());
                    }
                }
            }
        }
        return list;
    }

    private EasemobChatGroup easemobChatGroup = new EasemobChatGroup();
    //处理交往申请
    @Override
    public Object update(Object object) {
        HappyHandJw happyHandJw = (HappyHandJw) object;
        if(StringUtil.isNullOrEmpty(happyHandJw.getIs_check())){
            throw new ServiceException("ischecknull");
        }
        if(StringUtil.isNullOrEmpty(happyHandJw.getJwid())){
            throw new ServiceException("jwidisnull");
        }
        if("1".equals(happyHandJw.getIs_check())){
            //统一交往  判断
            //判断对方是否已经在交往中了
            Emp emp1 = empDao.findById(happyHandJw.getEmpid1());
            if(emp1 != null && "2".equals(emp1.getState())){
                //对方处于交往状态，查看是否是我俩交往中
                Map<String, Object> mapismine = new HashMap<>();
                mapismine.put("empid1", happyHandJw.getEmpid1());
                mapismine.put("empid2", happyHandJw.getEmpid2());
                mapismine.put("is_check", "1");
                List<HappyHandJw> ismines = jiaowangDao.lists(mapismine);
                if(ismines != null && ismines.size()>0){
                    //说明我俩是交往对象 删除这条请求信息
                    jiaowangDao.deleteById(happyHandJw);
                    return 200;
                }else{
                    //说明对方在交往中 交往对象不是我  所以删除我俩之间的交往请求
                    happyHandJw.setIs_check("2");
                    jiaowangDao.updateOther(happyHandJw);
                    throw new ServiceException("sheHasJwdx");//对方已经有交往对象了
                }

            }
            Emp emp2= empDao.findById(happyHandJw.getEmpid2());
            if(emp2 != null && "2".equals(emp2.getState())){
                //对方处于交往状态，查看是否是我俩交往中
                Map<String, Object> mapismine = new HashMap<>();
                mapismine.put("empid1", happyHandJw.getEmpid1());
                mapismine.put("empid2", happyHandJw.getEmpid2());
                mapismine.put("is_check", "1");
                List<HappyHandJw> ismines = jiaowangDao.lists(mapismine);
                if(ismines != null && ismines.size()>0){
                    //说明我俩是交往对象 删除这条请求信息
                    jiaowangDao.deleteById(happyHandJw);
                    return 200;
                }else{
                    //说明我在交往中，删除交往请求
                    happyHandJw.setIs_check("2");
                    jiaowangDao.updateOther(happyHandJw);
                    throw new ServiceException("youHasJwdx");//您已经有交往对象了
                }

            }
            happyHandJw.setStartime(System.currentTimeMillis()+"");
        }
        happyHandJw.setAccepttime(System.currentTimeMillis()+"");//处理时间

        jiaowangDao.update(happyHandJw);

        if("1".equals(happyHandJw.getIs_check())){
            //说明接受交往申请了
            HappyHandJw happyHandJw1 = new HappyHandJw();
            happyHandJw1.setJwid(UUIDFactory.random());
            happyHandJw1.setIs_check("1");
            happyHandJw1.setAccepttime(System.currentTimeMillis() + "");
            happyHandJw1.setApplytime(System.currentTimeMillis()+"");
            happyHandJw1.setEmpid1(happyHandJw.getEmpid2());
            happyHandJw1.setEmpid2(happyHandJw.getEmpid1());
            jiaowangDao.save(happyHandJw1);
            //更新会员表 申请人和被申请人都要处理
            empDao.updateState(happyHandJw.getEmpid1(), "2");
            empDao.updateState(happyHandJw.getEmpid2(), "2");
            //保存系统消息
            //todo
            Emp emp1 = empDao.findById(happyHandJw.getEmpid1());
            Emp emp2= empDao.findById(happyHandJw.getEmpid2());

            HappyHandMessage happyHandMessage1 = new HappyHandMessage();
            happyHandMessage1.setMsgid(UUIDFactory.random());
            happyHandMessage1.setDateline(System.currentTimeMillis() + "");
            happyHandMessage1.setTitle("恭喜你与"+emp2.getNickname()+"交往，期待你们传来好消息！");
            happyHandMessage1.setEmpid(happyHandJw.getEmpid1());
            messagesDao.save(happyHandMessage1);

            HappyHandMessage happyHandMessage2 = new HappyHandMessage();
            happyHandMessage2.setMsgid(UUIDFactory.random());
            happyHandMessage2.setDateline(System.currentTimeMillis() + "");
            happyHandMessage2.setTitle("恭喜你与"+emp1.getNickname()+"交往，期待你们传来好消息！");
            happyHandMessage2.setEmpid(happyHandJw.getEmpid2());
            messagesDao.save(happyHandMessage2);

            //加群
            easemobChatGroup.addSingleUserToChatGroup(Constants.DEFAULT_GROUP_ID2, emp1.getEmpid());
            easemobChatGroup.addSingleUserToChatGroup(Constants.DEFAULT_GROUP_ID2, emp2.getEmpid());

            EmpGroups empGroups1 = new EmpGroups();
            empGroups1.setEmpgroupsid(UUIDFactory.random());
            empGroups1.setDateline(System.currentTimeMillis() + "");
            empGroups1.setGroupid(Constants.DEFAULT_GROUP_ID2);
            empGroups1.setEmpid(emp1.getEmpid());

            Map<String, Object> map = new HashMap<>();
            map.put("empid", empGroups1.getEmpid());
            map.put("groupid", empGroups1.getGroupid());
            List<EmpGroups> lists = empGroupsDao.findById(map);
            if(lists != null && lists.size()>0){
            }else {
                empGroupsDao.save(empGroups1);
            }

            EmpGroups empGroups2 = new EmpGroups();
            empGroups2.setEmpgroupsid(UUIDFactory.random());
            empGroups2.setDateline(System.currentTimeMillis() + "");
            empGroups2.setGroupid(Constants.DEFAULT_GROUP_ID2);
            empGroups2.setEmpid(emp2.getEmpid());

            Map<String, Object> map1 = new HashMap<>();
            map1.put("empid", empGroups2.getEmpid());
            map1.put("groupid", empGroups2.getGroupid());
            List<EmpGroups> lists1 = empGroupsDao.findById(map1);
            if(lists1 != null && lists1.size()>0){
            }else {
                empGroupsDao.save(empGroups2);
            }

            if(!StringUtil.isNullOrEmpty(emp1.getChannelId())){
                BaiduPush.PushMsgToSingleDevice(Integer.parseInt(emp1.getDeviceType()), "交往消息", "恭喜找到了交往对象！" , "7", emp1.getChannelId());
            }
            if(!StringUtil.isNullOrEmpty(emp2.getChannelId())){
                BaiduPush.PushMsgToSingleDevice(Integer.parseInt(emp2.getDeviceType()), "交往消息", "恭喜找到了交往对象！" , "7", emp2.getChannelId());
            }

        }else{
            //拒绝请求 要通知对方
            Emp emp1 = empDao.findById(happyHandJw.getEmpid1());//对方
            Emp emp2 = empDao.findById(happyHandJw.getEmpid2());//我的
            if(emp1 != null && "2".equals(emp1.getState())){
                //对方处于交往状态，查看是否是我俩交往中
                Map<String, Object> mapismine = new HashMap<>();
                mapismine.put("empid1", happyHandJw.getEmpid1());
                mapismine.put("empid2", happyHandJw.getEmpid2());
                mapismine.put("is_check", "1");
                List<HappyHandJw> ismines = jiaowangDao.lists(mapismine);
                if(ismines != null && ismines.size()>0){
                    //说明我俩是交往对象 删除这条请求信息
                    jiaowangDao.deleteById(happyHandJw);
                    return 200;
                }
            }

            if(emp2 != null && "2".equals(emp2.getState())){
                //我处于交往状态，查看是否是我俩在交往中
                Map<String, Object> mapismine = new HashMap<>();
                mapismine.put("empid1", happyHandJw.getEmpid1());
                mapismine.put("empid2", happyHandJw.getEmpid2());
                mapismine.put("is_check", "1");
                List<HappyHandJw> ismines = jiaowangDao.lists(mapismine);
                if(ismines != null && ismines.size()>0){
                    //说明我俩是交往对象 删除这条请求信息
                    jiaowangDao.deleteById(happyHandJw);
                    return 200;
                }
            }

            HappyHandMessage happyHandMessage = new HappyHandMessage();
            happyHandMessage.setMsgid(UUIDFactory.random());
            happyHandMessage.setDateline(System.currentTimeMillis() + "");
            happyHandMessage.setTitle(emp2.getNickname()+"拒绝与你交往，继续努力哦，祝你早日找到幸福！");
            happyHandMessage.setEmpid(happyHandJw.getEmpid1());
            messagesDao.save(happyHandMessage);
            if(!StringUtil.isNullOrEmpty(emp2.getChannelId())){
                BaiduPush.PushMsgToSingleDevice(Integer.parseInt(emp2.getDeviceType()), "交往消息", emp2.getNickname()+"拒绝与你交往，继续努力哦，祝你早日找到幸福！", "4", emp2.getChannelId());
            }
        }

        return 200;
    }

    @Override
    public Object delete(Object object) throws ServiceException {
        HappyHandJw happyHandJw = (HappyHandJw) object;
        jiaowangDao.delete(happyHandJw);
        return 200;
    }


    @Override
    public Object execute(Object object) throws Exception {
        HappyHandJw happyHandJw = (HappyHandJw) object;
        jiaowangDao.delete(happyHandJw);
        empDao.updateState(happyHandJw.getEmpid1(), "1");
        empDao.updateState(happyHandJw.getEmpid2(), "1");

        Emp emp1 = empDao.findById(happyHandJw.getEmpid1());
        Emp emp2 = empDao.findById(happyHandJw.getEmpid2());

        HappyHandMessage happyHandMessage11 = new HappyHandMessage();
        happyHandMessage11.setMsgid(UUIDFactory.random());
        happyHandMessage11.setDateline(System.currentTimeMillis() + "");
        happyHandMessage11.setTitle("您与"+emp2.getNickname()+"已解除交往关系");
        happyHandMessage11.setEmpid(happyHandJw.getEmpid1());
        messagesDao.save(happyHandMessage11);

        if(!StringUtil.isNullOrEmpty(emp1.getChannelId())){
            BaiduPush.PushMsgToSingleDevice(Integer.parseInt(emp1.getDeviceType()), "系统消息", "温馨提示：交往关系已解除！！！", "6", emp1.getChannelId());
        }

        HappyHandMessage happyHandMessage12 = new HappyHandMessage();
        happyHandMessage12.setMsgid(UUIDFactory.random());
        happyHandMessage12.setDateline(System.currentTimeMillis() + "");
        happyHandMessage12.setTitle("您与"+emp1.getNickname()+"已解除交往关系");
        happyHandMessage12.setEmpid(happyHandJw.getEmpid2());
        messagesDao.save(happyHandMessage12);

        if(!StringUtil.isNullOrEmpty(emp2.getChannelId())){
            BaiduPush.PushMsgToSingleDevice(Integer.parseInt(emp2.getDeviceType()), "系统消息", "温馨提示：交往关系已解除！！！", "6", emp2.getChannelId());
        }

        //退出群-沈阳情侣群
        easemobChatGroup.removeSingleUserFromChatGroup(Constants.DEFAULT_GROUP_ID2, happyHandJw.getEmpid1());
        easemobChatGroup.removeSingleUserFromChatGroup(Constants.DEFAULT_GROUP_ID2, happyHandJw.getEmpid2());

        EmpGroups empGroups1= new EmpGroups();
        empGroups1.setGroupid(Constants.DEFAULT_GROUP_ID2);
        empGroups1.setEmpid(happyHandJw.getEmpid1());
        empGroupsDao.delete(empGroups1);

        EmpGroups empGroups2= new EmpGroups();
        empGroups2.setGroupid(Constants.DEFAULT_GROUP_ID2);
        empGroups2.setEmpid(happyHandJw.getEmpid2());
        empGroupsDao.delete(empGroups2);

        return 200;
    }
}
