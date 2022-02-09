package com.numble.karrot.member.repository;

import com.numble.karrot.config.QueryDslConfig;
import com.numble.karrot.member.domain.Member;
import com.numble.karrot.member.domain.MemberRole;
import org.aspectj.lang.annotation.Before;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(QueryDslConfig.class)
@DisplayName("MemberRepository 클래스")
class MemberRepositoryTest {

    private static final String VALID_EMAIL = "email@email.com";
    private static final String VALID_PASSWORD = "12345";
    private static final String VALID_NAME = "김형욱";
    private static final String VALID_NICKNAME = "김당근";
    private static final String VALID_PHONE = "010-0000-0000";
    private static final MemberRole VALID_ROLE = MemberRole.ROLE_USER;

    @Autowired
    private MemberRepository memberRepository;

    @Nested
    @DisplayName("save 메서드는")
    class Describe_save {

        @Nested
        @DisplayName("저장소에 등록할 멤버가 있을 경우에")
        class Context_exist_member {

            Member form;

            @BeforeEach
            void setUp() {
                form = createMember();
            }

            @Test
            @DisplayName("저장소에 멤버를 저장 후 저장된 멤버를 리턴합니다.")
            void It_saved_return_member() {
                Member savedMember = memberRepository.save(form);

                assertThat(savedMember.getEmail()).isEqualTo(VALID_EMAIL);
                assertThat(savedMember.getPassword()).isEqualTo(VALID_PASSWORD);
                assertThat(savedMember.getName()).isEqualTo(VALID_NAME);
                assertThat(savedMember.getNickName()).isEqualTo(VALID_NICKNAME);
                assertThat(savedMember.getPhone()).isEqualTo(VALID_PHONE);
                assertThat(savedMember.getMemberRole()).isEqualTo(VALID_ROLE);
            }

        }

    }

    @Nested
    @DisplayName("existByEmail 메서드는")
    class Describe_existByEmail {

        @Nested
        @DisplayName("저장할 멤버의 이메일이 저장소에 존재한다면")
        class Context_duplicate_email {

            @BeforeEach
            void setUp() {
                memberRepository.save(createMember());
            }

            @Test
            @DisplayName("true를 리턴해준다.")
            void It_return_true() {
                boolean result = memberRepository.existsByEmail(VALID_EMAIL);
                assertThat(result).isTrue();
            }

        }

        @Nested
        @DisplayName("저장할 멤버의 이메일이 저장소에 존재하지 않는다면")
        class Context_not_duplicate_email {

            @BeforeEach
            void setUp() {
                memberRepository.deleteAll();
            }

            @Test
            @DisplayName("false를 리턴해준다.")
            void It_return_true() {
                boolean result = memberRepository.existsByEmail(VALID_EMAIL);
                assertThat(result).isFalse();
            }

        }

    }

    @Nested
    @DisplayName("findByEmail 메서드는")
    class Describe_findByEmail {

        @Nested
        @DisplayName("저장소에 요청된 이메일의 사용자가 존재할 경우")
        class Context_exist_member {

            @BeforeEach
            void setUp() {
                memberRepository.save(createMember());
            }

            @Test
            @DisplayName("저장소에 회원을 조회하여 리턴합니다.")
            void It_select_return_member() {
                Optional<Member> findMember =
                        memberRepository.findByEmail(VALID_EMAIL);

                assertThat(findMember.isPresent()).isTrue();
                assertThat(findMember.get().getName()).isEqualTo(VALID_NAME);
            }

        }

        @Nested
        @DisplayName("저장소에 요청된 이메일의 사용자가 존재하지 않을 경우")
        class Context_not_exist_member {

            @BeforeEach
            void setUp() {
                memberRepository.deleteAll();
            }

            @Test
            @DisplayName("비어있는 멤버를 리턴합니다.")
            void It_return_noSuchElementException() {

                Optional<Member> findMember = memberRepository.findByEmail(VALID_EMAIL);
                assertThat(findMember.isPresent()).isFalse();

            }

        }

    }

    /**
     * 테스트 멤버 객체를 생성합니다.
     * @return
     */
    private Member createMember() {
        return Member.builder()
                .email(VALID_EMAIL)
                .password(VALID_PASSWORD)
                .nickName(VALID_NICKNAME)
                .name(VALID_NAME)
                .phone(VALID_PHONE)
                .memberRole(VALID_ROLE)
                .build();
    }

}
