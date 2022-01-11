package com.numble.karrot.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health")
public class HomeController {

    @GetMapping
    public String health() {
        return "Health Check!";
    }

}
