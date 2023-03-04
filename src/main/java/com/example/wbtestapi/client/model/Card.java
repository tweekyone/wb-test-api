package com.example.wbtestapi.client.model;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class Card {
    private Long imtID;
    private Long nmID;
    private List<String> mediaFiles;
    private String vendorCode;
    private List<Map<String, Object>> characteristics;
    private List<Size> sizes;
}
