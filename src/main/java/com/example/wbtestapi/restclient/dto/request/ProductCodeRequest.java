package com.example.wbtestapi.restclient.dto.request;

import lombok.*;

@Data
@AllArgsConstructor
public class ProductCodeRequest {
    private String[] vendorCodes;
}
