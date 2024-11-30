package com.retailedge.service.expense;

import com.retailedge.dto.expense.ExpenseCategoryDto;
import com.retailedge.dto.expense.ExpenseDto;
import com.retailedge.entity.expense.ExpenseCategory;
import com.retailedge.model.ResponseModel;
import com.retailedge.repository.expense.ExpenseCategoryRepository;
import com.retailedge.repository.expense.ExpenseRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExpenseCategoryService {

    @Autowired
    private ExpenseCategoryRepository expenseCategoryRepository;

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private ModelMapper modelMapper;

    public List<ExpenseCategory> list(){
        return expenseCategoryRepository.findAll();
    }

    public ExpenseCategory add(ExpenseCategoryDto expenseCategoryeDto) {
        ExpenseCategory expenseCategory = modelMapper.map(expenseCategoryeDto, ExpenseCategory.class);
        return expenseCategoryRepository.save(expenseCategory);
    }

    public ExpenseCategory update(Long expenseCategoryId, ExpenseCategoryDto expenseCategoryDto) {
        Optional<ExpenseCategory> optionalExpenseCategory = expenseCategoryRepository.findById(expenseCategoryId);

        if (optionalExpenseCategory.isPresent()) {
            ExpenseCategory expenseCategory = optionalExpenseCategory.get();
            modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
            modelMapper.getConfiguration().setPropertyCondition(conditions -> {
                return conditions.getSource() != null;
            });
            modelMapper.map(expenseCategoryDto, expenseCategory);
            return expenseCategoryRepository.save(expenseCategory);

        } else {
            throw new RuntimeException("Expense not found!");
        }

    }

    public ResponseEntity<ResponseModel<Object>> delete(Long expenseCategoryId) {
        try {
            if (!expenseCategoryRepository.existsById(expenseCategoryId)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseModel<>(false, "Expense Category not found", 404));
            }
            if (expenseRepository.existsByCategory_CategoryId(expenseCategoryId)) {
                throw new IllegalStateException("Cannot delete category as it is associated with one or more expenses.");
            }
            expenseCategoryRepository.deleteById(expenseCategoryId);
            return ResponseEntity.ok(new ResponseModel<>(true, "Deleted successfully", 200));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseModel<>(false, "Error deleting Expense Category: " + e.getMessage(), 500));
        }
    }
}
