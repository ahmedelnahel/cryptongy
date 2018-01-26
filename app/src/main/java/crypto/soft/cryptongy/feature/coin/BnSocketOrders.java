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

    @JsonProperty("p")
    public String getPriceChange() {
        return priceChange;
    }

    @JsonProperty("p")
    public void setPriceChange(String priceChange) {
        this.priceChange = priceChange;
    }

    @JsonProperty("P")
    public String getPriceChangePercent() {
        return priceChangePercent;
    }

    @JsonProperty("P")
    public void setPriceChangePercent(String priceChangePercent) {
        this.priceChangePercent = priceChangePercent;
    }

    @JsonProperty("w")
    public String getWeightedAvgPrice() {
        return weightedAvgPrice;
    }

    @JsonProperty("w")
    public void setWeightedAvgPrice(String weightedAvgPrice) {
        this.weightedAvgPrice = weightedAvgPrice;
    }

    @JsonProperty("x")
    public String getPrevClosePrice() {
        return prevClosePrice;
    }

    @JsonProperty("x")
    public void setPrevClosePrice(String prevClosePrice) {
        this.prevClosePrice = prevClosePrice;
    }

    @JsonProperty("c")
    public String getLastPrice() {
        return lastPrice;
    }

    @JsonProperty("c")
    public void setLastPrice(String lastPrice) {
        this.lastPrice = lastPrice;
    }

    @JsonProperty("Q")
    public String getLastQty() {
        return lastQty;
    }

    @JsonProperty("Q")
    public void setLastQty(String lastQty) {
        this.lastQty = lastQty;
    }

    @JsonProperty("b")
    public String getBidPrice() {
        return bidPrice;
    }

    @JsonProperty("b")
    public void setBidPrice(String bidPrice) {
        this.bidPrice = bidPrice;
    }

    @JsonProperty("B")
    public String getBidQty() {
        return bidQty;
    }

    @JsonProperty("B")
    public void setBidQty(String bidQty) {
        this.bidQty = bidQty;
    }

    @JsonProperty("a")
    public String getAskPrice() {
        return askPrice;
    }

    @JsonProperty("a")
    public void setAskPrice(String askPrice) {
        this.askPrice = askPrice;
    }

    @JsonProperty("A")
    public String getAskQty() {
        return askQty;
    }

    @JsonProperty("A")
    public void setAskQty(String askQty) {
        this.askQty = askQty;
    }

    @JsonProperty("o")
    public String getOpenPrice() {
        return openPrice;
    }

    @JsonProperty("o")
    public void setOpenPrice(String openPrice) {
        this.openPrice = openPrice;
    }

    @JsonProperty("h")
    public String getHighPrice() {
        return highPrice;
    }

    @JsonProperty("h")
    public void setHighPrice(String highPrice) {
        this.highPrice = highPrice;
    }

    @JsonProperty("l")
    public String getLowPrice() {
        return lowPrice;
    }

    @JsonProperty("l")
    public void setLowPrice(String lowPrice) {
        this.lowPrice = lowPrice;
    }

    @JsonProperty("v")
    public String getVolume() {
        return volume;
    }

    @JsonProperty("v")
    public void setVolume(String volume) {
        this.volume = volume;
    }

    @JsonProperty("q")
    public String getQuoteVolume() {
        return quoteVolume;
    }

    @JsonProperty("q")
    public void setQuoteVolume(String quoteVolume) {
        this.quoteVolume = quoteVolume;
    }

    @JsonProperty("O")
    public Long getOpenTime() {
        return openTime;
    }

    @JsonProperty("O")
    public void setOpenTime(Long openTime) {
        this.openTime = openTime;
    }

    @JsonProperty("C")
    public Long getCloseTime() {
        return closeTime;
    }

    @JsonProperty("C")
    public void setCloseTime(Long closeTime) {
        this.closeTime = closeTime;
    }

    @JsonProperty("F")
    public Long getFirstId() {
        return firstId;
    }

    @JsonProperty("F")
    public void setFirstId(Long firstId) {
        this.firstId = firstId;
    }

    @JsonProperty("L")
    public Long getLastId() {
        return lastId;
    }

    @JsonProperty("L")
    public void setLastId(Long lastId) {
        this.lastId = lastId;
    }

    @JsonProperty("n")
    public Long getCount() {
        return count;
    }


    @JsonProperty("n")
    public void setCount(Long count) {
        this.count = count;
    }
}
