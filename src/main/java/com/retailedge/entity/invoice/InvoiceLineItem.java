package com.retailedge.entity.invoice;

import com.retailedge.entity.inventory.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "invoice_line_item")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class InvoiceLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne // Specify the relationship
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "price_per_unit", nullable = false)
    private Double pricePerUnit;

    @Column(name = "line_total", nullable = false)
    private Double lineTotal;

    @Column(name = "sgst_amount", nullable = false)
    private Double sgstAmount;

    @Column(name = "cgst_amount", nullable = false)
    private Double cgstAmount;

    @Column(name = "igst_amount", nullable = false)
    private Double igstAmount;

    @Column(name = "total_tax_amount", nullable = false)
    private Double totalTaxAmount;

    @Column(name = "discount_amount")
    private Double discountAmount;

}
