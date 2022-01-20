package com.numble.karrot.member.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

/**
 * 로그인을 위한 요청 DTO
 */
@Getter
@Setter
@NoArgsConstructor
public class MemberLoginRequest {

    @NotEmpty(message = "이메일은 필수입니다.")
    private String email;
    @NotEmpty(message = "패스워드는 필수입니다.")
    private String password;

    @Builder
    public MemberLoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

}
