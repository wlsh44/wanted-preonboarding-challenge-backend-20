package com.example.wantedmarket.product.application;

import com.example.wantedmarket.common.annotation.ServiceTest;
import com.example.wantedmarket.common.exception.ErrorCode;
import com.example.wantedmarket.common.exception.WantedMarketException;
import com.example.wantedmarket.product.domain.Product;
import com.example.wantedmarket.product.domain.ProductRepository;
import com.example.wantedmarket.product.ui.dto.ProductResponse;
import com.example.wantedmarket.product.ui.dto.RegisterProductRequest;
import com.example.wantedmarket.user.domain.User;
import com.example.wantedmarket.user.domain.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@ServiceTest
class ProductServiceTest {

    @Autowired
    ProductService productService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProductRepository productRepository;

    @Test
    @DisplayName("제품을 등록해야 함")
    void registerProductTest() throws Exception {
        //given
        User user = userRepository.save(new User("name"));
        RegisterProductRequest request = new RegisterProductRequest("name", 1000);

        //when
        Long productId = productService.registerProduct(user.getId(), request);

        //then
        assertThat(productId).isNotNull();
    }

    @Test
    @DisplayName("없는 유저가 제품을 등록할 경우 예외가 발생해야 함")
    void registerProductTest_userNotFound() throws Exception {
        //given
        RegisterProductRequest request = new RegisterProductRequest("name", 1000);

        //when then
        assertThatThrownBy(() -> productService.registerProduct(1L, request))
                .isInstanceOf(WantedMarketException.class)
                .hasMessage(ErrorCode.USER_NOT_FOUND.getDescription());
    }

    @Test
    @DisplayName("제품 목록을 조회해야 함")
    void findProductListTest() throws Exception {
        //given
        User user = userRepository.save(new User("name"));
        productRepository.save(new Product(user.getId(), "name1", 1000));
        productRepository.save(new Product(user.getId(), "name3", 1000));
        productRepository.save(new Product(user.getId(), "name2", 1000));

        //when
        List<ProductResponse> productList = productService.findProductList();

        //then
        assertThat(productList).hasSize(3);
    }

    @Test
    @DisplayName("제품 상세조회를 해야 함")
    void findProductTest() throws Exception {
        //given
        User user = userRepository.save(new User("name"));
        Product product = productRepository.save(new Product(user.getId(), "name1", 1000));

        //when
        ProductResponse response = productService.findProduct(product.getId());

        //then
        assertAll(
                () -> assertThat(response.getId()).isEqualTo(product.getId()),
                () -> assertThat(response.getName()).isEqualTo(product.getName()),
                () -> assertThat(response.getPrice()).isEqualTo(product.getPrice()),
                () -> assertThat(response.getStatus()).isEqualTo(product.getStatus().ko())
        );
    }

    @Test
    @DisplayName("없는 제품 상세조회를 하면 예외가 발생해야 함")
    void findProductTest_notFound() throws Exception {
        //given

        //when then
        assertThatThrownBy(() -> productService.findProduct(1L))
                .isInstanceOf(WantedMarketException.class)
                .hasMessage(ErrorCode.PRODUCT_NOT_FOUND.getDescription());
    }
}
