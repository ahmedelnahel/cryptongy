package crypto.soft.cryptongy.feature.alert;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by maiAjam on 11/20/2017.
 */

public class CoinInfo extends RealmObject {
    @PrimaryKey
    int Id;
    Double lowValue, HighValue;
    String CoinName, ExchangeName;
    int alarmFreq; int reqCode;
    boolean isHigher, isLower;
    String status;
    String spinnerValue;

    public CoinInfo() {

    }

    public CoinInfo(String coinName,String exchangeName, Double highValue,  Double lowValue,
                    int alarmFreq, int reqCode, boolean isHigher, boolean isLower,String status,String spinnerValue) {
        this.lowValue = lowValue;
        HighValue = highValue;
        CoinName = coinName;
        ExchangeName = exchangeName;
        this.alarmFreq = alarmFreq;
        this.reqCode = reqCode;
        this.isHigher = isHigher;
        this.isLower = isLower;
        this.status=status;
        this.spinnerValue=spinnerValue;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public Double getLowValue() {
        return lowValue;
    }

    public void setLowValue(Double lowValue) {
        this.lowValue = lowValue;
    }

    public Double getHighValue() {
        return HighValue;
    }

    public void setHighValue(Double highValue) {
        HighValue = highValue;
    }

    public String getCoinName() {
        return CoinName;
    }

    public void setCoinName(String coinName) {
        CoinName = coinName;
    }

    public String getExchangeName() {
        return ExchangeName;
    }

    public void setExchangeName(String exchangeName) {
        ExchangeName = exchangeName;
    }

    public int getAlarmFreq() {
        return alarmFreq;
    }

    public void setAlarmFreq(int alarmFreq) {
        this.alarmFreq = alarmFreq;
    }

    public int getReqCode() {
        return reqCode;
    }

    public void setReqCode(int reqCode) {
        this.reqCode = reqCode;
    }

    public boolean isHigher() {
        return isHigher;
    }

    public void setHigher(boolean higher) {
        isHigher = higher;
    }

    public boolean isLower() {
        return isLower;
    }

    public void setLower(boolean lower) {
        isLower = lower;
    }

    public String getSpinnerValue() {
        return spinnerValue;
    }

    public void setSpinnerValue(String spinnerValue) {
        this.spinnerValue = spinnerValue;
    }
}
