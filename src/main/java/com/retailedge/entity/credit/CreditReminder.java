package com.retailedge.entity.credit;

import com.retailedge.enums.credit.CreditType;
import com.retailedge.entity.customer.Customer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;


@Entity
@Table(name = "credit_reminder")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreditReminder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;
    private Double totalCreditAmount;
    private Double lastPayment;
    private Double remainingBalance;
    private Instant dueDate;
    private Instant lastPaymentDate;
    private boolean reminderSent;
    private String description;
    @Enumerated(EnumType.STRING)
    private List<CreditType> creditType;

}
