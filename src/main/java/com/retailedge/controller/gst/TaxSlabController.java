package com.retailedge.controller.gst;

import com.retailedge.dto.gst.TaxSlabDto;
import com.retailedge.model.ResponseModel;
import com.retailedge.service.gst.TaxSlabService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tax-slab")
public class TaxSlabController {

    @Autowired
    private TaxSlabService taxSlabService;

    @PostMapping
    public ResponseEntity<ResponseModel<?>> add(@RequestBody TaxSlabDto taxSlabDto){
        return taxSlabService.add(taxSlabDto);
    }

    @GetMapping
    public ResponseEntity<ResponseModel<?>> list(){
        return taxSlabService.list();
    }

    @PutMapping("/{taxSlabId}")
    public ResponseEntity<ResponseModel<?>> update(@PathVariable("taxSlabId") Long taxSlabId, @RequestBody TaxSlabDto taxSlabDto){
        return taxSlabService.update(taxSlabId, taxSlabDto);
    }

    @DeleteMapping("/{taxSlabId}")
    public ResponseEntity<ResponseModel<?>> delete(@PathVariable("taxSlabId") Long taxSlabId) throws Exception {
       return taxSlabService.delete(taxSlabId);
    }

}
