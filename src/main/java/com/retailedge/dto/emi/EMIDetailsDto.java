package com.retailedge.dto.emi;

import com.retailedge.entity.customer.Customer;
import com.retailedge.entity.inventory.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EMIDetailsDto {
    private Customer customer;
    private Product product;
    private String financeProvider;
    private Double totalAmount;
    private Double emiAmount;
    private Double balanceAmount;
    private String dadoNumber;
    private Double upfront;
    private String scheme;
    private Instant startDate;
    private Instant endDate;
    private String description;
}
