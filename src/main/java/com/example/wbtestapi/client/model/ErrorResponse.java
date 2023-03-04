package com.example.wbtestapi.client.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class ErrorResponse {
    private String id;
    private String jsonrpc;
    Error error;

    @Getter @Setter @ToString
    public static class Error {
        private float code;
        private String message;
        Data data;

    }

    @Getter @Setter @ToString
    public static class Data {
        private String err;
        private String trKey;
        Cause cause;

    }

    @Getter @Setter @ToString
    public static class Cause {
        private String err;
    }
}
