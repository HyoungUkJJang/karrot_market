package com.numble.karrot.member.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MemberTest {

    @Test
    public void 회원도메인_생성테스트() {
        //GIVEN
        Member member = Member.builder()
                .email("test@mail.com")
                .password("12345")
                .name("김형욱")
                .phone("010-0000-0000")
                .nickName("캐럿맨")
                .memberRole(MemberRole.ROLE_USER)
                .build();

        //WHEN
        MemberRole checkedRole = MemberRole.ROLE_USER;
        String checkedEmail = "test@mail.com";
        String checkedName = "김형욱";

        //THEN
        assertEquals(checkedEmail, member.getEmail());
        assertEquals(checkedName, member.getName());
        assertEquals(checkedRole, member.getMemberRole());
    }

}
