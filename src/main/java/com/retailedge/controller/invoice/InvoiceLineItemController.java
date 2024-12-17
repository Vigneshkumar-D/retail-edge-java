package com.retailedge.controller.invoice;

import com.retailedge.dto.invoice.InvoiceLineItemDto;
import com.retailedge.entity.invoice.InvoiceLineItem;
import com.retailedge.model.ResponseModel;
import com.retailedge.service.invoice.InvoiceLineItemService;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/invoice-item")
public class
InvoiceLineItemController {

    @Autowired
    private InvoiceLineItemService invoiceLineItemService;

    @GetMapping
    public List<InvoiceLineItem> list(){
        return invoiceLineItemService.list();
    }

    @PostMapping
    public InvoiceLineItem add(@RequestBody InvoiceLineItemDto invoiceLineItemDto){
        return invoiceLineItemService.add(invoiceLineItemDto);
    }

    @PutMapping("/{invoiceLineItemId}")
    public InvoiceLineItem update(@PathVariable("invoiceLineItemId") Integer invoiceLineItemId, @RequestBody InvoiceLineItemDto invoiceLineItemDto){
        return invoiceLineItemService.update(invoiceLineItemId, invoiceLineItemDto);
    }

    @DeleteMapping("/{invoiceLineItemId}")
    public ResponseEntity<ResponseModel<?>> delete(@PathVariable("invoiceLineItemId") Integer invoiceLineItemId) throws Exception {
        return invoiceLineItemService.delete(invoiceLineItemId);
    }
}
