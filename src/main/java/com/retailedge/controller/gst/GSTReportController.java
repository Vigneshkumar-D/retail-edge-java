package com.retailedge.controller.gst;


import com.retailedge.entity.gst.GSTReport;
import com.retailedge.service.gst.GSTReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/gst-report")
public class GSTReportController {

    @Autowired
    private GSTReportService gstReportService;

    @GetMapping
    public GSTReport generateReport(@RequestParam("startDate") Instant startDate, @RequestParam("endDate") Instant endDate){
        return gstReportService.generateReport(startDate, endDate);
    }
}
