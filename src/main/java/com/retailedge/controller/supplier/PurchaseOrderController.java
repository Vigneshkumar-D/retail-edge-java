package com.retailedge.controller.supplier;

import com.retailedge.dto.supplier.PurchaseOrderDto;
import com.retailedge.entity.suppiler.PurchaseOrder;
import com.retailedge.model.ResponseModel;
import com.retailedge.service.supplier.PurchaseOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/purchase-order")
public class PurchaseOrderController {
    @Autowired
    private PurchaseOrderService purchaseOrderService;

    @GetMapping
    public List<PurchaseOrder> list(){
        return purchaseOrderService.list();
    }

    @PostMapping
    private PurchaseOrder add(@RequestBody PurchaseOrderDto purchaseOrderDto){
        return purchaseOrderService.add(purchaseOrderDto);
    }

    @PutMapping("/{purchaseOrderId}")
    public PurchaseOrder update(@PathVariable("purchaseOrderId") Integer purchaseOrderId, PurchaseOrderDto purchaseOrderDto){
        return purchaseOrderService.update(purchaseOrderId, purchaseOrderDto);
    }

    @DeleteMapping("/{purchaseOrderId}")
    public ResponseEntity<ResponseModel<?>> delete(@PathVariable("purchaseOrderId") Integer purchaseOrderId) throws Exception {
       return purchaseOrderService.delete(purchaseOrderId);
    }

    @GetMapping("/last-three/{supplierId}")
    public List<PurchaseOrder> getLastThreeOrdersBySupplier(@PathVariable("supplierId") Integer supplierId) {
        return purchaseOrderService.getLastThreeOrdersBySupplierId(supplierId);
    }
}
