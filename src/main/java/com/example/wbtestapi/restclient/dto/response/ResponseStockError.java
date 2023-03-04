package com.example.wbtestapi.restclient.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseStockError {
    private String code;
    private String message;
    private BarCodeData data;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BarCodeData{
        private String sku;
        private Integer amount;
    }
}