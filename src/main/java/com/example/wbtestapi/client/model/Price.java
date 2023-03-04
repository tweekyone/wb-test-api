package com.example.wbtestapi.client.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter @ToString @AllArgsConstructor
public class Price {
    //"nmId": 1234567,
    //    "price": 1000

    private long nmId;
    private int price;
}
