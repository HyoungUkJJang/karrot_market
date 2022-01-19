package com.numble.karrot.product.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 상품의 거래상태를 제공하는 클래스입니다.
 */
@Getter
@AllArgsConstructor
public enum ProductStatus {

    TRADING("거래중"),
    RESERVED("예약중"),
    TRADE_COMPLETED("거래완료");

    private String value;

}
