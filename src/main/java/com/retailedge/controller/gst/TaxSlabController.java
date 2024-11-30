package com.retailedge.controller.gst;



import com.retailedge.dto.gst.TaxSlabDto;
import com.retailedge.entity.gst.TaxSlab;
import com.retailedge.model.ResponseModel;
import com.retailedge.service.gst.TaxSlabService;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tax-slab")
public class TaxSlabController {

    @Autowired
    private TaxSlabService taxSlabService;

    @PostMapping
    public TaxSlab add(@RequestBody TaxSlabDto taxSlabDto){
        System.out.println("data trax "+taxSlabDto.getRegion());
        return taxSlabService.add(taxSlabDto);
    }

    @GetMapping
    public List<TaxSlab> list(){
        return taxSlabService.list();
    }

    @PutMapping("/{taxSlabId}")
    public TaxSlab update(@PathVariable("taxSlabId") Long taxSlabId, @RequestBody TaxSlabDto taxSlabDto){
        return taxSlabService.update(taxSlabId, taxSlabDto);
    }

    @DeleteMapping("/{taxSlabId}")
    public ResponseEntity<ResponseModel<?>> delete(@PathVariable("taxSlabId") Long taxSlabId) throws Exception {
       return taxSlabService.delete(taxSlabId);
    }

}
