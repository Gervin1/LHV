package ee.lhv.service;

import ee.lhv.calculator.model.Dividend;
import ee.lhv.calculator.model.ProfitResult;
import ee.lhv.calculator.model.Transaction;
import ee.lhv.calculator.model.TransactionType;
import ee.lhv.calculator.service.ProfitCalculatorService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class CalculatorServiceTest {

    @Test
    public void testCalculateProfitWithTransactionFeesNoDividends() {
        List<Transaction> transactions = List.of(
                new Transaction(TransactionType.BUY, 100, new BigDecimal("10.00"), new BigDecimal("2.00"),
                        Instant.parse("2024-01-01T10:00:00Z")),
                new Transaction(TransactionType.SELL, 100, new BigDecimal("15.00"), new BigDecimal("1.50"),
                        Instant.parse("2024-02-01T12:00:00Z")));

        List<Dividend> dividends = List.of();

        ProfitCalculatorService service = new ProfitCalculatorService();
        ProfitResult result = service.calculateProfit(transactions, dividends);

        Assertions.assertEquals(new BigDecimal("496.50"), result.getTotalProfit());
    }

    @Test
    public void testCalculateProfitWithMultipleTransactionsAndFees() {
        List<Transaction> transactions = List.of(
                new Transaction(TransactionType.BUY, 50, new BigDecimal("20.00"), new BigDecimal("1.00"),
                        Instant.parse("2024-01-05T09:00:00Z")),
                new Transaction(TransactionType.BUY, 50, new BigDecimal("22.00"), new BigDecimal("1.50"),
                        Instant.parse("2024-01-10T10:00:00Z")),
                new Transaction(TransactionType.SELL, 80, new BigDecimal("25.00"), new BigDecimal("2.00"),
                        Instant.parse("2024-02-15T11:00:00Z")),
                new Transaction(TransactionType.SELL, 20, new BigDecimal("24.00"), new BigDecimal("1.00"),
                        Instant.parse("2024-03-01T12:00:00Z")));

        List<Dividend> dividends = List.of();

        ProfitCalculatorService service = new ProfitCalculatorService();
        ProfitResult result = service.calculateProfit(transactions, dividends);

        Assertions.assertEquals(new BigDecimal("374.50"), result.getTotalProfit());
    }

    @Test
    public void testCalculateProfitWithDividends() {
        List<Transaction> transactions = List.of(
                new Transaction(TransactionType.BUY, 100, new BigDecimal("30.00"), new BigDecimal("3.00"),
                        Instant.parse("2024-01-01T09:00:00Z")),
                new Transaction(TransactionType.SELL, 100, new BigDecimal("30.00"), new BigDecimal("2.00"),
                        Instant.parse("2024-02-01T10:00:00Z")));

        List<Dividend> dividends = List.of(new Dividend(new BigDecimal("1.00"), Instant.parse("2024-01-15T09:00:00Z"),
                Instant.parse("2024-01-25T10:00:00Z")));

        ProfitCalculatorService service = new ProfitCalculatorService();
        ProfitResult result = service.calculateProfit(transactions, dividends);

        Assertions.assertEquals(new BigDecimal("95.00"), result.getTotalProfit());
    }

    @Test
    public void testCalculateProfitWithFeesAndDividends() {
        List<Transaction> transactions = List.of(
                new Transaction(TransactionType.BUY, 100, new BigDecimal("30.00"), new BigDecimal("3.00"),
                        Instant.parse("2024-01-01T09:00:00Z")),
                new Transaction(TransactionType.SELL, 50, new BigDecimal("35.00"), new BigDecimal("2.00"),
                        Instant.parse("2024-02-01T10:00:00Z")));

        List<Dividend> dividends = List.of(new Dividend(new BigDecimal("1.00"), Instant.parse("2024-01-15T09:00:00Z"),
                Instant.parse("2024-01-25T10:00:00Z")));

        ProfitCalculatorService service = new ProfitCalculatorService();
        ProfitResult result = service.calculateProfit(transactions, dividends);

        Assertions.assertEquals(new BigDecimal("346.50"), result.getTotalProfit());
    }

    @Test
    public void testCalculateProfitWithZeroProfitDueToFees() {
        List<Transaction> transactions = List.of(
                new Transaction(TransactionType.BUY, 10, new BigDecimal("100.00"), new BigDecimal("5.00"),
                        Instant.parse("2024-01-01T09:00:00Z")),
                new Transaction(TransactionType.SELL, 10, new BigDecimal("100.00"), new BigDecimal("5.00"),
                        Instant.parse("2024-01-02T10:00:00Z")));

        List<Dividend> dividends = List.of();

        ProfitCalculatorService service = new ProfitCalculatorService();
        ProfitResult result = service.calculateProfit(transactions, dividends);

        Assertions.assertEquals(new BigDecimal("-10.00"), result.getTotalProfit());
    }

    @Test
    public void calculateProfitWithDividendsExDividendAndPaymentDateInFuture() {
        List<Transaction> transactions = List.of(
                new Transaction(TransactionType.BUY, 100, new BigDecimal("30.00"), new BigDecimal("3.00"),
                        Instant.parse("2024-01-01T09:00:00Z")),
                new Transaction(TransactionType.SELL, 100, new BigDecimal("30.00"), new BigDecimal("2.00"),
                        Instant.parse("2024-02-01T10:00:00Z")));

        List<Dividend> dividends = List.of(new Dividend(new BigDecimal("1.00"), Instant.now().plus(1, ChronoUnit.DAYS),
                Instant.now().plus(11, ChronoUnit.DAYS)));

        ProfitCalculatorService service = new ProfitCalculatorService();
        ProfitResult result = service.calculateProfit(transactions, dividends);

        Assertions.assertEquals(new BigDecimal("-5.00"), result.getTotalProfit());
    }

    @Test
    public void calculateProfitWithDividendsPaymentDateInFuture() {
        List<Transaction> transactions = List.of(
                new Transaction(TransactionType.BUY, 100, new BigDecimal("30.00"), new BigDecimal("3.00"),
                        Instant.parse("2024-01-01T09:00:00Z")),
                new Transaction(TransactionType.SELL, 100, new BigDecimal("30.00"), new BigDecimal("2.00"),
                        Instant.parse("2024-02-01T10:00:00Z")));

        List<Dividend> dividends = List.of(new Dividend(new BigDecimal("1.00"), Instant.parse("2024-01-15T00:00:00Z"),
                Instant.now().plus(1, ChronoUnit.DAYS)));

        ProfitCalculatorService service = new ProfitCalculatorService();
        ProfitResult result = service.calculateProfit(transactions, dividends);

        Assertions.assertEquals(new BigDecimal("-5.00"), result.getTotalProfit());
    }

    @Test
    public void testCalculateProfitWithUnrealizedGains() {
        List<Transaction> transactions = List.of(
                new Transaction(TransactionType.BUY, 100, new BigDecimal("10.00"), new BigDecimal("1.00"),
                        Instant.parse("2024-01-01T10:00:00Z")),
                new Transaction(TransactionType.SELL, 50, new BigDecimal("15.00"), new BigDecimal("1.50"),
                        Instant.parse("2024-02-01T12:00:00Z")));

        List<Dividend> dividends = List.of();

        ProfitCalculatorService service = new ProfitCalculatorService();
        ProfitResult result = service.calculateProfit(transactions, dividends);

        Assertions.assertEquals(new BigDecimal("248.00"), result.getTotalProfit());
        Assertions.assertEquals(new BigDecimal("249.50"), result.getUnrealizedGains());
    }

    @Test
    // The 2nd buy order fee does not influence the profit calculation as FIFO is used
    public void testCalculateProfitWithTransactionFeeFIFO() {
        List<Transaction> transactions = List.of(
                new Transaction(TransactionType.BUY, 10, new BigDecimal("10.00"), new BigDecimal("5.00"),
                        Instant.parse("2024-01-01T10:00:00Z")),
                new Transaction(TransactionType.SELL, 8, new BigDecimal("15.00"), new BigDecimal("5.00"),
                        Instant.parse("2024-02-01T12:00:00Z")),
                new Transaction(TransactionType.BUY, 5, new BigDecimal("20.00"), new BigDecimal("10000.00"),
                        Instant.parse("2024-03-01T10:00:00Z")),
                new Transaction(TransactionType.SELL, 2, new BigDecimal("25.00"), new BigDecimal("5.00"),
                        Instant.parse("2024-04-01T12:00:00Z")));

        List<Dividend> dividends = List.of();

        ProfitCalculatorService service = new ProfitCalculatorService();
        ProfitResult result = service.calculateProfit(transactions, dividends);

        Assertions.assertEquals(new BigDecimal("55.00"), result.getTotalProfit());
    }
}
