package com.numble.karrot.trade.service;

import com.numble.karrot.trade.domain.Trade;

/**
 * 사용자의 거래정보를 제공하는 클래스입니다.
 */
public interface TradeService {

    /**
     * 사용자 거래 정보를 저장합니다.
     * @param trade 저장할 거래 정보
     * @return 저장된 거래 정보
     */
    Trade save(Trade trade);

}
