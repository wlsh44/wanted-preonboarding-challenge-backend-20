package com.example.wantedmarket.product.domain;

import com.example.wantedmarket.common.exception.ErrorCode;
import com.example.wantedmarket.common.exception.WantedMarketException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProductTest {

    @Test
    @DisplayName("유저가 null이면 안 됨")
    void validateUserTest_null() throws Exception {
        assertThatThrownBy(() -> new Product(null, "name", 1000, 1))
                .isInstanceOf(WantedMarketException.class)
                .hasMessage(ErrorCode.DOMAIN_EXCEPTION.getDescription());
    }

    @Test
    @DisplayName("제품 이름이 null이면 안 됨")
    void validateNameTest_null() throws Exception {
        assertThatThrownBy(() -> new Product(1L, null, 1000, 1))
                .isInstanceOf(WantedMarketException.class)
                .hasMessage(ErrorCode.DOMAIN_EXCEPTION.getDescription());
    }

    @Test
    @DisplayName("제품 이름이 공백이면 안 됨")
    void validateNameTest_empty() throws Exception {
        assertThatThrownBy(() -> new Product(1L, "", 1000, 1))
                .isInstanceOf(WantedMarketException.class)
                .hasMessage(ErrorCode.DOMAIN_EXCEPTION.getDescription());

        assertThatThrownBy(() -> new Product(1L, " ", 1000, 1))
                .isInstanceOf(WantedMarketException.class)
                .hasMessage(ErrorCode.DOMAIN_EXCEPTION.getDescription());
    }

    @Test
    @DisplayName("제품 가격이 음수이면 안 됨")
    void validatePriceTest_negative() throws Exception {
        assertThatThrownBy(() -> new Product(1L, "name", -1000, 1))
                .isInstanceOf(WantedMarketException.class)
                .hasMessage(ErrorCode.DOMAIN_EXCEPTION.getDescription());
    }

    @Test
    @DisplayName("제품을 생성하면 첫 상태는 판매중이어야 함")
    void validateStatusTest_onSale() throws Exception {
        Product product = new Product(1L, "name", 1000, 1);

        assertThat(product.getStatus()).isEqualTo(ProductStatus.ON_SALE);
    }

    @Test
    @DisplayName("제품 개수가 음수이면 안 됨")
    void validatePriceTest_quantity() throws Exception {
        assertThatThrownBy(() -> new Product(1L, "name", 1000, -1))
                .isInstanceOf(WantedMarketException.class)
                .hasMessage(ErrorCode.DOMAIN_EXCEPTION.getDescription());
    }

    @Test
    @DisplayName("남은 수량이 있으면 예약에 성공해야 함")
    void reserveTest() throws Exception {
        //given
        Product product = new Product(1L, "name", 1000, 10);

        //when
        product.reserve();

        //then
        assertThat(product.getQuantity()).isEqualTo(9);
    }

    @Test
    @DisplayName("예약에 성공했을 때 남은 수량이 없으면 예약 상태로 바뀌어야 함")
    void reserveTest_reservedStatus() throws Exception {
        //given
        Product product = new Product(1L, "name", 1000, 1);

        //when
        product.reserve();

        //then
        assertThat(product.getQuantity()).isEqualTo(0);
        assertThat(product.isTradable()).isFalse();
    }

    @Test
    @DisplayName("남은 수량이 없으면 예약에 실패해야 함")
    void reserveTest_noQuantity() throws Exception {
        //given
        Product product = new Product(1L, "name", 1000, 1);
        product.reserve();

        //when then
        assertThatThrownBy(product::reserve)
                .isInstanceOf(WantedMarketException.class)
                .hasMessage(ErrorCode.TRADABLE_PRODUCT_NOT_EXIST.getDescription());
    }
}
