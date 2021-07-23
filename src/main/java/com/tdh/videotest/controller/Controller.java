package com.tdh.videotest.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@org.springframework.stereotype.Controller
@RequestMapping("/test")
public class Controller {

    @GetMapping("/show")
    public String showVideo(){
        return "showVideo";
    }

    @GetMapping("/playVideo")
    public String playVideo(){
        return "playVideo";
    }

    @GetMapping("/videoJsTest")
    public String videoJsTest(){
        return "videojsTest";
    }




}
