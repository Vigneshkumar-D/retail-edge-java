package com.retailedge.repository.invoice;

import com.retailedge.entity.invoice.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;


@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Integer>, JpaSpecificationExecutor<Invoice> {
    Invoice findByInvoiceNumber(String invoiceNumber);
    List<Invoice> findAllByInvoiceDateBetween(Instant startDate, Instant endDate);
}
