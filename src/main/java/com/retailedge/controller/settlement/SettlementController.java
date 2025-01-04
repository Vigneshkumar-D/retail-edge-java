package com.retailedge.controller.settlement;

import com.retailedge.dto.settelement.SettlementDto;
import com.retailedge.dto.store.AccountDetailsDto;
import com.retailedge.model.ResponseModel;
import com.retailedge.service.settlement.SettlementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;

@RestController
@RequestMapping("/settlement")
public class SettlementController {

    @Autowired
    private SettlementService settlementService;

    @GetMapping
    public ResponseEntity<ResponseModel<?>> list(){
        return settlementService.list();
    }

    @GetMapping("/calculate-settlement")
    public ResponseEntity<ResponseModel<?>> calculateSettlement(@RequestParam("date") LocalDate date){
        return settlementService.calculateSettlement(date);
    }

    @PostMapping
    public ResponseEntity<ResponseModel<?>> add(@RequestBody SettlementDto settlementDto) throws IOException {
        return settlementService.add(settlementDto);
    }

    @PutMapping(value = "/{accountDetailsId}")
    public ResponseEntity<ResponseModel<?>> update(@PathVariable("settlementId") Integer settlementId, @RequestBody SettlementDto settlementDto) throws IOException {
        return settlementService.update(settlementId, settlementDto);
    }

    @DeleteMapping("/{accountDetailsId}")
    public ResponseEntity<ResponseModel<?>> delete(@PathVariable("settlementId") Integer settlementId) throws Exception {
        return settlementService.delete(settlementId);
    }

}
