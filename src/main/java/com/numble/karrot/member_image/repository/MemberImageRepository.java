package com.numble.karrot.member_image.repository;

import com.numble.karrot.member_image.domain.MemberImage;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 회원 이미지 리포지토리 입니다.
 */
public interface MemberImageRepository extends JpaRepository<MemberImage, Long> {
}
