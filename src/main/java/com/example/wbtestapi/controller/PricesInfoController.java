package com.example.wbtestapi.controller;

import com.example.wbtestapi.restclient.*;
import com.example.wbtestapi.restclient.dto.response.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import lombok.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequiredArgsConstructor
public class PricesInfoController {

    private final WildberriesRestClient wildberriesRestClient;

    @GetMapping("/pricesInfo")
    public ResponseEntity<String> getPricesInfo() throws JsonProcessingException {
        List<ResponsePricesInfo> responsePricesInfoList = wildberriesRestClient.getPricesInfo(1);
        ObjectMapper mapper = wildberriesRestClient.getMapper();
        String resultJson = mapper.writeValueAsString(responsePricesInfoList);

        return new ResponseEntity<>(resultJson, HttpStatus.OK);
    }
}
