package com.numble.karrot.controller;

import com.numble.karrot.aws.service.S3Uploader;
import com.numble.karrot.category.domain.Category;
import com.numble.karrot.category.service.CategoryService;
import com.numble.karrot.member.domain.Member;
import com.numble.karrot.member.dto.MemberFitResponse;
import com.numble.karrot.member.dto.MemberUpdateRequest;
import com.numble.karrot.member.service.MemberService;
import com.numble.karrot.member_image.domain.MemberImage;
import com.numble.karrot.member_image.service.MemberImageService;
import com.numble.karrot.product.domain.Product;
import com.numble.karrot.product.dto.ProductDetailResponse;
import com.numble.karrot.product.dto.ProductListResponse;
import com.numble.karrot.product.dto.ProductUpdateRequest;
import com.numble.karrot.product.service.ProductService;
import com.numble.karrot.product_image.domain.ProductImageNotInit;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.Banner;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    private final CategoryService categoryService;
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
        MemberFitResponse memberFitResponse = new MemberFitResponse(
                member.getId(),
                member.getNickName(),
                member.getMemberImage().getServerFileName(),
                member.getHearts().stream().map(h -> h.getProductInfo()).collect(Collectors.toList()));
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
        MemberFitResponse memberFitResponse = new MemberFitResponse(
                member.getId(),
                member.getNickName(),
                member.getMemberImage().getServerFileName(),
                member.getHearts().stream().map(h -> h.getProductInfo()).collect(Collectors.toList()));
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

    @GetMapping("/products/{product_id}")
    public String myProductDetailPage(@AuthenticationPrincipal UserDetails userDetails,
                                      @PathVariable Long product_id, Model model) {

        String email = userDetails.getUsername();
        Member member = memberService.findMember(email);
        MemberFitResponse memberFitResponse = new MemberFitResponse(
                member.getId(),
                member.getNickName(),
                member.getMemberImage().getServerFileName(),
                member.getHearts().stream().map(h -> h.getProductInfo()).collect(Collectors.toList()));


        Product product = productService.findProduct(product_id);
        ProductDetailResponse myProduct = toMyProductDetail(product);

        model.addAttribute("memberFitResponse", memberFitResponse);
        model.addAttribute("myProduct", myProduct);

        return "mypage/MyProductDetail";
    }

    @GetMapping("/products/{product_id}/update")
    public String myProductUpdatePage(@PathVariable Long product_id, Model model) {

        Product product = productService.findProduct(product_id);
        ProductDetailResponse myProduct = toMyProductDetail(product);
        List<Category> categoryList = categoryService.getCategoryList();
        model.addAttribute("myProduct", myProduct);
        model.addAttribute("categoryList", categoryList);
        return "mypage/MyProductUpdate";

    }

    @PostMapping("/products/{product_id}/update")
    public String myProductUpdateProc(@PathVariable Long product_id, @Validated ProductUpdateRequest form,
                                      @AuthenticationPrincipal UserDetails userDetails, Model model) {

        String email = userDetails.getUsername();
        Member member = memberService.findMember(email);
        MemberFitResponse memberFitResponse = new MemberFitResponse(
                member.getId(),
                member.getNickName(),
                member.getMemberImage().getServerFileName(),
                member.getHearts().stream().map(h -> h.getProductInfo()).collect(Collectors.toList()));
        Product myProduct = productService.updateProduct(product_id, form.toProductEntity());

        model.addAttribute("myProduct", myProduct);
        model.addAttribute("memberFitResponse", memberFitResponse);

        return "redirect:/mypage/products/" + product_id;

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

    /**
     * 나의 상품 상세조회 DTO로 변환합니다.
     * @param product 변환시킬 상품 엔티티
     * @return 응답 DTO
     */
    private ProductDetailResponse toMyProductDetail(Product product) {
        return ProductDetailResponse.builder()
                .id(product.getId())
                .productImages(product.getProductImages()
                        .stream().map(p -> p.getServerFileName()).collect(Collectors.toList()))
                .category(product.getCategory().getValue())
                .title(product.getTitle())
                .price(product.getPrice())
                .description(product.getDescription())
                .productStatus(product.getProductStatus())
                .build();
    }

}
