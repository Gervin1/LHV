package ee.lhv.calculator.service;

import ee.lhv.calculator.model.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.LinkedList;

public class ProfitCalculatorService {

    private static final int SCALE = 8;

    public ProfitResult calculateProfit(List<Transaction> transactions, List<Dividend> dividends) {
        List<Event> events = new ArrayList<>();
        for (Transaction tx : transactions) events.add(new TransactionEvent(tx));
        for (Dividend div : dividends) events.add(new DividendPaymentEvent(div));
        Collections.sort(events);

        Queue<Transaction> buys = new LinkedList<>();
        BigDecimal realizedStockProfit = BigDecimal.ZERO;
        BigDecimal dividendProfit = BigDecimal.ZERO;
        Instant currentDateTime = Instant.now();
        int remainingHoldings = 0;
        BigDecimal lastPrice = BigDecimal.ZERO;

        // Process all events (transactions and dividends)
        for (Event event : events) {
            if (event instanceof TransactionEvent) {
                Transaction transaction = ((TransactionEvent) event).getTransaction();
                lastPrice = transaction.getPrice();

                if (transaction.getType() == TransactionType.BUY) {
                    buys.offer(new Transaction(transaction.getType(), transaction.getQuantity(), transaction.getPrice(),
                            transaction.getFee(), transaction.getTimestamp()));
                    remainingHoldings += transaction.getQuantity();
                } else {
                    int quantityToSell = transaction.getQuantity();
                    BigDecimal totalSellFee = transaction.getFee();
                    remainingHoldings -= quantityToSell;

                    // Realize profits from selling stock
                    while (quantityToSell > 0 && !buys.isEmpty()) {
                        Transaction buy = buys.peek();
                        int quantityAvailable = buy.getQuantity();
                        int quantitySold = Math.min(quantityAvailable, quantityToSell);

                        BigDecimal sellFeeProportion = calculateProportionalAmount(totalSellFee, quantitySold, transaction.getQuantity());
                        BigDecimal buyFeeProportion = calculateProportionalAmount(buy.getFee(), quantitySold, buy.getQuantity());

                        buy.setFee(buy.getFee().subtract(buyFeeProportion));

                        BigDecimal buyCost = buy.getPrice().multiply(BigDecimal.valueOf(quantitySold));
                        BigDecimal sellRevenue = transaction.getPrice().multiply(BigDecimal.valueOf(quantitySold));

                        BigDecimal totalBuyAmount = buyCost.add(buyFeeProportion);
                        BigDecimal totalSellAmount = sellRevenue.subtract(sellFeeProportion);

                        BigDecimal profit = totalSellAmount.subtract(totalBuyAmount);
                        realizedStockProfit = realizedStockProfit.add(profit);

                        buy.setQuantity(quantityAvailable - quantitySold);
                        quantityToSell -= quantitySold;

                        if (buy.getQuantity() == 0) buys.poll();
                    }
                }
            } else if (event instanceof DividendPaymentEvent) {
                // Calculate dividend profit
                Dividend dividend = ((DividendPaymentEvent) event).getDividend();

                if (!dividend.getPaymentDate().isAfter(currentDateTime)) {
                    int holdingsAtExDate = calculateHoldingsAtDate(transactions, dividend.getExDividendDate());
                    BigDecimal dividendAmount = dividend.getAmountPerUnit().multiply(BigDecimal.valueOf(holdingsAtExDate));
                    dividendProfit = dividendProfit.add(dividendAmount);
                }
            }
        }

        // Calculate unrealized gains based on remaining holdings
        BigDecimal unrealizedGains = findUnrealizedGains(remainingHoldings, buys, lastPrice);

        // Total profit is the sum of realized stock profit and dividend profit
        BigDecimal totalProfit = realizedStockProfit.add(dividendProfit);

        return new ProfitResult(
                totalProfit.setScale(SCALE, RoundingMode.HALF_UP),
                realizedStockProfit.setScale(SCALE, RoundingMode.HALF_UP),
                dividendProfit.setScale(SCALE, RoundingMode.HALF_UP),
                unrealizedGains.setScale(SCALE, RoundingMode.HALF_UP)
        );
    }

    private static BigDecimal findUnrealizedGains(int remainingHoldings, Queue<Transaction> buys, BigDecimal lastPrice) {
        BigDecimal unrealizedGains = BigDecimal.ZERO;

        if (remainingHoldings > 0) {
            BigDecimal totalCost = BigDecimal.ZERO;

            for (Transaction buy : buys) {
                BigDecimal buyCost = buy.getPrice().multiply(BigDecimal.valueOf(buy.getQuantity()));
                totalCost = totalCost.add(buyCost).add(buy.getFee());
            }
            BigDecimal marketValue = lastPrice.multiply(BigDecimal.valueOf(remainingHoldings));
            unrealizedGains = marketValue.subtract(totalCost);
        }
        return unrealizedGains;
    }

    private BigDecimal calculateProportionalAmount(BigDecimal totalAmount, int part, int total) {
        return totalAmount.multiply(BigDecimal.valueOf(part))
                .divide(BigDecimal.valueOf(total), SCALE, RoundingMode.HALF_UP);
    }

    private int calculateHoldingsAtDate(List<Transaction> transactions, Instant date) {
        int holdings = 0;
        for (Transaction tx : transactions) {
            if (!tx.getTimestamp().isAfter(date)) {
                holdings += tx.getType() == TransactionType.BUY ? tx.getQuantity() : -tx.getQuantity();
            } else {
                break;
            }
        }
        return holdings;
    }
}
