package com.retailedge.dto.customer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CustomerDto {
    private Integer id;
    private String name;
    private String phoneNumber;
    private String email;
    private Instant dateOfBirth;
    private String address;
    private String state;
    private Integer pinCode;
    private String GSTIN;
}
