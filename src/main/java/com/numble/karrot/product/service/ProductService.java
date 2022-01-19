package com.numble.karrot.product.service;

import com.numble.karrot.product.domain.Product;

import java.util.List;

/**
 * 상품 등록, 조회, 수정, 삭제 기능을 제공하는 서비스 클래스 입니다.
 */
public interface ProductService {

    Product register(Product product);

    List<Product> getAllProducts();

    Product findProduct(Long id);

    List<Product> getMyProductList(Long memberId);

}
