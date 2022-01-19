package com.numble.karrot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 모든 사용자가 이용 가능한 HTTP 요청을 처리하는 컨트롤러 클래스입니다.
 */
@Controller
public class CommonController {

    /**
     * 회원가입 페이지로 이동합니다.
     * @return 회원가입 페이지 url
     */
    @GetMapping("/join")
    public String joinPage() {
        return "join";
    }

    /**
     * 로그인 페이지로 이동합니다.
     * @return 로그인 페이지 url
     */
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

}
