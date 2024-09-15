package ee.lhv.repository;

import ee.lhv.calculator.model.Transaction;
import ee.lhv.calculator.model.TransactionType;
import ee.lhv.calculator.repository.TransactionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class TransactionRepositoryTest {

    @Test
    public void testGeneratedTransactionsGHoldingsNeverNegative() {
        TransactionRepository repository = new TransactionRepository();
        List<Transaction> transactions = repository.generateTransactions(1000);

        int totalHoldings = 0;
        for (Transaction tx : transactions) {
            if (tx.getType() == TransactionType.BUY) {
                totalHoldings += tx.getQuantity();
            } else if (tx.getType() == TransactionType.SELL) {
                totalHoldings -= tx.getQuantity();
                Assertions.assertTrue(totalHoldings >= 0, "Holdings went negative after a sell transaction");
            }
        }
        Assertions.assertTrue(totalHoldings >= 0, "Final holdings are negative");
    }
}