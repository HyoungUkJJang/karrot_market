package com.numble.karrot.trade.domain;

import com.numble.karrot.common.BaseEntity;
import com.numble.karrot.member.domain.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * 회원 거래정보 도메인입니다.
 */
@Entity
@Getter
@NoArgsConstructor
public class Trade extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "trade_id")
    private Long id;

    private Integer tradeQuantity;
    private Integer donationQuantity;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public Trade(Integer tradeQuantity, Integer donationQuantity, Member member) {
        this.tradeQuantity = tradeQuantity;
        this.donationQuantity = donationQuantity;
        this.member = member;
    }

    /**
     * 거래 횟수 하나를 증가시킵니다.
     */
    public void addTradeQuantity() {
        this.tradeQuantity++;
    }

    /**
     * 거래 횟수 하나를 감소시킵니다.
     */
    public void deleteTradeQuantity() {
        this.tradeQuantity++;
    }

    /**
     * 기부 횟수 하나를 증가시킵니다.
     */
    public void addDonationQuantity() {
        this.donationQuantity++;
    }

    /**
     * 기부 횟수 하나를 감소시킵니다.
     */
    public void deleteDonationQuantity() {
        this.donationQuantity--;
    }

}
