package com.numble.karrot.member_image.repository;

import com.numble.karrot.member_image.domain.MemberImage;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 회원 이미지 리포지토리 입니다.
 */
public interface MemberImageRepository extends JpaRepository<MemberImage, Long> {

    /**
     * 회원의 이미지 엔티티를 조회합니다.
     * @param memberId 대응되는 회원의 아이디
     * @return 조회된 회원의 이미지 엔티티
     */
    MemberImage findByMemberId(Long memberId);

}
