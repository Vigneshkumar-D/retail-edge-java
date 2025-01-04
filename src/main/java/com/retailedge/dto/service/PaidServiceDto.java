package com.retailedge.dto.service;

import com.retailedge.dto.customer.CustomerDto;
import com.retailedge.dto.user.UserDTO;
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
public class PaidServiceDto {

    private CustomerDto customer;

    private String productName;

    private String complaintDescription;

    private Long imeiNumber;

    private User receivedBy;

    private String sparePartDescription;

    private Double advancePayment;

    private Double sparePartCost;

    private Double customerCost;

    private Double profitMargin;

    private Double postServicePayment;

    private Instant serviceDate;

    private String status;

    private Instant serviceCompletionDate;
}
