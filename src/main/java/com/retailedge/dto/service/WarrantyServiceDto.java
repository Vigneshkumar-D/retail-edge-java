package com.retailedge.dto.service;

import com.retailedge.entity.inventory.Product;
import com.retailedge.dto.customer.CustomerDto;
import com.retailedge.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class WarrantyServiceDto {

    private CustomerDto customer;

    private String productName;

    private User receivedBy;

    private String imeiNumber;

    private String serviceProvider;

    private String complaintDescription;

    private String sparePartDescription;

//    private LocalDate warrantyStartDate;
//
//    private LocalDate warrantyEndDate;

    private Instant serviceDate;
}
