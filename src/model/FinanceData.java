package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * In-memory sample ledger used by the classic UI.
 */
public class FinanceData {
    private final List<Transaction> transactions = new ArrayList<>();
    private final Map<String, Double> budgets = new LinkedHashMap<>();

    public FinanceData() {
        seed();
    }

    private void seed() {
        LocalDate today = LocalDate.now();
        add("Salary", "Income", "Income", 85000, today.minusDays(12));
        add("Freelance Design", "Income", "Income", 18000, today.minusDays(5));
        add("Grocery Store", "Food", "Expense", 4200, today.minusDays(2));
        add("Electric Bill", "Utilities", "Expense", 3100, today.minusDays(8));
        add("Metro Pass", "Transport", "Expense", 1500, today.minusDays(3));
        add("Cinema Night", "Entertainment", "Expense", 1800, today.minusDays(1));
        add("Pharmacy", "Health", "Expense", 950, today.minusDays(6));
        add("Online Course", "Education", "Expense", 4500, today.minusDays(10));

        budgets.put("Food", 12000.0);
        budgets.put("Utilities", 8000.0);
        budgets.put("Transport", 5000.0);
        budgets.put("Entertainment", 4000.0);
        budgets.put("Health", 3000.0);
        budgets.put("Education", 6000.0);
    }

    public void add(String description, String category, String type, double amount, LocalDate date) {
        transactions.add(0, new Transaction(
                UUID.randomUUID().toString().substring(0, 8),
                date,
                description,
                category,
                type,
                amount
        ));
    }

    public List<Transaction> getTransactions() {
        return new ArrayList<>(transactions);
    }

    public Map<String, Double> getBudgets() {
        return new LinkedHashMap<>(budgets);
    }

    public double getTotalIncome() {
        return transactions.stream().filter(Transaction::isIncome).mapToDouble(Transaction::getAmount).sum();
    }

    public double getTotalExpense() {
        return transactions.stream().filter(t -> !t.isIncome()).mapToDouble(Transaction::getAmount).sum();
    }

    public double getBalance() {
        return getTotalIncome() - getTotalExpense();
    }

    public Map<String, Double> getExpenseByCategory() {
        Map<String, Double> map = new LinkedHashMap<>();
        for (Transaction t : transactions) {
            if (!t.isIncome()) {
                map.merge(t.getCategory(), t.getAmount(), Double::sum);
            }
        }
        return map;
    }
}
