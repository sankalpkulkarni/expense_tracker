package com.sankalp.expensetracker;

import java.time.LocalDate;


class Transaction {
    String type; // income or expens
    String category;
    double amount;
    LocalDate date;

    public Transaction(String type, String category, double amount, LocalDate date) {
        this.type = type;
        this.category = category;
        this.amount = amount;
        this.date = date;
    }

    @Override
    public String toString() {
        return date + "," + type + "," + category + "," + amount;
    }
}
