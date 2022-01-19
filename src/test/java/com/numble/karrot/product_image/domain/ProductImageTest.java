package com.numble.karrot.product_image.domain;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProductImageTest {

    @Test
    public void 상품이미지_생성테스트() throws Exception {

        //GIVEN
        ProductImage productImage = ProductImage.builder()
                .filePath("/home")
                .originalFileName("karrot.png")
                .serverFileName(UUID.randomUUID().toString())
                .build();

        //WHEN
        String checkedFilePath = "/home";
        String checkedServerFileName = productImage.getServerFileName();

        //THEN
        assertEquals(checkedFilePath, productImage.getFilePath());
        assertEquals(checkedServerFileName, productImage.getServerFileName());

    }

}
