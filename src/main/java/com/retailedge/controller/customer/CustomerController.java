package com.retailedge.controller.customer;

import com.retailedge.dto.customer.CustomerDto;
import com.retailedge.entity.customer.Customer;
import com.retailedge.model.ResponseModel;
import com.retailedge.service.customer.CustomerService;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping
    public ResponseEntity<ResponseModel<?>> list(){
        return customerService.list();
    }

    @PostMapping
    public ResponseEntity<ResponseModel<?>> add(@RequestBody CustomerDto customerDto){
        return customerService.add(customerDto);
    }

    @PutMapping("/{customerId}")
    public ResponseEntity<ResponseModel<?>> update(@PathVariable("customerId") Integer customerId, @RequestBody CustomerDto customerDto){
        return customerService.update(customerId, customerDto);
    }

    @DeleteMapping("/{customerId}")
    public ResponseEntity<ResponseModel<?>> delete(@PathVariable("customerId") Integer customerId) throws Exception {
      return   customerService.delete(customerId);
    }
}

