package com.retailedge.service.expense;

import com.retailedge.dto.expense.ExpenseDto;
import com.retailedge.entity.expense.Expense;
import com.retailedge.model.ResponseModel;
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
public class ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private ModelMapper modelMapper;

    public List<Expense> list(){
        return expenseRepository.findAll();
    }

    public Expense add(ExpenseDto expenseDto) {
        Expense expense = modelMapper.map(expenseDto, Expense.class);
        return expenseRepository.save(expense);
    }

    public Expense update(Long expenseId, ExpenseDto expenseDto) {
        Optional<Expense> optionalExpense = expenseRepository.findById(expenseId);

        if (optionalExpense.isPresent()) {
            Expense expense = optionalExpense.get();
            modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
            modelMapper.getConfiguration().setPropertyCondition(conditions -> {
                return conditions.getSource() != null;
            });
            modelMapper.map(expenseDto, expense);
            return expenseRepository.save(expense);

        } else {
            throw new RuntimeException("Expense not found!");
        }

    }

    public ResponseEntity<ResponseModel<Object>> delete(Long expenseId) {
        try {
            if (!expenseRepository.existsById(expenseId)) {
                // Return 404 Not Found
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseModel<>(false, "Expense not found", 404));
            }
            expenseRepository.deleteById(expenseId);
            // Return 200 OK if the category is deleted successfully
            return ResponseEntity.ok(new ResponseModel<>(true, "Deleted successfully", 200));
        } catch (Exception e) {
            // Return 500 Internal Server Error for any unexpected errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseModel<>(false, "Error deleting Expense: " + e.getMessage(), 500));
        }
    }
}
