package com.example.household_budget.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class MonthlyBudget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String budgetMonth;
    private int amount;

    public MonthlyBudget(){
    }

    public MonthlyBudget(String budgetMonth, int amount) {
        this.budgetMonth = budgetMonth;
        this.amount = amount;
    }

    public String getBudgetMonth() {
        return budgetMonth;
    }

    public int getAmount() {
        return amount;
    }

    public void setBudgetMonth(String budgetMonth) {
        this.budgetMonth = budgetMonth;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
    
    public Long getId() {
        return id;
    }

}

