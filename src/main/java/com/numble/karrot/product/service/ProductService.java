package com.numble.karrot.product.service;

import com.numble.karrot.product.domain.Product;

import java.util.List;

/**
 * 상품 등록, 조회, 수정, 삭제 기능을 제공하는 서비스 클래스 입니다.
 */
public interface ProductService {

    /**
     * 상품을 등록하고 등록된 상품을 리턴합니다.
     * @param product 등록하려는 상품의 정보
     * @return 등록된 상품
     */
    Product register(Product product);

    /**
     * 전체 상품을 조회하여 리턴합니다.
     * @return 상품리스트
     */
    List<Product> getAllProducts();

    /**
     * 상품 하나를 조회하여 리턴합니다.
     * @param id 조회하려는 상품의 id
     * @return 조회된 상품
     */
    Product findProduct(Long id);

    /**
     * 내가 등록한 상품들을 조회하여 리턴합니다.
     * @param memberId 나의 아이디
     * @return 조회된 상품리스트
     */
    List<Product> getMyProductList(Long memberId);

    /**
     * 특정 유저의 상품들을 조회하여 리턴합니다.
     * @param memberId 상품을 조회할 유저의 아이디
     * @return 조회된 상품리스트
     */
    List<Product> getMemberProductList(Long memberId);

    Product updateProduct(Long product_id, Product updateForm);

    List<Product> getMyHeartsProductList(Long memberId);

}
