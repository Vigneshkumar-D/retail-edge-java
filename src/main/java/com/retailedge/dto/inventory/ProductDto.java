package com.retailedge.dto.inventory;

import com.retailedge.entity.inventory.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {

        private Integer id;

        private String productName;

        private Double actualPrice;

        private Double sellingPrice;

        private Category category;

        private int stockLevel;

        private String brand;

        private String model;

        private String barcodeImage;

        private String barcode;

        private int lowStockThreshold;

        private String variant;

        private String imeiNumber;

}
