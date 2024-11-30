package com.retailedge.entity.emi;

import com.retailedge.entity.inventory.Product;
import com.retailedge.entity.customer.Customer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "emi_details")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EMIDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(name = "finance_provider")
    private String financeProvider;

    private Double totalAmount;

    private Double balanceAmount;

    private Double emiAmount;

    private String dadoNumber;

    private Double upfront;

    private String scheme;

    private Instant startDate;

    private Instant endDate;

    private String description;
}
