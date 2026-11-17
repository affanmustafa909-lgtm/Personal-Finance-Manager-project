package model;

import java.time.LocalDate;

public class Transaction {
    private final String id;
    private final LocalDate date;
    private final String description;
    private final String category;
    private final String type; // Income | Expense
    private final double amount;

    public Transaction(String id, LocalDate date, String description, String category, String type, double amount) {
        this.id = id;
        this.date = date;
        this.description = description;
        this.category = category;
        this.type = type;
        this.amount = amount;
    }

    public String getId() { return id; }
    public LocalDate getDate() { return date; }
    public String getDescription() { return description; }
    public String getCategory() { return category; }
    public String getType() { return type; }
    public double getAmount() { return amount; }

    public boolean isIncome() {
        return "Income".equalsIgnoreCase(type);
    }
}
