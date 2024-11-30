package com.retailedge.repository.invoice;

import com.retailedge.entity.invoice.InvoiceLineItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceLineItemRepository extends JpaRepository<InvoiceLineItem, Integer>, JpaSpecificationExecutor<InvoiceLineItem> {
}
