package com.numble.karrot.member.exception;

public class MemberEmailDuplicateException extends RuntimeException {
    public MemberEmailDuplicateException() {
        super("회원님이 입력하신 이메일이 중복입니다.");
    }
}
