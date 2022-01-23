package com.numble.karrot.member.exception;

/**
 * 사용자를 찾을 수 없을 경우 발생시킬 예외 클래스 입니다.
 */
public class MemberNotFoundException extends RuntimeException {
    public MemberNotFoundException() {
        super("사용자를 찾을 수 없습니다.");
    }
}
