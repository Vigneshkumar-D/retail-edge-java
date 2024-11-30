package com.retailedge.repository.inventory;

import com.retailedge.entity.inventory.StockReportHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface StockReportHistoryRepository  extends JpaRepository<StockReportHistory,Long>, JpaSpecificationExecutor<StockReportHistory> {

}
