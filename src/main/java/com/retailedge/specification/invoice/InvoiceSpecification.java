package com.retailedge.specification.invoice;

import com.retailedge.entity.invoice.Invoice;
import com.retailedge.entity.ordertrack.Order;
import com.retailedge.specification.SearchCriteria;
import com.retailedge.specification.generic.GenericSpecification;


public class InvoiceSpecification extends GenericSpecification<Invoice> {
    public InvoiceSpecification(SearchCriteria criteria) {
        super(criteria);
    }
}
