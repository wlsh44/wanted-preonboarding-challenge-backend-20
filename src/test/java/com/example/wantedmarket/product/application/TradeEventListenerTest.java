package com.example.wantedmarket.product.application;

import com.example.wantedmarket.common.IntegrationTest;
import com.example.wantedmarket.product.domain.Product;
import com.example.wantedmarket.product.domain.ProductRepository;
import com.example.wantedmarket.product.domain.ProductStatus;
import com.example.wantedmarket.trade.domain.Trade;
import com.example.wantedmarket.trade.domain.TradeCompletedEvent;
import com.example.wantedmarket.trade.domain.TradeRepository;
import com.example.wantedmarket.trade.domain.TradeReservedEvent;
import com.example.wantedmarket.user.domain.User;
import com.example.wantedmarket.user.domain.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class TradeEventListenerTest extends IntegrationTest {

    @Autowired
    TradeEventListener tradeEventListener;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    TradeRepository tradeRepository;

    @Test
    @DisplayName("거래 예약이 되었을 때, 상품 개수가 0일 경우 RESERVED 상태가 되어야 함")
    void tradeReservedEventHandlerTest_reserved() throws Exception {
        //given
        User seller = userRepository.save(new User("seller"));
        User buyer = userRepository.save(new User("buyer"));
        Product product = productRepository.save(new Product(seller.getId(), "product", 1000, 1));
        tradeRepository.save(new Trade(buyer.getId(), seller.getId(), product.getId(), 1000));

        //when
        tradeEventListener.tradeReservedEventHandler(new TradeReservedEvent(product.getId()));

        //then
        Product findProduct = productRepository.findById(product.getId()).get();
        assertThat(findProduct.isReserved()).isTrue();
    }

    @Test
    @DisplayName("거래 예약이 되었을 때, 상품 개수가 0이 아닌 경우 ON_SALE 상태가 되어야 함")
    void tradeReservedEventHandlerTest_onSale() throws Exception {
        //given
        User seller = userRepository.save(new User("seller"));
        User buyer = userRepository.save(new User("buyer"));
        Product product = productRepository.save(new Product(seller.getId(), "product", 1000, 2));
        tradeRepository.save(new Trade(buyer.getId(), seller.getId(), product.getId(), 1000));

        //when
        tradeEventListener.tradeReservedEventHandler(new TradeReservedEvent(product.getId()));

        //then
        Product findProduct = productRepository.findById(product.getId()).get();
        assertThat(findProduct.isTradable()).isTrue();
    }

    @Test
    @DisplayName("거래 완료가 되었을 때, 모든 상품이 거래 완료일 경우 COMPLETE 상태가 되어야 함")
    void tradeCompletedEventHandlerTest_completed() throws Exception {
        //given
        User seller = userRepository.save(new User("seller"));
        User buyer = userRepository.save(new User("buyer"));
        Product product = new Product(seller.getId(), "product", 1000, 1);
        product.reserve();
        product = productRepository.save(product);
        Trade trade = new Trade(buyer.getId(), seller.getId(), product.getId(), 1000);
        trade.approve();
        trade.complete();
        tradeRepository.save(trade);

        //when
        tradeEventListener.tradeCompletedEventHandler(new TradeCompletedEvent(product.getId()));

        //then
        Product findProduct = productRepository.findById(product.getId()).get();
        assertThat(findProduct.getStatus()).isEqualTo(ProductStatus.COMPLETED);
    }

    @Test
    @DisplayName("거래 완료가 되었을 때, 거래 완료가 되지 않은 상품이 있을 경우 RESERVED 상태가 되어야 함")
    void tradeCompletedEventHandlerTest_reserved() throws Exception {
        //given
        User seller = userRepository.save(new User("seller"));
        User buyer = userRepository.save(new User("buyer"));
        Product product = productRepository.save(new Product(seller.getId(), "product", 1000, 2));
        Trade trade1 = new Trade(buyer.getId(), seller.getId(), product.getId(), 1000);
        Trade trade2 = new Trade(buyer.getId(), seller.getId(), product.getId(), 1000);
        trade1.approve();
        tradeRepository.save(trade1);
        tradeRepository.save(trade2);

        //when
        tradeEventListener.tradeCompletedEventHandler(new TradeCompletedEvent(product.getId()));

        //then
        Product findProduct = productRepository.findById(product.getId()).get();
        assertThat(findProduct.getStatus()).isNotEqualTo(ProductStatus.COMPLETED);
    }
}
