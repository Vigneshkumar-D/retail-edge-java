package com.retailedge.dto.supplier;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PurchaseProductDto {
    private String productName;
    private int quantity;
    private double pricePerUnit;
    private String remark;
    private String model;
    private String brand;
    private String variant;
}
