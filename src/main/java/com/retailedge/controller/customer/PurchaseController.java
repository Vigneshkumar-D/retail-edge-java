package com.retailedge.controller.customer;

import com.retailedge.dto.customer.PurchaseDto;
import com.retailedge.entity.customer.Purchase;
import com.retailedge.model.ResponseModel;
import com.retailedge.service.customer.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/purchase")
public class PurchaseController {

    @Autowired
    private PurchaseService purchaseService;

    @GetMapping
    public List<Purchase> list(){
        return purchaseService.list();
    }

    @GetMapping("/{customerId}")
    public List<Purchase> findByCustomer(@PathVariable("customerId") Integer customerId){
        return purchaseService.findByCustomer(customerId);
    }

    @PostMapping
    public Purchase add(@RequestBody PurchaseDto purchaseDto){
        return purchaseService.add(purchaseDto);
    }

    @PutMapping("/{purchaseId}")
    public Purchase update(@PathVariable("purchaseId") Integer purchaseId, @RequestBody PurchaseDto purchaseDto){
        return purchaseService.update(purchaseId, purchaseDto);
    }

    @DeleteMapping("/{purchaseId}")
    public ResponseEntity<ResponseModel<?>> delete(@PathVariable("purchaseId") Integer purchaseId) throws Exception {
       return purchaseService.delete(purchaseId);
    }
}
