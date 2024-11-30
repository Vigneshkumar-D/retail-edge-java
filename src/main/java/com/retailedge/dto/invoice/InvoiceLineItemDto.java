package com.retailedge.dto.invoice;

import com.retailedge.entity.inventory.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class InvoiceLineItemDto {
    private Product product;
    private Integer quantity;
    private Double pricePerUnit;
    private Double lineTotal;
    private Double sgstAmount;
    private Double cgstAmount;
    private Double igstAmount;
    private Double totalTaxAmount;
    private Double discountAmount;
}
