package com.example.wantedmarket.product.application;

import com.example.wantedmarket.common.IntegrationTest;
import com.example.wantedmarket.product.domain.Product;
import com.example.wantedmarket.product.domain.ProductRepository;
import com.example.wantedmarket.product.domain.ProductStatus;
import com.example.wantedmarket.trade.domain.CompleteTradeEvent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class SaleCompletedEventListenerTest extends IntegrationTest {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    SaleCompletedEventListener eventListener;

    @Test
    @DisplayName("상품의 상태가 complete가 되어야 함")
    void completeProductTest() throws Exception {
        //given
        Product product = new Product(1L, "name", 1000);
        product.reserve();
        product = productRepository.save(product);

        //when
        eventListener.completeProduct(new CompleteTradeEvent(product.getId()));

        //then
        Product findProduct = productRepository.findById(product.getId()).get();
        assertThat(findProduct.getStatus()).isEqualTo(ProductStatus.COMPLETED);
    }
}
