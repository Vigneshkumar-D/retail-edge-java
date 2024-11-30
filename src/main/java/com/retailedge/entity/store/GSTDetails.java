package com.retailedge.entity.store;

import com.retailedge.entity.customer.Customer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "gst_details")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GSTDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String GSTIN;

    private String legalName;

    private String tradeName;

    private String typeOfRegistration;

    private String state;

    private Double jurisdiction;

}
