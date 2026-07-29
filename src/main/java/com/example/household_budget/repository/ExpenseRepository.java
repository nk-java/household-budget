package com.example.household_budget.repository;

import com.example.household_budget.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    List<Expense> findByCategory(String category);

    List<Expense> findAllByOrderByExpenseDateDescIdDesc();

    List<Expense> findByCategoryOrderByExpenseDateDescIdDesc(String category);
}