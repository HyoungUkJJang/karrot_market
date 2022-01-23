package com.numble.karrot.reply.service;

import com.numble.karrot.reply.domain.Reply;

import java.util.List;

/**
 * 댓글 등록 및 조회하는 서비스 클래스 입니다.
 */
public interface ReplyService {

    /**
     * 댓글을 등록합니다.
     * @param reply 등록 할 댓글 정보
     * @return 등록된 댓글
     */
    Reply registerReply(Reply reply);

    /**
     * 댓글을 조회합니다.
     * @param productId 조회할 댓글의 상품 아이디
     * @return 조회된 댓글 리스트
     */
    List<Reply> getReplyList(Long productId);

    void deleteReply(Long productId);

}
