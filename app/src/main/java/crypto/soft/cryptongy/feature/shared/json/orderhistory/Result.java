package crypto.soft.cryptongy.feature.shared.json.orderhistory;

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
        "OrderUuid",
        "Exchange",
        "TimeStamp",
        "OrderType",
        "Limit",
        "Quantity",
        "QuantityRemaining",
        "Commission",
        "Price",
        "PricePerUnit",
        "Opened",
        "IsConditional",
        "Condition",
        "ConditionTarget",
        "ImmediateOrCancel"
})
public class Result {

    @JsonProperty("OrderUuid")
    private String orderUuid;
    @JsonProperty("Exchange")
    private String exchange;
    @JsonProperty("TimeStamp")
    private String timeStamp;
    @JsonProperty("OrderType")
    private String orderType;
    @JsonProperty("Limit")
    private Double limit;
    @JsonProperty("Quantity")
    private Double quantity;
    @JsonProperty("QuantityRemaining")
    private Double quantityRemaining;
    @JsonProperty("Commission")
    private Double commission;
    @JsonProperty("Price")
    private Double price;
    @JsonProperty("PricePerUnit")
    private Double pricePerUnit;
    @JsonProperty("Opened")
    private String opened;
    @JsonProperty("IsConditional")
    private Boolean isConditional;
    @JsonProperty("Condition")
    private Object condition;
    @JsonProperty("ConditionTarget")
    private Object conditionTarget;
    @JsonProperty("ImmediateOrCancel")
    private Boolean immediateOrCancel;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("OrderUuid")
    public String getOrderUuid() {
        return orderUuid;
    }

    @JsonProperty("OrderUuid")
    public void setOrderUuid(String orderUuid) {
        this.orderUuid = orderUuid;
    }

    @JsonProperty("Exchange")
    public String getExchange() {
        return exchange;
    }

    @JsonProperty("Exchange")
    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    @JsonProperty("TimeStamp")
    public String getTimeStamp() {
        return timeStamp;
    }

    @JsonProperty("TimeStamp")
    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    @JsonProperty("OrderType")
    public String getOrderType() {
        return orderType;
    }

    @JsonProperty("OrderType")
    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    @JsonProperty("Limit")
    public Double getLimit() {
        return limit;
    }

    @JsonProperty("Limit")
    public void setLimit(Double limit) {
        this.limit = limit;
    }

    @JsonProperty("Quantity")
    public Double getQuantity() {
        return quantity;
    }

    @JsonProperty("Quantity")
    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    @JsonProperty("QuantityRemaining")
    public Double getQuantityRemaining() {
        return quantityRemaining;
    }

    @JsonProperty("QuantityRemaining")
    public void setQuantityRemaining(Double quantityRemaining) {
        this.quantityRemaining = quantityRemaining;
    }

    @JsonProperty("Commission")
    public Double getCommission() {
        return commission;
    }

    @JsonProperty("Commission")
    public void setCommission(Double commission) {
        this.commission = commission;
    }

    @JsonProperty("Price")
    public Double getPrice() {
        return price;
    }

    @JsonProperty("Price")
    public void setPrice(Double price) {
        this.price = price;
    }

    @JsonProperty("PricePerUnit")
    public Double getPricePerUnit() {
        return pricePerUnit;
    }

    @JsonProperty("PricePerUnit")
    public void setPricePerUnit(Double pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }

    @JsonProperty("Opened")
    public String getOpened() {
        return opened;
    }

    @JsonProperty("Opened")
    public void setOpened(String opened) {
        this.opened = opened;
    }

    @JsonProperty("IsConditional")
    public Boolean getIsConditional() {
        return isConditional;
    }

    @JsonProperty("IsConditional")
    public void setIsConditional(Boolean isConditional) {
        this.isConditional = isConditional;
    }

    @JsonProperty("Condition")
    public Object getCondition() {
        return condition;
    }

    @JsonProperty("Condition")
    public void setCondition(Object condition) {
        this.condition = condition;
    }

    @JsonProperty("ConditionTarget")
    public Object getConditionTarget() {
        return conditionTarget;
    }

    @JsonProperty("ConditionTarget")
    public void setConditionTarget(Object conditionTarget) {
        this.conditionTarget = conditionTarget;
    }

    @JsonProperty("ImmediateOrCancel")
    public Boolean getImmediateOrCancel() {
        return immediateOrCancel;
    }

    @JsonProperty("ImmediateOrCancel")
    public void setImmediateOrCancel(Boolean immediateOrCancel) {
        this.immediateOrCancel = immediateOrCancel;
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