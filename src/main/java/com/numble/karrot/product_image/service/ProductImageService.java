package com.numble.karrot.product_image.service;

import com.numble.karrot.product_image.domain.ProductImage;

/**
 * 상품 이미지와 관련된 기능을 제공하는 클래스 입니다.
 */
public interface ProductImageService {

    /**
     * 상품 이미지 정보를 저장합니다.
     * @param productImage 저장할 상품 이미지 정보
     * @return 저장된 상품 이미지 정보
     */
    ProductImage save(ProductImage productImage);

}
