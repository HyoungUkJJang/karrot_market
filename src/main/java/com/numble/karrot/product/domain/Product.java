package com.numble.karrot.product.domain;

import com.numble.karrot.category.domain.Category;
import com.numble.karrot.common.BaseEntity;
import com.numble.karrot.member.domain.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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

}
