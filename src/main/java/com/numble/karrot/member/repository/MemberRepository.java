package com.numble.karrot.member.repository;

import com.numble.karrot.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * 회원 도메인 리포지토리
 */
public interface MemberRepository extends JpaRepository<Member,Long> {

    Optional<Member> findByEmail(String email);

    boolean existsByEmail(String email);

}
