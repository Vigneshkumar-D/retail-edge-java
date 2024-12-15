package com.retailedge.entity.inventory;

import com.retailedge.entity.gst.HSNCode;
import com.retailedge.enums.inventory.ProductStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "products")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
//@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
//@DiscriminatorColumn(name = "dtype", discriminatorType = DiscriminatorType.STRING)
//@DiscriminatorColumn(name = "product_type", discriminatorType = DiscriminatorType.STRING)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String productName;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    private int stockLevel;

    private String brand;

    private String model;

    @ManyToOne
    private HSNCode hsnCode;

//    @Transient
//    private String barcodeImage;
//
//    private String barcode;

    @Column(name = "low_stock_threshold")
    private int lowStockThreshold;

    @Column(name = "actual_price", nullable = false)
    private Double actualPrice;

    @Column(name = "selling_price", nullable = false)
    private Double sellingPrice;

    private String variant;

    @Column(unique = true)
    private String imeiNumber;

    @Enumerated(EnumType.STRING)
    private ProductStatus status;

}


