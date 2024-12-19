package com.retailedge.controller.service;

import com.retailedge.dto.service.PaidServiceDto;
import com.retailedge.entity.service.PaidService;
import com.retailedge.model.ResponseModel;
import com.retailedge.service.service.PaidServiceDetailsService;
import com.retailedge.specification.service.PaidServiceSpecificationBuilder;
import jakarta.websocket.server.PathParam;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/paid-service")
public class PaidServiceController {

    @Autowired
    private PaidServiceDetailsService paidService;

    @GetMapping
    public ResponseEntity<ResponseModel<?>> list(
            @RequestParam(required = false, name = "id")Integer id,
            @RequestParam(required = false, name = "customerName")String customerName,
            @RequestParam(required = false, name = "phoneNumber") String phoneNumber,
            @RequestParam(required = false, name = "productName")String productName,
            @RequestParam(required = false, name = "imeiNumber") Long imeiNumber,
            @RequestParam(required = false, name = "serviceDate") Instant serviceDate,
            @RequestParam(required = false, name = "receivedBy") String receivedBy){

        PaidServiceSpecificationBuilder builder=new PaidServiceSpecificationBuilder();
        if(id!=null)builder.with("id",":",id);
        if(customerName!=null)builder.with("customer.name",":",customerName);
        if(phoneNumber!=null)builder.with("customer.phoneNumber",":",phoneNumber);
        if(productName!=null)builder.with("productName",":",productName);
        if(imeiNumber!=null)builder.with("imeiNumber",":",imeiNumber);
        if(serviceDate!=null)builder.with("serviceDate",":",serviceDate);
        if(receivedBy!=null)builder.with("receivedBy",":",receivedBy);

        return paidService.list(builder);
    }

    @PostMapping
    public ResponseEntity<ResponseModel<?>> add(@RequestBody PaidServiceDto paidServiceDto){
        return paidService.add(paidServiceDto);
    }

    @PutMapping("/{paidServiceId}")
    public ResponseEntity<ResponseModel<?>> update(@PathVariable("paidServiceId") Integer paidServiceId, @RequestBody PaidServiceDto paidServiceDto){
        return paidService.update(paidServiceId, paidServiceDto);
    }

    @DeleteMapping("/{paidServiceId}")
    public ResponseEntity<ResponseModel<?>> delete(@PathVariable("paidServiceId") Integer paidServiceId) throws Exception {
       return paidService.delete(paidServiceId);
    }
}
