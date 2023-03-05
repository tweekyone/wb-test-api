package com.example.wbtestapi.controller;

import com.example.wbtestapi.restclient.*;
import com.example.wbtestapi.restclient.dto.request.*;
import lombok.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@AllArgsConstructor
public class StockRemainsController {
    private WildberriesRestClient client;

    @GetMapping("/stocks")
    public ResponseEntity<String> updateStocks() {
        RequestStocks requestStocks = new RequestStocks();
        RequestStocks.Stock stock = requestStocks.new Stock("BarcodeTest123", 10);
        requestStocks.setRequestStocks(Arrays.asList(stock));

        client.refreshStocks(140313L, requestStocks);

        return ResponseEntity.ok("ok");
    }


}

