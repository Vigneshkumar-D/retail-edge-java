package com.retailedge.repository.customer;

import com.retailedge.entity.customer.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface PurchaseRepository extends JpaRepository<Purchase,Integer>, JpaSpecificationExecutor<Purchase> {
    List<Purchase> findByInvoiceId(Integer invoiceId);
//    List<Purchase> findByCustomerId(Integer customerId);
}
