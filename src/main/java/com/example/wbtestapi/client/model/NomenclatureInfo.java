package com.example.wbtestapi.client.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NomenclatureInfo {
    private Long nmId;
    private Integer price;
    private Integer discount;
    private Integer promoCode;
}
