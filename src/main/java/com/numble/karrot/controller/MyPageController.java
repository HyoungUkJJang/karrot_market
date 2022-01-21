package com.numble.karrot.controller;

import com.numble.karrot.aws.service.S3Uploader;
import com.numble.karrot.member.domain.Member;
import com.numble.karrot.member.dto.MemberFitResponse;
import com.numble.karrot.member.dto.MemberUpdateRequest;
import com.numble.karrot.member.service.MemberService;
import com.numble.karrot.member_image.domain.MemberImage;
import com.numble.karrot.member_image.service.MemberImageService;
import com.numble.karrot.product.dto.ProductListResponse;
import com.numble.karrot.product.service.ProductService;
import com.numble.karrot.product_image.domain.ProductImageNotInit;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 마이 페이지와 관련된 HTTP 요청을 처리하는 클래스 입니다.
 */
@Controller
@RequestMapping("/mypage")
@RequiredArgsConstructor
public class MyPageController {

    private final MemberService memberService;
    private final MemberImageService memberImageService;
    private final S3Uploader s3Uploader;
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
        MemberFitResponse memberFitResponse = new MemberFitResponse(member.getId(), member.getNickName(), member.getMemberImage().getServerFileName());
        model.addAttribute("memberFitResponse", memberFitResponse);

        return "mypage/MyPage";
    }

    /**
     * 수정페이지로 이동합니다.
     * @param userDetails 회읜을 조회 할 로그인 된 사용자 정보
     * @param model 사용자 정보를 담을 모델
     * @return 수정 페이지 url
     */
    @GetMapping("/update")
    public String updatePage(@AuthenticationPrincipal UserDetails userDetails, Model model) {

        String email = userDetails.getUsername();
        Member member = memberService.findMember(email);
        MemberFitResponse memberFitResponse = new MemberFitResponse(member.getId(), member.getNickName(), member.getMemberImage().getServerFileName());
        model.addAttribute("memberFitResponse", memberFitResponse);

        return "mypage/MyProfileUpdate";

    }

    /**
     * 내가 올린 상품 페이지로 이동합니다.
     * @param userDetails
     * @param model
     * @return
     */
    @GetMapping("/products")
    public String myProductsPage(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        String email = userDetails.getUsername();
        Member findMember = memberService.findMember(email);
        List<ProductListResponse> myProducts = toMyProductList(findMember);
        model.addAttribute("myProducts", myProducts);

        return "mypage/MyProducts";
    }

    /**
     * 회원정보를 수정합니다.
     * @param form 수정할 정보
     * @return 다시 마이페이지로 이동합니다.
     */
    @PostMapping("/update")
    public String updateProc(@AuthenticationPrincipal UserDetails userDetails, MemberUpdateRequest form) throws IOException {
        String email = userDetails.getUsername();
        Member member = memberService.findMember(email);

        if (!form.getMemberImage().isEmpty()) {

            MemberImage memberImage = memberImageService.findMemberImage(member.getId());
            String serverUrl = s3Uploader.upload(form.getMemberImage(), "members");
            memberImageService.updateMemberImage(memberImage, "members",
                    form.getMemberImage().getOriginalFilename(), serverUrl);

        }
        memberService.update(member, form.getNickName());

        return "redirect:/mypage";
    }

    /**
     * 나의 상품조회 리스트 DTO로 변환합니다.
     * @param findMember 조회할 회원의 아이디
     * @return 상품 리스트
     */
    private List<ProductListResponse> toMyProductList(Member findMember) {
        return productService.getMyProductList(findMember.getId())
                .stream().map(p -> ProductListResponse.builder()
                        .id(p.getId())
                        .title(p.getTitle())
                        .price(p.getPrice())
                        .thumbnailImage(
                                p.getProductImages().size() == 0 ?
                                        ProductImageNotInit.SERVER_FILE_NAME :
                                        p.getProductImages().get(0).getServerFileName())
                        .build()
                ).collect(Collectors.toList());
    }

}
