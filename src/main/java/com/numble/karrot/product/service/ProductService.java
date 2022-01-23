package com.numble.karrot.product.service;

import com.numble.karrot.product.domain.Product;
import com.numble.karrot.product.domain.ProductStatus;

import java.util.Collection;
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

    /**
     * 상품을 수정 후 수정된 상품을 리턴합니다.
     * @param product_id 수정할 상품의 아이디
     * @param updateForm 수정될 상품의 정보
     * @return 수정된 상품
     */
    Product updateProduct(Long product_id, Product updateForm);

    /**
     * 상품을 삭제합니다.
     * @param product_id 삭제할 상품의 아이디
     */
    void deleteProduct(Long product_id);

    /**
     * 나의 관심목록 상품을 조회하여 리턴합니다.
     * @param memberId 회원의 아이디
     * @return 관심상품 목록
     */
    List<Product> getMyHeartsProductList(Long memberId);

    /**
     * 내가 판매중인 상품 목록을 리턴합니다.
     * @param memberId 회원의 아이디
     * @param productStatuses 판매중 or 예약중인 상품
     * @return 조회된 상품 리스트
     */
    List<Product> getMyTradingProductList(Long memberId, Collection<ProductStatus> productStatuses);

    /**
     * 거래 완료된 상품 목록을 리턴합니다.
     * @param memberId 회원의 아이디
     * @param productStatus 거래완료 상태
     * @return 조회된 상품 리스트
     */
    List<Product> getMyCompleteProductList(Long memberId, ProductStatus productStatus);

    /**
     * 상품의 상태를 변경합니다.
     * @param status 변경될 상품의 상태
     */
    void changedProductStatus(Product product,ProductStatus status);

    /**
     * 좋아요 개수를 증가시킵니다.
     * @param product 해당 엔티티
     */
    void addHeartCount(Product product);

    /**
     * 좋아요 개수를 감소시킵니다.
     * @param product 해당 엔티티
     */
    void deleteHeartCount(Product product);

    /**
     * 댓글 개수를 증가시킵니다.
     * @param product 해당 엔티티
     */
    void addReplyCount(Product product);

    /**
     * 댓글 개수를 감소시킵니다.
     * @param product 해당 엔티티
     */
    void deleteReplyCount(Product product);

}
