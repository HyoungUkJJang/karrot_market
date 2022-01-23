package com.numble.karrot.heart.domain;

import com.numble.karrot.member.domain.Member;
import com.numble.karrot.product.domain.Product;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * 관심상품 엔티티 도메인
 */
@Entity
@Getter
@NoArgsConstructor
public class Heart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long productInfo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Builder
    public Heart(Long productInfo, Member member, Product product) {
        this.productInfo = productInfo;
        this.member = member;
        this.product = product;
    }

}
