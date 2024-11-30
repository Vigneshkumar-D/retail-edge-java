package com.retailedge.dto.inventory;

import com.retailedge.enums.inventory.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class StockTransactionDto {

    private ProductDto product;

    private TransactionType type;

    private int quantity;

    private String fromOrTo;

    private Instant transactionDate;

}
