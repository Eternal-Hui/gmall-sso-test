package com.atguigu.gmall.order.controller;


import com.atguigu.gmall.order.anotation.LoginRequired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class OrderController {

    @RequestMapping("index")
    public String index(){

        return "index";
    }

    @LoginRequired
    @RequestMapping("toTrade")
    public String toTrade(){

        return "trade";
    }

}
