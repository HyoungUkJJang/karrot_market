package com.numble.karrot.member.exception;

/**
 * 가입 시 이메일이 중복일 경우 발생시킬 예외 클래스 입니다.
 */
public class MemberEmailDuplicateException extends RuntimeException {
    public MemberEmailDuplicateException() {
        super("회원님이 입력하신 이메일이 중복입니다.");
    }
}
