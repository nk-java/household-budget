package com.example.household_budget.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import com.example.household_budget.model.Expense;
import java.util.List;

@DataJpaTest
class ExpenseRepositoryTest {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void saveAndFindById_returnsSavedExpense() {

        // Arrange：テストに必要なデータを準備する
        Expense expense = new Expense("昼食", 850, "食費");
        expense.setExpenseDate(LocalDate.of(2026, 7, 28));

        // Act：テスト対象の処理を実行する
        Expense savedExpense = expenseRepository.saveAndFlush(expense);
        Long savedId = savedExpense.getId();

        entityManager.clear();

        // Assert：実行結果が期待どおりか検証する
        assertThat(savedId).isNotNull();

        assertThat(expenseRepository.findById(savedId))
            .hasValueSatisfying(foundExpense -> {
                assertThat(foundExpense.getExpenseName()).isEqualTo("昼食");
                assertThat(foundExpense.getAmount()).isEqualTo(850);
                assertThat(foundExpense.getCategory()).isEqualTo("食費");
                assertThat(foundExpense.getExpenseDate())
                    .isEqualTo(LocalDate.of(2026, 7, 28));
            });
    }

    @Test
    void findByCategoryOrderByExpenseDateDescIdDesc_returnsMatchingExpensesInExpectedOrder() {

        // Arrange：テストデータを準備する
        Expense oldFood = new Expense("朝食", 500, "食費");
        oldFood.setExpenseDate(LocalDate.of(2026, 7, 27));

        Expense firstFoodOnSameDate = new Expense("昼食", 850, "食費");
        firstFoodOnSameDate.setExpenseDate(LocalDate.of(2026, 7, 28));

        Expense transportation = new Expense("電車", 300, "交通費");
        transportation.setExpenseDate(LocalDate.of(2026, 7, 29));

        Expense secondFoodOnSameDate = new Expense("夕食", 1200, "食費");
        secondFoodOnSameDate.setExpenseDate(LocalDate.of(2026, 7, 28));

        expenseRepository.saveAndFlush(oldFood);
        expenseRepository.saveAndFlush(firstFoodOnSameDate);
        expenseRepository.saveAndFlush(transportation);
        expenseRepository.saveAndFlush(secondFoodOnSameDate);

        entityManager.clear();

        // Act：食費だけを日付・IDの降順で取得する
        List<Expense> result = expenseRepository.findByCategoryOrderByExpenseDateDescIdDesc("食費");

        // Assert：交通費を除き、期待した順番になっているか検証する
        assertThat(result)
            .extracting(Expense::getExpenseName)
            .containsExactly("夕食", "昼食", "朝食");
    }
}