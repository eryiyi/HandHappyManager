package com.liangxunwang.unimanager.service.app;

import com.liangxunwang.unimanager.chat.impl.EasemobIMUsers;
import com.liangxunwang.unimanager.dao.*;
import com.liangxunwang.unimanager.model.*;
import com.liangxunwang.unimanager.service.*;
import com.liangxunwang.unimanager.util.*;
import io.swagger.client.model.RegisterUsers;
import io.swagger.client.model.User;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.*;


@Service("appEmpService")
public class AppEmpService implements ExecuteService,SaveService,UpdateService,ListService,FindService {

    @Autowired
    @Qualifier("empDao")
    private EmpDao empDao;

    @Autowired
    @Qualifier("empKuDao")
    private EmpKuDao empKuDao;

    private EasemobIMUsers easemobIMUsers = new EasemobIMUsers();

    @Autowired
    @Qualifier("messagesDao")
    private MessagesDao messagesDao;

    @Autowired
    @Qualifier("loginEmpDao")
    private LoginEmpDao loginEmpDao;

    @Override
    public Object execute(Object object) throws ServiceException {
        Object[] params = (Object[]) object;
        String mobile = (String) params[0];
        String password = (String) params[1];
        Emp member = empDao.findByMobile(mobile);
        if (member == null){
            throw new ServiceException("NotFound");
        }
        if(StringUtil.isNullOrEmpty(member.getPassword())){
            //密码为空
            throw new ServiceException("PassNull");
        }
        if (!new MD5Util().getMD5ofStr(password).equals(member.getPassword())){
            throw new ServiceException("PassError");
        }
        if ("0".equals(member.getIs_use())){
            throw new ServiceException("NotUse");
        }
//        if("2".equals(member.getIs_use())){
//            throw new ServiceException("NotUpdateZiliao");
//        }
        if(!StringUtil.isNullOrEmpty(member.getCover())){
            if (member.getCover().startsWith("upload")) {
                member.setCover(Constants.URL + member.getCover());
            }else {
                member.setCover(Constants.QINIU_URL + member.getCover()+"-yasuoone");
            }
        }
        if(!StringUtil.isNullOrEmpty(member.getCardpic())){
            if (member.getCardpic().startsWith("upload")) {
                member.setCardpic(Constants.URL + member.getCardpic());
            }else {
                member.setCardpic(Constants.QINIU_URL + member.getCardpic()+"-yasuoone");
            }
        }
        if(!StringUtil.isNullOrEmpty(member.getPname())){
            member.setPname(member.getPname().trim());
        }
        //统计登录
        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("empid", member.getEmpid());
        map1.put("logindate", DateUtil.getDateAndTimeTwo1());
        long l1 = loginEmpDao.count(map1);
        if(l1 == 0){
            //说明今天没有登录过，可以统计新的登录
            LoginEmp loginEmp = new LoginEmp();
            loginEmp.setLogin_id(UUIDFactory.random());
            loginEmp.setDateline(System.currentTimeMillis() + "");
            loginEmp.setEmpid(member.getEmpid());
            loginEmp.setIs_login("0");
            loginEmp.setLogindate(DateUtil.getDateAndTimeTwo1());
            loginEmpDao.save(loginEmp);
        }else{
            //说明今天登陆过了 更新为登录状态
            LoginEmp loginEmp = new LoginEmp();
            loginEmp.setEmpid(member.getEmpid());
            loginEmp.setIs_login("0");
            loginEmp.setLogindate(DateUtil.getDateAndTimeTwo1());
            loginEmpDao.update(loginEmp);
        }

        return member;
    }

    @Override
    public Object save(Object object) throws ServiceException {
        Emp emp = (Emp) object;
        //根据mobile查询库里面是否存在改手机号码
//        EmpKu empKu = empKuDao.findByMobile(emp.getMobile());
//        if(empKu == null){
//            throw new ServiceException("MobileNotFound");
//        }
        //查询该手机号是否已经注册
        Emp emp1 = empDao.findByMobile(emp.getMobile());
        if(emp1 != null){
            throw new ServiceException("MobileHasExist");
        }
        emp.setEmpid(UUIDFactory.random());
        emp.setDateline(System.currentTimeMillis() + "");
        emp.setIs_use("2");//0否 1是 2尚未维护资料
        if(!StringUtil.isNullOrEmpty(emp.getPassword())){
            emp.setPassword(new MD5Util().getMD5ofStr(emp.getPassword()));//密码加密
        }
        empDao.save(emp);
        //同步在环信注册该用户
        RegisterUsers users = new RegisterUsers();
//        User user = new User().username(emp.getEmpid() + new Random().nextInt(500)).password("123456");
        User user = new User().username(emp.getEmpid()).password("123456");
        users.add(user);
        Object result = easemobIMUsers.createNewIMUserSingle(users);
        Assert.assertNotNull(result);
        //注册成功之后 系统通知
        //todo
        HappyHandMessage happyHandMessage = new HappyHandMessage();
        happyHandMessage.setMsgid(UUIDFactory.random());
        happyHandMessage.setDateline(System.currentTimeMillis() + "");
        happyHandMessage.setTitle("欢迎并感谢注册、使用幸福牵手吧APP！请使用搜索功能，查找、添加会员，浏览会员信息。\n" +
                "温馨提示：幸福牵手吧是一个真实的婚恋交友平台，请您真诚交友，祝你早日找到幸福！\n");
        happyHandMessage.setEmpid(emp.getEmpid());
        messagesDao.save(happyHandMessage);
        return emp.getEmpid();
    }

