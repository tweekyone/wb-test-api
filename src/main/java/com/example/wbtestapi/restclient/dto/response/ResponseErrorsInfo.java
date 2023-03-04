package com.example.wbtestapi.restclient.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseErrorsInfo {
    private List <Data> data;
    private boolean error;
    private String errorText;
    private String additionalError;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Data {
        private String object;
        private String vendorCode;
        private String updateAt;
        private List<String> errors;
    }
}


