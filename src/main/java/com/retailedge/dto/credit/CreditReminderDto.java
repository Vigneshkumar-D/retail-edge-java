package com.retailedge.dto.credit;

import com.retailedge.dto.customer.CustomerDto;
import com.retailedge.enums.credit.CreditType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreditReminderDto {
    private CustomerDto customer;
    private Double totalCreditAmount;
    private Double remainingBalance;
    private Double lastPayment;
    private Instant dueDate;
    private Instant lastPaymentDate;
    private boolean reminderSent;
    private String description;
    @Enumerated(EnumType.STRING)
    private List<CreditType> creditType;
}
