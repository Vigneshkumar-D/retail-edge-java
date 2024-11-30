package com.retailedge.dto.supplier;

import com.retailedge.entity.suppiler.Supplier;
import com.retailedge.service.supplier.SupplierService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PaymentDetailsDto {
    private Supplier supplier;
    private Instant paymentDate;
    private Double paymentAmount;
    private String remarks;
    private String paymentMethod;
    private Long transactionNumber;
    private String paymentStatus;
}
