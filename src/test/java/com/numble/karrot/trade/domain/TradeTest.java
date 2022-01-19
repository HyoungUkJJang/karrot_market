package com.numble.karrot.trade.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TradeTest {

    @Test
    public void 거래정보_생성테스트() {
        //GIVEN
        Trade trade = Trade.builder()
                .tradeQuantity(TradeInit.TRADE_QUANTITY)
                .donationQuantity(TradeInit.DONATION_QUANTITY)
                .build();

        //WHEN
        int checkedTradeQuantity = 0;
        int checkedDonationQuantity = 0;

        //THEN
        assertEquals(checkedTradeQuantity, trade.getTradeQuantity());
        assertEquals(checkedDonationQuantity, trade.getDonationQuantity());
    }

}
