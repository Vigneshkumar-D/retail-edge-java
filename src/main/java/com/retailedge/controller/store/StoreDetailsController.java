package com.retailedge.controller.store;

import com.retailedge.dto.store.GSTDetailsDto;
import com.retailedge.dto.store.StoreDetailsDto;
import com.retailedge.entity.store.GSTDetails;
import com.retailedge.entity.store.StoreDetails;
import com.retailedge.model.ResponseModel;
import com.retailedge.service.store.GSTDetailsService;
import com.retailedge.service.store.StoreDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("store-details")
public class StoreDetailsController {
    @Autowired
    public StoreDetailsService storeDetailsService;

    @GetMapping
    public List<StoreDetails> list(){
        return storeDetailsService.list();
    }

    @PostMapping
    public StoreDetails add(@RequestBody StoreDetailsDto storeDetailsDto){
        return storeDetailsService.add(storeDetailsDto);
    }

    @PutMapping("/{storeDetailsId}")
    public StoreDetails update(@PathVariable("storeDetailsId") Integer storeDetailsId, @RequestBody StoreDetailsDto storeDetailsDto){
        return storeDetailsService.update(storeDetailsId, storeDetailsDto);
    }

    @DeleteMapping("/{storeDetailsId}")
    public ResponseEntity<ResponseModel<?>> delete(@PathVariable("storeDetailsId") Integer storeDetailsId) throws Exception {
      return storeDetailsService.delete(storeDetailsId);
    }
}
