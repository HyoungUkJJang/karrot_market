package com.numble.karrot.product.dto;

import com.numble.karrot.product.domain.ProductStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 상품 상태변경 DTO 클래스 입니다.
 */
@Getter
@Setter
@NoArgsConstructor
public class ProductStatusRequest {

    private ProductStatus productStatus;

    public ProductStatusRequest(ProductStatus productStatus) {
        this.productStatus = productStatus;
    }

}
