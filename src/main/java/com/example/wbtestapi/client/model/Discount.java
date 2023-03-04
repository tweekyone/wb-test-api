package com.example.wbtestapi.client.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter @ToString @AllArgsConstructor
public class Discount {
    private int discount;
    private long nm;
}
