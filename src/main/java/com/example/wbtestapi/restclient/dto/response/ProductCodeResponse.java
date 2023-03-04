package com.example.wbtestapi.restclient.dto.response;

import lombok.*;

import java.util.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductCodeResponse {
    private List<ProductCodeData> data;
    private Boolean error;
    private String errorText;
    private String additionalError;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ProductCodeData {
        Integer imtId;
        Integer nmId;
        String vendorCode;
        List<String> mediaFiles;
        List<Sizes> sizes;
        List<Map<String, String>> characteristics;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Sizes {
        private String techSize;
        private String wbSize;
        private Integer price;
        private Integer chrtID;
        private List<String> skus;
    }
}

