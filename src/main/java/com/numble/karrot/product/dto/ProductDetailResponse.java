package com.numble.karrot.product.dto;

import com.numble.karrot.product_image.domain.ProductImage;
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
    private String category;
    private List<ProductImage> productImages;
    private Integer heartCount;
    private Integer replyCount;

    @Builder
    public ProductDetailResponse(Long id, String title, int price, String description,
                                 String category, List<ProductImage> productImages, Integer heartCount, Integer replyCount) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.description = description;
        this.category = category;
        this.productImages = productImages;
        this.heartCount = heartCount;
        this.replyCount = replyCount;
    }

}
