package com.retailedge.entity.inventory;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Embeddable
public class ShortageProducts {
    private String productName;
    private String imeiOrBarcode;
    private Integer shortageQuantity;
    private Integer actualQuantity;
}
