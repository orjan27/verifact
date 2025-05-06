package com.ai.agent.verifact.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ai.agent.verifact.service.AiService;


@RestController
@RequestMapping("/api/v1")
public class AiController {
    @Autowired
    AiService aiService;

    @GetMapping("/isFakeNews")
    public String isFakeNews(@RequestParam String news){
        return aiService.isFakeNews(news);
    }
}
