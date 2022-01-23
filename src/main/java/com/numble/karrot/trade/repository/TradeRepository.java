package com.numble.karrot.trade.repository;

import com.numble.karrot.trade.domain.Trade;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 거래 정보 리포지토리입니다.
 */
public interface TradeRepository extends JpaRepository<Trade, Long> {

    /**
     * 사용자 거래정보 엔티티를 조회합니다.
     * @param memberId 조회할 사용자 아이디
     * @return 조회된 거래정보 엔티티
     */
    Trade findByMemberId(Long memberId);

}
