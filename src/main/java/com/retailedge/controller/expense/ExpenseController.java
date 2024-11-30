package com.retailedge.controller.expense;

import com.retailedge.dto.expense.ExpenseDto;
import com.retailedge.entity.expense.Expense;
import com.retailedge.entity.expense.ExpenseCategory;
import com.retailedge.model.ResponseModel;
import com.retailedge.service.expense.ExpenseService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("expense")
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    @GetMapping
    public List<Expense> list(){
        return expenseService.list();
    }

    @PostMapping
    public Expense add(@RequestBody ExpenseDto expenseDto){
        return expenseService.add(expenseDto);
    }

    @PutMapping("{expenseId}")
    public Expense update(@RequestBody ExpenseDto expenseDto, @PathVariable("expenseId") Long expenseId){
        return expenseService.update(expenseId, expenseDto);
    }

    @DeleteMapping("{expenseId}")
    public ResponseEntity<ResponseModel<Object>> delete(@PathVariable("expenseId") Long expenseId){
        return expenseService.delete(expenseId);
    }
}
