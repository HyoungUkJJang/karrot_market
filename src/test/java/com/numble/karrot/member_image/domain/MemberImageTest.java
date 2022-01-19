package com.numble.karrot.member_image.domain;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class MemberImageTest {

    @Test
    public void 회원이미지_생성테스트() {
        //GIVEN
        MemberImage memberImage = MemberImage.builder()
                .filePath("/home")
                .originalFileName("profile.png")
                .serverFileName(UUID.randomUUID().toString())
                .build();

        //WHEN
        String checkedFilePath = "/home";
        String checkedServerFileName = memberImage.getServerFileName();

        //THEN
        assertEquals(checkedFilePath, memberImage.getFilePath());
        assertEquals(checkedServerFileName, memberImage.getServerFileName());
    }

}
