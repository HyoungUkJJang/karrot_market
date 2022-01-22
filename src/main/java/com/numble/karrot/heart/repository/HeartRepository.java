package com.numble.karrot.heart.repository;

import com.numble.karrot.heart.domain.Heart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HeartRepository extends JpaRepository<Heart, Long> {
    void deleteByProductIdAndMemberId(Long productId, Long memberId);
}
