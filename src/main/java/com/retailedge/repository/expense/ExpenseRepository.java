package com.retailedge.repository.expense;

import com.retailedge.entity.expense.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long>, JpaSpecificationExecutor<Expense> {
    boolean existsByCategory_Id(Long categoryId);

    @Query("SELECT s FROM Expense s " +
            "WHERE CAST(s.date AS date) = CAST(:date AS date)")
    List<Expense> findExpenseByExactDate(@Param("date")LocalDate now);
}

