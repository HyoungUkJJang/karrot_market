package com.numble.karrot.category.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CategoryTest {

    @Test
    public void 카테고리_생성테스트() {

        //GIVEN
        Category category = new Category("DIGITAL", "디지털기기");

        //WHEN
        String checkedValue = "디지털기기";

        //THEN
        assertEquals(checkedValue, category.getValue());

    }

}
