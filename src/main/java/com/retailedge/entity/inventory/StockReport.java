package com.retailedge.entity.inventory;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "stock_report")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class StockReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String productName;

    private String IMEIorBarcode;

    private Integer quantity;

    private String variant;
}
