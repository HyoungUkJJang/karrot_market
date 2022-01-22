package com.numble.karrot.product.dto;

import com.numble.karrot.product.domain.ProductStatus;
import com.numble.karrot.product_image.domain.ProductImage;
import com.numble.karrot.product_image.domain.ProductImageNotInit;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * 상품 상세조회 응답 DTO 클래스입니다.
 */
@Getter
@Setter
@NoArgsConstructor
public class ProductDetailResponse {

    private Long id;
    private String title;
    private int price;
    private String description;
    private ProductStatus productStatus;
    private String category;
    private List<String> productImages;
    private Integer heartCount;
    private Integer replyCount;

    @Builder
    public ProductDetailResponse(Long id, String title, int price, String description, ProductStatus productStatus, String category, List<String> productImages, Integer heartCount, Integer replyCount) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.description = description;
        this.productStatus = productStatus;
        this.category = category;
        this.productImages = productImages;
        this.heartCount = heartCount;
        this.replyCount = replyCount;
    }

    /**
     * 상품에 사진이 없을 경우 기본 이미지를 세팅해줍니다.
     */
    public void addProductNotImage() {
        productImages.add(ProductImageNotInit.SERVER_FILE_NAME);
    }

}
