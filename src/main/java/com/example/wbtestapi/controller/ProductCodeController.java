package com.example.wbtestapi.controller;

import com.example.wbtestapi.restclient.*;
import com.example.wbtestapi.restclient.dto.request.*;
import com.example.wbtestapi.restclient.dto.response.ProductCodeResponse;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import lombok.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ProductCodeController {
    private final WildberriesRestClient wildberriesRestClient;

    @GetMapping("/productCode")
    public ResponseEntity<String> getProductCode() throws JsonProcessingException {
        ProductCodeRequest requestProductCodeRequest = new ProductCodeRequest(new String[]{"6000000001"});
        ProductCodeResponse productCodeResponse = wildberriesRestClient.getProductCodeResponse(requestProductCodeRequest);
        ObjectMapper mapper = wildberriesRestClient.getMapper();
        String resultJson = mapper.writeValueAsString(productCodeResponse);

        return new ResponseEntity<>(resultJson, HttpStatus.OK);
    }
}
