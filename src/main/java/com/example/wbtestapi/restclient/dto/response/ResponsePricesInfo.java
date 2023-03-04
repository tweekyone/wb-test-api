package com.example.wbtestapi.restclient.dto.response;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponsePricesInfo {
    private Integer nmId;
    private Long price;
    private Integer discount;
    private Long promoCode;
}
