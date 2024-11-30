package com.retailedge.dto.store;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GSTDetailsDto {

    private String GSTIN;

    private String legalName;

    private String tradeName;

    private String typeOfRegistration;

    private String state;

    private Double jurisdiction;
}
