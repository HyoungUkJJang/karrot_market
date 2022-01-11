package com.numble.karrot.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/health")
public class HomeController {

    @GetMapping
    public String health() {
        return "CI/CD Test Task3";
    }

}
