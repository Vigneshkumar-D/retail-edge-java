package com.retailedge.entity.suppiler;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;
@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SupplierContactDetails {
    private String contactName;
    private String contactEmail;
    private String contactPhone;
    private String address;
}
