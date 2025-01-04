package com.retailedge.service.invoice;

import com.retailedge.dto.credit.CreditReminderDto;
import com.retailedge.dto.customer.CustomerDto;
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
import com.retailedge.utils.ExceptionHandler.ExceptionHandlerUtil;
import com.retailedge.utils.invoice.InvoiceNumberUtil;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
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

    @Autowired
    private ExceptionHandlerUtil exceptionHandlerUtil;

    public ResponseEntity<ResponseModel<?>> list(InvoiceSpecificationBuilder builder) {
        try{
            System.out.println("Hit Invoice");
            return ResponseEntity.ok(new ResponseModel<>(true, "Success", 200, invoiceRepository.findAll(builder.build())));
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseModel<>(false, "Error retrieving invoice details: " + exceptionHandlerUtil.sanitizeErrorMessage(e.getMessage()), 500));
        }
    }

    @Transactional
    public ResponseEntity<ResponseModel<?>> add(InvoiceDto invoiceDto) {
        // Fetch or create the customer based on phone number

        try{
            Customer customer = customerRepository.findByPhoneNumber(invoiceDto.getCustomer().getPhoneNumber());
            if (customer == null) {
                Customer customer1 = modelMapper.map(invoiceDto.getCustomer(), Customer.class);
                customer = customerRepository.save(customer1);
            }

            // Initialize credit and EMI objects
            CreditReminder creditReminder = null;
            EMIDetails emiDetails = null;
            List<Product> productList = new ArrayList<>();

            // Handle payment methods
            if (invoiceDto.getPaymentMethod().contains(PaymentMethod.CREDIT)) {
                CreditReminderDto creditReminderDto = invoiceDto.getCreditReminder();
                if (creditReminderDto != null) {
                    creditReminder = modelMapper.map(creditReminderDto, CreditReminder.class);
                    creditReminder.setTotalAmount(invoiceDto.getTotalAmount());
                    creditReminder.setTotalPaidAmount(0.0);
                    creditReminder.setRemainingBalance(creditReminderDto.getTotalCreditAmount());
                    creditReminder.setLastPayment(0.0);
                    creditReminder.setStatus("Pending");
                    creditReminder.setCustomer(customer); // Set customer for CreditReminder
                }
            }

            if (invoiceDto.getPaymentMethod().contains(PaymentMethod.FINANCE)) {
                EMIDetailsDto emiDetailsDto = invoiceDto.getEmiDetails();
                if (emiDetailsDto != null) {
                    emiDetails = modelMapper.map(emiDetailsDto, EMIDetails.class);
                    emiDetails.setCustomer(customer);

                    for(InvoiceLineItemDto invoiceLineItemDto: invoiceDto.getLineItems()){
                        Optional<Product> optionalProduct = productRepository.findById(Math.toIntExact(invoiceLineItemDto.getProduct().getId()));
                        productList.add(optionalProduct.get());
                    }
                    emiDetails.setProduct(productList);
                   // Set customer for EMIDetails
                }
            }

            // Map the invoice DTO to the entity
            Invoice invoice = modelMapper.map(invoiceDto, Invoice.class);
            invoice.setCustomer(customer); // Set the customer in the invoice
            invoice.setCreditReminder(creditReminder); // Associate CreditReminder
            invoice.setEmiDetails(emiDetails); // Associate EMIDetails
            invoice.setSoldBy(userRepository.findById(invoiceDto.getSoldBy().getId()).get());

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

            // Save and return the invoic

            return ResponseEntity.ok(new ResponseModel<>(true, "Success", 200, invoiceRepository.save(savedInvoice)));
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseModel<>(false, "Error adding invoice details: " + exceptionHandlerUtil.sanitizeErrorMessage(e.getMessage()), 500));
        }
    }


