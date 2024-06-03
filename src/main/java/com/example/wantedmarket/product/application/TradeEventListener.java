package com.example.wantedmarket.product.application;

import com.example.wantedmarket.common.exception.ErrorCode;
import com.example.wantedmarket.common.exception.WantedMarketException;
import com.example.wantedmarket.product.domain.Product;
import com.example.wantedmarket.product.domain.ProductRepository;
import com.example.wantedmarket.trade.domain.TradeCompletedEvent;
import com.example.wantedmarket.trade.domain.TradeRepository;
import com.example.wantedmarket.trade.domain.TradeReservedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@Transactional
@RequiredArgsConstructor
public class TradeEventListener {

    private final ProductRepository productRepository;
    private final TradeRepository tradeRepository;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void tradeReservedEventHandler(TradeReservedEvent event) {
        Long productId = event.productId();
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new WantedMarketException(ErrorCode.PRODUCT_NOT_FOUND));
        product.reserve();
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void tradeCompletedEventHandler(TradeCompletedEvent event) {
        Long productId = event.productId();
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new WantedMarketException(ErrorCode.PRODUCT_NOT_FOUND));
        if (product.isReserved() && !tradeRepository.existsByOnTrading(productId)) {
            product.complete();
        }
    }
}
