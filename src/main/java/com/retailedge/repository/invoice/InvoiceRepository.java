package com.retailedge.repository.invoice;

import com.retailedge.dto.invoice.InvoiceDto;
import com.retailedge.entity.invoice.Invoice;
import com.retailedge.entity.service.PaidService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;


@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Integer>, JpaSpecificationExecutor<Invoice> {
    Invoice findByInvoiceNumber(String invoiceNumber);
    List<Invoice> findAllByInvoiceDateBetween(Instant startDate, Instant endDate);
//    @Query("SELECT s FROM Invoice s " +
//            "WHERE CAST(s.invoiceDate AS date) = CAST(:date AS date)")
//    List<InvoiceDto> findInvoiceByExactDate(@Param("date") LocalDate date);

    @Query("SELECT new com.retailedge.dto.invoice.InvoiceDto(" +
            "i.id, i.invoiceNumber, i.invoiceDate, i.totalAmount, " +
            "i.cashPayment, i.upiPayment, i.cardPayment, " +
            "cr.remainingBalance) " +
            "FROM Invoice i " +
            "LEFT JOIN i.creditReminder cr " +
            "WHERE CAST(i.invoiceDate AS date) = CAST(:date AS date)")
    List<InvoiceDto> findInvoiceByExactDate(@Param("date") LocalDate date);




}
