package com.example.wantedmarket.trade.application;

import com.example.wantedmarket.common.IntegrationTest;
import com.example.wantedmarket.common.exception.ErrorCode;
import com.example.wantedmarket.common.exception.WantedMarketException;
import com.example.wantedmarket.product.domain.Product;
import com.example.wantedmarket.product.domain.ProductRepository;
import com.example.wantedmarket.trade.domain.Trade;
import com.example.wantedmarket.trade.domain.TradeRepository;
import com.example.wantedmarket.trade.domain.TradeReservedEvent;
import com.example.wantedmarket.trade.domain.TradeStatus;
import com.example.wantedmarket.trade.ui.dto.ReserveRequest;
import com.example.wantedmarket.user.domain.User;
import com.example.wantedmarket.user.domain.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TradeServiceTest extends IntegrationTest {

    @Autowired
    TradeService tradeService;

    @Autowired
    TradeRepository tradeRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    UserRepository userRepository;

    @Test
    @DisplayName("구매자가 예약하면 거래가 시작되어야 함")
    void reserveTest() throws Exception {
        //given
        User user = userRepository.save(new User("name"));
        Product product = productRepository.save(new Product(user.getId(), "name", 1000, 1));
        ReserveRequest request = new ReserveRequest(product.getId());

        //when
        Long reserveId = tradeService.reserve(user.getId(), request);

        //then
        assertThat(reserveId).isNotNull();
        assertThat(events.stream(TradeReservedEvent.class).count()).isEqualTo(1);
    }

    @Test
    @DisplayName("없는 유저가 거래하는 경우 예외가 발생해야 함")
    void reserveTest_userNotFound() throws Exception {
        //given
        ReserveRequest request = new ReserveRequest(1L);

        //when then
        assertThatThrownBy(() -> tradeService.reserve(1L, request))
                .isInstanceOf(WantedMarketException.class)
                .hasMessage(ErrorCode.USER_NOT_FOUND.getDescription());
    }

    @Test
    @DisplayName("없는 상품을 거래하는 경우 예외가 발생해야 함")
    void reserveTest_productNotFound() throws Exception {
        //given
        User user = userRepository.save(new User("name"));
        ReserveRequest request = new ReserveRequest(1L);

        //when then
        assertThatThrownBy(() -> tradeService.reserve(user.getId(), request))
                .isInstanceOf(WantedMarketException.class)
                .hasMessage(ErrorCode.PRODUCT_NOT_FOUND.getDescription());
    }

    @Test
    @DisplayName("판매자가 판매 승인을 하는 경우 거래가 APPROVED 상태로 바뀌어야 함")
    void approveTest() throws Exception {
        //given
        User seller = userRepository.save(new User("seller"));
        User buyer = userRepository.save(new User("buyer"));
        Product product = new Product(seller.getId(), "product", 1000, 1);
        product.reserve();
        product = productRepository.save(product);
        Trade trade = tradeRepository.save(new Trade(buyer.getId(), seller.getId(), product.getId()));

        //when
        tradeService.approve(seller.getId(), trade.getId());

        //then
        Trade findTrade = tradeRepository.findById(trade.getId()).get();
        assertThat(findTrade.getStatus()).isEqualTo(TradeStatus.APPROVED);
    }

    @Test
    @DisplayName("예약 중인 상품이 아닐 경우 예외가 발생해야 함")
    void approveTest_notReserved() throws Exception {
        //given
        User seller = userRepository.save(new User("seller"));
        User buyer = userRepository.save(new User("buyer"));
        Product product = productRepository.save(new Product(seller.getId(), "product", 1000, 1));
        Trade trade = new Trade(buyer.getId(), seller.getId(), product.getId());
        trade.approve();
        Trade savedTrade = tradeRepository.save(trade);

        //when then
        assertThatThrownBy(() -> tradeService.approve(seller.getId(), savedTrade.getId()))
                .isInstanceOf(WantedMarketException.class)
                .hasMessage(ErrorCode.NOT_RESERVED_TRADE.getDescription());
    }

    @Test
    @DisplayName("판매자가 아닌 유저가 판매 승인을 하는 경우 예외가 발생해야 함")
    void approveTest_notSeller() throws Exception {
        //given
        User seller = userRepository.save(new User("seller"));
        User buyer = userRepository.save(new User("buyer"));
        Product product = productRepository.save(new Product(seller.getId(), "product", 1000, 1));
        Trade trade = tradeRepository.save(new Trade(buyer.getId(), seller.getId(), product.getId()));

        //when then
        assertThatThrownBy(() -> tradeService.approve(buyer.getId(), trade.getId()))
                .isInstanceOf(WantedMarketException.class)
                .hasMessage(ErrorCode.NOT_PRODUCT_SELLER.getDescription());
    }

    @Test
    @DisplayName("존재하지 않는 유저일 경우 예외가 발생해야 함")
    void approveTest_userNotFound() throws Exception {
        //given

        //when then
        assertThatThrownBy(() -> tradeService.approve(1L, 1L))
                .isInstanceOf(WantedMarketException.class)
                .hasMessage(ErrorCode.USER_NOT_FOUND.getDescription());
    }

    @Test
    @DisplayName("존재하지 않는 거래일 경우 예외가 발생해야 함")
    void approveTest_tradeNotFound() throws Exception {
        //given
        User seller = userRepository.save(new User("seller"));
        userRepository.save(new User("buyer"));
        productRepository.save(new Product(seller.getId(), "product", 1000, 1));

        //when then
        assertThatThrownBy(() -> tradeService.approve(seller.getId(), 1L))
                .isInstanceOf(WantedMarketException.class)
                .hasMessage(ErrorCode.TRADE_NOT_FOUND.getDescription());
    }

    //
    @Test
    @DisplayName("구매자가 거래 완료를 하는 경우 거래가 COMPLETE 상태로 바뀌어야 함")
    void completeTest() throws Exception {
        //given
        User seller = userRepository.save(new User("seller"));
        User buyer = userRepository.save(new User("buyer"));
        Product product = new Product(seller.getId(), "product", 1000, 1);
        product = productRepository.save(product);
        Trade trade = new Trade(buyer.getId(), seller.getId(), product.getId());
        trade.approve();
        trade = tradeRepository.save(trade);

        //when
        tradeService.complete(buyer.getId(), trade.getId());

        //then
        Trade findTrade = tradeRepository.findById(trade.getId()).get();
        assertThat(findTrade.getStatus()).isEqualTo(TradeStatus.COMPLETED);
    }

    @Test
    @DisplayName("승인된 상품이 아닐 경우 예외가 발생해야 함")
    void completeTest_notApproved() throws Exception {
        //given
        User seller = userRepository.save(new User("seller"));
        User buyer = userRepository.save(new User("buyer"));
        Product product = productRepository.save(new Product(seller.getId(), "product", 1000, 1));
        Trade trade = new Trade(buyer.getId(), seller.getId(), product.getId());
        Trade savedTrade = tradeRepository.save(trade);

        //when then
        assertThatThrownBy(() -> tradeService.complete(buyer.getId(), savedTrade.getId()))
                .isInstanceOf(WantedMarketException.class)
                .hasMessage(ErrorCode.NOT_APPROVED_TRADE.getDescription());
    }

    @Test
    @DisplayName("구매자가 아닌 유저가 판매 승인을 하는 경우 예외가 발생해야 함")
    void completeTest_notBuyer() throws Exception {
        //given
        User seller = userRepository.save(new User("seller"));
        User buyer = userRepository.save(new User("buyer"));
        Product product = productRepository.save(new Product(seller.getId(), "product", 1000, 1));
        Trade trade = new Trade(buyer.getId(), seller.getId(), product.getId());
        trade.approve();
        Trade savedTrade = tradeRepository.save(trade);

        //when then
        assertThatThrownBy(() -> tradeService.complete(seller.getId(), savedTrade.getId()))
                .isInstanceOf(WantedMarketException.class)
                .hasMessage(ErrorCode.NOT_PRODUCT_BUYER.getDescription());
    }

    @Test
    @DisplayName("존재하지 않는 유저일 경우 예외가 발생해야 함")
    void completeTest_userNotFound() throws Exception {
        //given

        //when then
        assertThatThrownBy(() -> tradeService.complete(1L, 1L))
                .isInstanceOf(WantedMarketException.class)
                .hasMessage(ErrorCode.USER_NOT_FOUND.getDescription());
    }

    @Test
    @DisplayName("존재하지 않는 거래일 경우 예외가 발생해야 함")
    void completeTest_tradeNotFound() throws Exception {
        //given
        User seller = userRepository.save(new User("seller"));
        User buyer = userRepository.save(new User("buyer"));
        productRepository.save(new Product(seller.getId(), "product", 1000, 1));

        //when then
        assertThatThrownBy(() -> tradeService.complete(buyer.getId(), 1L))
                .isInstanceOf(WantedMarketException.class)
                .hasMessage(ErrorCode.TRADE_NOT_FOUND.getDescription());
    }
}
