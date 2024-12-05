package com.retailedge.service.customer;

import com.retailedge.dto.customer.CustomerDto;
import com.retailedge.entity.customer.Customer;
import com.retailedge.model.ResponseModel;
import com.retailedge.repository.customer.CustomerRepository;
import org.apache.poi.ss.formula.functions.T;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ModelMapper modelMapper;

    public List<Customer> list(){
        return customerRepository.findAll();
    }

    public Customer add(CustomerDto customerDto){
        Customer exstingCustomer = customerRepository.findByPhoneNumber(customerDto.getPhoneNumber());
        if(exstingCustomer==null){
            Customer customer =  modelMapper.map(customerDto, Customer.class);
            return customerRepository.save(customer);
        }
        return exstingCustomer;
    }

    public Customer update(Integer customerId, CustomerDto customerDto){
        Optional<Customer> customerOptional = customerRepository.findById(customerId);
        if (!customerOptional.isPresent()) {
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

    public ResponseEntity<ResponseModel<?>> delete(Integer customerId) {
        try {
            // Check if the customer exists
            if (!customerRepository.existsById(customerId)) {
                // Return 404 Not Found
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseModel<>(false, "Customer not found", 404));
            }

            // Delete the customer
            customerRepository.deleteById(customerId);
            // Return 200 OK
            return ResponseEntity.ok(new ResponseModel<>(true, "Deleted successfully", 200));
        } catch (Exception e) {
            // Return 500 Internal Server Error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseModel<>(false, "Error deleting Customer: " + e.getMessage(), 500));
        }
    }

}
