package com.retailedge.controller.store;

import com.retailedge.dto.store.GSTDetailsDto;
import com.retailedge.entity.store.GSTDetails;
import com.retailedge.model.ResponseModel;
import com.retailedge.service.store.GSTDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("gst-details")
public class GSTDetailsController {
    @Autowired
    public GSTDetailsService gstDetailsService;

    @GetMapping
    public List<GSTDetails> list(){
        return gstDetailsService.list();
    }

    @PostMapping
    public GSTDetails add(@RequestBody GSTDetailsDto gstDetailsDto){
        return gstDetailsService.add(gstDetailsDto);
    }

    @PutMapping("/{gstDetailsId}")
    public GSTDetails update(@PathVariable("gstDetailsId") Integer gstDetailsId, @RequestBody GSTDetailsDto gstDetailsDto){
        return gstDetailsService.update(gstDetailsId, gstDetailsDto);
    }

    @DeleteMapping("/{gstDetailsId}")
    public ResponseEntity<ResponseModel<?>> delete(@PathVariable("gstDetailsId") Integer accountDetailsId) throws Exception {
       return gstDetailsService.delete(accountDetailsId);
    }
}
