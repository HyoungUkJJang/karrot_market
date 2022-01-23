package com.numble.karrot.trade.service;

import com.numble.karrot.trade.domain.Trade;
import com.numble.karrot.trade.repository.TradeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TradeServiceImpl implements TradeService {

    private final TradeRepository tradeRepository;

    @Override
    @Transactional
    public Trade save(Trade trade) {
        return tradeRepository.save(trade);
    }

    @Override
    public Trade findTrade(Long memberId) {
        return findTrade(memberId);
    }

    @Override
    @Transactional
    public void addTradeQuantity(Trade trade) {
        trade.addTradeQuantity();
    }

    @Override
    @Transactional
    public void deleteTradeQuantity(Trade trade) {
        trade.deleteTradeQuantity();
    }

    @Override
    @Transactional
    public void addDonationQuantity(Trade trade) {
        trade.addDonationQuantity();
    }

    @Override
    @Transactional
    public void deleteDonationQuantity(Trade trade) {
        trade.deleteDonationQuantity();
    }

}
