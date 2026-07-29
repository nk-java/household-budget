package com.example.household_budget.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.household_budget.model.MonthlyBudget;
import com.example.household_budget.repository.MonthlyBudgetRepository;

@Service
@Transactional
public class MonthlyBudgetService {

    private final MonthlyBudgetRepository monthlyBudgetRepository;

    public MonthlyBudgetService(
            MonthlyBudgetRepository monthlyBudgetRepository) {

        this.monthlyBudgetRepository = monthlyBudgetRepository;
    }

    public void registerOrUpdate(String budgetMonth, int amount) {

        MonthlyBudget monthlyBudget =
                monthlyBudgetRepository.findByBudgetMonth(budgetMonth);

        if (monthlyBudget == null) {
            monthlyBudget = new MonthlyBudget(budgetMonth, amount);
        } else {
            monthlyBudget.setAmount(amount);
        }

        monthlyBudgetRepository.save(monthlyBudget);
    }
}