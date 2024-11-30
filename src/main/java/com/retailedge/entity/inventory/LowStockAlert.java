package com.retailedge.entity.inventory;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "low_stock_alerts")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LowStockAlert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private String brand;

    private String model;

    private Integer minQuantity;

    private boolean alertSent;

    private LocalDateTime alertDate;
}
