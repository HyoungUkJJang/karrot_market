package com.numble.karrot.trade.repository;

import com.numble.karrot.trade.domain.Trade;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 거래 정보 리포지토리입니다.
 */
public interface TradeRepository extends JpaRepository<Trade, Long> {
}
