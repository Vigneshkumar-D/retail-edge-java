package com.retailedge.controller.store;

import com.retailedge.dto.store.StoreDetailsDto;
import com.retailedge.entity.inventory.Product;
import com.retailedge.entity.store.StoreDetails;
import com.retailedge.model.ResponseModel;
import com.retailedge.service.store.StoreDetailsService;
import com.retailedge.utils.ExceptionHandler.ExceptionHandlerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("store-details")
public class StoreDetailsController {
    @Autowired
    public StoreDetailsService storeDetailsService;



    @GetMapping
    public ResponseEntity<ResponseModel<?>> list(){
        return storeDetailsService.list();

    }

    @PutMapping(value = "/{storeDetailsId}",consumes = {"multipart/form-data"})
    public ResponseEntity<ResponseModel<?>> update(@PathVariable("storeDetailsId") Integer storeDetailsId, @ModelAttribute StoreDetailsDto storeDetailsDto) throws IOException {
        return storeDetailsService.update(storeDetailsId , storeDetailsDto);
    }

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<ResponseModel<?>> add(@ModelAttribute StoreDetailsDto storeDetailsDto) throws IOException {
        return storeDetailsService.add(storeDetailsDto);
    }

    @DeleteMapping("/{storeDetailsId}")
    public ResponseEntity<ResponseModel<?>> delete(@PathVariable("storeDetailsId") Integer storeDetailsId) throws Exception {
      return storeDetailsService.delete(storeDetailsId);
    }
}
