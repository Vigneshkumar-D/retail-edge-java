package com.retailedge.entity.suppiler;

import jakarta.persistence.*;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "suppliers")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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