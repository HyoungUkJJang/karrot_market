package com.numble.karrot.product.repository;

import com.numble.karrot.product.domain.Product;
import com.numble.karrot.product.domain.ProductStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

/**
 * 상품 리포지토리 입니다.
 */
public interface ProductRepository extends JpaRepository<Product, Long>, ProductRepositoryCustom {

    /**
     * 회원이 올린 상품들을 조회하여 리턴합니다.
     * @param memberId 조회할 회원의 아이디
     * @return 회원의 상품리스트
     */
    List<Product> findByMemberId(Long memberId);

    List<Product> findByMemberIdAndProductStatus(Long memberId, ProductStatus productStatus);

    List<Product> findByMemberIdAndProductStatusIn(Long memberId, Collection<ProductStatus> productStatuses);





}
