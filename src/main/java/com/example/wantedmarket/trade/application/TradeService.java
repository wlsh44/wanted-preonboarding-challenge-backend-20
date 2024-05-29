package com.example.wantedmarket.trade.application;

import com.example.wantedmarket.common.exception.ErrorCode;
import com.example.wantedmarket.common.exception.WantedMarketException;
import com.example.wantedmarket.product.domain.Product;
import com.example.wantedmarket.product.domain.ProductRepository;
import com.example.wantedmarket.trade.domain.CompleteTradeEvent;
import com.example.wantedmarket.trade.domain.Trade;
import com.example.wantedmarket.trade.domain.TradeRepository;
import com.example.wantedmarket.trade.ui.dto.ReserveRequest;
import com.example.wantedmarket.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class TradeService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final TradeRepository tradeRepository;
    private final ApplicationEventPublisher publisher;

    public Long reserve(Long buyerId, ReserveRequest request) {
        validateUser(buyerId);
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new WantedMarketException(ErrorCode.PRODUCT_NOT_FOUND));

        product.reserve();
        Trade trade = tradeRepository.save(new Trade(buyerId, product.getId(), request.getProductId()));
        return trade.getId();
    }

    private void validateUser(Long buyerId) {
        if (!userRepository.existsById(buyerId)) {
            throw new WantedMarketException(ErrorCode.USER_NOT_FOUND);
        }
    }

    public void approveSelling(Long sellerId, Long tradeId) {
        validateUser(sellerId);

        Trade trade = tradeRepository.findById(tradeId)
                .orElseThrow(() -> new WantedMarketException(ErrorCode.TRADE_NOT_FOUND));

        validateSeller(sellerId, trade);
        publisher.publishEvent(new CompleteTradeEvent(trade.getProductId()));
    }

    private void validateSeller(Long sellerId, Trade trade) {
        if (!trade.isSeller(sellerId)) {
            throw new WantedMarketException(ErrorCode.NOT_PRODUCT_SELLER);
        }
    }
}
