package com.example.wantedmarket.product.application;

import com.example.wantedmarket.common.exception.ErrorCode;
import com.example.wantedmarket.common.exception.WantedMarketException;
import com.example.wantedmarket.product.domain.Product;
import com.example.wantedmarket.product.domain.ProductRepository;
import com.example.wantedmarket.product.ui.dto.ProductResponse;
import com.example.wantedmarket.product.ui.dto.RegisterProductRequest;
import com.example.wantedmarket.user.domain.User;
import com.example.wantedmarket.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Transactional
    public Long registerProduct(Long userId, RegisterProductRequest request) {
        validateUser(userId);
        Product product = productRepository.save(new Product(userId, request.getName(), request.getPrice()));
        return product.getId();
    }

    public void validateUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new WantedMarketException(ErrorCode.USER_NOT_FOUND);
        }
    }

    public List<ProductResponse> findProductList() {
        List<Product> productList = productRepository.findAll();
        return productList.stream()
                .map(ProductResponse::new)
                .toList();
    }

    public ProductResponse findProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new WantedMarketException(ErrorCode.PRODUCT_NOT_FOUND));
        return new ProductResponse(product);
    }


}
