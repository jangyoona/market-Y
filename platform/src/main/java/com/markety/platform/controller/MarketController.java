package com.markety.platform.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/market")
public class MarketController {

    @GetMapping("/form")
    public String showMarketForm() {
        return "market/form";
    }

    @GetMapping("/list")
    public String showMarketList() {
        return "market/list";
    }

}
