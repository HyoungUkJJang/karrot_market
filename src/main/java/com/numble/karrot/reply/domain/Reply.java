package com.numble.karrot.reply.domain;

import com.numble.karrot.common.BaseEntity;
import com.numble.karrot.member.domain.Member;
import com.numble.karrot.product.domain.Product;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * 상품의 댓글 도메인 클래스 입니다.
 */
@Entity
@Getter
@NoArgsConstructor
public class Reply extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "TEXT")
    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public Reply(String comment, Product product, Member member) {
        this.comment = comment;
        this.product = product;
        this.member = member;
    }

}
