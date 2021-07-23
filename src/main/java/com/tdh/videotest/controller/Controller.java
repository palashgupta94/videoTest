package com.tdh.videotest.controller;

import com.tdh.videotest.apiAuth.GetAuthorizationCode;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


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
