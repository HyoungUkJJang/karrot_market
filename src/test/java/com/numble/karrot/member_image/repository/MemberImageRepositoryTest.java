package com.numble.karrot.member_image.repository;

import com.numble.karrot.config.QueryDslConfig;
import com.numble.karrot.member.domain.Member;
import com.numble.karrot.member_image.domain.MemberImage;
import com.numble.karrot.member_image.domain.MemberImageInit;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(QueryDslConfig.class)
@DisplayName("MemberImageRepository 클래스")
class MemberImageRepositoryTest {

    @Autowired
    private MemberImageRepository memberImageRepository;

    @Nested
    @DisplayName("save 메서드는")
    class Describe_save {

        @Nested
        @DisplayName("저장할 멤버의 프로필사진이 있을 경우에")
        class Context_exist_memberImage {

            MemberImage memberImage;

            @BeforeEach
            void setUp() {
                memberImage = createMemberImage();
            }

            @Test
            @DisplayName("저장소에 맴버 이미지 정보를 저장 후 리턴합니다.")
            void It_return_memberImage() {
                MemberImage saveMemberImage = memberImageRepository.save(memberImage);
                assertThat(saveMemberImage.getServerFileName())
                        .isEqualTo(MemberImageInit.DEFAULT_SERVER_FILE_NAME);
            }

        }

    }



    /**
     * 테스트 멤버이미지 객체를 생성합니다.
     * @return
     */
    MemberImage createMemberImage() {
        return MemberImage.builder()
                .filePath(MemberImageInit.DEFAULT_PATH)
                .originalFileName(MemberImageInit.DEFAULT_ORIGINAL_FILE_NAME)
                .serverFileName(MemberImageInit.DEFAULT_SERVER_FILE_NAME)
                .build();
    }

}
