package com.example.wantedmarket.trade.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TradeRepository extends JpaRepository<Trade, Long> {
    boolean existsByBuyerIdAndProductId(Long buyerId, Long productId);
}
