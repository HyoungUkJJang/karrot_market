package com.numble.karrot.product_image.repository;

import com.numble.karrot.product_image.domain.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 상품 이미지 리포지토리 입니다.
 */
public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
    void deleteByProductId(Long product_id);
}
