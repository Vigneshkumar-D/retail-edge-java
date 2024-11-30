package com.retailedge.dto.expense;

import com.retailedge.entity.expense.ExpenseCategory;
import com.retailedge.entity.user.User;
import lombok.*;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ExpenseDto {

    private Instant date;

    private ExpenseCategory category;

    private User user;

    private Double amount;

    private String description;

    private String paymentMethod;

    private Long storeId;
}
