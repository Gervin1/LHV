package ee.lhv.calculator.model;

public class DividendPaymentEvent extends Event {
    private final Dividend dividend;

    public DividendPaymentEvent(Dividend dividend) {
        super(dividend.getPaymentDate());
        this.dividend = dividend;
    }

    public Dividend getDividend() {
        return dividend;
    }
}
