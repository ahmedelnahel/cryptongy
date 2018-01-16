
package crypto.soft.cryptongy.feature.shared.json.marketsummary;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "MarketName",
    "High",
    "Low",
    "Volume",
    "Last",
    "BaseVolume",
    "TimeStamp",
    "Bid",
    "Ask",
    "OpenBuyOrders",
    "OpenSellOrders",
    "PrevDay",
    "Created",
    "DisplayMarketName"
})
public class Result {
    public Result() {
    }
    public Result(crypto.soft.cryptongy.feature.shared.json.binance.marketsummary.Result result) {
        this.marketName = result.getSymbol();
        this.high = result.getHighPrice()==null?0.0:Double.valueOf(result.getHighPrice());
        this.low = result.getLowPrice()==null?0.0:Double.valueOf(result.getLowPrice());
        this.volume = result.getVolume()==null?0.0:Double.valueOf(result.getVolume());
        this.last = result.getLastPrice()==null?0.0:Double.valueOf(result.getLastPrice());
        this.bid = result.getBidPrice()==null?0.0:Double.valueOf(result.getBidPrice());
        this.ask = result.getAskPrice()==null?0.0:Double.valueOf(result.getAskPrice());

    }

    @JsonProperty("MarketName")
    private String marketName;
    @JsonProperty("High")
    private Double high;
    @JsonProperty("Low")
    private Double low;
    @JsonProperty("Volume")
    private Double volume;
    @JsonProperty("Last")
    private Double last;
    @JsonProperty("BaseVolume")
    private Double baseVolume;
    @JsonProperty("TimeStamp")
    private String timeStamp;
    @JsonProperty("Bid")
    private Double bid;
    @JsonProperty("Ask")
    private Double ask;
    @JsonProperty("OpenBuyOrders")
    private Integer openBuyOrders;
    @JsonProperty("OpenSellOrders")
    private Integer openSellOrders;
    @JsonProperty("PrevDay")
    private Double prevDay;
    @JsonProperty("Created")
    private String created;
    @JsonProperty("DisplayMarketName")
    private Object displayMarketName;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("MarketName")
    public String getMarketName() {
        return marketName;
    }

    @JsonProperty("MarketName")
    public void setMarketName(String marketName) {
        this.marketName = marketName;
    }

    @JsonProperty("High")
    public Double getHigh() {
        return high;
    }

    @JsonProperty("High")
    public void setHigh(Double high) {
        this.high = high;
    }

    @JsonProperty("Low")
    public Double getLow() {
        return low;
    }

    @JsonProperty("Low")
    public void setLow(Double low) {
        this.low = low;
    }

    @JsonProperty("Volume")
    public Double getVolume() {
        return volume;
    }

    @JsonProperty("Volume")
    public void setVolume(Double volume) {
        this.volume = volume;
    }

    @JsonProperty("Last")
    public Double getLast() {
        return last;
    }

    @JsonProperty("Last")
    public void setLast(Double last) {
        this.last = last;
    }

    @JsonProperty("BaseVolume")
    public Double getBaseVolume() {
        return baseVolume;
    }

    @JsonProperty("BaseVolume")
    public void setBaseVolume(Double baseVolume) {
        this.baseVolume = baseVolume;
    }

    @JsonProperty("TimeStamp")
    public String getTimeStamp() {
        return timeStamp;
    }

    @JsonProperty("TimeStamp")
    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    @JsonProperty("Bid")
    public Double getBid() {
        return bid;
    }

    @JsonProperty("Bid")
    public void setBid(Double bid) {
        this.bid = bid;
    }

    @JsonProperty("Ask")
    public Double getAsk() {
        return ask;
    }

    @JsonProperty("Ask")
    public void setAsk(Double ask) {
        this.ask = ask;
    }

    @JsonProperty("OpenBuyOrders")
    public Integer getOpenBuyOrders() {
        return openBuyOrders;
    }

    @JsonProperty("OpenBuyOrders")
    public void setOpenBuyOrders(Integer openBuyOrders) {
        this.openBuyOrders = openBuyOrders;
    }

    @JsonProperty("OpenSellOrders")
    public Integer getOpenSellOrders() {
        return openSellOrders;
    }

    @JsonProperty("OpenSellOrders")
    public void setOpenSellOrders(Integer openSellOrders) {
        this.openSellOrders = openSellOrders;
    }

    @JsonProperty("PrevDay")
    public Double getPrevDay() {
        return prevDay;
    }

    @JsonProperty("PrevDay")
    public void setPrevDay(Double prevDay) {
        this.prevDay = prevDay;
    }

    @JsonProperty("Created")
    public String getCreated() {
        return created;
    }

    @JsonProperty("Created")
    public void setCreated(String created) {
        this.created = created;
    }

    @JsonProperty("DisplayMarketName")
    public Object getDisplayMarketName() {
        return displayMarketName;
    }

    @JsonProperty("DisplayMarketName")
    public void setDisplayMarketName(Object displayMarketName) {
        this.displayMarketName = displayMarketName;
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
