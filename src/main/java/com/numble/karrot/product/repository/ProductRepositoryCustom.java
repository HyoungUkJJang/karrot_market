package com.numble.karrot.product.repository;

import com.numble.karrot.product.domain.Product;

import java.util.List;

/**
 * 상품과 관련된 쿼리문을 위한 커스텀 리포지토리 클래스 입니다.
 */
public interface ProductRepositoryCustom {

    /**
     * 자신이 등록한 상품 리스트를 조회하여 리턴합니다.
     * @param memberId 조회할 회원의 아이디
     * @return 자신의 상품리스트
     */
    List<Product> findMyProducts(Long memberId);

}
