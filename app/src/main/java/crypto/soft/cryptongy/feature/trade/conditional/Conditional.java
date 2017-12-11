package crypto.soft.cryptongy.feature.trade.conditional;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by tseringwongelgurung on 12/7/17.
 */

public class Conditional extends RealmObject {
    @PrimaryKey
    private int id;
    private String orderType;
    private String orderCoin;
    private Double units;
    private Double last;
    private Double against;
    private Double lowCondition;
    private String conditionType;
    private Double highCondition;
    private String conditionHighType;
    private Double lowPrice;
    private String priceType;
    private Double highPrice;
    private String highType;
    private String stopLossType;
    private String orderStatus;
    private boolean isHigh;
    private String error="";

    public Conditional() {
    }

    public Conditional(boolean isHigh,String orderType, String orderCoin, Double units, Double last, Double against,
                       Double lowCondition, String conditionType, Double lowPrice, String priceType,
                       String stopLossType, String orderStatus) {
        this.orderType = orderType;
        this.orderCoin = orderCoin;
        this.units = units;
        this.last = last;
        this.against = against;
        this.stopLossType = stopLossType;
        this.orderStatus = orderStatus;
        this.isHigh=isHigh;
        if (isHigh){
            this.highCondition = lowCondition;
            this.conditionHighType = conditionType;
            this.highPrice = lowPrice;
            this.highType = priceType;
        }else {
            this.lowCondition = lowCondition;
            this.conditionType = conditionType;
            this.lowPrice = lowPrice;
            this.priceType = priceType;
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getOrderCoin() {
        return orderCoin;
    }

    public void setOrderCoin(String orderCoin) {
        this.orderCoin = orderCoin;
    }

    public Double getUnits() {
        return units;
    }

    public void setUnits(Double units) {
        this.units = units;
    }

    public Double getLast() {
        return last;
    }

    public void setLast(Double last) {
        this.last = last;
    }

    public Double getAgainst() {
        return against;
    }

    public void setAgainst(Double against) {
        this.against = against;
    }

    public Double getLowCondition() {
        return lowCondition;
    }

    public void setLowCondition(Double lowCondition) {
        this.lowCondition = lowCondition;
    }

    public String getConditionType() {
        return conditionType;
    }

    public void setConditionType(String conditionType) {
        this.conditionType = conditionType;
    }

    public Double getLowPrice() {
        return lowPrice;
    }

    public void setLowPrice(Double lowPrice) {
        this.lowPrice = lowPrice;
    }

    public String getPriceType() {
        return priceType;
    }

    public void setPriceType(String priceType) {
        this.priceType = priceType;
    }

    public String getStopLossType() {
        return stopLossType;
    }

    public void setStopLossType(String stopLossType) {
        this.stopLossType = stopLossType;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Double getHighCondition() {
        return highCondition;
    }

    public void setHighCondition(Double highCondition) {
        this.highCondition = highCondition;
    }

    public String getConditionHighType() {
        return conditionHighType;
    }

    public void setConditionHighType(String conditionHighType) {
        this.conditionHighType = conditionHighType;
    }

    public Double getHighPrice() {
        return highPrice;
    }

    public void setHighPrice(Double highPrice) {
        this.highPrice = highPrice;
    }

    public String getHighType() {
        return highType;
    }

    public void setHighType(String highType) {
        this.highType = highType;
    }

    public boolean isHigh() {
        return isHigh;
    }

    public void setHigh(boolean high) {
        isHigh = high;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
//order.type=buy,order.coin=coin,
// order.units=units,order.last=marketsummary.last,
// order.against=btc,order.condition.low=0.05,
// order.condition.type=%,order.price.low0.0034,
// order.price.type: #,
// order.stoploss.type=fixed,order.status=open