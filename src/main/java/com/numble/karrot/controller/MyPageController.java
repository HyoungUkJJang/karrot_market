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
import com.numble.karrot.product.domain.ProductStatus;
import com.numble.karrot.product.dto.ProductDetailResponse;
import com.numble.karrot.product.dto.ProductListResponse;
import com.numble.karrot.product.dto.ProductStatusRequest;
import com.numble.karrot.product.dto.ProductUpdateRequest;
import com.numble.karrot.product.exception.ProductNotFoundException;
import com.numble.karrot.product.service.ProductService;
import com.numble.karrot.product_image.domain.ProductImage;
import com.numble.karrot.product_image.domain.ProductImageNotInit;
import com.numble.karrot.product_image.service.ProductImageService;
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
import java.util.Arrays;
import java.util.Collections;
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
    private final ProductImageService productImageService;

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
        MemberFitResponse memberInfo = MemberFitResponse.builder()
                .memberId(member.getId())
                .nickName(member.getNickName())
                .profileImage(member.getMemberImage().getServerFileName())
                .build();

        model.addAttribute("memberInfo", memberInfo);

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

        MemberUpdateRequest form = MemberUpdateRequest.builder()
                .nickName(member.getNickName())
                .build();

        model.addAttribute("form", form);
        model.addAttribute("profileImage", member.getMemberImage().getServerFileName());

        return "mypage/MyProfileUpdate";

    }


    /**
     * 회원정보를 수정합니다.
     * @param form 수정할 정보
     * @return 다시 마이페이지로 이동합니다.
     */
    @PostMapping("/update")
    public String updateProc(@AuthenticationPrincipal UserDetails userDetails, @ModelAttribute MemberUpdateRequest form) throws IOException {

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
     * 관심상품 페이지로 이동합니다.
     * @param userDetails 유저정보
     * @param model 관심상품 목록을 담을 모델
     * @return 관심상품 url
     */
    @GetMapping("/hearts")
    public String myHeartsPage(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        Member findMember = memberService.findMember(userDetails.getUsername());
        List<ProductListResponse> productList = toMyHeartProductList(findMember);
        model.addAttribute("productList", productList);
        return "mypage/MyHeartProducts";
    }

    /**
     * 내가 판매중인 상품 페이지로 이동합니다.
     * @param userDetails
     * @param model
     * @return
     */
    @GetMapping("/products")
    public String myProductsPage(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        String email = userDetails.getUsername();
        Member findMember = memberService.findMember(email);
        List<ProductListResponse> myProducts = toMyTradingProductList(findMember);

        model.addAttribute("productList", myProducts);

        return "mypage/MyProducts";
    }

    /**
     * 내가 거래완료 한 상품 페이지로 이동합니다.
     * @param userDetails
     * @param model
     * @return
     */
    @GetMapping("/products/complete")
    public String myProductsCompletePage(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        String email = userDetails.getUsername();
        Member findMember = memberService.findMember(email);
        List<ProductListResponse> myProducts = toMyCompleteProductList(findMember);

        model.addAttribute("productList", myProducts);

        return "mypage/MyProducts";
    }


    @GetMapping("/products/{product_id}")
    public String myProductDetailPage(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long product_id,
                                      Model model) {

        String email = userDetails.getUsername();

        Product product = productService.findProduct(product_id);
        if(!email.equals(product.getMember().getEmail())) throw new ProductNotFoundException();

        ProductDetailResponse myProduct = toMyProductDetail(product);
        MemberFitResponse memberFitResponse = toMemberFitResponse(memberService.findMember(email));

        model.addAttribute("productStatus", ProductStatus.values());
        model.addAttribute("memberInfo", memberFitResponse);
        model.addAttribute("product", myProduct);

        return "mypage/MyProductDetail";

    }

    /**
     * 상품의 상태변경 요청을 수행 후 200 상태코드를 리턴합니다.
     * @param product_id 변경될 상품의 아이디
     * @param productStatusRequest 변경될 상품의 상태정보
     * @return
     */
    @PutMapping("/products/{product_id}/setStatus")
    @ResponseBody
    public String myProductStatusChange(@PathVariable Long product_id, @RequestBody ProductStatusRequest productStatusRequest) {

        productService.changedProductStatus(
                productService.findProduct(product_id),
                productStatusRequest.getProductStatus()
        );

        return "success";
    }

    /**
     * 상품을 삭제합니다.
     * @param product_id 상품의 아이디
     * @return
     */
    @GetMapping("/products/{product_id}/delete")
    public String myProductDeletePage(@PathVariable Long product_id) {

        Product product = productService.findProduct(product_id);
        List<ProductImage> productImages = product.getProductImages();

        // 기본이미지가 아니라면 s3에 이미지 지우기
        if(!productImages.get(0).getServerFileName().equals(ProductImageNotInit.SERVER_FILE_NAME)) {
            for (ProductImage productImage : productImages) {
                s3Uploader.delete(productImage.getFilePath() + productImage.getOriginalFileName());
            }
        }

        // 상품이미지 테이블 상품테이블 지우기
        productImageService.deleteProductImage(product_id);
        productService.deleteProduct(product_id);

        return "redirect:/mypage";

    }

    /**
     * 내 상품 수정 페이지로 이동합니다.
     * @param product_id 상품의 아이디
     * @param model 상품정보를 담을 모델
     * @return 상품수정 url
     */
    @GetMapping("/products/{product_id}/update")
    public String myProductUpdatePage(@PathVariable Long product_id, Model model) {

        Product product = productService.findProduct(product_id);
        ProductUpdateRequest form = ProductUpdateRequest.builder()
                .title(product.getTitle())
                .price(product.getPrice())
                .category(product.getCategory())
                .description(product.getDescription())
                .build();
        List<Category> categoryList = categoryService.getCategoryList();

        model.addAttribute("productId", product_id);
        model.addAttribute("form", form);
        model.addAttribute("categoryList", categoryList);
        return "mypage/MyProductUpdate";

    }

    /**
     * 상품 수정을 수행합니다.
     * @param product_id
     * @param form
     * @param userDetails
     * @param model
     * @return
     */
    @PostMapping("/products/{product_id}/update")
    public String myProductUpdateProc(@PathVariable Long product_id, @Validated ProductUpdateRequest form,
                                      @AuthenticationPrincipal UserDetails userDetails, Model model) {

        String email = userDetails.getUsername();
        Member member = memberService.findMember(email);
        MemberFitResponse memberFitResponse = toMemberFitResponse(member);
        Product myProduct = productService.updateProduct(product_id, form.toProductEntity());

        if (form.getProductImages().size() == 1 && form.getProductImages().get(0)
                .getOriginalFilename().equals(""))  {
        }

        model.addAttribute("myProduct", myProduct);
        model.addAttribute("memberFitResponse", memberFitResponse);

        return "redirect:/mypage/products/" + product_id;

    }

    /**
     * 회원 응답 DTO로 변환합니다.
     * @param member 회원 엔티티
     * @return 회원정보 DTO
     */
    private MemberFitResponse toMemberFitResponse(Member member) {
        return new MemberFitResponse(
                member.getId(),
                member.getNickName(),
                member.getMemberImage().getServerFileName(),
                member.getHearts().stream().map(h -> h.getProductInfo()).collect(Collectors.toList()));
    }

    /**
     * 나의 상품조회 리스트 DTO로 변환합니다.
     * @param findMember 조회할 회원의 아이디
     * @return 상품 리스트
     */
    private List<ProductListResponse> toMyTradingProductList(Member findMember) {

        return productService.getMyTradingProductList(findMember.getId(), Arrays.asList(ProductStatus.TRADING, ProductStatus.RESERVED))
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
     * 나의 상품조회 리스트 DTO로 변환합니다.
     * @param findMember 조회할 회원의 아이디
     * @return 상품 리스트
     */
    private List<ProductListResponse> toMyCompleteProductList(Member findMember) {

        return productService.getMyCompleteProductList(findMember.getId(), ProductStatus.TRADE_COMPLETED)
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
     * 나의 관심상풍 리스트 DTO로 변환합니다.
     * @param findMember 조회할 회원의 아이디
     * @return 상품 리스트
     */
    private List<ProductListResponse> toMyHeartProductList(Member findMember) {
        return productService.getMyHeartsProductList(findMember.getId())
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
