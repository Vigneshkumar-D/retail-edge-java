package com.retailedge.service.service;

import com.retailedge.entity.customer.Customer;
import com.retailedge.entity.user.User;
import com.retailedge.model.ResponseModel;
import com.retailedge.repository.customer.CustomerRepository;
import com.retailedge.dto.service.PaidServiceDto;
import com.retailedge.entity.service.PaidService;
import com.retailedge.repository.service.PaidServiceRepository;
import com.retailedge.repository.user.UserRepository;
//import com.retailedge.specification.supplier.service.PaidServiceSpecificationBuilder;
import com.retailedge.specification.service.PaidServiceSpecificationBuilder;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PaidServiceDetailsService {

    @Autowired
    private PaidServiceRepository paidServiceRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    public  List<PaidService> list(PaidServiceSpecificationBuilder builder){
        return paidServiceRepository.findAll(builder.build());
    }

    public PaidService add(PaidServiceDto paidServiceDto){
        Customer customer = customerRepository.findByPhoneNumber(paidServiceDto.getCustomer().getPhoneNumber());
        if(customer==null){
            if (paidServiceDto.getCustomer() != null) {
                customer = modelMapper.map(paidServiceDto.getCustomer(), Customer.class);
            }
        }
        assert customer != null;
        Customer savedCustomer =  customerRepository.save(customer);
        PaidService paidService = modelMapper.map(paidServiceDto, PaidService.class);
        paidService.setReceivedBy(userRepository.findById(paidServiceDto.getReceivedBy().getId()).get());
        paidService.setCustomer(savedCustomer);
        return paidServiceRepository.save(paidService);
    }

//    public PaidService update(Integer paidServiceId, PaidServiceDto paidServiceDto){
//        Optional<PaidService> paidServiceOptional = paidServiceRepository.findById(paidServiceId);
//
//        if (paidServiceOptional.isEmpty()) {
//            throw new RuntimeException("Paid Service Details not found with id: " + paidServiceId);
//        }
//
//        PaidService paidService = paidServiceOptional.get();
//
//        modelMapper.map(paidServiceDto, paidService);
//
//        return paidServiceRepository.save(paidService);
//    }

//    public PaidService update(Integer paidServiceId, PaidServiceDto paidServiceDto) {
//        // Retrieve the PaidService entity by ID
//        PaidService paidService = paidServiceRepository.findById(paidServiceId)
//                .orElseThrow(() -> new RuntimeException("Paid Service Details not found with id: " + paidServiceId));
//        paidService.setReceivedBy(userRepository.findById(paidServiceDto.getReceivedBy().getId()).get());
//        // Check if the customer exists by email
//        Customer customer = customerRepository.findByPhoneNumber(paidServiceDto.getCustomer().getPhoneNumber());
//
//        // If customer does not exist, create a new one
//        if (customer == null) {
//            if (paidServiceDto.getCustomer() != null) {
//                // Map and save the new Customer
//                customer = modelMapper.map(paidServiceDto.getCustomer(), Customer.class);
//                customerRepository.save(customer);
//            }
//        }
//
//        // Set the customer on the PaidService entity
//
//
//        // Map other fields from DTO to entity, except for customer
//        // Skip setting customer to avoid altering the existing relationship
//        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
//        modelMapper.getConfiguration().setPropertyCondition(conditions -> {
//            return conditions.getSource() != null;
//        });
//        modelMapper.map(paidServiceDto, paidService);
//        paidService.setCustomer(customer);
//
//        modelMapper.typeMap(PaidServiceDto.class, PaidService.class)
//                .addMappings(mapper -> mapper.skip(PaidService::setCustomer));
//
//        return paidServiceRepository.save(paidService);
//    }
//


    public PaidService update(Integer paidServiceId, PaidServiceDto paidServiceDto) {
        // Retrieve the PaidService entity by ID
        PaidService paidService = paidServiceRepository.findById(paidServiceId)
                .orElseThrow(() -> new RuntimeException("Paid Service Details not found with id: " + paidServiceId));

        // Update the `receivedBy` field
        if (paidServiceDto.getReceivedBy() != null && paidServiceDto.getReceivedBy().getId() != null) {
            User receivedBy = userRepository.findById(paidServiceDto.getReceivedBy().getId())
                    .orElseThrow(() -> new RuntimeException("User not found with id: " + paidServiceDto.getReceivedBy().getId()));
            paidService.setReceivedBy(receivedBy);
        }

        // Handle the customer relationship
        Customer customer = null;
        if (paidServiceDto.getCustomer() != null && paidServiceDto.getCustomer().getPhoneNumber() != null) {
            customer = customerRepository.findByPhoneNumber(paidServiceDto.getCustomer().getPhoneNumber());

            if (customer == null) {
                // If the customer doesn't exist, create a new one
                customer = new Customer();
                customer.setName(paidServiceDto.getCustomer().getName());
                customer.setEmail(paidServiceDto.getCustomer().getEmail());
                customer.setPhoneNumber(paidServiceDto.getCustomer().getPhoneNumber());
                customer.setAddress(paidServiceDto.getCustomer().getAddress());
                customer.setDateOfBirth(paidServiceDto.getCustomer().getDateOfBirth());
                customer.setGSTIN(paidServiceDto.getCustomer().getGSTIN());
                customer.setPinCode(paidServiceDto.getCustomer().getPinCode());
                customer.setState(paidServiceDto.getCustomer().getState());
                customerRepository.save(customer);
            }
        }

        // Update the `customer` relationship
        if (customer != null) {
            customer.setName(paidServiceDto.getCustomer().getName());
            customer.setEmail(paidServiceDto.getCustomer().getEmail());
            customer.setPhoneNumber(paidServiceDto.getCustomer().getPhoneNumber());
            customer.setAddress(paidServiceDto.getCustomer().getAddress());
            customer.setDateOfBirth(paidServiceDto.getCustomer().getDateOfBirth());
            customer.setGSTIN(paidServiceDto.getCustomer().getGSTIN());
            customer.setPinCode(paidServiceDto.getCustomer().getPinCode());
            customer.setState(paidServiceDto.getCustomer().getState());
            customerRepository.save(customer);
        }

        paidService.setCustomer(customer);

        // Manually update fields in the PaidService entity
        if (paidServiceDto.getProductName() != null) {
            paidService.setProductName(paidServiceDto.getProductName());
        }

        if (paidServiceDto.getImeiNumber() != null) {
            paidService.setImeiNumber(paidServiceDto.getImeiNumber());
        }

        if (paidServiceDto.getComplaintDescription() != null) {
            paidService.setComplaintDescription(paidServiceDto.getComplaintDescription());
        }

        if (paidServiceDto.getSparePartDescription() != null) {
            paidService.setSparePartDescription(paidServiceDto.getSparePartDescription());
        }

        if (paidServiceDto.getAdvancePayment() != null) {
            paidService.setAdvancePayment(paidServiceDto.getAdvancePayment());
        }

        if (paidServiceDto.getSparePartCost() != null) {
            paidService.setSparePartCost(paidServiceDto.getSparePartCost());
        }

        if (paidServiceDto.getCustomerCost() != null) {
            paidService.setCustomerCost(paidServiceDto.getCustomerCost());
        }

        if (paidServiceDto.getProfitMargin() != null) {
                paidService.setProfitMargin(paidServiceDto.getProfitMargin());
        }

        if (paidServiceDto.getServiceDate() != null) {
            paidService.setServiceDate(paidServiceDto.getServiceDate());
        }

        // Save and return the updated PaidService entity
        return paidServiceRepository.save(paidService);
    }


    public ResponseEntity<ResponseModel<?>> delete(Integer paidServiceId) throws Exception {

        try {
            if (!paidServiceRepository.existsById(paidServiceId)) {
                // Return 404 Not Found
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseModel<>(false, "Paid Service not found", 404));
            }
            paidServiceRepository.deleteById(paidServiceId);
            // Return 200 OK if the category is deleted successfully
            return ResponseEntity.ok(new ResponseModel<>(true, "Deleted successfully", 200));
        } catch (Exception e) {
            // Return 500 Internal Server Error for any unexpected errors
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseModel<>(false, "Error deleting Paid Service: " + e.getMessage(), 500));
        }
    }




}
