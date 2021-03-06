package com.numble.karrot.product.domain;

import com.numble.karrot.category.domain.Category;
import com.numble.karrot.common.BaseEntity;
import com.numble.karrot.member.domain.Member;
import com.numble.karrot.product_image.domain.ProductImage;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 상품 도메인 클래스입니다.
 */
@Entity
@Getter
@NoArgsConstructor
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    Long id;

    @Column(length = 30)
    private String title;
    @Column(columnDefinition = "TEXT")
    private String description;
    private Integer price;
    private Integer heartCount;
    private Integer replyCount;

    @Enumerated(EnumType.STRING)
    private ProductStatus productStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "product")
    private List<ProductImage> productImages = new ArrayList<>();

    @Builder
    public Product(String title, String description, Integer price, Integer heartCount,
                   Integer replyCount, ProductStatus productStatus, Member member, Category category) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.heartCount = heartCount;
        this.replyCount = replyCount;
        this.productStatus = productStatus;
        this.member = member;
        this.category = category;
    }

    /**
     * 상품을 수정합니다.
     * @param product 수정될 상품의 정보
     * @return 수정된 정보
     */
    public Product update(Product product) {
        this.title = product.getTitle();
        this.price = product.getPrice();
        this.description = product.getDescription();
        this.category = product.getCategory();
        return this;
    }

    /**
     * 관심 갯수를 증가시킵니다.
     */
    public void addHeartCount() {
        this.heartCount++;
    }

    /**
     * 관심 갯수를 하나 감소합니다.
     */
    public void deleteHeartCount() {
        this.heartCount--;
    }

    /**
     * 댓글 갯수를 증가시킵니다.
     */
    public void addReplyCount() {
        this.replyCount++;
    }

    /**
     * 댓글 갯수를 하나 감소합니다.
     */
    public void deleteReplyCount() {
        this.replyCount--;
    }

    /**
     * 상품 상태를 수정합니다.
     * @param productStatus 수정할 상태정보
     */
    public void changedStatus(ProductStatus productStatus) {
        this.productStatus = productStatus;
    }

}
