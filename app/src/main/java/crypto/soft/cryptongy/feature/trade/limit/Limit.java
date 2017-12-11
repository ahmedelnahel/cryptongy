package crypto.soft.cryptongy.feature.trade.limit;

import crypto.soft.cryptongy.feature.shared.module.Account;

/**
 * Created by tseringwongelgurung on 12/3/17.
 */

public class Limit {
    private String market;
    private Double quantity;
    private Double rate;
    private Account account;

    public Limit() {
    }

    public Limit(String market, Double quantity, Double rate, Account account) {
        this.market = market;
        this.quantity = quantity;
        this.rate = rate;
        this.account = account;
    }

    public String getMarket() {
        return market;
    }

    public void setMarket(String market) {
        this.market = market;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}
