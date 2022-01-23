package com.numble.karrot.heart.service;

import com.numble.karrot.heart.domain.Heart;

/**
 * 관심상품 도메인 관련 기능을 담당하는 서비스 클래스 입니다.
 */
public interface HeartService {

   /**
    * 관심상품 테이블에 추가합니다.
    * @param heart 관심상품 엔티티
    * @return 추가된 관심상품
    */
   Heart addHeart(Heart heart);

   /**
    * 관심상품을 삭제합니다.
    * @param productId 삭제 할 관심상품 아이디
    * @param memberId 삭제 할 회원의 아이디
    */
   void deleteHeart(Long productId, Long memberId);

   void deleteHeartAll(Long productId);

}
