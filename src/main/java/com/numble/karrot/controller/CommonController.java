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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequiredArgsConstructor
public class CommonController {

    private final MemberService memberService;
    private final TradeService tradeService;
    private final MemberImageService memberImageService;

    @Tag(name = "회원가입 페이지 이동")
    @GetMapping("/join")
    public String joinPage(Model model) {
        model.addAttribute("form", new MemberJoinRequest());
        return "join";
    }

    @Operation(
        summary = "회원가입 처리", description = "사용자 회원가입을 처리합니다."
    )
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

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    private MemberImage createProductImage(Member joinMember) {
        return MemberImage.builder()
                .filePath(MemberImageInit.DEFAULT_PATH)
                .serverFileName(MemberImageInit.DEFAULT_SERVER_FILE_NAME)
                .originalFileName(MemberImageInit.DEFAULT_ORIGINAL_FILE_NAME)
                .member(joinMember)
                .build();
    }

    private Trade createTradeEntity(Member joinMember) {
        return Trade.builder()
                .tradeQuantity(TradeInit.TRADE_QUANTITY)
                .donationQuantity(TradeInit.DONATION_QUANTITY)
                .member(joinMember)
                .build();
    }

    private boolean duplicateCheck(String email) {
        return memberService.duplicateEmailCheck(email);
    }

}
