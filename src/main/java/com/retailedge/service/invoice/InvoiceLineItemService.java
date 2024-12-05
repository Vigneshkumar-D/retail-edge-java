package com.retailedge.service.invoice;

import com.retailedge.dto.invoice.InvoiceLineItemDto;
import com.retailedge.entity.invoice.InvoiceLineItem;
import com.retailedge.model.ResponseModel;
import com.retailedge.repository.customer.CustomerRepository;
import com.retailedge.repository.invoice.InvoiceLineItemRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InvoiceLineItemService {

    @Autowired
    private InvoiceLineItemRepository invoiceLineItemRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CustomerRepository customerRepository;

    public List<InvoiceLineItem> list(){
        return invoiceLineItemRepository.findAll();
    }

    public InvoiceLineItem add(InvoiceLineItemDto invoiceLineItemDto){
        InvoiceLineItem invoiceLineItem = modelMapper.map(invoiceLineItemDto, InvoiceLineItem.class);
        return invoiceLineItemRepository.save(invoiceLineItem);
    }

    public InvoiceLineItem update(Integer invoiceLineItemId, InvoiceLineItemDto invoiceLineItemDto) {
        Optional<InvoiceLineItem> invoiceLineItemOptional = invoiceLineItemRepository.findById(invoiceLineItemId);
        if (!invoiceLineItemOptional.isPresent()) {
            throw new RuntimeException("Invoice Line Item Details not found with id: " + invoiceLineItemId);
        }
        InvoiceLineItem invoiceLineItem = invoiceLineItemOptional.get();

        modelMapper.map(invoiceLineItemId, invoiceLineItem);
        return invoiceLineItemRepository.save(invoiceLineItem);
    }

    public ResponseEntity<ResponseModel<?>> delete(Integer invoiceLineItemId) throws Exception {
        try {
            if (!invoiceLineItemRepository.existsById(invoiceLineItemId)) {
                // Return 404 Not Found
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseModel<>(false, "Invoice Line Item not found", 404));
            }
            invoiceLineItemRepository.deleteById(invoiceLineItemId);
            // Return 200 OK if the category is deleted successfully
            return ResponseEntity.ok(new ResponseModel<>(true, "Deleted successfully", 200));
        } catch (Exception e) {
            // Return 500 Internal Server Error for any unexpected errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseModel<>(false, "Error deleting Invoice Line Item: " + e.getMessage(), 500));
        }
    }

}
