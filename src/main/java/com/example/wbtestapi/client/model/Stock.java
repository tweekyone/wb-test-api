package com.example.wbtestapi.client.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class Stock {
    private String barcode;
    private int stock;
    private int warehouseId;

    public Stock() { }

    public Stock(String barcode, int stock) {
        this.barcode = barcode;
        this.stock = stock;
        this.warehouseId = 140313;
    }

    public Stock(String barcode, int stock, int warehouseId) {
        this.barcode = barcode;
        this.stock = stock;
        this.warehouseId = warehouseId;
    }

}
