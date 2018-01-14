
package crypto.soft.cryptongy.feature.shared.json.binance.marketsummary;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "symbol",
    "priceChange",
    "priceChangePercent",
    "weightedAvgPrice",
    "prevClosePrice",
    "lastPrice",
    "lastQty",
    "bidPrice",
    "bidQty",
    "askPrice",
    "askQty",
    "openPrice",
    "highPrice",
    "lowPrice",
    "volume",
    "quoteVolume",
    "openTime",
    "closeTime",
    "firstId",
    "lastId",
    "count"
})
public class Result {

    @JsonProperty("symbol")
    private String symbol;
    @JsonProperty("priceChange")
    private String priceChange;
    @JsonProperty("priceChangePercent")
    private String priceChangePercent;
    @JsonProperty("weightedAvgPrice")
    private String weightedAvgPrice;
    @JsonProperty("prevClosePrice")
    private String prevClosePrice;
    @JsonProperty("lastPrice")
    private String lastPrice;
    @JsonProperty("lastQty")
    private String lastQty;
    @JsonProperty("bidPrice")
    private String bidPrice;
    @JsonProperty("bidQty")
    private String bidQty;
    @JsonProperty("askPrice")
    private String askPrice;
    @JsonProperty("askQty")
    private String askQty;
    @JsonProperty("openPrice")
    private String openPrice;
    @JsonProperty("highPrice")
    private String highPrice;
    @JsonProperty("lowPrice")
    private String lowPrice;
    @JsonProperty("volume")
    private String volume;
    @JsonProperty("quoteVolume")
    private String quoteVolume;
    @JsonProperty("openTime")
    private Integer openTime;
    @JsonProperty("closeTime")
    private Integer closeTime;
    @JsonProperty("firstId")
    private Integer firstId;
    @JsonProperty("lastId")
    private Integer lastId;
    @JsonProperty("count")
    private Integer count;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("symbol")
    public String getSymbol() {
        return symbol;
    }

    @JsonProperty("symbol")
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    @JsonProperty("priceChange")
    public String getPriceChange() {
        return priceChange;
    }

    @JsonProperty("priceChange")
    public void setPriceChange(String priceChange) {
        this.priceChange = priceChange;
    }

    @JsonProperty("priceChangePercent")
    public String getPriceChangePercent() {
        return priceChangePercent;
    }

    @JsonProperty("priceChangePercent")
    public void setPriceChangePercent(String priceChangePercent) {
        this.priceChangePercent = priceChangePercent;
    }

    @JsonProperty("weightedAvgPrice")
    public String getWeightedAvgPrice() {
        return weightedAvgPrice;
    }

    @JsonProperty("weightedAvgPrice")
    public void setWeightedAvgPrice(String weightedAvgPrice) {
        this.weightedAvgPrice = weightedAvgPrice;
    }

    @JsonProperty("prevClosePrice")
    public String getPrevClosePrice() {
        return prevClosePrice;
    }

    @JsonProperty("prevClosePrice")
    public void setPrevClosePrice(String prevClosePrice) {
        this.prevClosePrice = prevClosePrice;
    }

    @JsonProperty("lastPrice")
    public String getLastPrice() {
        return lastPrice;
    }

    @JsonProperty("lastPrice")
    public void setLastPrice(String lastPrice) {
        this.lastPrice = lastPrice;
    }

    @JsonProperty("lastQty")
    public String getLastQty() {
        return lastQty;
    }

    @JsonProperty("lastQty")
    public void setLastQty(String lastQty) {
        this.lastQty = lastQty;
    }

    @JsonProperty("bidPrice")
    public String getBidPrice() {
        return bidPrice;
    }

    @JsonProperty("bidPrice")
    public void setBidPrice(String bidPrice) {
        this.bidPrice = bidPrice;
    }

    @JsonProperty("bidQty")
    public String getBidQty() {
        return bidQty;
    }

    @JsonProperty("bidQty")
    public void setBidQty(String bidQty) {
        this.bidQty = bidQty;
    }

    @JsonProperty("askPrice")
    public String getAskPrice() {
        return askPrice;
    }

    @JsonProperty("askPrice")
    public void setAskPrice(String askPrice) {
        this.askPrice = askPrice;
    }

    @JsonProperty("askQty")
    public String getAskQty() {
        return askQty;
    }

    @JsonProperty("askQty")
    public void setAskQty(String askQty) {
        this.askQty = askQty;
    }

    @JsonProperty("openPrice")
    public String getOpenPrice() {
        return openPrice;
    }

    @JsonProperty("openPrice")
    public void setOpenPrice(String openPrice) {
        this.openPrice = openPrice;
    }

    @JsonProperty("highPrice")
    public String getHighPrice() {
        return highPrice;
    }

    @JsonProperty("highPrice")
    public void setHighPrice(String highPrice) {
        this.highPrice = highPrice;
    }

    @JsonProperty("lowPrice")
    public String getLowPrice() {
        return lowPrice;
    }

    @JsonProperty("lowPrice")
    public void setLowPrice(String lowPrice) {
        this.lowPrice = lowPrice;
    }

    @JsonProperty("volume")
    public String getVolume() {
        return volume;
    }

    @JsonProperty("volume")
    public void setVolume(String volume) {
        this.volume = volume;
    }

    @JsonProperty("quoteVolume")
    public String getQuoteVolume() {
        return quoteVolume;
    }

    @JsonProperty("quoteVolume")
    public void setQuoteVolume(String quoteVolume) {
        this.quoteVolume = quoteVolume;
    }

    @JsonProperty("openTime")
    public Integer getOpenTime() {
        return openTime;
    }

    @JsonProperty("openTime")
    public void setOpenTime(Integer openTime) {
        this.openTime = openTime;
    }

    @JsonProperty("closeTime")
    public Integer getCloseTime() {
        return closeTime;
    }

    @JsonProperty("closeTime")
    public void setCloseTime(Integer closeTime) {
        this.closeTime = closeTime;
    }

    @JsonProperty("firstId")
    public Integer getFirstId() {
        return firstId;
    }

    @JsonProperty("firstId")
    public void setFirstId(Integer firstId) {
        this.firstId = firstId;
    }

    @JsonProperty("lastId")
    public Integer getLastId() {
        return lastId;
    }

    @JsonProperty("lastId")
    public void setLastId(Integer lastId) {
        this.lastId = lastId;
    }

    @JsonProperty("count")
    public Integer getCount() {
        return count;
    }

    @JsonProperty("count")
    public void setCount(Integer count) {
        this.count = count;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