    @Autowired
    @Qualifier("photosDao")
    private PhotosDao photosDao;

    @Override
    public Object update(Object object) {
        Emp emp = (Emp) object;
        if(!StringUtil.isNullOrEmpty(emp.getEmpid()) && !StringUtil.isNullOrEmpty(emp.getCover())){
            //更新头像
            empDao.updateCover(emp.getEmpid(), emp.getCover());
            //添加头像文件到相册
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("empid", emp.getEmpid());
            List<HappyHandPhoto> lists = photosDao.findByEmpid(map);

            if(lists != null && lists.size() > 0){
                //说明存在了 更新
                HappyHandPhoto photo = lists.get(0);//原先的相册
                photo.setPhotos(photo.getPhotos()+","+ emp.getCover());
                photosDao.update(photo);
            }else {
                //添加
                HappyHandPhoto photo = new  HappyHandPhoto();
                photo.setPhotoid(UUIDFactory.random());
                photo.setDateline(System.currentTimeMillis() + "");
                photo.setEmpid(emp.getEmpid());
                photo.setPhotos(emp.getCover());
                photosDao.save(photo);
            }
        }
        return 200;
    }


    @Autowired
    @Qualifier("chooseDao")
    private ChooseDao chooseDao;

    //查询推荐人
    @Override
    public Object list(Object object) throws ServiceException {
        Map<String, String> map1 = (Map<String, String>) object;
        String empid = map1.get("empid");
        String size = map1.get("size");
        String sex = map1.get("sex");

        List<Emp> list = new ArrayList<Emp>();
        Emp emp = empDao.findById(empid);
        if(emp == null){
            throw new ServiceException("EmpNull");
        }
        HappyHandChoose happyHandChoose = chooseDao.findByEmpid(empid);
        if(happyHandChoose != null){
            String agestart = happyHandChoose.getAgestart();
            String ageend = happyHandChoose.getAgeend();
            String heightlstart = happyHandChoose.getHeightlstart();
            String heightlend = happyHandChoose.getHeightlend();
            String educationm = happyHandChoose.getEducationm();
            String marriagem = happyHandChoose.getMarriagem();
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("index", 0);
            map.put("size", Integer.parseInt(size));
            map.put("agestart", agestart);
            map.put("ageend", ageend);
            map.put("heightlstart", heightlstart);
            map.put("heightlend", heightlend);
            map.put("educationm", educationm);
            map.put("marriagem", marriagem);

            if(!StringUtil.isNullOrEmpty(sex)){
                map.put("sex", sex);
            }
            map.put("state", "1");
            List<Emp> list1 = empDao.listsChoose(map);//单身的
            map.put("state", "2");
            List<Emp> list2 = empDao.listsChoose(map);//交往中的

            if(list1 != null){
                for(Emp member:list1){
                    if(!StringUtil.isNullOrEmpty(member.getCover())){
                        if (member.getCover().startsWith("upload")) {
                            member.setCover(Constants.URL + member.getCover());
                        }else {
                            member.setCover(Constants.QINIU_URL + member.getCover()+"-yasuoone");
                        }
                    }
                    if(!StringUtil.isNullOrEmpty(member.getCardpic())){
                        if (member.getCardpic().startsWith("upload")) {
                            member.setCardpic(Constants.URL + member.getCardpic());
                        }else {
                            member.setCardpic(Constants.QINIU_URL + member.getCardpic()+"-yasuoone");
                        }
                    }
                    if(!StringUtil.isNullOrEmpty(member.getPname())){
                        member.setPname(member.getPname().trim());
                    }
                    list.add(member);
                }
            }
            if(list2 != null){
                for(Emp member:list2){
                    if(!StringUtil.isNullOrEmpty(member.getCover())){
                        if (member.getCover().startsWith("upload")) {
                            member.setCover(Constants.URL + member.getCover());
                        }else {
                            member.setCover(Constants.QINIU_URL + member.getCover()+"-yasuoone");
                        }
                    }
                    if(!StringUtil.isNullOrEmpty(member.getCardpic())){
                        if (member.getCardpic().startsWith("upload")) {
                            member.setCardpic(Constants.URL + member.getCardpic());
                        }else {
                            member.setCardpic(Constants.QINIU_URL + member.getCardpic()+"-yasuoone");
                        }
                    }
                    if(!StringUtil.isNullOrEmpty(member.getPname())){
                        member.setPname(member.getPname().trim());
                    }
                    list.add(member);
                }
            }


        }
        return list;
    }

    @Override
    public Object findById(Object object) throws ServiceException {
        String empid = (String) object;
        Emp member = empDao.findById(empid);
        if(member != null){
            if(!StringUtil.isNullOrEmpty(member.getCover())){
                if (member.getCover().startsWith("upload")) {
                    member.setCover(Constants.URL + member.getCover());
                }else {
                    member.setCover(Constants.QINIU_URL + member.getCover()+"-yasuoone");
                }
            }
            if(!StringUtil.isNullOrEmpty(member.getCardpic())){
                if (member.getCardpic().startsWith("upload")) {
                    member.setCardpic(Constants.URL + member.getCardpic());
                }else {
                    member.setCardpic(Constants.QINIU_URL + member.getCardpic()+"-yasuoone");
                }
            }
            if(!StringUtil.isNullOrEmpty(member.getPname())){
                member.setPname(member.getPname().trim());
            }
        }
        return member;
    }
}
