package com.example.wantedmarket.product.application;

import com.example.wantedmarket.common.exception.ErrorCode;
import com.example.wantedmarket.common.exception.WantedMarketException;
import com.example.wantedmarket.product.domain.Product;
import com.example.wantedmarket.product.domain.ProductRepository;
import com.example.wantedmarket.trade.domain.TradeReservedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class TradeEventListener {

    private final ProductRepository productRepository;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void tradeReservedEventHandler(TradeReservedEvent event) {
        Long productId = event.productId();
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new WantedMarketException(ErrorCode.PRODUCT_NOT_FOUND));
        product.reserve();
    }
}
