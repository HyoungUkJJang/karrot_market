package com.numble.karrot.member.service;

import com.numble.karrot.member.domain.Member;

/**
 * 회원 생성, 수정, 삭제 서비스를 제공하는 클래스입니다.
 */
public interface MemberService {

    /**
     * 사용자의 정보를 받아 회원가입을 수행합니다.
     * @param member 사용자의 정보
     * @return 가입된 사용자 정보
     */
    Member join(Member member);

    /**
     * 사용자의 이메일을 통해 사용자를 조회합니다.
     * @param email 조회할 사용자 이메일
     * @return 조회된 사용자
     */
    Member findMember(String email);

    /**
     * 사용자의 닉네임을 수정합니다.
     * @param email 수정활 사용자 이메일
     * @param newNickName 수정될 닉네임
     * @return 수정된 유저
     */
    Member update(String email, String newNickName);

    /**
     * 사용자 아이디의 이메일 중복 여부를 학인하여 true / false 를 리턴합니다.
     * @param email 검증할 이메일
     * @return 중복 : true | 사용가능 : false
     */
    boolean duplicateEmailCheck(String email);

}
