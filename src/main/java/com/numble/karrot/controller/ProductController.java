package com.numble.karrot.controller;

import com.numble.karrot.aws.service.S3Uploader;
import com.numble.karrot.category.service.CategoryService;
import com.numble.karrot.heart.domain.Heart;
import com.numble.karrot.heart.service.HeartService;
import com.numble.karrot.member.domain.Member;
import com.numble.karrot.member.dto.MemberFitResponse;
import com.numble.karrot.member.service.MemberService;
import com.numble.karrot.product.domain.Product;
import com.numble.karrot.product.dto.ProductDetailResponse;
import com.numble.karrot.product.dto.ProductListResponse;
import com.numble.karrot.product.dto.ProductRegisterRequest;
import com.numble.karrot.product.service.ProductService;
import com.numble.karrot.product_image.domain.ProductImage;
import com.numble.karrot.product_image.domain.ProductImageNotInit;
import com.numble.karrot.product_image.service.ProductImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 상품과 관련된 HTTP 요청을 처리하는 컨트롤러 클래스입니다.
 */
@Controller
@CrossOrigin
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final ProductImageService productImageService;
    private final CategoryService categoryService;
    private final MemberService memberService;
    private final HeartService heartService;
    private final S3Uploader s3Uploader;

    /**
     * 상품리스트 페이지로 이동합니다.
     * @param model 상품 리스트 정보를 담을 모델
     * @return 상품리스트 페이지 url
     */
    @GetMapping
    public String productsPage(@AuthenticationPrincipal UserDetails userDetails, Model model) {

        Member findMember = memberService.findMember(userDetails.getUsername());
        MemberFitResponse memberInfo = toMemberFitResponse(findMember);

        model.addAttribute("memberInfo", memberInfo);
        model.addAttribute("productList", toProductListResponse());
        return "products/ProductList";

    }

    /**
     * 상품 상세조회 페이지 이동
     * @param product_id 상세조회 할 상품의 아이디
     * @param model      상품 상세정보, 회원 정보를 담을 모델
     * @return 상품 상세조회 페이지 url
     */
    @GetMapping("/{product_id}")
    public String detailProduct(@PathVariable Long product_id, @AuthenticationPrincipal UserDetails userDetails,
                                Model model) {

        Product findProduct = productService.findProduct(product_id);
        ProductDetailResponse responseProduct =
                toProductDetailResponse(findProduct);

        if (responseProduct.getProductImages().size() == 0) responseProduct.addProductNotImage();

        Member member = memberService
                .findMember(findProduct.getMember().getEmail());
        MemberFitResponse responseMember = toMemberFitResponse(member);

        Member my = memberService.findMember(userDetails.getUsername());
        MemberFitResponse myInfo = toMemberFitResponse(my);

        model.addAttribute("product", responseProduct);
        model.addAttribute("ownerInfo", responseMember);
        model.addAttribute("myInfo", myInfo);

        return "products/ProductDetail";

    }

    /**
     * 회원 판매상품 페이지로 이동합니다.
     * @param product_id 상품의 아이디
     * @param owner_id 상품 주인의 아이디
     * @param model 상품 주안의 판매상품을 담을 모델
     * @return 상품페이지 url
     */
    @GetMapping("/{product_id}/memberProducts/{owner_id}")
    public String memberProductsPage(@PathVariable Long product_id, @PathVariable Long owner_id,
                                     @AuthenticationPrincipal UserDetails userDetails, Model model) {

        List<ProductListResponse> productList = productService.getMemberProductList(owner_id)
                .stream().map(p -> ProductListResponse.builder()
                        .id(p.getId())
                        .title(p.getTitle())
                        .price(p.getPrice())
                        .productStatus(p.getProductStatus())
                        .heartCount(p.getHeartCount())
                        .replyCount(p.getReplyCount())
                        .thumbnailImage(
                                p.getProductImages().size() == 0 ?
                                        ProductImageNotInit.SERVER_FILE_NAME :
                                        p.getProductImages().get(0).getServerFileName()
                        ).build())
                .collect(Collectors.toList());

        Member findMember = memberService.findMember(userDetails.getUsername());
        MemberFitResponse memberInfo = toMemberFitResponse(findMember);

        model.addAttribute("memberInfo", memberInfo);
        model.addAttribute("pageInfo", product_id);
        model.addAttribute("productList", productList);

        return "products/MemberProductList";

    }

    /**
     * 상품등록 페이지로 이동합니다.
     * @param model 카테고리 리스트를 담을 모델
     * @return 상품등록 페이지 url
     */
    @GetMapping("/register")
    public String registerPage(Model model) {

        model.addAttribute("form", new ProductRegisterRequest());
        model.addAttribute("categoryList", categoryService.getCategoryList());
        return "products/registerForm";

    }

    /**
     * 상픔등록 요청을 처리 후 201 상태코드를 반환합니다.
     * @param form 등록 할 상품정보
     * @param userDetails 대응되는 회원정보
     * @return 상품리스트 페이지
     * @throws IOException
     */
    @PostMapping("/register")
    public String registerProc(@ModelAttribute @Validated ProductRegisterRequest form,
                               @AuthenticationPrincipal UserDetails userDetails) throws IOException {

        // 사용자 조회
        Member findMember = memberService.findMember(userDetails.getUsername());
        // 상품엔티티 변환
        Product product = form.toProductEntity(findMember);
        Product register = productService.register(product);

        // 이미지가 없다면 기본이미지로 셋팅
        if (form.getProductImages().size() == 1 && form.getProductImages().get(0)
                .getOriginalFilename().equals("")) {
            ProductImage productImage = ProductImage.builder()
                    .filePath(ProductImageNotInit.FILE_PATH)
                    .originalFileName(ProductImageNotInit.ORIGINAL_FILE_NAME)
                    .serverFileName(ProductImageNotInit.SERVER_FILE_NAME)
                    .product(register)
                    .build();
            productImageService.save(productImage);
        } else { // 이미지가 있다면 S3에 이미지 등록 후 DB에 저장
            ArrayList<ProductImage> productImages = s3Uploader.uploadList(form.getProductImages(), "products", register);
            for (ProductImage productImage : productImages) {
                productImageService.save(productImage);
            }
        }

        return "redirect:/products/";

    }

    /**
     * 좋아요를 반영합니다.
     * @param productId 반영될 상품 아이디
     * @param userDetails 좋아요 버튼을 누른 회원
     * @return 성공 메시지
     */
    @PostMapping("/{productId}/addHeart")
    @ResponseBody
    public String addHeart(@PathVariable Long productId, @AuthenticationPrincipal UserDetails userDetails) {

        // 사용자 및 상품조회
        Member member = memberService.findMember(userDetails.getUsername());
        Product product = productService.findProduct(productId);

        // 관심상품 테이블에 추가
        heartService.addHeart(new Heart(
                productId, member, product
        ));

        // 상품 관심 갯수 증가
        productService.addHeartCount(product);

        return "success";
    }

    /**
     * 좋아요를 취소합니다.
     * @param productId 취소될 상품 아이디
     * @param userDetails 취소 버튼을 누른 회원
     * @return 성공 메시지
     */
    @DeleteMapping("/{productId}/deleteHeart")
    @ResponseBody
    public String deleteHeart(@PathVariable Long productId, @AuthenticationPrincipal UserDetails userDetails) {
        Member member = memberService.findMember(userDetails.getUsername());
        heartService.deleteHeart(productId, member.getId());
        productService.deleteHeartCount(productService.findProduct(productId));
        return "success";
    }

    /**
     * 상품 상세조회 DTO로 변환합니다.
     * @param product 상품엔티티
     * @return
     */
    private ProductDetailResponse toProductDetailResponse(Product product) {
        return ProductDetailResponse.builder()
                .id(product.getId())
                .title(product.getTitle())
                .description(product.getDescription())
                .price(product.getPrice())
                .category(product.getCategory().getValue())
                .productStatus(product.getProductStatus())
                .heartCount(product.getHeartCount())
                .replyCount(product.getReplyCount())
                .productImages(product.getProductImages()
                        .stream()
                        .map(p -> p.getServerFileName())
                        .collect(Collectors.toList()))
                .build();
    }

    /**
     * 상품리스트 DTO로 변환합니다.
     * @return 상품리스트 DTO
     */
    private List<ProductListResponse> toProductListResponse() {
        return productService.getAllProducts()
                .stream().map(p -> new ProductListResponse(
                        p.getId(),
                        p.getTitle(),
                        p.getPrice(),
                        p.getHeartCount(),
                        p.getReplyCount(),
                        p.getProductStatus(),
                        p.getProductImages().size() == 0 ?
                                ProductImageNotInit.SERVER_FILE_NAME : p.getProductImages().get(0).getServerFileName()
                )).collect(Collectors.toList ());
    }

    /**
     * 맴버 정보 DTO로 변환합니다.
     * @param member 맴버 엔티티
     * @return
     */
    private MemberFitResponse toMemberFitResponse(Member member) {
        return MemberFitResponse.builder()
                .memberId(member.getId())
                .nickName(member.getNickName())
                .profileImage(
                        member.getMemberImage().getServerFileName())
                .heartProducts(
                        member.getHearts().stream().map(h -> h.getProductInfo()).collect(Collectors.toList())
                )
                .build();
    }

}
