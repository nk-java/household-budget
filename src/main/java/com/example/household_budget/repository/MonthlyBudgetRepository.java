package com.example.household_budget.repository;

import com.example.household_budget.model.MonthlyBudget;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MonthlyBudgetRepository extends JpaRepository<MonthlyBudget, Long>{

    MonthlyBudget findByBudgetMonth(String budgetMonth);

}