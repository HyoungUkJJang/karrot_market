package com.numble.karrot.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 서버가 살아있는지 체크합니다.
 */
@RestController
@RequestMapping("/health")
public class HealthController {

    @GetMapping
    public String health() {
        return "Health Check!";
    }

}
