package com.retailedge.controller.service;

import com.retailedge.dto.service.WarrantyServiceDto;
import com.retailedge.entity.service.PaidService;
import com.retailedge.entity.service.WarrantyService;
import com.retailedge.model.ResponseModel;
import com.retailedge.service.service.WarrantyServiceDetailsService;
import com.retailedge.specification.service.PaidServiceSpecificationBuilder;
import com.retailedge.specification.service.WarrantyServiceSpecificationBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/warranty-service")
public class WarrantyServiceController {

    @Autowired
    private WarrantyServiceDetailsService warrantyService;


    @GetMapping
    public List<WarrantyService> list(
            @RequestParam(required = false, name = "id")Integer id,
            @RequestParam(required = false, name = "customerName")String customerName,
            @RequestParam(required = false, name = "phoneNumber") String phoneNumber,
            @RequestParam(required = false, name = "serviceProvider") String serviceProvider,
            @RequestParam(required = false, name = "productName")String productName,
            @RequestParam(required = false, name = "imeiNumber") Long imeiNumber,
            @RequestParam(required = false, name = "serviceDate") Instant serviceDate,
            @RequestParam(required = false, name = "receivedBy") String receivedBy){

        WarrantyServiceSpecificationBuilder builder=new WarrantyServiceSpecificationBuilder();
        if(id!=null)builder.with("id",":",id);
        if(customerName!=null)builder.with("customer.name",":",customerName);
        if(phoneNumber!=null)builder.with("customer.phoneNumber",":",phoneNumber);
        if(productName!=null)builder.with("productName",":",productName);
        if(serviceProvider!=null)builder.with("serviceProvider",":",serviceProvider);
        if(imeiNumber!=null)builder.with("imeiNumber",":",imeiNumber);
        if(serviceDate!=null)builder.with("serviceDate",":",serviceDate);
        if(receivedBy!=null)builder.with("receivedBy",":",receivedBy);

        return warrantyService.list(builder);
    }

    @PostMapping
    public WarrantyService add(@RequestBody WarrantyServiceDto warrantyServiceId){
        return warrantyService.add(warrantyServiceId);
    }

    @PutMapping("/{warrantyServiceId}")
    public WarrantyService update(@PathVariable("warrantyServiceId") Integer warrantyServiceId, @RequestBody WarrantyServiceDto warrantyServiceDto){
        return warrantyService.update(warrantyServiceId, warrantyServiceDto);
    }

    @DeleteMapping("/{warrantyServiceId}")
    public ResponseEntity<ResponseModel<?>> delete(@PathVariable("warrantyServiceId") Integer warrantyServiceId) throws Exception {
       return warrantyService.delete(warrantyServiceId);
    }
}
