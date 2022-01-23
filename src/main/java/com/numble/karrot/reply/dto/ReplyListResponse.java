package com.numble.karrot.reply.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 댓글 리스트 응답 DTO 클래스 입니다.
 */
@Getter
@Setter
@NoArgsConstructor
public class ReplyListResponse {

    private String comment;
    private String memberEmail;
    private String memberProfile;

    @Builder
    public ReplyListResponse(String comment, String memberEmail, String memberProfile) {
        this.comment = comment;
        this.memberEmail = memberEmail;
        this.memberProfile = memberProfile;
    }

}
