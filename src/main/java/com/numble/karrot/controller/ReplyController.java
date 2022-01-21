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

@Controller
@RequiredArgsConstructor
@RequestMapping("/products/{product_id}/reply")
public class ReplyController {

    private final MemberService memberService;
    private final ProductService productService;
    private final ReplyService replyService;


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

    @GetMapping("/register")
    public String replyRegisterPage(@PathVariable Long product_id, Model model) {
        model.addAttribute("pageInfo", product_id);
        return "reply/ReplyRegister";
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public String replyRegisterProc(@AuthenticationPrincipal UserDetails userDetails,
                                    @PathVariable Long product_id, @Validated ReplyRegisterRequest form, Model model) {

        Member findMember = memberService.findMember(userDetails.getUsername());
        Product findProduct = productService.findProduct(product_id);
        Reply reply = form.toReplyEntity(findMember, findProduct);
        replyService.registerReply(reply);
        return "products/ProductList";
    }

}
