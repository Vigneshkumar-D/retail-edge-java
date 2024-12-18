package com.retailedge.controller.supplier;

import com.retailedge.dto.supplier.SupplierDto;
import com.retailedge.entity.suppiler.Supplier;
import com.retailedge.model.ResponseModel;
import com.retailedge.service.supplier.SupplierService;
import com.retailedge.specification.supplier.SupplierSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/supplier-management")
public class SupplierController {

    @Autowired
    private SupplierService supplierService;

    @GetMapping
    public ResponseEntity<ResponseModel<?>> list(@RequestParam(required = false, name = "supplierId") Integer supplierId){
        Specification<Supplier> spec = SupplierSpecification.hasSupplierId(supplierId);
        return supplierService.list(spec);
    }

    @PostMapping
    private ResponseEntity<ResponseModel<?>> add(@RequestBody SupplierDto supplierDto){
        return supplierService.add(supplierDto);
    }

    @PutMapping("/{supplierId}")
    public ResponseEntity<ResponseModel<?>> update(@PathVariable("supplierId") Integer supplierId, @RequestBody SupplierDto supplierDto){
        return supplierService.update(supplierId, supplierDto);
    }

    @DeleteMapping("/{supplierId}")
    public ResponseEntity<ResponseModel<?>> delete(@PathVariable("supplierId") Integer supplierId) throws Exception {
       return supplierService.delete(supplierId);
    }
}
