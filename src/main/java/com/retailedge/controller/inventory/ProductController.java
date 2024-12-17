package com.retailedge.controller.inventory;


import com.retailedge.dto.inventory.ProductDto;
import com.retailedge.entity.inventory.Product;
import com.retailedge.model.ResponseModel;
import com.retailedge.service.inventory.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public ResponseEntity<ResponseModel<?>> list(){
        return productService.list();
    }

    @PostMapping
    public ResponseEntity<ResponseModel<?>> add(@RequestBody ProductDto productDto) throws Exception {
        return productService.add(productDto);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<ResponseModel<?>> update(@PathVariable(name = "productId") Integer productId, @RequestBody ProductDto productDto){
        return productService.update(productId, productDto);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<ResponseModel<?>> delete(@PathVariable(name = "productId") Integer productId) throws Exception {
       return this.productService.delete(productId);
    }

    @PostMapping("/bulk-upload")
    public ResponseEntity<ResponseModel<?>> bulkUpload(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseModel<>(false, "File is empty!", 400));
        }
        return productService.bulkUpload(file);
    }
}
