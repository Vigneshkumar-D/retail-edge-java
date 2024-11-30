package com.retailedge.repository.inventory;

import com.retailedge.entity.inventory.LowStockAlert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface LowStockAlertRepository extends JpaRepository<LowStockAlert,Integer>, JpaSpecificationExecutor<LowStockAlert> {

}