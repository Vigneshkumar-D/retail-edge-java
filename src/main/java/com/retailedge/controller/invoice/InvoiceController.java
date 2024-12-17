package com.retailedge.controller.invoice;

import com.retailedge.dto.invoice.InvoiceDto;
import com.retailedge.entity.invoice.Invoice;
import com.retailedge.entity.ordertrack.Order;
import com.retailedge.model.ResponseModel;
import com.retailedge.service.invoice.InvoiceService;
import com.retailedge.specification.invoice.InvoiceSpecificationBuilder;
import com.retailedge.specification.ordertrack.OrderSpecificationBuilder;
import jakarta.websocket.server.PathParam;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.relational.core.sql.In;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/invoice")
public class InvoiceController {

    @Autowired
    private InvoiceService invoiceService;

    @GetMapping
    public ResponseEntity<ResponseModel<?>> list(
            @RequestParam(required = false, name = "id")Integer id,
            @RequestParam(required = false, name = "customerName")String customerName,
            @RequestParam(required = false, name = "phoneNumber") String phoneNumber,
            @RequestParam(required = false, name = "invoiceDate") Instant invoiceDate,
            @RequestParam(required = false, name = "soldBy") String soldBy,
            @RequestParam(required = false, name = "invoiceNumber") String invoiceNumber){

        InvoiceSpecificationBuilder builder=new InvoiceSpecificationBuilder();
        if(id!=null)builder.with("id",":",id);
        if(customerName!=null)builder.with("customer.name",":",customerName);
        if(phoneNumber!=null)builder.with("customer.phoneNumber",":",phoneNumber);
        if(invoiceDate!=null)builder.with("invoiceDate",":",invoiceDate);
        if(soldBy!=null)builder.with("status",":",soldBy);
        if(invoiceNumber!=null)builder.with("invoiceNumber",":",invoiceNumber);

        return invoiceService.list(builder);
    }


    @PostMapping
    public ResponseEntity<ResponseModel<?>> add(@RequestBody InvoiceDto invoiceDto){
        return invoiceService.add(invoiceDto);
    }

    @PutMapping("/{invoiceId}")
    public ResponseEntity<ResponseModel<?>> update(@PathVariable("invoiceId") Integer invoiceId, @RequestBody InvoiceDto invoiceDto){
        return invoiceService.update(invoiceId, invoiceDto);
    }

    @DeleteMapping("/{invoiceId}")
    public ResponseEntity<ResponseModel<?>> delete(@PathVariable("invoiceId") Integer invoiceId) throws Exception {
        return invoiceService.delete(invoiceId);
    }

    @GetMapping("/{invoiceNumber}")
    public ResponseEntity<ResponseModel<?>> findByInvoiceNumber(@PathVariable("invoiceNumber") String invoiceNumber){
        return invoiceService.findByInvoiceNumber(invoiceNumber);
    }
}
