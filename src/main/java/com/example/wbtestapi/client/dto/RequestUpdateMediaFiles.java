package com.example.wbtestapi.client.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class RequestUpdateMediaFiles {
    private String vendorCode;
    private List<String> data;
}
