package ee.lhv.calculator.repository;

import ee.lhv.calculator.model.Dividend;
import ee.lhv.calculator.model.Transaction;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DividendRepository {

    public List<Dividend> generateDividends(List<Transaction> transactions) {
        List<Dividend> dividends = new ArrayList<>();
        Random rand = new Random();
        Instant startDate = transactions.get(0).getTimestamp();
        Instant endDate = transactions.get(transactions.size() - 1).getTimestamp();
        Instant exDividendDate = startDate.plus(90, ChronoUnit.DAYS);
        while (exDividendDate.isBefore(endDate)) {
            BigDecimal amountPerUnit = BigDecimal.valueOf(0.5 + (2.0 - 0.5) * rand.nextDouble()).setScale(2, RoundingMode.HALF_UP);
            Instant paymentDate = exDividendDate.plus(10, ChronoUnit.DAYS);
            dividends.add(new Dividend(amountPerUnit, exDividendDate, paymentDate));
            exDividendDate = exDividendDate.plus(90, ChronoUnit.DAYS);
        }
        return dividends;
    }
}