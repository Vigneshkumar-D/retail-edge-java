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
@Table(name = "paid_service_details")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PaidService {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "customer_id", referencedColumnName = "id", nullable = false)
    private Customer customer;

    private String productName;

    private Long imeiNumber;

    @ManyToOne
    private User receivedBy;

    private String complaintDescription;

    private String sparePartDescription;

    private Double advancePayment;

    private Double sparePartCost;

    private Double customerCost;

    private Double profitMargin;

    private Instant serviceDate;

}
