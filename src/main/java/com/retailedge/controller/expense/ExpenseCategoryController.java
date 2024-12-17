package com.retailedge.controller.expense;


import com.retailedge.dto.expense.ExpenseCategoryDto;
import com.retailedge.dto.expense.ExpenseDto;
import com.retailedge.entity.expense.Expense;
import com.retailedge.entity.expense.ExpenseCategory;
import com.retailedge.model.ResponseModel;
import com.retailedge.service.expense.ExpenseCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("expense-category")
public class ExpenseCategoryController {

    @Autowired
    private ExpenseCategoryService expenseCategoryService;

    @GetMapping
    public ResponseEntity<ResponseModel<?>> list(){
        return expenseCategoryService.list();
    }

    @PostMapping
    public ResponseEntity<ResponseModel<?>> add(@RequestBody ExpenseCategoryDto expenseCategoryDto){
        return expenseCategoryService.add(expenseCategoryDto);
    }

    @PutMapping("{expenseId}")
    public ResponseEntity<ResponseModel<?>> update(@RequestBody ExpenseCategoryDto expenseCategoryDto, @PathVariable("expenseId") Long expenseCategoryId){
        return expenseCategoryService.update(expenseCategoryId, expenseCategoryDto);
    }

    @DeleteMapping("{expenseId}")
    public ResponseEntity<ResponseModel<Object>> delete(@PathVariable("expenseId") Long expenseCategoryId){
        return expenseCategoryService.delete(expenseCategoryId);
    }

}
