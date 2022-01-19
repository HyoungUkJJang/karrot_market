package com.numble.karrot.member.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MemberFitResponse {

    private String nickName;
    private String profileImage;

    @Builder
    public MemberFitResponse(String nickName, String profileImage) {
        this.nickName = nickName;
        this.profileImage = profileImage;
    }

}
