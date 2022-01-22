package com.numble.karrot.member.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class MemberFitResponse {

    private Long memberId;
    private String nickName;
    private String profileImage;
    private List<Long> heartProducts;

    @Builder
    public MemberFitResponse(Long memberId, String nickName, String profileImage, List<Long> heartProducts) {
        this.memberId = memberId;
        this.nickName = nickName;
        this.profileImage = profileImage;
        this.heartProducts = heartProducts;
    }

}
