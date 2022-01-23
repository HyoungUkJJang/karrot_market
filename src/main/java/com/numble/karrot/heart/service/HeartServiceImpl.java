package com.numble.karrot.heart.service;

import com.numble.karrot.heart.domain.Heart;
import com.numble.karrot.heart.repository.HeartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class HeartServiceImpl implements HeartService {

    private final HeartRepository heartRepository;

    @Override
    @Transactional
    public Heart addHeart(Heart heart) {
        return heartRepository.save(heart);
    }

    @Override
    @Transactional
    public void deleteHeart(Long productId, Long memberId) {
        heartRepository.deleteByProductIdAndMemberId(productId, memberId);
    }

    @Override
    @Transactional
    public void deleteHeartAll(Long productId) {
        heartRepository.deleteByProductId(productId);
    }
}
