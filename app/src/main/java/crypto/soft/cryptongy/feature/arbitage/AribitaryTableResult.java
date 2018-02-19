package crypto.soft.cryptongy.feature.arbitage;

/**
 * Created by aapple on 19/02/18.
 */

public class AribitaryTableResult {
    String coinName;
    String price1;
    String price2;
    String percentage;

    AribitaryTableResult(String coinName,String price1,String price2,String percentage){
        this.coinName=coinName;
        this.price1=price1;
        this.price2=price2;
        this.percentage=percentage;
    }
}
