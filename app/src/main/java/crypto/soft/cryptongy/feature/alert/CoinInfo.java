package crypto.soft.cryptongy.feature.alert;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by maiAjam on 11/20/2017.
 */

public class CoinInfo extends RealmObject {

    @PrimaryKey
    int Id ;
    Double lowValue,HighValue ;
    String CoinName,ExchangeName;

    public  CoinInfo ()
    {

    }

    public CoinInfo(String coinName, String exchangeName, Double highValueEn, Double lowValueEn) {

        this.CoinName = coinName ;
        this.ExchangeName = exchangeName ;
        this.lowValue = lowValueEn ;
        this.HighValue = highValueEn ;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getId() {
        return Id;
    }
    //

    public void setHighValue(Double highValue) {
        HighValue = highValue;
    }

    public Double getHighValue() {
        return HighValue;
    }


    ////


    public void setLowValue(Double lowValue) {
        this.lowValue = lowValue;
    }

    public Double getLowValue() {
        return lowValue;
    }
    //

    public void setCoinName(String coinName) {
        CoinName = coinName;
    }

    public String getCoinName() {
        return CoinName;
    }
    //


    public void setExchangeName(String exchangeName) {
        ExchangeName = exchangeName;
    }

    public String getExchangeName() {
        return ExchangeName;
    }
}
