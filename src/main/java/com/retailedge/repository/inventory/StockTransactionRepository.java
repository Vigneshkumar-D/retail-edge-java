package com.retailedge.repository.inventory;

import com.retailedge.entity.inventory.StockTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface StockTransactionRepository extends JpaRepository<StockTransaction, Integer>, JpaSpecificationExecutor<StockTransaction> {

}