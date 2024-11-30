package com.retailedge.dto.supplier;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class SupplierDto {

    private String supplierName;

    private Double totalOrderValue;

    private Double paidTotal;

    private Double balance;

    private Double lastPayment;

    private String contactName;

    private String contactEmail;

    private String contactPhone;

    private String address;

}