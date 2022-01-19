package com.numble.karrot.product.exception;

/**
 * 상품을 찾을 수 없을 경우에 던지는 예외 클래스 입니다.
 */
public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException() {
        super("조회하신 상품은 존재하지 않습니다.");
    }
}
