package com.retailedge.controller.inventory;


import com.retailedge.dto.inventory.LowStockAlertDto;
import com.retailedge.entity.inventory.LowStockAlert;
import com.retailedge.model.ResponseModel;
import com.retailedge.service.inventory.LowStockAlertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("stock-alert")
public class LowStockAlertController {

    @Autowired
    private LowStockAlertService lowStockAlertService;


    @GetMapping
    public List<LowStockAlert> lost(){
        return  lowStockAlertService.list();
    }

    @PostMapping
    public LowStockAlert add(LowStockAlertDto lowStockAlertDto){
        return lowStockAlertService.add(lowStockAlertDto);
    }

    @PutMapping("/{lowStockAlertId}")
    public  LowStockAlert update(@PathVariable("lowStockAlertId") Integer lowStockAlertId, @RequestBody LowStockAlertDto lowStockAlertDto){
        return  lowStockAlertService.update(lowStockAlertId, lowStockAlertDto);
    }

    @DeleteMapping("/{lowStockAlertId}")
    public ResponseEntity<ResponseModel<?>> delete(@PathVariable("lowStockAlertId") Integer lowStockAlertId) throws Exception {
       return lowStockAlertService.delete(lowStockAlertId);
    }

}
