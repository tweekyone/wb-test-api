package com.example.wbtestapi.client.model;

import lombok.Data;

import java.util.List;

@Data
public class Size {
    // Размер поставщика (пример S, M, L, XL, 42, 42-43)
    private String techSize;
    private List<String> skus;
    // Рос. размер
    private String wbSize;
    private Integer price;
    private Long chrtID;
}
