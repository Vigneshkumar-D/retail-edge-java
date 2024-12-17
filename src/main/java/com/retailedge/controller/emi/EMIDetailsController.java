package com.retailedge.controller.emi;

import com.retailedge.dto.emi.EMIDetailsDto;
import com.retailedge.entity.emi.EMIDetails;
import com.retailedge.model.ResponseModel;
import com.retailedge.service.emi.EMIDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/emi-details")
public class EMIDetailsController {

    @Autowired
    private EMIDetailsService emiDetailsService;

    @GetMapping
    public ResponseEntity<ResponseModel<?>> list(){
        return emiDetailsService.list();
    }

    @PostMapping
    public ResponseEntity<ResponseModel<?>> add(@RequestBody EMIDetailsDto emiDetailsDto){
        return emiDetailsService.add(emiDetailsDto);
    }

    @PutMapping("/{emiDetailsId}")
    public ResponseEntity<ResponseModel<?>> update(@PathVariable("emiDetailsId") Integer emiDetailsId, @RequestBody EMIDetailsDto emiDetailsDto){
        return emiDetailsService.update(emiDetailsId, emiDetailsDto);
    }

    @DeleteMapping("/{emiDetailsId}")
    public ResponseEntity<ResponseModel<?>> delete(@PathVariable("emiDetailsId") Integer emiDetailsId) throws Exception {
       return emiDetailsService.delete(emiDetailsId);
    }
}
