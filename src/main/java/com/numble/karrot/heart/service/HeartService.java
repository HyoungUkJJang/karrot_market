package com.numble.karrot.heart.service;

import com.numble.karrot.heart.domain.Heart;

public interface HeartService {
   Heart addHeart(Heart heart);

   void deleteHeart(Long productId, Long memberId);
}
