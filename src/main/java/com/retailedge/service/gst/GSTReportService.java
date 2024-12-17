package com.retailedge.service.gst;

import com.retailedge.entity.invoice.Invoice;
import com.retailedge.entity.invoice.InvoiceLineItem;
import com.retailedge.entity.inventory.Category;
import com.retailedge.entity.inventory.Product;
import com.retailedge.entity.gst.*;
import com.retailedge.model.ResponseModel;
import com.retailedge.repository.gst.*;
import com.retailedge.repository.invoice.InvoiceRepository;
import com.retailedge.repository.inventory.ProductRepository;
import com.retailedge.utils.ExceptionHandler.ExceptionHandlerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Service
public class GSTReportService {

    @Autowired
    private GSTReportRepository gstReportRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private TaxSlabRepository taxSlabRepository;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private ExceptionHandlerUtil exceptionHandlerUtil;

    public ResponseEntity<ResponseModel<?>> generateReport(Instant startDate, Instant endDate) {
        try{
            List<Invoice> invoiceList = invoiceRepository.findAllByInvoiceDateBetween(startDate, endDate);
            BigDecimal totalSGST = BigDecimal.ZERO;
            BigDecimal totalCGST = BigDecimal.ZERO;
            BigDecimal totalIGST = BigDecimal.ZERO;

            for (Invoice invoice : invoiceList) {
                List<InvoiceLineItem> invoiceLineItems = invoice.getLineItems();
                for(InvoiceLineItem invoiceLineItem: invoiceLineItems){
                    Product product = invoiceLineItem.getProduct();
                    Category category = product.getCategory();
                    TaxSlab taxSlab = taxSlabRepository.findByCategory_Id(category.getId());
                    Double price = product.getActualPrice();
                    totalSGST = totalSGST.add(taxSlab.getSgst().multiply(BigDecimal.valueOf(price)).divide(BigDecimal.valueOf(100)));
                    totalCGST = totalCGST.add(taxSlab.getCgst().multiply(BigDecimal.valueOf(price)).divide(BigDecimal.valueOf(100)));
                    totalIGST = totalIGST.add(taxSlab.getIgst().multiply(BigDecimal.valueOf(price)).divide(BigDecimal.valueOf(100)));
                }

            }

            GSTReport report = new GSTReport();
            report.setStartDate(startDate);
            report.setEndDate(endDate);
            report.setTotalSGST(totalSGST);
            report.setTotalCGST(totalCGST);
            report.setTotalIGST(totalIGST);
            report.setTotalTax(totalSGST.add(totalCGST).add(totalIGST));

            gstReportRepository.save(report);
            return ResponseEntity.ok(new ResponseModel<>(true, "Success", 200, report));
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseModel<>(false, "Error retrieving gst report: " + exceptionHandlerUtil.sanitizeErrorMessage(e.getMessage()), 500));
        }
    }
}
