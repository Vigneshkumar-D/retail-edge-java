package com.retailedge.entity.suppiler;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "purchase_products")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PurchaseProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String productName;
    private int quantity;
    private double pricePerUnit;
    private String remark;
    private String model;
    private String brand;
    private String variant;
    @ManyToOne
    @JoinColumn(name = "purchase_order_id") // Foreign key column
    @JsonBackReference
    private PurchaseOrder purchaseOrderId;
}
