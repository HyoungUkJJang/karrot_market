package com.numble.karrot.controller;

import com.numble.karrot.member.domain.Member;
import com.numble.karrot.member.service.MemberService;
import com.numble.karrot.product.domain.Product;
import com.numble.karrot.product.service.ProductService;
import com.numble.karrot.reply.domain.Reply;
import com.numble.karrot.reply.dto.ReplyListResponse;
import com.numble.karrot.reply.dto.ReplyRegisterRequest;
import com.numble.karrot.reply.service.ReplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 댓글 페이지 이동 및 작성 HTTP 요청을 처리하는 컨트롤러 클래스 입니다.
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/products/{product_id}/reply")
public class ReplyController {

    private final MemberService memberService;
    private final ProductService productService;
    private final ReplyService replyService;

    /**
     * 댓글목록 페이지로 이동합니다.
     * @param product_id 댓글 상품의 아이디
     * @param model 댓글정보를 담을 모델
     * @return 댓글목록 페이지 url
     */
    @GetMapping
    public String replyListPage(@PathVariable Long product_id, Model model) {

        List<ReplyListResponse> replyList = replyService.getReplyList(product_id)
                .stream().map(r -> ReplyListResponse.builder()
                        .comment(r.getComment())
                        .memberEmail(r.getMember().getEmail())
                        .memberProfile(r.getMember().getMemberImage().getServerFileName())
                        .build())
                .collect(Collectors.toList());

        model.addAttribute("replyList", replyList);
        model.addAttribute("pageInfo", product_id);

        return "reply/ReplyList";
    }

    /**
     * 댓글등록 페이지로 이동합니다.
     * @param product_id 댓글을 작성할 상품의 아이디
     * @param model 댓글 폼을 담을 모델
     * @return 댓글 작성 페이지 url
     */
    @GetMapping("/register")
    public String replyRegisterPage(@PathVariable Long product_id, Model model) {
        model.addAttribute("pageInfo", product_id);
        return "reply/ReplyRegister";
    }

    /**
     * 댓글 작성 요청을 처리합니다.
     * @param userDetails 작성한 회원 정보
     * @param product_id 댓글 단 상품의 아이디
     * @param form 댓글 코멘트 정보
     * @return 댓글 목록으로 리다이렉트
     */
    @PostMapping("/register")
    public String replyRegisterProc(@AuthenticationPrincipal UserDetails userDetails,
                                    @PathVariable Long product_id, @Validated ReplyRegisterRequest form) {

        Member findMember = memberService.findMember(userDetails.getUsername());
        Product findProduct = productService.findProduct(product_id);
        Reply reply = form.toReplyEntity(findMember, findProduct);
        replyService.registerReply(reply);
        productService.addReplyCount(findProduct);
        return "redirect:/products/" + product_id + "/reply";

    }

}
