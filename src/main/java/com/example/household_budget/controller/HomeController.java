package com.example.household_budget.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.example.household_budget.model.Expense;
import com.example.household_budget.repository.ExpenseRepository;
import java.util.List;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.ArrayList;
import java.time.YearMonth;
import java.util.Map;
import java.util.LinkedHashMap;
import com.example.household_budget.repository.MonthlyBudgetRepository;
import com.example.household_budget.model.MonthlyBudget;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.example.household_budget.service.MonthlyBudgetService;

@Controller
public class HomeController {
    private final ExpenseRepository expenseRepository;
    private final MonthlyBudgetRepository monthlyBudgetRepository;
    private final MonthlyBudgetService monthlyBudgetService;

    public HomeController(ExpenseRepository expenseRepository,MonthlyBudgetRepository monthlyBudgetRepository,MonthlyBudgetService monthlyBudgetService) {
        this.expenseRepository = expenseRepository;
        this.monthlyBudgetRepository = monthlyBudgetRepository;
        this.monthlyBudgetService = monthlyBudgetService;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("today", LocalDate.now());
        model.addAttribute("currentMonth", YearMonth.now());

        return "index";
    }

    @PostMapping("/register")
    public String register(
            @RequestParam String expenseName,
            @RequestParam(required = false) Integer amount,
            @RequestParam String category,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate expenseDate,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (expenseName == null || expenseName.trim().isEmpty()) {
            model.addAttribute("errorMessage", "支出名を入力してください");
            model.addAttribute("today", LocalDate.now());
            model.addAttribute("currentMonth", YearMonth.now());

            model.addAttribute("enteredCategory", category);
            model.addAttribute("enteredAmount", amount);
            model.addAttribute("enteredExpenseDate", expenseDate);
            return "index";
        }

        if (category == null || category.trim().isEmpty()) {
            model.addAttribute("errorMessage", "カテゴリを選択してください");
            model.addAttribute("today", LocalDate.now());
            model.addAttribute("currentMonth", YearMonth.now());

            model.addAttribute("enteredExpenseName", expenseName);
            model.addAttribute("enteredCategory", category);
            model.addAttribute("enteredAmount", amount);
            model.addAttribute("enteredExpenseDate", expenseDate);
            return "index";
        }

        if (expenseDate == null) {
            model.addAttribute("errorMessage", "支出日を入力してください");
            model.addAttribute("today", LocalDate.now());
            model.addAttribute("currentMonth", YearMonth.now());

            model.addAttribute("enteredExpenseName", expenseName);
            model.addAttribute("enteredCategory", category);
            model.addAttribute("enteredAmount", amount);
            model.addAttribute("enteredExpenseDate", expenseDate);
            return "index";
        }

        if (amount == null || amount <= 0) {
            model.addAttribute("errorMessage", "金額は1円以上で入力してください");
            model.addAttribute("today", LocalDate.now());
            model.addAttribute("currentMonth", YearMonth.now());

            model.addAttribute("enteredExpenseName", expenseName);
            model.addAttribute("enteredCategory", category);
            model.addAttribute("enteredAmount", amount);
            model.addAttribute("enteredExpenseDate", expenseDate);
            return "index";
        }

        Expense expense = new Expense(expenseName, amount, category);
        expense.setExpenseDate(expenseDate);
        expenseRepository.save(expense);

        String formattedAmount = String.format("%,d", amount);

        redirectAttributes.addFlashAttribute("expenseName", expenseName);
        redirectAttributes.addFlashAttribute("amount", formattedAmount);

        return "redirect:/result";
    }

    @GetMapping("/result")
    public String result(Model model) {
        model.addAttribute(
                "expenses",
                expenseRepository.findAllByOrderByExpenseDateDescIdDesc());
        model.addAttribute("total", calculateTotal());

        return "result";
    }

    @GetMapping("/detail")
    public String detail(
            @RequestParam Long id,
            Model model,
            RedirectAttributes redirectAttributes) {

        Expense expense = expenseRepository.findById(id).orElse(null);

        if (expense == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "指定された支出が見つかりません");
            return "redirect:/result";
        }

        model.addAttribute("expense", expense);

