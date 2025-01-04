package com.retailedge.controller.settlement;

import com.retailedge.model.ResponseModel;
import com.retailedge.service.settlement.SettlementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/settlement")
public class SettlementController {

    @Autowired
    private SettlementService settlementService;

    @GetMapping
    public ResponseEntity<ResponseModel<?>> list(){
        return settlementService.list();
    }

}
