package com.retailedge.controller.inventory;

import com.retailedge.dto.inventory.StockReportHistoryDto;
import com.retailedge.entity.inventory.StockReportHistory;
import com.retailedge.model.ResponseModel;
import com.retailedge.service.inventory.StockReportHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("stock-report-history")
public class StockReportHistoryController {

    @Autowired
    private StockReportHistoryService stockReportHistoryService;

    @GetMapping
    public List<StockReportHistory> list(){
        return stockReportHistoryService.list();
    }

    @PostMapping
    public StockReportHistory add(@RequestBody StockReportHistoryDto stockReportHistoryDto){
        return stockReportHistoryService.add(stockReportHistoryDto);
    }
    @PutMapping("{stockReportHistoryId}")
    public StockReportHistory update(@RequestBody StockReportHistoryDto stockReportHistoryDto, @PathVariable(name = "stockReportHistoryId") Integer stockReportHistoryId){
        return stockReportHistoryService.update(stockReportHistoryDto, stockReportHistoryId);
    }

    @DeleteMapping("{stockReportHistoryId}")
    public ResponseEntity<ResponseModel<?>> delete(@PathVariable(name = "stockReportHistoryId") Integer stockReportHistoryId) throws Exception {
        return stockReportHistoryService.delete(stockReportHistoryId);
    }
}
