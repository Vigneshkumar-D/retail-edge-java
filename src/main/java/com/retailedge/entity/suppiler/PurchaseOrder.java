package com.retailedge.entity.suppiler;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "purchase_orders")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PurchaseOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "supplier_id", nullable = false)
    private Supplier supplier;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "purchaseOrderId")
    @JsonManagedReference
    private List<PurchaseProduct> purchaseProducts;
    private Double orderTotal;
    private LocalDate deliveryDate;
    private String status;
}


