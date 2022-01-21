package com.numble.karrot.reply.dto;

import com.numble.karrot.member.domain.Member;
import com.numble.karrot.product.domain.Product;
import com.numble.karrot.reply.domain.Reply;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 댓글 등록 DTO 클래스입니다.
 */

@Getter
@Setter
@NoArgsConstructor
public class ReplyRegisterRequest {
    private String comment;

    public ReplyRegisterRequest(String comment) {
        this.comment = comment;
    }

    /**
     * 댓글 엔티티로 변환하여 리턴합니다.
     * @param member 대응되는 회원 엔티티
     * @param product 대응되는 상품 엔티티
     * @return
     */
    public Reply toReplyEntity(Member member, Product product) {
        return Reply.builder()
                .comment(this.comment)
                .member(member)
                .product(product)
                .build();
    }

}
