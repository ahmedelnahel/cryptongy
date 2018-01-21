
package crypto.soft.cryptongy.feature.shared.json.binance.order;

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
    "orderId",
    "clientOrderId",
    "price",
    "origQty",
    "executedQty",
    "status",
    "timeInForce",
    "type",
    "side",
    "stopPrice",
    "icebergQty",
    "time"
})

public class Result {

    @JsonProperty("symbol")
    private String symbol;
    @JsonProperty("orderId")
    private Long orderId;
    @JsonProperty("clientOrderId")
    private String clientOrderId;
    @JsonProperty("price")
    private String price;
    @JsonProperty("origQty")
    private String origQty;
    @JsonProperty("executedQty")
    private String executedQty;
    @JsonProperty("status")
    private String status;
    @JsonProperty("timeInForce")
    private String timeInForce;
    @JsonProperty("type")
    private String type;
    @JsonProperty("side")
    private String side;
    @JsonProperty("stopPrice")
    private String stopPrice;
    @JsonProperty("icebergQty")
    private String icebergQty;
    @JsonProperty("time")
    private Long time;
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

    @JsonProperty("orderId")
    public Long getOrderId() {
        return orderId;
    }

    @JsonProperty("orderId")
    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    @JsonProperty("clientOrderId")
    public String getClientOrderId() {
        return clientOrderId;
    }

    @JsonProperty("clientOrderId")
    public void setClientOrderId(String clientOrderId) {
        this.clientOrderId = clientOrderId;
    }

    @JsonProperty("price")
    public String getPrice() {
        return price;
    }

    @JsonProperty("price")
    public void setPrice(String price) {
        this.price = price;
    }

    @JsonProperty("origQty")
    public String getOrigQty() {
        return origQty;
    }

    @JsonProperty("origQty")
    public void setOrigQty(String origQty) {
        this.origQty = origQty;
    }

    @JsonProperty("executedQty")
    public String getExecutedQty() {
        return executedQty;
    }

    @JsonProperty("executedQty")
    public void setExecutedQty(String executedQty) {
        this.executedQty = executedQty;
    }

    @JsonProperty("status")
    public String getStatus() {
        return status;
    }

    @JsonProperty("status")
    public void setStatus(String status) {
        this.status = status;
    }

    @JsonProperty("timeInForce")
    public String getTimeInForce() {
        return timeInForce;
    }

    @JsonProperty("timeInForce")
    public void setTimeInForce(String timeInForce) {
        this.timeInForce = timeInForce;
    }

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }

    @JsonProperty("side")
    public String getSide() {
        return side;
    }

    @JsonProperty("side")
    public void setSide(String side) {
        this.side = side;
    }

    @JsonProperty("stopPrice")
    public String getStopPrice() {
        return stopPrice;
    }

    @JsonProperty("stopPrice")
    public void setStopPrice(String stopPrice) {
        this.stopPrice = stopPrice;
    }

    @JsonProperty("icebergQty")
    public String getIcebergQty() {
        return icebergQty;
    }

    @JsonProperty("icebergQty")
    public void setIcebergQty(String icebergQty) {
        this.icebergQty = icebergQty;
    }

    @JsonProperty("time")
    public Long getTime() {
        return time;
    }

    @JsonProperty("time")
    public void setTime(Long time) {
        this.time = time;
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
