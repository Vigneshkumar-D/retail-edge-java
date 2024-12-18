package com.retailedge.controller.supplier;


import com.retailedge.dto.supplier.PaymentDetailsDto;
import com.retailedge.entity.suppiler.PaymentDetails;
import com.retailedge.model.ResponseModel;
import com.retailedge.service.supplier.PaymentDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payments")
public class PaymentDetailsController {

    @Autowired
    private PaymentDetailsService paymentDetailsService;

    @GetMapping
    public ResponseEntity<ResponseModel<?>> list(){
        return paymentDetailsService.list();
    }

    @PostMapping
    private ResponseEntity<ResponseModel<?>> add(@RequestBody PaymentDetailsDto paymentDetailsDto){
        return paymentDetailsService.add(paymentDetailsDto);
    }

    @PutMapping("/{paymentDetailsId}")
    public ResponseEntity<ResponseModel<?>> update(@PathVariable("paymentDetailsId") Integer paymentDetailsId, @RequestBody PaymentDetailsDto paymentDetailsDto){
        return paymentDetailsService.update(paymentDetailsId, paymentDetailsDto);
    }

    @DeleteMapping("/{paymentDetailsId}")
    public ResponseEntity<ResponseModel<?>> delete(@PathVariable("paymentDetailsId") Integer paymentDetailsId) throws Exception {
       return paymentDetailsService.delete(paymentDetailsId);
    }

    @GetMapping("/last-three/{supplierId}")
    public ResponseEntity<ResponseModel<?>> getLastThreePaymentsBySupplier(@PathVariable("supplierId") Integer supplierId) {
        return paymentDetailsService.getLastThreePaymentsBySupplierId(supplierId);
    }
}
