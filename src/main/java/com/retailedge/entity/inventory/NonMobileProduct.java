package com.retailedge.entity.inventory;

import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
//@Table(name = "non_mobile_products")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class NonMobileProduct extends Product {

    @Transient
    private String barcodeImagePath;

    @Lob
    private byte[] barcodeImage;

    private String barcode;

}
