package com.tdh.videotest.controller;

import com.tdh.videotest.youtubeDataModel.VideoItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;


@org.springframework.stereotype.Controller
//@RequestMapping()
public class Controller {

    @Autowired
    private RestApiController restApiController;


    @GetMapping(value = {"/", "/show"})
    public String showVideo(Model model){

        List<VideoItem> youtubeApiData = restApiController.getYoutubeApiData();
        model.addAttribute("youtubeApiData" , youtubeApiData);
        return "showVideo";
    }

    @GetMapping("/playVideo/{videoId}")
    public String playVideo(@PathVariable("videoId") String videoId , Model model){

        model.addAttribute("videoId" , videoId);

        return "playVideo";
    }

    @GetMapping("/videoJsTest")
    public String videoJsTest(){
        return "videojsTest";
    }




}
