package com.retailedge.service.expense;

import com.retailedge.dto.expense.ExpenseCategoryDto;
import com.retailedge.dto.expense.ExpenseDto;
import com.retailedge.entity.expense.ExpenseCategory;
import com.retailedge.model.ResponseModel;
import com.retailedge.repository.expense.ExpenseCategoryRepository;
import com.retailedge.repository.expense.ExpenseRepository;
import com.retailedge.utils.ExceptionHandler.ExceptionHandlerUtil;
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

    @Autowired
    private ExceptionHandlerUtil exceptionHandlerUtil;

    public ResponseEntity<ResponseModel<?>> list(){
        try{
            return ResponseEntity.ok(new ResponseModel<>(true, "Success", 200, expenseCategoryRepository.findAll()));
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseModel<>(false, "Error retrieving expense category details: " + exceptionHandlerUtil.sanitizeErrorMessage(e.getMessage()), 500));
        }
    }

    public ResponseEntity<ResponseModel<?>> add(ExpenseCategoryDto expenseCategoryDto) {
        try{
            ExpenseCategory expenseCategory = modelMapper.map(expenseCategoryDto, ExpenseCategory.class);
            return ResponseEntity.ok(new ResponseModel<>(true, "Success", 200, expenseCategoryRepository.save(expenseCategory)));
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseModel<>(false, "Error adding expense category details: " + exceptionHandlerUtil.sanitizeErrorMessage(e.getMessage()), 500));
        }
    }

    public ResponseEntity<ResponseModel<?>> update(Long expenseCategoryId, ExpenseCategoryDto expenseCategoryDto) {
        try{
            Optional<ExpenseCategory> optionalExpenseCategory = expenseCategoryRepository.findById(expenseCategoryId);
            if (optionalExpenseCategory.isPresent()) {
                ExpenseCategory expenseCategory = optionalExpenseCategory.get();
                modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
                modelMapper.getConfiguration().setPropertyCondition(conditions -> {
                    return conditions.getSource() != null;
                });
                modelMapper.map(expenseCategoryDto, expenseCategory);
                return ResponseEntity.ok(new ResponseModel<>(true, "Success", 200, expenseCategoryRepository.save(expenseCategory)));
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ResponseModel<>(false, "Expense category not found! ", 500));
            }
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseModel<>(false, "Error updating expense category details: " + exceptionHandlerUtil.sanitizeErrorMessage(e.getMessage()), 500));
        }


    }

    public ResponseEntity<ResponseModel<Object>> delete(Long expenseCategoryId) {
        try {
            if (!expenseCategoryRepository.existsById(expenseCategoryId)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseModel<>(false, "Expense Category not found", 404));
            }
            if (expenseRepository.existsByCategory_Id(expenseCategoryId)) {
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
