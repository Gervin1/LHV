package ee.lhv.calculator.model;

import java.math.BigDecimal;
import java.time.Instant;

public class Dividend {
    private final BigDecimal amountPerUnit;
    private final Instant exDividendDate;
    private final Instant paymentDate;

    public Dividend(BigDecimal amountPerUnit, Instant exDividendDate, Instant paymentDate) {
        this.amountPerUnit = amountPerUnit;
        this.exDividendDate = exDividendDate;
        this.paymentDate = paymentDate;
    }

    public BigDecimal getAmountPerUnit() {
        return amountPerUnit;
    }

    public Instant getExDividendDate() {
        return exDividendDate;
    }

    public Instant getPaymentDate() {
        return paymentDate;
    }
}

