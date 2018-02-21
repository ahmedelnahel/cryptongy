package crypto.soft.cryptongy.feature.arbitage;

/**
 * Created by vishalguptahmh on 02/20/18.
 */

public class AribitaryTableResult {
    private String coinName;
    private String price1;
    private String price2;
    private String percentage;

    AribitaryTableResult(String coinName, String price1, String price2, String percentage) {
        this.coinName = coinName;
        this.price1 = price1;
        this.price2 = price2;
        this.percentage = percentage;
    }


    public String getCoinName() {
        return coinName;
    }

    public void setCoinName(String coinName) {
        this.coinName = coinName;
    }

    public String getPrice1() {
        return price1;
    }

    public void setPrice1(String price1) {
        this.price1 = price1;
    }

    public String getPrice2() {
        return price2;
    }

    public void setPrice2(String price2) {
        this.price2 = price2;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }
}