//    @Transactional
//    public ResponseEntity<ResponseModel<?>> update(Integer invoiceId, InvoiceDto invoiceDto) {
//        try{
//            System.out.println("invoice 0 "+ invoiceId);
//            Optional<Invoice> invoiceOptional = invoiceRepository.findById(invoiceId);
//            if (invoiceOptional.isEmpty()) {
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                        .body(new ResponseModel<>(false, "Invoice Details not found!", 500));
//            }
//            System.out.println("invoice 1 "+ invoiceId);
//            Invoice invoice = invoiceOptional.get();
//            Customer customer = customerRepository.phoneNumber(invoiceDto.getCustomer().getPhoneNumber());
//            Customer savedCustomer = this.updateCustomer(customer.getId(), invoiceDto.getCustomer());
//            List<InvoiceLineItem> invoiceLineItems = new ArrayList<>();
//            for(InvoiceLineItemDto invoiceLineItemDto : invoiceDto.getLineItems()){
//                InvoiceLineItem savedInvoiceLineItem = invoiceLineItemService.add(invoiceLineItemDto);
//                invoiceLineItems.add(savedInvoiceLineItem);
//            }
//            System.out.println("invoice 2 "+ invoiceId);
//            modelMapper.map(invoiceDto, invoice);
//            invoice.setCustomer(savedCustomer);
//            invoice.setLineItems(invoiceLineItems);
//            invoice.setSoldBy(userRepository.findById(invoiceDto.getSoldBy().getId()).get());
//            System.out.println("invoice "+ invoice);
//            return ResponseEntity.ok(new ResponseModel<>(true, "Success", 200, invoiceRepository.save(invoice)));
//        }catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(new ResponseModel<>(false, "Error updating invoice details: " + exceptionHandlerUtil.sanitizeErrorMessage(e.getMessage()), 500));
//        }
//    }

    @Transactional
    public ResponseEntity<ResponseModel<?>> update(Integer invoiceId, InvoiceDto invoiceDto) {
        try {
            Optional<Invoice> invoiceOptional = invoiceRepository.findById(invoiceId);
            if (invoiceOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ResponseModel<>(false, "Invoice Details not found!", 500));
            }

            Invoice invoice = invoiceOptional.get();
            // Step 1: Handle Customer Update
            Customer customer = customerRepository.findByPhoneNumber(invoiceDto.getCustomer().getPhoneNumber());
            Customer savedCustomer = this.updateCustomer(customer.getId(), invoiceDto.getCustomer());

            // Step 3: Update Other Invoice Fields
            modelMapper.map(invoiceDto, invoice);
            invoice.setCustomer(savedCustomer);
            invoice.setSoldBy(userRepository.findById(invoiceDto.getSoldBy().getId()).orElse(null));

            // Step 4: Save and Return Updated Invoice
            Invoice updatedInvoice = invoiceRepository.save(invoice);
            return ResponseEntity.ok(new ResponseModel<>(true, "Success", 200, updatedInvoice));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseModel<>(false, "Error updating invoice details: " + exceptionHandlerUtil.sanitizeErrorMessage(e.getMessage()), 500));
        }
    }


    public ResponseEntity<ResponseModel<?>> findByInvoiceNumber(String invoiceNumber) {
        try{
            return ResponseEntity.ok(new ResponseModel<>(true, "Success", 200, invoiceRepository.findByInvoiceNumber(invoiceNumber)));
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseModel<>(false, "Error retrieving invoice details: " + exceptionHandlerUtil.sanitizeErrorMessage(e.getMessage()), 500));
        }
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


    public Customer updateCustomer(Integer customerId, CustomerDto customerDto){
        Optional<Customer> customerOptional = customerRepository.findById(customerId);
        if (customerOptional.isEmpty()) {
            throw new RuntimeException("Customer Details not found with id: " + customerId);
        }
        Customer customer = customerOptional.get();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        modelMapper.getConfiguration().setPropertyCondition(conditions -> {
            return conditions.getSource() != null;
        });
        modelMapper.map(customerDto, customer);
        return customerRepository.save(customer);
    }

}
