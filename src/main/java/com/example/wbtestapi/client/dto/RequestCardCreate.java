package com.example.wbtestapi.client.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashMap;

@Getter
@Setter @ToString
public class RequestCardCreate {
    private String id;
    private String jsonrpc = "2.0";
    private HashMap<String, Object> Params;
}
