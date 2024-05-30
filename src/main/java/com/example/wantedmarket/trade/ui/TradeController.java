package com.example.wantedmarket.trade.ui;

import com.example.wantedmarket.auth.ui.Authorized;
import com.example.wantedmarket.trade.application.TradeService;
import com.example.wantedmarket.trade.ui.dto.ReserveRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/trade")
@RequiredArgsConstructor
public class TradeController {

    private final TradeService tradeService;

    @PostMapping("")
    public ResponseEntity<Void> reserveProduct(@Authorized Long userId, @RequestBody @Valid ReserveRequest request) {
        tradeService.reserve(userId, request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{tradeId}")
    public ResponseEntity<Void> approveSelling(@Authorized Long userId, @PathVariable(name = "tradeId") Long tradeId) {
        tradeService.approve(userId, tradeId);
        return ResponseEntity.ok().build();
    }
}
