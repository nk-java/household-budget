package com.example.household_budget.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.household_budget.model.MonthlyBudget;
import com.example.household_budget.repository.MonthlyBudgetRepository;

@ExtendWith(MockitoExtension.class)
class MonthlyBudgetServiceTest {

    @Mock
    private MonthlyBudgetRepository monthlyBudgetRepository;

    private MonthlyBudgetService monthlyBudgetService;

    @BeforeEach
    void setUp() {
        monthlyBudgetService =
                new MonthlyBudgetService(monthlyBudgetRepository);
    }

    @Test
    void registerOrUpdate_whenBudgetDoesNotExist_savesNewBudget() {

        // Arrange：対象月の予算が存在しない状態を作る
        when(monthlyBudgetRepository.findByBudgetMonth("2026-07"))
                .thenReturn(null);

        // Act：登録・更新処理を実行する
        monthlyBudgetService.registerOrUpdate("2026-07", 100000);

        // Assert：Repositoryへ渡されたデータを取得する
        ArgumentCaptor<MonthlyBudget> budgetCaptor =
                ArgumentCaptor.forClass(MonthlyBudget.class);

        verify(monthlyBudgetRepository)
                .save(budgetCaptor.capture());

        MonthlyBudget savedBudget = budgetCaptor.getValue();

        assertThat(savedBudget.getBudgetMonth())
                .isEqualTo("2026-07");

        assertThat(savedBudget.getAmount())
                .isEqualTo(100000);
    }

    @Test
    void registerOrUpdate_whenBudgetExists_updatesExistingBudget() {

        // Arrange：すでに対象月の予算が存在する状態を作る
        MonthlyBudget existingBudget =
                new MonthlyBudget("2026-07", 80000);

        when(monthlyBudgetRepository.findByBudgetMonth("2026-07"))
                .thenReturn(existingBudget);

        // Act：同じ月に新しい金額を登録する
        monthlyBudgetService.registerOrUpdate("2026-07", 100000);

        // Assert：既存データの金額が更新されたか確認する
        assertThat(existingBudget.getAmount())
                .isEqualTo(100000);

        verify(monthlyBudgetRepository)
                .save(existingBudget);
    }
}