package com.numble.karrot.product.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    @Test
    public void 상품도메인_생성테스트() {

        //GIVEN
        Product product = Product.builder()
                .title("title1")
                .description("description1")
                .price(1500)
                .heartCount(ProductInit.HEART_COUNT)
                .replyCount(ProductInit.REPLY_COUNT)
                .build();

        //WHEN
        String checkedTitle = "title1";
        int checkedPrice = 1500;
        int checkedReplyCount = 0;

        //THEN
        assertEquals(checkedTitle, product.getTitle());
        assertEquals(checkedPrice, product.getPrice());
        assertEquals(checkedReplyCount, product.getReplyCount());

    }

}
