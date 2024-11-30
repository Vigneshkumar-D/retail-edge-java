package com.retailedge.dto.inventory;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LowStockAlertDto {

    private String product;

    private String brand;

    private String model;

    private Integer minQuantity;

    private boolean alertSent;

    private LocalDateTime alertDate;
}
