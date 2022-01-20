package com.numble.karrot.member.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

/**
 * 회원 수정요청 DTO 클래스입니다.
 */
@Getter
@Setter
@NoArgsConstructor
public class MemberUpdateRequest {

    private MultipartFile memberImage;
    private String nickName;

    @Builder
    public MemberUpdateRequest(MultipartFile memberImage, String nickName) {
        this.memberImage = memberImage;
        this.nickName = nickName;
    }

}
