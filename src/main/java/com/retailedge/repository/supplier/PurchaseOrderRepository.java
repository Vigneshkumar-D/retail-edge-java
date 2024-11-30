package com.retailedge.repository.supplier;

import com.retailedge.entity.suppiler.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseOrderRepository  extends JpaRepository<PurchaseOrder,Integer>, JpaSpecificationExecutor<PurchaseOrder> {

    @Query( value = "SELECT p FROM PurchaseOrder p WHERE p.supplier.id = :supplierId ORDER BY p.orderDate DESC LIMIT 3")
    List<PurchaseOrder> findTop3BySupplierIdOrderByOrderDateDesc(@Param("supplierId") Integer supplierId);
}

