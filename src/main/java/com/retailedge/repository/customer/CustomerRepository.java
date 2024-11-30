package com.retailedge.repository.customer;

import com.retailedge.entity.customer.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CustomerRepository extends JpaRepository<Customer,Integer>, JpaSpecificationExecutor<Customer> {
    Customer findByEmail(String email);

    Customer phoneNumber(String phoneNumber);

    Customer findByPhoneNumber(String phoneNumber);

    boolean existsById(Integer id);
}
