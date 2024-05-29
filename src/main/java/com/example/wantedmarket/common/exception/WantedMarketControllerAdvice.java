package com.example.wantedmarket.common.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class WantedMarketControllerAdvice {

    @ExceptionHandler(WantedMarketException.class)
    public ResponseEntity<ErrorResponse> wantedMarketException(WantedMarketException e) {
        ErrorResponse response = new ErrorResponse(e.getMessage());
        return ResponseEntity.badRequest()
                .body(response);
    }
}
