package com.numble.karrot.member.exception;

public class MemberNotFoundException extends RuntimeException {
    public MemberNotFoundException() {
        super("사용자를 찾을 수 없습니다.");
    }
}
