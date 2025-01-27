package com.retailedge.dto.supplier;

import com.retailedge.entity.suppiler.Supplier;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PurchaseOrderDto {
    private Supplier supplier;
    private List<PurchaseProductDto> purchaseProducts;
    private Double orderTotal;
    private String remark;
    private Instant orderDate;
    private Instant deliveryDate;
    private String status;
}