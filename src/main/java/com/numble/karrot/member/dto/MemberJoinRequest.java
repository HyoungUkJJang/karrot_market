package com.numble.karrot.member.dto;

import com.numble.karrot.member.domain.Member;
import com.numble.karrot.member.domain.MemberRole;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

/**
 * 회원가입 요청을 위한 DTO 클래스입니다.
 */
@Getter
@Setter
@NoArgsConstructor
public class MemberJoinRequest {

    @NotEmpty(message = "이메일 입력은 필수입니다.")
    private String email;
    @NotEmpty(message = "패스워드 입력은 필수입니다.")
    private String password;
    @NotEmpty(message = "이름 입력은 필수입니다.")
    private String name;
    @NotEmpty(message = "핸드폰 번호 입력은 필수입니다.")
    private String phone;
    @NotEmpty(message = "닉네임 이름은 필수입니다.")
    private String nickName;

    @Builder
    public MemberJoinRequest(String email, String password, String name, String phone, String nickName) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.nickName = nickName;
    }

    /**
     * 회원 엔티티로 변환합니다.
     * @return 회원 엔티티
     */
    public Member toMemberEntity() {
        return Member.builder()
                .email(this.getEmail())
                .password(this.getPassword())
                .name(this.getName())
                .phone(this.getPhone())
                .nickName(this.getNickName())
                .memberRole(MemberRole.ROLE_USER)
                .build();
    }

}
