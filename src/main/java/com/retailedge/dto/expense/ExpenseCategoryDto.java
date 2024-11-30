package com.retailedge.dto.expense;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ExpenseCategoryDto {

    private String name;

    private Double budget;

//    private List<Expense> expenses;
}
