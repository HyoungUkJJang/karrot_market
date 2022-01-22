package com.numble.karrot.controller;

import com.numble.karrot.member.domain.Member;
import com.numble.karrot.member.dto.MemberJoinRequest;
import com.numble.karrot.member.exception.MemberEmailDuplicateException;
import com.numble.karrot.member.service.MemberService;
import com.numble.karrot.member_image.domain.MemberImage;
import com.numble.karrot.member_image.domain.MemberImageInit;
import com.numble.karrot.member_image.service.MemberImageService;
import com.numble.karrot.trade.domain.Trade;
import com.numble.karrot.trade.domain.TradeInit;
import com.numble.karrot.trade.service.TradeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 모든 사용자가 이용 가능한 HTTP 요청을 처리하는 컨트롤러 클래스입니다.
 */
@Controller
@RequiredArgsConstructor
public class CommonController {

    private final MemberService memberService;
    private final TradeService tradeService;
    private final MemberImageService memberImageService;

    /**
     * 회원가입 페이지로 이동합니다.
     * @return 회원가입 페이지 url
     */
    @GetMapping("/join")
    public String joinPage(Model model) {
        model.addAttribute("form", new MemberJoinRequest());
        return "join";
    }

    /**
     * 회원의 회원가입 요청을 수행 후 201 상태코드를 반환합니다.
     * @param form 가입될 회읜의 정보
     * @return 시작페이지로 이동
     */
    @PostMapping("/join")
    @ResponseStatus(HttpStatus.CREATED)
    public String joinProc(@ModelAttribute @Validated MemberJoinRequest form) {

        // 이메일 중복 시 예외
        if (duplicateCheck(form.getEmail())) throw new MemberEmailDuplicateException();

        Member joinMember = memberService.join(form.toMemberEntity());

        if (joinMember.getId() != null) {
            tradeService.save(createTradeEntity(joinMember));
            memberImageService.save(createProductImage(joinMember));
        }

        return "start";
    }

    /**
     * 로그인 페이지로 이동합니다.
     * @return 로그인 페이지 url
     */
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    /**
     * 회원 이미지 엔티티를 생성하여 반환합니다.
     * @param joinMember 대응되는 회원정보
     * @return 회원 이미지 엔티티
     */
    private MemberImage createProductImage(Member joinMember) {
        return MemberImage.builder()
                .filePath(MemberImageInit.DEFAULT_PATH)
                .serverFileName(MemberImageInit.DEFAULT_SERVER_FILE_NAME)
                .originalFileName(MemberImageInit.DEFAULT_ORIGINAL_FILE_NAME)
                .member(joinMember)
                .build();
    }

    /**
     * 회원 Trade 엔티티를 생성하여 반환합니다.
     * @param joinMember 대응되는 회원정보
     * @return Trade 엔티티
     */
    private Trade createTradeEntity(Member joinMember) {
        return Trade.builder()
                .tradeQuantity(TradeInit.TRADE_QUANTITY)
                .donationQuantity(TradeInit.DONATION_QUANTITY)
                .member(joinMember)
                .build();
    }

    /**
     * 이메일 중복확인을 검증합니다.
     * @param email 검증할 이메일
     * @return 중복 : True / 사용가능 : False
     */
    private boolean duplicateCheck(String email) {
        return memberService.duplicateEmailCheck(email);
    }

}
