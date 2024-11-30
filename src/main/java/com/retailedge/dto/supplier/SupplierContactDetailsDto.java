package com.retailedge.dto.supplier;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SupplierContactDetailsDto {
    private String contactName;
    private String contactEmail;
    private String contactPhone;
    private String address;
}
