package com.liangxunwang.unimanager.mvc.admin;

import com.liangxunwang.unimanager.model.Admin;
import com.liangxunwang.unimanager.model.HappyHandNotice;
import com.liangxunwang.unimanager.query.NoticeQuery;
import com.liangxunwang.unimanager.service.ExecuteService;
import com.liangxunwang.unimanager.service.ListService;
import com.liangxunwang.unimanager.service.SaveService;
import com.liangxunwang.unimanager.util.ControllerConstants;
import com.liangxunwang.unimanager.util.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("noticeController")
public class NoticeController extends ControllerConstants {
    @Autowired
    @Qualifier("noticeService")
    private ListService noticeServiceList;

    @Autowired
    @Qualifier("noticeService")
    private SaveService noticeServiceSave;

    @Autowired
    @Qualifier("noticeService")
    private ExecuteService noticeServiceExe;


    @RequestMapping("list")
    public String list(HttpSession session,ModelMap map, NoticeQuery query, Page page){
        Admin manager = (Admin) session.getAttribute(ACCOUNT_KEY);
        query.setIndex(page.getPage() == 0 ? 1 : page.getPage());
        query.setSize(query.getSize() == 0 ? page.getDefaultSize() : query.getSize());

        Object[] results = (Object[]) noticeServiceList.list(query);
        map.put("list", results[0]);
        long count = (Long) results[1];
        page.setCount(count);
        page.setPageCount(calculatePageCount(query.getSize(), count));
        map.addAttribute("page", page);
        map.addAttribute("query", query);
        return "/notice/list";
    }

    @RequestMapping("toAdd")
    public String add(){
        return "/notice/add";
    }

    @RequestMapping("add")
    @ResponseBody
    public String add(HttpSession session,HappyHandNotice notice){
        noticeServiceSave.save(notice);
        return toJSONString(SUCCESS);
    }

    @RequestMapping("/toDetail")
    public String toUpdateType(HttpSession session,ModelMap map, String id) throws Exception {
        Admin manager = (Admin) session.getAttribute(ACCOUNT_KEY);
        HappyHandNotice notice = (HappyHandNotice) noticeServiceExe.execute(id);
        map.put("notice", notice);
        return "/notice/detail";
    }


}
