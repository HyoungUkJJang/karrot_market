package com.numble.karrot.product.dto;

import com.numble.karrot.product.domain.ProductStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 상품 리스트 응답 전용 DTO 클래스 입니다.
 */
@Getter
@Setter
@NoArgsConstructor
public class ProductListResponse {

    private Long id;
    private String title;
    private int price;
    private int heartCount;
    private int replyCount;
    private ProductStatus productStatus;
    private String thumbnailImage;

    @Builder
    public ProductListResponse(Long id, String title, int price, int heartCount, int replyCount, ProductStatus productStatus, String thumbnailImage) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.heartCount = heartCount;
        this.replyCount = replyCount;
        this.productStatus = productStatus;
        this.thumbnailImage = thumbnailImage;
    }
}
