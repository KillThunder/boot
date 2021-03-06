package com.spring.boot.controller;

import com.spring.boot.util.KafkaProduce;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;

@Controller
public class indexController {

    @Autowired
    private KafkaProduce kafkaProduce;

    @RequestMapping("/")
    public String index(Model model) {
        /*Msg msg = new Msg("测试标题", "测试内容", "额外信息，只对管理员显示");
        model.addAttribute("msg", msg);*/
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("测试标题");
        arrayList.add("测试内容");
        arrayList.add("额外信息，只对管理员显示");
        model.addAttribute("msg", arrayList);
        return "index";
    }

    @RequestMapping("/login")
    public String login() {
        kafkaProduce.send("测试测试");
        return "login";
    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @RequestMapping(value = "/admin/test1")
    @ResponseBody
    public String adminTest1() {
        return "ROLE_USER";
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @RequestMapping("/admin/test2")
    @ResponseBody
    public String adminTest2() {
        return "ROLE_ADMIN";
    }

}
