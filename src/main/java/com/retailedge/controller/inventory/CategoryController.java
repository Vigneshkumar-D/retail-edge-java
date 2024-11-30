package com.retailedge.controller.inventory;


import com.retailedge.entity.inventory.Category;
import com.retailedge.dto.inventory.*;
import com.retailedge.model.ResponseModel;
import com.retailedge.service.inventory.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public Category add(@RequestBody CategoryDto categoryDto){
        System.out.println("categoty "+ categoryDto.getCategory());
         return  categoryService.add(categoryDto);
    }

    @GetMapping
    public List<Category> list(){
        return categoryService.list();
    }

    @PutMapping("/{categoryId}")
    public Category update(@PathVariable(name = "categoryId") Long categoryId, @RequestBody CategoryDto categoryDto){
        return categoryService.update(categoryId, categoryDto);
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<ResponseModel<?>> delete(@PathVariable(name = "categoryId") Long categoryId) throws Exception {
      return categoryService.delete(categoryId);
    }
}
