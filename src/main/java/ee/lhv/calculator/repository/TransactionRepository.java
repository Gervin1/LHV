package ee.lhv.calculator.repository;

import ee.lhv.calculator.model.Transaction;
import ee.lhv.calculator.model.TransactionType;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TransactionRepository {

    private static final int SCALE = 2;
    private static final BigDecimal MIN_FEE = new BigDecimal("1.00");
    private static final BigDecimal MAX_FEE = new BigDecimal("10.00");

    public List<Transaction> generateTransactions(int numTransactions) {
        List<Transaction> transactions = new ArrayList<>();
        Random rand = new Random();

        Instant startTime = Instant.now().minus(365 * 2, ChronoUnit.DAYS);
        Instant endTime = Instant.now().minus(30, ChronoUnit.DAYS);
        long totalMinutes = ChronoUnit.MINUTES.between(startTime, endTime);

        int maxQuantity = 100;
        BigDecimal meanPrice = new BigDecimal("100.00");
        BigDecimal stddevPrice = new BigDecimal("20.00");

        int totalHoldings = 0;

        Instant currentTimestamp = startTime;

        for (int i = 0; i < numTransactions; i++) {
            TransactionType type;

            if (totalHoldings == 0) {
                type = TransactionType.BUY;
            } else {
                type = rand.nextBoolean() ? TransactionType.BUY : TransactionType.SELL;
            }

            int quantity;
            if (type == TransactionType.BUY) {
                quantity = rand.nextInt(maxQuantity) + 1;
                totalHoldings += quantity;
            } else {
                quantity = rand.nextInt(totalHoldings) + 1;
                totalHoldings -= quantity;
            }

            BigDecimal price = generateNormalDistributedPrice(meanPrice, stddevPrice, rand);
            BigDecimal fee = calculateCost(price.multiply(BigDecimal.valueOf(quantity)));

            long remainingMinutes = totalMinutes - ChronoUnit.MINUTES.between(startTime, currentTimestamp);
            long randomMinutes = rand.nextInt((int) remainingMinutes / (numTransactions - i));
            currentTimestamp = currentTimestamp.plus(randomMinutes, ChronoUnit.MINUTES);

            transactions.add(new Transaction(type, quantity, price, fee, currentTimestamp));
        }

        return transactions;
    }

    private BigDecimal calculateCost(BigDecimal orderCost) {
        BigDecimal fee = orderCost.multiply(new BigDecimal("0.005"));
        if (fee.compareTo(MIN_FEE) < 0) {
            fee = MIN_FEE;
        } else if (fee.compareTo(MAX_FEE) > 0) {
            fee = MAX_FEE;
        }
        return fee.setScale(SCALE, RoundingMode.HALF_UP);
    }

    private BigDecimal generateNormalDistributedPrice(BigDecimal mean, BigDecimal stddev, Random rand) {
        double gaussianValue = rand.nextGaussian();
        BigDecimal price = mean.add(stddev.multiply(BigDecimal.valueOf(gaussianValue)));
        if (price.compareTo(BigDecimal.ONE) < 0) {
            price = BigDecimal.ONE;
        }
        return price.setScale(SCALE, RoundingMode.HALF_UP);
    }
}
