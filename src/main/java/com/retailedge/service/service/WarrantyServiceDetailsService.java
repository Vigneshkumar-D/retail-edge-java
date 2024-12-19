package com.retailedge.service.service;


import com.retailedge.entity.customer.Customer;
import com.retailedge.entity.service.PaidService;
import com.retailedge.entity.user.User;
import com.retailedge.model.ResponseModel;
import com.retailedge.repository.customer.CustomerRepository;
import com.retailedge.dto.service.WarrantyServiceDto;
import com.retailedge.entity.service.WarrantyService;
import com.retailedge.repository.service.WarrantyServiceRepository;
import com.retailedge.repository.user.UserRepository;
import com.retailedge.specification.service.PaidServiceSpecificationBuilder;
import com.retailedge.specification.service.WarrantyServiceSpecificationBuilder;
import com.retailedge.utils.ExceptionHandler.ExceptionHandlerUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WarrantyServiceDetailsService {

    @Autowired
    private WarrantyServiceRepository warrantyServiceRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ExceptionHandlerUtil exceptionHandlerUtil;

    public ResponseEntity<ResponseModel<?>> list(WarrantyServiceSpecificationBuilder builder){
        try{
            return ResponseEntity.ok(new ResponseModel<>(true, "Success", 200, warrantyServiceRepository.findAll(builder.build())));
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseModel<>(false, "Error retrieving warranty service details: " + exceptionHandlerUtil.sanitizeErrorMessage(e.getMessage()), 500));
        }
    }

    public ResponseEntity<ResponseModel<?>> add(WarrantyServiceDto warrantyServiceDto){
        try{
            Customer customer = customerRepository.findByPhoneNumber(warrantyServiceDto.getCustomer().getPhoneNumber());
            if(customer==null){
                if (warrantyServiceDto.getCustomer() != null) {
                    customer  = modelMapper.map(warrantyServiceDto.getCustomer(), Customer.class);
                }
            }
            assert customer != null;
            Customer savedCustomer = customerRepository.save(customer);
            customerRepository.save(savedCustomer);
            WarrantyService warrantyService = modelMapper.map(warrantyServiceDto, WarrantyService.class);
            warrantyService.setReceivedBy(userRepository.findById(warrantyServiceDto.getReceivedBy().getId()).get());
            warrantyService.setCustomer(customer);
            return ResponseEntity.ok(new ResponseModel<>(true, "Success", 200, warrantyServiceRepository.save(warrantyService)));
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseModel<>(false, "Error adding warranty service details: " + exceptionHandlerUtil.sanitizeErrorMessage(e.getMessage()), 500));
        }
    }

    public ResponseEntity<ResponseModel<?>> update(Integer warrantyServiceId, WarrantyServiceDto warrantyServiceDto) {

        try{
            // Retrieve the WarrantyService entity by ID
            Optional<WarrantyService> optionalWarrantyService = warrantyServiceRepository.findById(warrantyServiceId);
            if(optionalWarrantyService.isEmpty()){
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ResponseModel<>(false, "Warranty Service Details not found!", 500));
            }

            WarrantyService warrantyService = optionalWarrantyService.get();

            // Update the `receivedBy` field if provided
            if (warrantyServiceDto.getReceivedBy() != null && warrantyServiceDto.getReceivedBy().getId() != null) {
                Optional<User> receivedBy = userRepository.findById(warrantyServiceDto.getReceivedBy().getId());

                if(receivedBy.isEmpty()){
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(new ResponseModel<>(false, "User details not found!", 500));
                }
                warrantyService.setReceivedBy(receivedBy.get());

            }

            // Handle the customer relationship
            Customer customer = null;
            if (warrantyServiceDto.getCustomer() != null && warrantyServiceDto.getCustomer().getPhoneNumber() != null) {
                customer = customerRepository.findByPhoneNumber(warrantyServiceDto.getCustomer().getPhoneNumber());
                if (customer == null) {
                    // If the customer doesn't exist, create a new one
                    customer = new Customer();
                    customer.setName(warrantyServiceDto.getCustomer().getName());
                    customer.setEmail(warrantyServiceDto.getCustomer().getEmail());
                    customer.setPhoneNumber(warrantyServiceDto.getCustomer().getPhoneNumber());
                    customer.setAddress(warrantyServiceDto.getCustomer().getAddress());
                    customer.setGSTIN(warrantyServiceDto.getCustomer().getGSTIN());
                    customer.setDateOfBirth(warrantyServiceDto.getCustomer().getDateOfBirth());
                    customer.setPinCode(warrantyServiceDto.getCustomer().getPinCode());
                    customer.setState(warrantyServiceDto.getCustomer().getState());
                    customerRepository.save(customer);
                }
            }

            // Update the `customer` relationship if found or created
            if (customer != null) {
                customer.setName(warrantyServiceDto.getCustomer().getName());
                customer.setEmail(warrantyServiceDto.getCustomer().getEmail());
                customer.setPhoneNumber(warrantyServiceDto.getCustomer().getPhoneNumber());
                customer.setAddress(warrantyServiceDto.getCustomer().getAddress());
                customer.setGSTIN(warrantyServiceDto.getCustomer().getGSTIN());
                customer.setDateOfBirth(warrantyServiceDto.getCustomer().getDateOfBirth());
                customer.setPinCode(warrantyServiceDto.getCustomer().getPinCode());
                customer.setState(warrantyServiceDto.getCustomer().getState());
                customerRepository.save(customer);
            }
            warrantyService.setCustomer(customer);

            // Manually update fields in the WarrantyService entity
            if (warrantyServiceDto.getProductName() != null) {
                warrantyService.setProductName(warrantyServiceDto.getProductName());
            }

            if (warrantyServiceDto.getImeiNumber() != null) {
                warrantyService.setImeiNumber(warrantyServiceDto.getImeiNumber());
            }

            if (warrantyServiceDto.getServiceProvider() != null) {
                warrantyService.setServiceProvider(warrantyServiceDto.getServiceProvider());
            }

            if (warrantyServiceDto.getComplaintDescription() != null) {
                warrantyService.setComplaintDescription(warrantyServiceDto.getComplaintDescription());
            }

            if (warrantyServiceDto.getSparePartDescription() != null) {
                warrantyService.setSparePartDescription(warrantyServiceDto.getSparePartDescription());
            }

            if (warrantyServiceDto.getServiceDate() != null) {
                warrantyService.setServiceDate(warrantyServiceDto.getServiceDate());
            }

            return ResponseEntity.ok(new ResponseModel<>(true, "Success", 200, warrantyServiceRepository.save(warrantyService)));
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseModel<>(false, "Error updating warranty service details: " + exceptionHandlerUtil.sanitizeErrorMessage(e.getMessage()), 500));
        }
    }

    public ResponseEntity<ResponseModel<?>> delete(Integer warrantyServiceId) throws Exception {

        try {
            if (!warrantyServiceRepository.existsById(warrantyServiceId)) {
                // Return 404 Not Found
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseModel<>(false, "Warranty Service not found", 404));
            }
            warrantyServiceRepository.deleteById(warrantyServiceId);
            // Return 200 OK if the category is deleted successfully
            return ResponseEntity.ok(new ResponseModel<>(true, "Deleted successfully", 200));
        } catch (Exception e) {
            // Return 500 Internal Server Error for any unexpected errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseModel<>(false, "Error deleting category: " + e.getMessage(), 500));
        }
    }


}
