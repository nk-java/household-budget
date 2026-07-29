package com.example.household_budget.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String category;
    private String expenseName;
    private int amount;
    private LocalDate expenseDate;

    public Expense() {
    }

    public Expense(String expenseName, int amount, String category) {
        this.expenseName = expenseName;
        this.amount = amount;
        this.category = category;
    }

    public String getExpenseName() {
        return expenseName;
    }

    public int getAmount() {
        return amount;
    }

    public void setExpenseName(String expenseName) {
        this.expenseName = expenseName;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public LocalDate getExpenseDate() {
        return expenseDate;
    }

    public void setExpenseDate(LocalDate expenseDate) {
        this.expenseDate = expenseDate;
    }

    public Long getId() {
        return id;
    }
}