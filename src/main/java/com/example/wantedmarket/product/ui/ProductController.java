package com.example.wantedmarket.product.ui;

import com.example.wantedmarket.auth.ui.Authorized;
import com.example.wantedmarket.product.application.ProductService;
import com.example.wantedmarket.product.ui.dto.ProductResponse;
import com.example.wantedmarket.product.ui.dto.RegisterProductRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping("")
    public ResponseEntity<Void> registerProduct(@Authorized Long userId,
                                                @RequestBody @Valid RegisterProductRequest request) {
        productService.registerProduct(userId, request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("")
    public ResponseEntity<List<ProductResponse>> findProductList() {
        List<ProductResponse> response = productService.findProductList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponse> findProduct(@PathVariable(name = "productId") Long productId) {
        ProductResponse response = productService.findProduct(productId);
        return ResponseEntity.ok(response);
    }
}
