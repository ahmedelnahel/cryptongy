package crypto.soft.cryptongy.feature.trade.limit;

import crypto.soft.cryptongy.feature.shared.module.Account;

/**
 * Created by tseringwongelgurung on 12/3/17.
 */

public class Limit {
    private String market;
    private String quantity;
    private String rate;
    private Account account;

    public String getMarket() {
        return market;
    }

    public void setMarket(String market) {
        this.market = market;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}
