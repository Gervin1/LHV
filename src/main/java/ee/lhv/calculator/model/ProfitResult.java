package ee.lhv.calculator.model;

import java.math.BigDecimal;

public class ProfitResult {
    private BigDecimal totalProfit;
    private BigDecimal realizedStockProfit;
    private BigDecimal dividendProfit;
    private BigDecimal unrealizedGains;

    public ProfitResult(BigDecimal totalProfit, BigDecimal realizedStockProfit, BigDecimal dividendProfit,
                        BigDecimal unrealizedGains) {
        this.totalProfit = totalProfit;
        this.realizedStockProfit = realizedStockProfit;
        this.dividendProfit = dividendProfit;
        this.unrealizedGains = unrealizedGains;
    }

    public BigDecimal getTotalProfit() {
        return totalProfit;
    }

    public void setTotalProfit(BigDecimal totalProfit) {
        this.totalProfit = totalProfit;
    }

    public BigDecimal getRealizedStockProfit() {
        return realizedStockProfit;
    }

    public void setRealizedStockProfit(BigDecimal realizedStockProfit) {
        this.realizedStockProfit = realizedStockProfit;
    }

    public BigDecimal getDividendProfit() {
        return dividendProfit;
    }

    public void setDividendProfit(BigDecimal dividendProfit) {
        this.dividendProfit = dividendProfit;
    }

    public BigDecimal getUnrealizedGains() {
        return unrealizedGains;
    }

    public void setUnrealizedGains(BigDecimal unrealizedGains) {
        this.unrealizedGains = unrealizedGains;
    }
}
