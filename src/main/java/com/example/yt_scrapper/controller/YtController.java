package com.example.yt_scrapper.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.yt_scrapper.services.YtService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


@Controller
public class YtController {
    
    @Autowired
    private YtService youtubService;

    @GetMapping("/")
    public String getHome() {
        return "index";
    }

    @PostMapping("/processing")
    public String getData(@RequestParam String videoLink, Model model) {
        String videoId = youtubService.exractVideoId(videoLink);
        //System.out.println("video - id =  "+videoId);
        if(videoId !=null){
                try {
                    JsonNode videoDetails = youtubService.getVideoDetails(videoId);
                    // System.out.println(videoDetails);
                   
                    String title = videoDetails.path("title").asText();
                    String description = videoDetails.path("description").asText();
                    String thumUrl= videoDetails.path("thumbnails").path("standard").path("url").asText();
                    String tags[] = new ObjectMapper().treeToValue(videoDetails.path("tags"), String[].class);

                    model.addAttribute("vtitle", title);
                    model.addAttribute("vdesc", description);
                    model.addAttribute("vthumb", thumUrl);
                    model.addAttribute("vtags", tags);
                   
                   // System.out.println(model);
                    return "details";
                } catch (Exception e) {
                    e.printStackTrace();
                    return "error";
                }
        }
        return "error";
    }
    
}
