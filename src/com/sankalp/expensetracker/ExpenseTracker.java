package com.sankalp.expensetracker;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class ExpenseTracker {
    private static final List<Transaction> transactions = new ArrayList<>();
    private static final Scanner scanner = new Scanner(System.in);
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static void main(String[] args) throws IOException {
        while (true) {
            System.out.println("1. Add Transaction\n2. Load from File\n3. Save to File\n4. Show Monthly Summary\n5. Exit");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    addTransaction();
                    break;
                case "2":
                    System.out.println("Enter file path to load:");
                    String loadPath = scanner.nextLine();
                    loadFromFile(loadPath);
                    break;
                case "3":
                    System.out.println("Enter file path to save:");
                    String savePath = scanner.nextLine();
                    saveToFile(savePath);
                    break;
                case "4":
                    showMonthlySummary();
                    break;
                case "5":
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private static void addTransaction() {
        System.out.println("Enter transaction type (income/expense):");
        String type = scanner.nextLine().toLowerCase();
        if (!type.equals("income") && !type.equals("expense")) {
            System.out.println("Invalid type.");
            return;
        }

        System.out.println("Select category:");
        if (type.equals("income")) {
            System.out.println("1. Salary\n2. Business\n3. Other");
        } else {
            System.out.println("1. Food\n2. Rent\n3. Travel\n4. Other");
        }

        String categoryChoice = scanner.nextLine();
        String category = "Other";
        if (type.equals("income")) {
            switch (categoryChoice) {
                case "1": category = "Salary"; break;
                case "2": category = "Business"; break;
            }
        } else {
            switch (categoryChoice) {
                case "1": category = "Food"; break;
                case "2": category = "Rent"; break;
                case "3": category = "Travel"; break;
            }
        }

        System.out.println("Enter amount:");
        double amount = Double.parseDouble(scanner.nextLine());

        LocalDate date = LocalDate.now();
        transactions.add(new Transaction(type, category, amount, date));
        System.out.println("Transaction added.");
    }

    private static void loadFromFile(String filePath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");
            if (parts.length != 4) continue;
            String type = parts[0].trim().toLowerCase();
            String category = parts[1].trim();
            double amount = Double.parseDouble(parts[2].trim());
            LocalDate date = LocalDate.parse(parts[3].trim(), formatter);
            transactions.add(new Transaction(type, category, amount, date));
        }
        reader.close();
        System.out.println("Data loaded successfully.");
    }

    private static void saveToFile(String filePath) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
        for (Transaction t : transactions) {
            writer.write(t.toString());
            writer.newLine();
        }
        writer.close();
        System.out.println("Data saved successfully.");
    }

    private static void showMonthlySummary() {
        Map<String, Double> incomeSummary = new HashMap<>();
        Map<String, Double> expenseSummary = new HashMap<>();
        double totalIncome = 0, totalExpense = 0;

        for (Transaction t : transactions) {
            if (t.date.getMonth() == LocalDate.now().getMonth() && t.date.getYear() == LocalDate.now().getYear()) {
                if (t.type.equals("income")) {
                    incomeSummary.put(t.category, incomeSummary.getOrDefault(t.category, 0.0) + t.amount);
                    totalIncome += t.amount;
                } else {
                    expenseSummary.put(t.category, expenseSummary.getOrDefault(t.category, 0.0) + t.amount);
                    totalExpense += t.amount;
                }
            }
        }

        System.out.println("Monthly Summary:");
        System.out.println("Total Income: " + totalIncome);
        System.out.println("Total Expense: " + totalExpense);
        System.out.println("Net Savings: " + (totalIncome - totalExpense));

        System.out.println("Income by Category:");
        incomeSummary.forEach((cat, amt) -> System.out.println(cat + ": " + amt));

        System.out.println("Expenses by Category:");
        expenseSummary.forEach((cat, amt) -> System.out.println(cat + ": " + amt));
    }
}
