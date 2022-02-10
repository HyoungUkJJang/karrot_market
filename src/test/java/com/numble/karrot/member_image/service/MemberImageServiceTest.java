package com.numble.karrot.member_image.service;

import com.numble.karrot.member_image.domain.MemberImage;
import com.numble.karrot.member_image.domain.MemberImageInit;
import com.numble.karrot.member_image.repository.MemberImageRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.verify;

@DisplayName("MemberImageService 클래스는")
@ExtendWith(MockitoExtension.class)
class MemberImageServiceTest {

    private static final Long VALID_ID = 1L;
    private static final String UPDATE_FILEPATH = "newPath";
    private static final String UPDATE_ORIGINAL_FILENAME = "newOriginalFileName.jpg";
    private static final String UPDATE_SERVER_FILENAME = "newServerFileName.jpg";

    @Mock
    private MemberImageRepository memberImageRepository;
    @InjectMocks
    private MemberImageServiceImpl memberImageService;

    @Nested
    @DisplayName("save 메서드는")
    class Describe_save {

        @Nested
        @DisplayName("사용자가 회원가입을 했을 경우에")
        class Context_join_member {

            MemberImage memberImage;

            @BeforeEach
            void setUp() {
                memberImage = createMemberImage();
                given(memberImageRepository.save(any(MemberImage.class)))
                        .willReturn(memberImage);
            }

            @Test
            @DisplayName("기본값 프로필 이미지 객체를 생성하여 저장 후 리턴합니다.")
            void It_save_default_memberImage() {
                MemberImage saveMemberImage = memberImageService.save(createMemberImage());
                assertThat(saveMemberImage.getServerFileName())
                        .isEqualTo(MemberImageInit.DEFAULT_SERVER_FILE_NAME);
                verify(memberImageRepository).save(any(MemberImage.class));
            }

        }

    }

    @Nested
    @DisplayName("findMemberImage 메서드는")
    class Describe_findMemberImage {

        @Nested
        @DisplayName("조회하려는 사용자의 이미지를")
        class Context_select_memberImage {

            MemberImage memberImage;

            @BeforeEach
            void setUp() {
                memberImage = createMemberImage();
                given(memberImageRepository.findByMemberId(any(Long.class)))
                        .willReturn(memberImage);
            }

            @Test
            @DisplayName("사용자의 아이디를 통해 이미지를 조회하여 리턴합니다.")
            void It_select_memberId_return() {

                MemberImage findMemberImage = memberImageService.findMemberImage(VALID_ID);
                assertThat(findMemberImage.getServerFileName())
                        .isEqualTo(MemberImageInit.DEFAULT_SERVER_FILE_NAME);
                verify(memberImageRepository).findByMemberId(any(Long.class));

            }

        }

    }

    @Nested
    @DisplayName("updateMemberImage 메서드는")
    class Describe_updateMemberImage {

        @Nested
        @DisplayName("사용짜가 프로필 이미지를 수정한 경우에")
        class Context_update_req_memberImage {

            @Mock
            MemberImage memberImage = createMemberImage();

            @BeforeEach
            void setUp() {
                willDoNothing().given(memberImage).updateMemberImage(
                        any(String.class),
                        any(String.class),
                        any(String.class)
                );
            }

            @Test
            @DisplayName("새로운 사용자 이미지 정보로 업데이트 합니다.")
            void It_update_memberImage() {
                memberImageService.updateMemberImage(
                        memberImage,
                        UPDATE_FILEPATH,
                        UPDATE_ORIGINAL_FILENAME,
                        UPDATE_SERVER_FILENAME
                );
                verify(memberImage).updateMemberImage(
                        any(String.class),
                        any(String.class),
                        any(String.class)
                );
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
