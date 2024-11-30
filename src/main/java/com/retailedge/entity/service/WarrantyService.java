package com.retailedge.entity.service;

import com.retailedge.entity.customer.Customer;
import com.retailedge.entity.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.Instant;

@Entity
@Table(name = "warranty_service_details")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class WarrantyService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne
    private User receivedBy;

    private String productName;

    private String imeiNumber;

    private String serviceProvider;

    private String complaintDescription;

    private String sparePartDescription;

    private Instant serviceDate;
}
