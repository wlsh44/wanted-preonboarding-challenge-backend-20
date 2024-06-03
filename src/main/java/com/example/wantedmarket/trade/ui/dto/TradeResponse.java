package com.example.wantedmarket.trade.ui.dto;

import com.example.wantedmarket.user.domain.User;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TradeResponse {

    private Long tradeId;
    private UserDto seller;
    private UserDto buyer;
    private int price;

    public TradeResponse(Long tradeId, User buyer, User seller, int price) {
        this.tradeId = tradeId;
        this.buyer = new UserDto(buyer);
        this.seller = new UserDto(seller);
        this.price = price;
    }

    @Getter
    public static class UserDto {
        private Long id;
        private String name;

        public UserDto(User user) {
            this.id = user.getId();
            this.name = user.getName();
        }
    }
}
