package com.numble.karrot.controller;

import com.numble.karrot.aws.service.S3Uploader;
import com.numble.karrot.category.service.CategoryService;
import com.numble.karrot.member.domain.Member;
import com.numble.karrot.member.dto.MemberFitResponse;
import com.numble.karrot.member.service.MemberService;
import com.numble.karrot.product.domain.Product;
import com.numble.karrot.product.dto.ProductDetailResponse;
import com.numble.karrot.product.dto.ProductListResponse;
import com.numble.karrot.product.dto.ProductRegisterRequest;
import com.numble.karrot.product.service.ProductService;
import com.numble.karrot.product_image.domain.ProductImage;
import com.numble.karrot.product_image.service.ProductImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 상품과 관련된 HTTP 요청을 처리하는 컨트롤러 클래스입니다.
 */
@Controller
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final ProductImageService productImageService;
    private final CategoryService categoryService;
    private final MemberService memberService;
    private final S3Uploader s3Uploader;

    /**
     * 상품리스트 페이지로 이동합니다.
     * @param model 상품 리스트 정보를 담을 모델
     * @return 상품리스트 페이지 url
     */
    @GetMapping
    public String productsPage(Model model) {

        List<ProductListResponse> productList = toProductListResponse();
        model.addAttribute("productList", productList);
        return "/products/ProductList";

    }

    /**
     * 상품 상세조회 페이지 이동
     * @param id 상세조회 할 상품의 아이디
     * @param model 상품 상세정보, 회원 정보를 담을 모델
     * @return 상품 상세조회 페이지 url
     */
    @GetMapping("/{id}")
    public String detailProduct(@PathVariable Long id, Model model) {

        Product findProduct = productService.findProduct(id);
        ProductDetailResponse responseProduct =
                toProductDetailResponse(findProduct);

        Member member = memberService
                .findMember(findProduct.getMember().getEmail());
        MemberFitResponse responseMember = toMemberFitResponse(member);

        model.addAttribute("productDetail", responseProduct);
        model.addAttribute("memberInfo", responseMember);

        return "/products/ProductDetail";

    }

    /**
     * 상품등록 페이지로 이동합니다.
     * @param model 카테고리 리스트를 담을 모델
     * @return 상품등록 페이지 url
     */
    @GetMapping("/register")
    public String registerPage(Model model) {

        model.addAttribute("categoryList", categoryService.getCategoryList());
        return "/products/registerForm";

    }

    /**
     * 상픔등록 요청을 처리 후 201 상태코드를 반환합니다.
     * @param form 등록 할 상품정보
     * @param userDetails 대응되는 회원정보
     * @return 상품리스트 페이지
     * @throws IOException
     */
    @PostMapping("/register")
    public String registerProc(@Validated ProductRegisterRequest form,
                               @AuthenticationPrincipal UserDetails userDetails) throws IOException {

        Member findMember = memberService.findMember(userDetails.getUsername());
        Product product = form.toProductEntity(findMember);
        Product register = productService.register(product);

        ArrayList<ProductImage> productImages = s3Uploader.uploadList(form.getProductImages(), "products", register);
        for (ProductImage productImage : productImages) {
            productImageService.save(productImage);
        }

        return "/products/ProductList";

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
                .heartCount(product.getHeartCount())
                .replyCount(product.getReplyCount())
                .productImages(product.getProductImages())
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
                        p.getProductImages().get(0).getServerFileName()
                )).collect(Collectors.toList());
    }

    /**
     * 맴버 정보 DTO로 변환합니다.
     * @param member 맴버 엔티티
     * @return
     */
    private MemberFitResponse toMemberFitResponse(Member member) {
        return MemberFitResponse.builder()
                .nickName(member.getNickName())
                .profileImage( member.getMemberImage().getFilePath()+
                        member.getMemberImage().getServerFileName())
                .build();
    }

}
