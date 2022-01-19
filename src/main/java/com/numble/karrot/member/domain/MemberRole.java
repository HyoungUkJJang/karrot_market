package com.numble.karrot.member.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 회원의 권한정보를 가지고 있는 클래스
 */
@Getter
@AllArgsConstructor
public enum MemberRole {

    ROLE_USER("ROLE_USER"),
    ROLE_MANAGER("ROLE_MANAGER"),
    ROLE_ADMIN("ROLE_ADMIN");

    private String value;

}
