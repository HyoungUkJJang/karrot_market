package com.numble.karrot.member.service;

import com.numble.karrot.member.domain.Member;
import com.numble.karrot.member.domain.MemberRole;
import com.numble.karrot.member.exception.MemberNotFoundException;
import com.numble.karrot.member.repository.MemberRepository;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@DisplayName("MemberService 클래스")
@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    private static final String VALID_EMAIL = "email@email.com";
    private static final String INVALID_EMAIL = "invalid" + VALID_EMAIL;
    private static final String VALID_PASSWORD = "12345";
    private static final String VALID_NAME = "김형욱";
    private static final String VALID_NICKNAME = "김당근";
    private static final String NEW_VALID_NICKNAME = "김당근_수정";
    private static final String VALID_PHONE = "010-0000-0000";
    private static final MemberRole VALID_ROLE = MemberRole.ROLE_USER;

    @Mock
    private MemberRepository memberRepository;
    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private MemberServiceImpl memberService;


    @Nested
    @DisplayName("join 메서드는")
    class Describe_join {

        @Nested
        @DisplayName("회원가입 요청을 한 사용자가 있을 경우에")
        class Context_join_req_user {

            Member member;

            @BeforeEach
            void setUp() {
                member = createMember();
                given(memberRepository.save(any(Member.class))).willReturn(member);
            }

            @Test
            @DisplayName("회원가입을 완료 후 회원을 리턴시킵니다.")
            void It_join_user_return() {
                Member joinMember = memberService.join(createMember());
                assertThat(joinMember.getEmail()).isEqualTo(VALID_EMAIL);
                verify(memberRepository).save(any(Member.class));
            }

        }

    }

    @Nested
    @DisplayName("findMember 메서드는")
    class Describe_findMember {

        @Nested
        @DisplayName("가입된 사용자 중 조회할 사용자가 있을 경우에")
        class Context_exist_member {

            Member member;

            @BeforeEach
            void setUp() {
                member = createMember();
                given(memberRepository.findByEmail(any(String.class))).willReturn(Optional.of(member));
            }

            @Test
            @DisplayName("사용자의 이메일을 통해 사용자를 조회하여 리턴합니다.")
            void It_return_findMember() {
                Member findMember = memberService.findMember(VALID_EMAIL);
                assertThat(findMember.getName()).isEqualTo(VALID_NAME);
                verify(memberRepository).findByEmail(any(String.class));
            }

        }

        @Nested
        @DisplayName("가입된 사용자 중 조회할 사용자가 없을 경우에")
        class Context_not_exist_member {

            @BeforeEach
            void setUp() {
                given(memberRepository.findByEmail(any(String.class)))
                        .willThrow(MemberNotFoundException.class);
            }

            @Test
            @DisplayName("MemberNotFoundException 예외를 던진다.")
            void It_return_memberNotFoundException() {
                assertThatThrownBy(() -> memberService.findMember(INVALID_EMAIL))
                        .isInstanceOf(MemberNotFoundException.class);
                verify(memberRepository).findByEmail(any(String.class));
            }

        }

    }

    @Nested
    @DisplayName("update 메서드는")
    class Describe_update {

        @Nested
        @DisplayName("사용자 닉네임 수정 요청이 들어올 경우에")
        class Context_update_user {

            Member member;

            @BeforeEach
            void setUp() {
            }

            @Test
            @DisplayName("수정된 멤버 엔티티를 리턴합니다.")
            void It_return_updated_member() {
                memberService.update(createMember(), NEW_VALID_NICKNAME);
            }

        }

    }

    @Nested
    @DisplayName("duplicateEmailCheck 메서드는")
    class Describe_duplicateEmailCheck {

        @Nested
        @DisplayName("회원가입 하려는 사용자의 이메일이 중복일 경우에")
        class Context_duplicate_email {

            @BeforeEach
            void setUp() {
                given(memberRepository.existsByEmail(any(String.class)))
                        .willReturn(true);
            }

            @Test
            @DisplayName("True를 리턴한다.")
            void It_return_true() {
                boolean result = memberService.duplicateEmailCheck(INVALID_EMAIL);
                verify(memberRepository).existsByEmail(any(String.class));
                assertThat(result).isTrue();
            }

        }

        @Nested
        @DisplayName("회원가입 하려는 사용자의 이메일이 중복이 아닐 경우에")
        class Context_not_duplicate_email {

            @BeforeEach
            void setUp() {
                given(memberRepository.existsByEmail(any(String.class)))
                        .willReturn(false);
            }

            @Test
            @DisplayName("False를 리턴한다.")
            void It_return_false() {
                boolean result = memberService.duplicateEmailCheck(VALID_EMAIL);
                verify(memberRepository).existsByEmail(any(String.class));
                assertThat(result).isFalse();
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

    /**
     * 테스트 수정 객체를 생성합니다.
     * @return
     */
    private Member updateMember() {
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
