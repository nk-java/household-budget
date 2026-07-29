package com.example.household_budget.controller;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.household_budget.repository.ExpenseRepository;
import com.example.household_budget.repository.MonthlyBudgetRepository;
import com.example.household_budget.service.MonthlyBudgetService;

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
}