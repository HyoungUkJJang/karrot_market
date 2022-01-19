package com.numble.karrot.product.repository;

import com.numble.karrot.product.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 상품 리포지토리 입니다.
 */
public interface ProductRepository extends JpaRepository<Product, Long> {
}
