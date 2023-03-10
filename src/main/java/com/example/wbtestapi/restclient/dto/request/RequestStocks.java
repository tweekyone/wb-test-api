package com.example.wbtestapi.restclient.dto.request;


import lombok.*;

import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestStocks {
    private List<Stock> requestStocks;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Stock {
        private String sku;
        private Integer amount;
    }
}

