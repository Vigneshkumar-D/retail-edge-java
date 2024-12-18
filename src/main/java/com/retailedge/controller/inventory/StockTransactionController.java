package com.retailedge.controller.inventory;

import com.retailedge.dto.inventory.StockTransactionDto;
import com.retailedge.entity.inventory.StockTransaction;
import com.retailedge.model.ResponseModel;
import com.retailedge.service.inventory.StockTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/stock-transaction")
public class StockTransactionController {

    @Autowired
    private StockTransactionService stockTransactionService;

    @GetMapping
    public ResponseEntity<ResponseModel<?>> list(){
        return stockTransactionService.list();
    }

    @PostMapping
    public ResponseEntity<ResponseModel<?>> add(@RequestBody StockTransactionDto stockTransactionDto){
        return stockTransactionService.add(stockTransactionDto);
    }

    @PutMapping("/{stockTransactionId}")
    public ResponseEntity<ResponseModel<?>> update(@PathVariable(name = "stockTransactionId") Integer stockTransactionId, @RequestBody StockTransactionDto stockTransactionDto){
        return stockTransactionService.update(stockTransactionId, stockTransactionDto);
    }

    @DeleteMapping("/{stockTransactionId}")
    public ResponseEntity<ResponseModel<?>> delete(@PathVariable(name = "stockTransactionId") Integer stockTransactionId) throws Exception {
        return this.stockTransactionService.delete(stockTransactionId);
    }

}
