package com.example.wantedmarket.trade.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TradeRepository extends JpaRepository<Trade, Long> {
    boolean existsByBuyerIdAndProductId(Long buyerId, Long productId);

    @Query("select count(t) > 0 from Trade t where t.productId = :productId and t.status != com.example.wantedmarket.trade.domain.TradeStatus.COMPLETED")
    boolean existsByOnTrading(@Param("productId") Long productId);

    @Query("select t from Trade t where t.productId = :productId and (t.sellerId = :traderId or t.buyerId = :traderId)")
    Optional<Trade> findByProductIdWithTraderId(@Param("productId") Long productId, @Param("traderId") Long traderId);
}
