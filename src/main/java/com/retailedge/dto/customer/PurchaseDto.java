package com.retailedge.dto.customer;


import com.retailedge.entity.customer.Customer;
import com.retailedge.entity.customer.Feedback;
import com.retailedge.entity.invoice.Invoice;
import com.retailedge.enums.customer.PurchaseType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PurchaseDto {

    private Customer customer;

    private Invoice invoice;

    private LocalDateTime purchaseDate;

    private Double amount;

    private PurchaseType purchaseType;

    private List<Feedback> feedbacks = new ArrayList<>();

}
