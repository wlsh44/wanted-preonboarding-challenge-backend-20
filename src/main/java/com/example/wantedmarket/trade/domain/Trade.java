package com.example.wantedmarket.trade.domain;

import com.example.wantedmarket.common.exception.ErrorCode;
import com.example.wantedmarket.common.exception.WantedMarketException;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Trade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long buyerId;

    private Long sellerId;

    private Long productId;

    private TradeStatus status;
    private TradeInfo tradeInfo;

    public Trade(Long buyerId, Long sellerId, Long productId, int price) {
        validateNull(sellerId, buyerId, productId);
        this.sellerId = sellerId;
        this.buyerId = buyerId;
        this.productId = productId;
        this.status = TradeStatus.RESERVED;
        this.tradeInfo = new TradeInfo(price);
    }

    private void validateNull(Long sellerId, Long buyerId, Long productId) {
        if (Objects.isNull(sellerId) || Objects.isNull(buyerId) || Objects.isNull(productId)) {
            throw new WantedMarketException(ErrorCode.DOMAIN_EXCEPTION);
        }
    }

    public boolean isSeller(Long userId) {
        return sellerId.equals(userId);
    }

    public void approve() {
        if (!TradeStatus.isReserved(status)) {
            throw new WantedMarketException(ErrorCode.NOT_RESERVED_TRADE);
        }
        status = TradeStatus.APPROVED;
    }

    public void complete() {
        if (!TradeStatus.isApproved(status)) {
            throw new WantedMarketException(ErrorCode.NOT_APPROVED_TRADE);
        }
        status = TradeStatus.COMPLETED;
    }

    public boolean isBuyer(Long buyerId) {
        return this.buyerId.equals(buyerId);
    }
}
