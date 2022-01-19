package com.numble.karrot.product.dto;

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
    private String thumbnailImage;

    @Builder
    public ProductListResponse(Long id, String title, int price, String thumbnailImage) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.thumbnailImage = thumbnailImage;
    }

}
