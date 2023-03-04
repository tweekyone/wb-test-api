package com.example.wbtestapi.controller;

import com.example.wbtestapi.restclient.*;
import com.example.wbtestapi.restclient.dto.request.*;
import lombok.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class StockRemainsController {
    private WildberriesRestClient client;

    @GetMapping("/stocks")
    public ResponseEntity<String> updateStocks() {
        RequestStocks requestStocks = new RequestStocks("BarcodeTest123", 10, 140313);

        return ResponseEntity.ok("ok");
    }


}

