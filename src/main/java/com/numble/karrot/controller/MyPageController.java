package com.numble.karrot.controller;

import com.numble.karrot.aws.service.S3Uploader;
import com.numble.karrot.category.domain.Category;
import com.numble.karrot.category.service.CategoryService;
import com.numble.karrot.heart.service.HeartService;
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
import com.numble.karrot.reply.service.ReplyService;
import com.numble.karrot.trade.domain.Trade;
import com.numble.karrot.trade.service.TradeService;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ?????? ???????????? ????????? HTTP ????????? ???????????? ????????? ?????????.
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
    private final HeartService heartService;
    private final ReplyService replyService;

    /**
     * ?????????????????? ???????????????.
     * @param userDetails ????????? ?????? ??? ????????? ??? ????????? ??????
     * @param model ????????? ????????? ?????? ??????
     * @return ?????? ????????? url
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
     * ?????????????????? ???????????????.
     * @param userDetails ????????? ?????? ??? ????????? ??? ????????? ??????
     * @param model ????????? ????????? ?????? ??????
     * @return ?????? ????????? url
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
     * ??????????????? ???????????????.
     * @param form ????????? ??????
     * @return ?????? ?????????????????? ???????????????.
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
     * ???????????? ???????????? ???????????????.
     * @param userDetails ????????????
     * @param model ???????????? ????????? ?????? ??????
     * @return ???????????? url
     */
    @GetMapping("/hearts")
    public String myHeartsPage(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        Member findMember = memberService.findMember(userDetails.getUsername());
        List<ProductListResponse> productList = toMyHeartProductList(findMember);
        model.addAttribute("productList", productList);
        return "mypage/MyHeartProducts";
    }

    /**
     * ?????? ???????????? ?????? ???????????? ???????????????.
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
     * ?????? ???????????? ??? ?????? ???????????? ???????????????.
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
     * ????????? ???????????? ????????? ?????? ??? 200 ??????????????? ???????????????.
     * @param product_id ????????? ????????? ?????????
     * @param productStatusRequest ????????? ????????? ????????????
     * @return
     */
    @PutMapping("/products/{product_id}/setStatus")
    @ResponseBody
    public String myProductStatusChange(@PathVariable Long product_id, @AuthenticationPrincipal UserDetails userDetails,
                                        @RequestBody ProductStatusRequest productStatusRequest) {

          productService.changedProductStatus(
                productService.findProduct(product_id),
                productStatusRequest.getProductStatus()
        );

        // ???????????? ???????????? ????????? or ??????????????? ????????? ??????????????? ???????????????.
        /*
        if ("????????????".equals(findProduct.getProductStatus().getValue()) && (
                "?????????".equals(productStatusRequest.getProductStatus().getValue()) ||
                        "?????????".equals(productStatusRequest.getProductStatus().getValue()))) {
            if(findProduct.getPrice() > 0) {
                tradeService.deleteTradeQuantity(trade);
            } else {
                tradeService.deleteDonationQuantity(trade);
            }
        } else if ("????????????".equals(productStatusRequest.getProductStatus().getValue()) && (
                "?????????".equals(findProduct.getProductStatus().getValue()) ||
                        "?????????".equals(findProduct.getProductStatus().getValue()))) {
            if(findProduct.getPrice() > 0) {
                tradeService.addTradeQuantity(trade);
            } else {
                tradeService.addDonationQuantity(trade);
            }
        }
        */

        return "success";

    }

    /**
     * ????????? ???????????????.
     * @param product_id ????????? ?????????
     * @return
     */
    @GetMapping("/products/{product_id}/delete")
    public String myProductDeletePage(@PathVariable Long product_id, @AuthenticationPrincipal UserDetails userDetails) {
        Member member = memberService.findMember(userDetails.getUsername());
        Product product = productService.findProduct(product_id);
        List<ProductImage> productImages = product.getProductImages();

        // ?????????????????? ???????????? s3??? ????????? ?????????
        if(!productImages.get(0).getServerFileName().equals(ProductImageNotInit.SERVER_FILE_NAME)) {
            for (ProductImage productImage : productImages) {
                s3Uploader.delete(productImage.getFilePath() + productImage.getOriginalFileName());
            }
        }

        // ??????????????? ????????? ??????????????? ?????????
        replyService.deleteReply(product_id);
        heartService.deleteHeartAll(product_id);
        productImageService.deleteProductImage(product_id);
        productService.deleteProduct(product_id);

        return "redirect:/mypage";

    }

    /**
     * ??? ?????? ?????? ???????????? ???????????????.
     * @param product_id ????????? ?????????
     * @param model ??????????????? ?????? ??????
     * @return ???????????? url
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
     * ?????? ????????? ???????????????.
     * @param product_id
     * @param form
     * @param userDetails
     * @param model
     * @return
     */
    @PostMapping("/products/{product_id}/update")
    public String myProductUpdateProc(@PathVariable Long product_id, @Validated ProductUpdateRequest form,
                                      @AuthenticationPrincipal UserDetails userDetails, Model model) throws IOException {

        String email = userDetails.getUsername();
        Member member = memberService.findMember(email);
        MemberFitResponse memberFitResponse = toMemberFitResponse(member);
        Product myProduct = productService.updateProduct(product_id, form.toProductEntity());

        model.addAttribute("myProduct", myProduct);
        model.addAttribute("memberFitResponse", memberFitResponse);

        return "redirect:/mypage/products/" + product_id;

    }

    /**
     * ?????? ?????? DTO??? ???????????????.
     * @param member ?????? ?????????
     * @return ???????????? DTO
     */
    private MemberFitResponse toMemberFitResponse(Member member) {
        return new MemberFitResponse(
                member.getId(),
                member.getNickName(),
                member.getMemberImage().getServerFileName(),
                member.getHearts().stream().map(h -> h.getProductInfo()).collect(Collectors.toList()));
    }

    /**
     * ?????? ???????????? ????????? DTO??? ???????????????.
     * @param findMember ????????? ????????? ?????????
     * @return ?????? ?????????
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
     * ?????? ???????????? ????????? DTO??? ???????????????.
     * @param findMember ????????? ????????? ?????????
     * @return ?????? ?????????
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
     * ?????? ???????????? ????????? DTO??? ???????????????.
     * @param findMember ????????? ????????? ?????????
     * @return ?????? ?????????
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
     * ?????? ?????? ???????????? DTO??? ???????????????.
     * @param product ???????????? ?????? ?????????
     * @return ?????? DTO
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
