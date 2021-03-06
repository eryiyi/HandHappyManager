package com.liangxunwang.unimanager.dao;

import com.liangxunwang.unimanager.model.Emp;
import com.liangxunwang.unimanager.model.EmpKu;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository("empDao")
public interface EmpDao {
    void save(Emp emp);

    void updatePass(@Param(value = "empid") String empid, @Param(value = "password") String password);

    List<Emp> lists(Map<String, Object> map);

    long count(Map<String, Object> map);

    long countDay(Map<String, Object> map);


    Emp findById(String empid);

    Emp findByMobile(String mobile);

    void updateStatus(@Param(value = "empid") String empid, @Param(value = "is_use") String is_use);

    void updateCover(@Param(value = "empid") String empid, @Param(value = "cover") String is_use);

    //前台更新
    void updateProfile(Emp emp);

    //后台管理员修改会员数据
    void updateManage(Emp emp);


    void updateMobile(@Param(value = "empid") String empid, @Param(value = "mobile") String is_use);

    void updatePassByMobile(@Param(value = "mobile") String empid, @Param(value = "password") String password);

    void updateCard(@Param(value = "empid") String empid, @Param(value = "cardpic") String password);
    void updateCardOnly(@Param(value = "empid") String empid, @Param(value = "cardpic") String password);

    void updateRzstate2(@Param(value = "empid") String empid, @Param(value = "rzstate2") String password);
    void updateRzstate3(@Param(value = "empid") String empid, @Param(value = "rzstate3") String password);

    void saveList(List<Emp> list);

    //选择合适的对象集合
    List<Emp> listsChoose(Map<String, Object> map);

    void updateState(@Param(value = "empid") String empid, @Param(value = "state") String password);

    /**
     * 根据ID更新pushId
     * {id, userId, channelId, type}
     */
    void updatePushId(@Param(value = "empid") String id, @Param(value = "userId") String userId, @Param(value = "channelId") String channelId, @Param(value = "deviceType")String deviceType);

    List<Emp> listsSearch(Map<String, Object> map);

    void updateIsPush(@Param(value = "is_push") String empid, @Param(value = "password") String password);

}
