package ee.lhv.calculator.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import ee.lhv.calculator.model.Dividend;
import ee.lhv.calculator.model.ProfitResult;
import ee.lhv.calculator.model.Transaction;
import ee.lhv.calculator.repository.DividendRepository;
import ee.lhv.calculator.repository.TransactionRepository;
import ee.lhv.calculator.service.ProfitCalculatorService;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class CalculatorController {

    public static void main(String[] args) {
        TransactionRepository transactionRepository = new TransactionRepository();
        DividendRepository dividendRepository = new DividendRepository();
        ProfitCalculatorService profitCalculatorService = new ProfitCalculatorService();
        ObjectMapper om = new ObjectMapper();
        om.registerModule(new JavaTimeModule());

        if (args.length == 1 && args[0].equals("generate")) {
            try {
                List<Transaction> transactions = transactionRepository.generateTransactions(1000);
                om.writeValue(new File("transactions.json"), transactions);
                System.out.println("Transactions have been written to transactions.json");
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.exit(0);
        }

        if (args.length == 1) {
            String fileName = args[0];
            try {
                File file = new File(fileName);
                if (file.exists()) {
                    List<Transaction> transactions = om.readValue(file, om.getTypeFactory().constructCollectionType(List.class, Transaction.class));
                    System.out.println("Successfully read " + transactions.size() + " transactions from file.");
                    List<Dividend> dividends = dividendRepository.generateDividends(transactions);
                    ProfitResult profitResult = profitCalculatorService.calculateProfit(transactions, dividends);
                    System.out.printf("Total Profit/Loss: $%.2f%n", profitResult.getTotalProfit());
                    System.out.printf("Profit/Loss from Stock Gain/Fall: $%.2f%n", profitResult.getRealizedStockProfit());
                    System.out.printf("Profit/Loss from Dividends: $%.2f%n", profitResult.getDividendProfit());
                    System.out.printf("Unrealized Gains/Losses (based on the latest transaction price): $%.2f%n", profitResult.getUnrealizedGains());
                } else {
                    System.out.println("The specified file does not exist: " + fileName);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.exit(0);
        }

        // Default case: Generate transactions internally and calculate P/L
        List<Transaction> transactions = transactionRepository.generateTransactions(1000);
        List<Dividend> dividends = dividendRepository.generateDividends(transactions);
        ProfitResult profitResult = profitCalculatorService.calculateProfit(transactions, dividends);
        System.out.printf("Total Profit/Loss: $%.2f%n", profitResult.getTotalProfit());
        System.out.printf("Profit/Loss from Stock Gain/Fall: $%.2f%n", profitResult.getRealizedStockProfit());
        System.out.printf("Profit/Loss from Dividends: $%.2f%n", profitResult.getDividendProfit());
        System.out.printf("Unrealized Gains/Losses (based on the latest transaction price): $%.2f%n", profitResult.getUnrealizedGains());

    }
}