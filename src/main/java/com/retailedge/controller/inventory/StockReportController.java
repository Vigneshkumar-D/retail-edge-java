package com.retailedge.controller.inventory;

import com.retailedge.entity.inventory.StockReport;
import com.retailedge.service.inventory.StockReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("stock-report")
public class StockReportController {
    @Autowired
    private StockReportService stockReportService;

    @GetMapping
    public List<StockReport> list(){
        return stockReportService.list();
    }
}
