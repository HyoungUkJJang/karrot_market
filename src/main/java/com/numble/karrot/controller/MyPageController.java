package com.numble.karrot.controller;

import com.numble.karrot.member.domain.Member;
import com.numble.karrot.member.dto.MemberFitResponse;
import com.numble.karrot.member.service.MemberService;
import com.numble.karrot.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 마이 페이지와 관련된 HTTP 요청을 처리하는 클래스 입니다.
 */
@Controller
@RequestMapping("/mypage")
@RequiredArgsConstructor
public class MyPageController {

    private final MemberService memberService;
    private final ProductService productService;

    /**
     * 마이페이지로 이동합니다.
     * @param userDetails 회원을 조회 할 로그인 된 사용자 정보
     * @param model 사용자 정보를 담을 모델
     * @return 마이 페이지 url
     */
    @GetMapping
    public String myPage(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        String email = userDetails.getUsername();
        Member member = memberService.findMember(email);
        MemberFitResponse memberFitResponse = new MemberFitResponse(member.getNickName(), member.getMemberImage().getServerFileName());
        model.addAttribute("memberInfo", memberFitResponse);
        return "mypage/MyPage";
    }

}
