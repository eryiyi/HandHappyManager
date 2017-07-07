package com.liangxunwang.unimanager.mvc;

import com.liangxunwang.unimanager.model.Admin;
import com.liangxunwang.unimanager.service.ListService;
import com.liangxunwang.unimanager.util.ControllerConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by zhl on 2015/1/29.
 */
@Controller
public class IndexController extends ControllerConstants {

    @RequestMapping("/index")
    public String index(HttpSession session, ModelMap map){
        Admin admin = (Admin) session.getAttribute(ControllerConstants.ACCOUNT_KEY);
        if (admin != null){
            map.put("admin", admin);
            return "/index";
        }
        return "/adminLogin";
    }

    @RequestMapping("/main")
    public String left(){

        return "/index";
    }



    @Autowired
    @Qualifier("indexService")
    private ListService indexServiceList;

    @RequestMapping("/mainPage")
    public String mainPage(HttpSession session,ModelMap map){
        Admin admin = (Admin) session.getAttribute(ControllerConstants.ACCOUNT_KEY);
        List<String> list = (List<String>) indexServiceList.list("");
        map.put("empCountAll", list.get(0));
        map.put("empCountDay", list.get(1));
        map.put("orderAmountAll", list.get(2));
        map.put("orderAmountDay", list.get(3));
        map.put("orderAmountAllHy", list.get(4));
        map.put("orderAmountAllCx", list.get(5));

        map.put("empCountAllTy", list.get(6));
        map.put("empCountDayTy", list.get(7));
        map.put("empCountAllRz", list.get(8));
        map.put("empCountDayRz", list.get(9));
        map.put("lon1", list.get(10));
        map.put("lon11", list.get(11));
        map.put("empCountAllCx", list.get(12));

        return "/main";
    }

    /**
     *
     * @return
     */
    @RequestMapping("/logout")
    public String logout(HttpSession session){
        Enumeration en = session.getAttributeNames();
        while (en.hasMoreElements()) {
            session.removeAttribute((String)en.nextElement());
        }
        return "redirect:/index.do";
    }

    @RequestMapping("/welcome")
    public String welcome(){
        return "html/index";
    }

}
