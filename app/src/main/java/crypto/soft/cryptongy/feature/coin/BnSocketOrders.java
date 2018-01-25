package crypto.soft.cryptongy.feature.coin;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Created by aapple on 24/01/18.
 */


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "e",
        "E",
        "s",
        "p",
        "P",
        "w",
        "x",
        "c",
        "Q",
        "b",
        "B",
        "a",
        "A",
        "o",
        "h",
        "l",
        "v",
        "q",
        "O",
        "C",
        "F",
        "L",
        "n"

})
public class BnSocketOrders {


    @JsonProperty("e")
    private String code;
    @JsonProperty("E")
    private String msg;
    @JsonProperty("s")
    private String symbol;

    @JsonProperty("p")
    private String priceChange;
    @JsonProperty("P")
    private String priceChangePercent;
    @JsonProperty("w")
    private String weightedAvgPrice;
    @JsonProperty("x")
    private String prevClosePrice;
    @JsonProperty("c")
    private String lastPrice;
    @JsonProperty("Q")
    private String lastQty;
    @JsonProperty("b")
    private String bidPrice;
    @JsonProperty("B")
    private String bidQty;
    @JsonProperty("a")
    private String askPrice;
    @JsonProperty("A")
    private String askQty;
    @JsonProperty("o")
    private String openPrice;
    @JsonProperty("h")
    private String highPrice;
    @JsonProperty("l")
    private String lowPrice;
    @JsonProperty("v")
    private String volume;
    @JsonProperty("q")
    private String quoteVolume;
    @JsonProperty("O")
    private Long openTime;
    @JsonProperty("C")
    private Long closeTime;
    @JsonProperty("F")
    private Long firstId;
    @JsonProperty("L")
    private Long lastId;
    @JsonProperty("n")
    private Long count;


    @JsonProperty("e")
    public String getCode() {
        return code;
    }

    @JsonProperty("e")
    public void setCode(String code) {
        this.code = code;
    }

    @JsonProperty("E")
    public String getMsg() {
        return msg;
    }

    @JsonProperty("E")
    public void setMsg(String msg) {
        this.msg = msg;
    }

    @JsonProperty("s")
    public String getSymbol() {
        return symbol;
    }

    @JsonProperty("s")
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }


//    public String getPriceChange() {
//        return priceChange;
//    }
//
//    public void setPriceChange(String priceChange) {
//        this.priceChange = priceChange;
//    }
//
//    public String getPriceChangePercent() {
//        return priceChangePercent;
//    }
//
//    public void setPriceChangePercent(String priceChangePercent) {
//        this.priceChangePercent = priceChangePercent;
//    }
//
//    public String getWeightedAvgPrice() {
//        return weightedAvgPrice;
//    }
//
//    public void setWeightedAvgPrice(String weightedAvgPrice) {
//        this.weightedAvgPrice = weightedAvgPrice;
//    }
//
//    public String getPrevClosePrice() {
//        return prevClosePrice;
//    }
//
//    public void setPrevClosePrice(String prevClosePrice) {
//        this.prevClosePrice = prevClosePrice;
//    }
//
@JsonProperty("c")
    public String getLastPrice() {
        return lastPrice;
    }

    @JsonProperty("c")
    public void setLastPrice(String lastPrice) {
        this.lastPrice = lastPrice;
    }
//
//    public String getLastQty() {
//        return lastQty;
//    }
//
//    public void setLastQty(String lastQty) {
//        this.lastQty = lastQty;
//    }
//
@JsonProperty("b")
    public String getBidPrice() {
        return bidPrice;
    }

    @JsonProperty("b")
    public void setBidPrice(String bidPrice) {
        this.bidPrice = bidPrice;
    }

//    public String getBidQty() {
//        return bidQty;
//    }
//
//    public void setBidQty(String bidQty) {
//        this.bidQty = bidQty;
//    }
//
@JsonProperty("a")
    public String getAskPrice() {
        return askPrice;
    }

    @JsonProperty("a")
    public void setAskPrice(String askPrice) {
        this.askPrice = askPrice;
    }

//    public String getAskQty() {
//        return askQty;
//    }
//
//    public void setAskQty(String askQty) {
//        this.askQty = askQty;
//    }

    public String getOpenPrice() {
        return openPrice;
    }

    public void setOpenPrice(String openPrice) {
        this.openPrice = openPrice;
    }

    public String getHighPrice() {
        return highPrice;
    }

    public void setHighPrice(String highPrice) {
        this.highPrice = highPrice;
    }

    public String getLowPrice() {
        return lowPrice;
    }

    public void setLowPrice(String lowPrice) {
        this.lowPrice = lowPrice;
    }

//    public String getVolume() {
//        return volume;
//    }
//
//    public void setVolume(String volume) {
//        this.volume = volume;
//    }
//
//    public String getQuoteVolume() {
//        return quoteVolume;
//    }
//
//    public void setQuoteVolume(String quoteVolume) {
//        this.quoteVolume = quoteVolume;
//    }
//
//    public Long getOpenTime() {
//        return openTime;
//    }
//
//    public void setOpenTime(Long openTime) {
//        this.openTime = openTime;
//    }
//
//    public Long getCloseTime() {
//        return closeTime;
//    }
//
//    public void setCloseTime(Long closeTime) {
//        this.closeTime = closeTime;
//    }
//
//    public Long getFirstId() {
//        return firstId;
//    }
//
//    public void setFirstId(Long firstId) {
//        this.firstId = firstId;
//    }
//
//    public Long getLastId() {
//        return lastId;
//    }
//
//    public void setLastId(Long lastId) {
//        this.lastId = lastId;
//    }
//
//    public Long getCount() {
//        return count;
//    }
//
//    public void setCount(Long count) {
//        this.count = count;
//    }
}
