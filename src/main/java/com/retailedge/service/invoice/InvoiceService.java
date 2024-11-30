package com.retailedge.service.invoice;

import com.retailedge.dto.credit.CreditReminderDto;
import com.retailedge.dto.emi.EMIDetailsDto;
import com.retailedge.dto.invoice.InvoiceDto;
import com.retailedge.dto.invoice.InvoiceLineItemDto;
import com.retailedge.entity.credit.CreditReminder;
import com.retailedge.entity.customer.Customer;
import com.retailedge.entity.emi.EMIDetails;
import com.retailedge.entity.invoice.Invoice;
import com.retailedge.entity.invoice.InvoiceLineItem;
import com.retailedge.entity.inventory.Product;
import com.retailedge.entity.service.PaidService;
import com.retailedge.enums.invoice.PaymentMethod;
import com.retailedge.model.ResponseModel;
import com.retailedge.repository.customer.CustomerRepository;
import com.retailedge.repository.invoice.InvoiceRepository;
import com.retailedge.repository.inventory.ProductRepository;
import com.retailedge.repository.user.UserRepository;
import com.retailedge.service.customer.CustomerService;
import com.retailedge.service.customer.PurchaseService;
//import com.retailedge.service.kafka.NotificationService;
//import com.retailedge.service.kafka.NotificationService;
import com.retailedge.service.inventory.ProductService;
import com.retailedge.specification.invoice.InvoiceSpecificationBuilder;
import com.retailedge.specification.service.PaidServiceSpecificationBuilder;
import com.retailedge.utils.invoice.InvoiceNumberUtil;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class InvoiceService {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private InvoiceLineItemService invoiceLineItemService;

    @Autowired
    private PurchaseService purchaseService;

    @Autowired
    private InvoiceNumberUtil invoiceNumberUtil;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserRepository userRepository;

//    @Autowired
//    private NotificationService notificationService;

    @Autowired
    private ProductRepository productRepository;

    public  List<Invoice> list(InvoiceSpecificationBuilder builder){
        return invoiceRepository.findAll(builder.build());
    }

    @Transactional
    public Invoice add(InvoiceDto invoiceDto) {
        // Fetch or create the customer based on phone number
        Customer customer = customerRepository.phoneNumber(invoiceDto.getCustomer().getPhoneNumber());
        if (customer == null) {
            customer = customerService.add(invoiceDto.getCustomer());
        }

        // Initialize credit and EMI objects
        CreditReminder creditReminder = null;
        EMIDetails emiDetails = null;

        // Handle payment methods
        if (invoiceDto.getPaymentMethod().contains(PaymentMethod.CREDIT)) {
            CreditReminderDto creditReminderDto = invoiceDto.getCreditReminder();
            if (creditReminderDto != null) {
                creditReminder = modelMapper.map(creditReminderDto, CreditReminder.class);
                creditReminder.setCustomer(customer); // Set customer for CreditReminder
            }
        }

        if (invoiceDto.getPaymentMethod().contains(PaymentMethod.FINANCE)) {
            EMIDetailsDto emiDetailsDto = invoiceDto.getEmiDetails();
            if (emiDetailsDto != null) {
                emiDetails = modelMapper.map(emiDetailsDto, EMIDetails.class);
                emiDetails.setCustomer(customer);
                Optional<Product> product = productRepository.findById(Math.toIntExact(invoiceDto.getLineItems().get(0).getProduct().getId()));
                emiDetails.setProduct(product.get());// Set customer for EMIDetails
            }
        }

        // Map the invoice DTO to the entity
        Invoice invoice = modelMapper.map(invoiceDto, Invoice.class);
        invoice.setCustomer(customer); // Set the customer in the invoice
        invoice.setCreditReminder(creditReminder); // Associate CreditReminder
        invoice.setEmiDetails(emiDetails); // Associate EMIDetails
        invoice.setSoldBy(userRepository.findById(invoiceDto.getSoldBy()).get());

        // Map and add invoice line items
        List<InvoiceLineItem> invoiceLineItems = new ArrayList<>();
        for (InvoiceLineItemDto invoiceLineItemDto : invoiceDto.getLineItems()) {
            InvoiceLineItem invoiceLineItem = invoiceLineItemService.add(invoiceLineItemDto);
            productService.updateProductQuantity(invoiceLineItem.getProduct(), invoiceLineItem.getQuantity());
            invoiceLineItems.add(invoiceLineItem);
        }
        invoice.setLineItems(invoiceLineItems);

        // Generate and set the invoice number
        Invoice savedInvoice  = invoiceRepository.save(invoice);
        savedInvoice.setInvoiceNumber(InvoiceNumberUtil.generateInvoiceNumber(savedInvoice.getId()));

        // Save and return the invoice
        return invoiceRepository.save(savedInvoice);
    }


    public Invoice update(Integer invoiceId, InvoiceDto invoiceDto) {
        Optional<Invoice> invoiceOptional = invoiceRepository.findById(invoiceId);
        if (invoiceOptional.isEmpty()) {
            throw new RuntimeException("Invoice Details not found with id: " + invoiceId);
        }
        Invoice invoice = invoiceOptional.get();
        Customer  savedCustomer = customerService.update(invoiceDto.getCustomer().getId(), invoiceDto.getCustomer());
        List<InvoiceLineItem> invoiceLineItems = new ArrayList<>();
        for(InvoiceLineItemDto invoiceLineItemDto : invoiceDto.getLineItems()){
            InvoiceLineItem savedInvoiceLineItem = invoiceLineItemService.add(invoiceLineItemDto);
            invoiceLineItems.add(savedInvoiceLineItem);
        }
        modelMapper.map(invoiceDto, invoice);
        invoice.setCustomer(savedCustomer);
        invoice.setLineItems(invoiceLineItems);
        return invoiceRepository.save(invoice);
    }

    public Invoice findByInvoiceNumber(String invoiceNumber) {
        return invoiceRepository.findByInvoiceNumber(invoiceNumber);
    }

    public ResponseEntity<ResponseModel<?>> delete(Integer invoiceId) throws Exception {
        try {
            if (!invoiceRepository.existsById(invoiceId)) {
                // Return 404 Not Found
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseModel<>(false, "Invoice not found", 404));
            }
            invoiceRepository.deleteById(invoiceId);
            // Return 200 OK if the category is deleted successfully
            return ResponseEntity.ok(new ResponseModel<>(true, "Deleted successfully", 200));
        } catch (Exception e) {
            // Return 500 Internal Server Error for any unexpected errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseModel<>(false, "Error deleting Invoice: " + e.getMessage(), 500));
        }
    }

}
