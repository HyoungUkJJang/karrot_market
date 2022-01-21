package com.numble.karrot.member.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MemberFitResponse {

    private Long memberId;
    private String nickName;
    private String profileImage;

    @Builder
    public MemberFitResponse(Long memberId, String nickName, String profileImage) {
        this.memberId = memberId;
        this.nickName = nickName;
        this.profileImage = profileImage;
    }

}