        return "detail";
    }

    @GetMapping("/edit")
    public String edit(
            @RequestParam Long id,
            Model model) {

        Expense expense = expenseRepository.findById(id).orElse(null);

        model.addAttribute("expense", expense);

        return "edit";
    }

    @PostMapping("/update")
    public String update(
            @RequestParam Long id,
            @RequestParam String expenseName,
            @RequestParam(required = false) Integer amount,
            @RequestParam(required = false) String category,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate expenseDate,
            Model model) {

        Expense expense = expenseRepository.findById(id).orElse(null);

        if (expenseName == null || expenseName.trim().isEmpty()) {
            model.addAttribute("errorMessage", "支出名を入力してください");
            model.addAttribute("expense", expense);
            model.addAttribute("enteredExpenseDate", expenseDate);
            model.addAttribute("enteredExpenseName", expenseName);
            model.addAttribute("enteredAmount", amount);
            model.addAttribute("enteredCategory", category);
            return "edit";
        }

        if (category == null || category.trim().isEmpty()) {
            model.addAttribute("errorMessage", "カテゴリを選択してください");
            model.addAttribute("expense", expense);
            model.addAttribute("enteredExpenseDate", expenseDate);
            model.addAttribute("enteredExpenseName", expenseName);
            model.addAttribute("enteredAmount", amount);
            model.addAttribute("enteredCategory", category);
            return "edit";
        }

        if (expenseDate == null) {
            model.addAttribute("errorMessage", "支出日を入力してください");
            model.addAttribute("expense", expense);
            model.addAttribute("enteredExpenseDate", expenseDate);
            model.addAttribute("enteredExpenseName", expenseName);
            model.addAttribute("enteredAmount", amount);
            model.addAttribute("enteredCategory", category);
            return "edit";
        }

        if (amount == null || amount <= 0) {
            model.addAttribute("errorMessage", "金額は1円以上で入力してください");
            model.addAttribute("expense", expense);
            model.addAttribute("enteredExpenseDate", expenseDate);
            model.addAttribute("enteredExpenseName", expenseName);
            model.addAttribute("enteredAmount", amount);
            model.addAttribute("enteredCategory", category);
            return "edit";
        }

        if (expense != null) {
            expense.setExpenseName(expenseName);
            expense.setAmount(amount);
            expense.setCategory(category);
            expense.setExpenseDate(expenseDate);
            expenseRepository.save(expense);
        }

        return "redirect:/result";
    }

    @PostMapping("/delete")
    public String delete(
            @RequestParam Long id) {

        expenseRepository.deleteById(id);

        return "redirect:/result";
    }

    @GetMapping("/filter")
    public String filter(
            @RequestParam String category,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate expenseDate,
            @RequestParam(required = false) String expenseMonth,
            Model model) {

        List<Expense> expenses;

        if (category.isEmpty()) {
            expenses = expenseRepository.findAllByOrderByExpenseDateDescIdDesc();
        } else {
            expenses = expenseRepository
                    .findByCategoryOrderByExpenseDateDescIdDesc(category);
        }

        if (expenseDate != null) {
            List<Expense> dateFilteredExpenses = new ArrayList<>();

            for (Expense expense : expenses) {
                if (expenseDate.equals(expense.getExpenseDate())) {
                    dateFilteredExpenses.add(expense);
                }
            }

            expenses = dateFilteredExpenses;
        }

        if (expenseMonth != null && !expenseMonth.isEmpty()) {
            YearMonth selectedMonth = YearMonth.parse(expenseMonth);
            List<Expense> monthFilteredExpenses = new ArrayList<>();

            for (Expense expense : expenses) {
                if (expense.getExpenseDate() != null
                        && selectedMonth.equals(YearMonth.from(expense.getExpenseDate()))) {
                            monthFilteredExpenses.add(expense);
                        }
            }
            expenses = monthFilteredExpenses;
        }

        int total = 0;

        for (Expense expense : expenses) {
            total += expense.getAmount();
        }

        if (expenseMonth != null && !expenseMonth.isEmpty()) {
            MonthlyBudget selectedBudget =
                monthlyBudgetRepository.findByBudgetMonth(expenseMonth);

            if (selectedBudget != null) {
                YearMonth budgetYearMonth = YearMonth.parse(expenseMonth);
                int monthlyExpenseTotal = 0;

                for (Expense expense : expenseRepository.findAll()) {
                    if (expense.getExpenseDate() != null
                            && budgetYearMonth.equals(
                                    YearMonth.from(expense.getExpenseDate()))) {

                        monthlyExpenseTotal += expense.getAmount();
                    }
                }

                int remainingBudget =
                        selectedBudget.getAmount() - monthlyExpenseTotal;

                model.addAttribute("selectedMonth", expenseMonth);
                model.addAttribute("budgetAmount", selectedBudget.getAmount());
                model.addAttribute("monthlyExpenseTotal", monthlyExpenseTotal);
                model.addAttribute("remainingBudget", remainingBudget);
            } else {
                model.addAttribute("budgetNotSet", true);
                model.addAttribute("selectedMonth", expenseMonth);
            }
        }

        Map<String, Integer> categoryTotals = new LinkedHashMap<>();

        categoryTotals.put("食費", 0);
        categoryTotals.put("交通費", 0);
        categoryTotals.put("趣味", 0);
        categoryTotals.put("日用品", 0);
        categoryTotals.put("その他", 0);

        for (Expense expense : expenses) {
            String expenseCategory = expense.getCategory();
            int currentTotal = categoryTotals.getOrDefault(expenseCategory, 0);

            categoryTotals.put(
                    expenseCategory,
                    currentTotal + expense.getAmount());
        }

        model.addAttribute("expenses", expenses);
        model.addAttribute("total", total);
        model.addAttribute("categoryTotals", categoryTotals);

        model.addAttribute("selectedCategory", category);
        model.addAttribute("selectedDate", expenseDate);
        model.addAttribute("selectedExpenseMonth", expenseMonth);

        return "result";
    }

    private int calculateTotal() {
        int total = 0;

        for (Expense expense : expenseRepository.findAll()) {
            total += expense.getAmount();
        }

        return total;
    }

    @PostMapping("/budget/register")
    public String budgetRegister(
            @RequestParam String budgetMonth,
            @RequestParam int amount) {

        monthlyBudgetService.registerOrUpdate(budgetMonth, amount);

        return "redirect:/";
    }
}