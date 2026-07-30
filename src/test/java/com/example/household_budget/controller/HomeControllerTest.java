package com.example.household_budget.controller;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.household_budget.repository.ExpenseRepository;
import com.example.household_budget.repository.MonthlyBudgetRepository;
import com.example.household_budget.service.MonthlyBudgetService;
import com.example.household_budget.model.Expense;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;

import java.time.LocalDate;

import org.mockito.ArgumentCaptor;

@WebMvcTest(HomeController.class)
class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ExpenseRepository expenseRepository;

    @MockitoBean
    private MonthlyBudgetRepository monthlyBudgetRepository;

    @MockitoBean
    private MonthlyBudgetService monthlyBudgetService;

    @Test
    void budgetRegister_callsServiceAndRedirectsToHome()
            throws Exception {

        // Act・Assert：月予算登録のPOSTリクエストを送信する
        mockMvc.perform(
                post("/budget/register")
                        .param("budgetMonth", "2026-07")
                        .param("amount", "100000"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        // Assert：フォームの値でServiceが呼ばれたか確認する
        verify(monthlyBudgetService)
                .registerOrUpdate("2026-07", 100000);
    }

    @Test
    void register_whenExpenseNameIsBlank_returnsFormWithValidationError()
            throws Exception {

        mockMvc.perform(
                post("/register")
                        .param("expenseName", "")
                        .param("amount", "850")
                        .param("category", "食費")
                        .param("expenseDate", "2026-07-31"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attributeHasFieldErrors(
                        "expenseForm",
                        "expenseName"));

        verify(expenseRepository, never())
                .save(any(Expense.class));
    }

    @Test
    void register_whenInputIsValid_savesExpenseAndRedirectsToResult()
            throws Exception {

        mockMvc.perform(
                post("/register")
                        .param("expenseName", "昼食")
                        .param("amount", "850")
                        .param("category", "食費")
                        .param("expenseDate", "2026-07-31"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/result"))
                .andExpect(flash().attribute("expenseName", "昼食"))
                .andExpect(flash().attribute("amount", "850"));

        ArgumentCaptor<Expense> expenseCaptor =
                ArgumentCaptor.forClass(Expense.class);

        verify(expenseRepository)
            .save(expenseCaptor.capture());

        Expense savedExpense = expenseCaptor.getValue();

        assertThat(savedExpense.getExpenseName())
                .isEqualTo("昼食");

        assertThat(savedExpense.getAmount())
                .isEqualTo(850);

        assertThat(savedExpense.getCategory())
                .isEqualTo("食費");

        assertThat(savedExpense.getExpenseDate())
                .isEqualTo(LocalDate.of(2026, 7, 31));
    }
}