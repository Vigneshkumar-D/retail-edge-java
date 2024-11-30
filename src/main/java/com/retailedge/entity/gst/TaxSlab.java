package com.retailedge.entity.gst;

import com.retailedge.entity.inventory.Category;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "tax_slab")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TaxSlab {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String region;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    private BigDecimal sgst;
    private BigDecimal cgst;
    private BigDecimal igst;
    private String serviceType;

}
