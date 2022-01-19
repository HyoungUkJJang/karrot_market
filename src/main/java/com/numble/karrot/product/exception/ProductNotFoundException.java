package com.numble.karrot.product.exception;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException() {
        super("조회하신 상품은 존재하지 않습니다.");
    }
}
