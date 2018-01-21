
package crypto.soft.cryptongy.feature.shared.json.order;

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
    "AccountId",
    "OrderUuid",
    "Exchange",
    "Type",
    "Quantity",
    "QuantityRemaining",
    "Limit",
    "Reserved",
    "ReserveRemaining",
    "CommissionReserved",
    "CommissionReserveRemaining",
    "CommissionPaid",
    "Price",
    "PricePerUnit",
    "Opened",
    "Closed",
    "IsOpen",
    "Sentinel",
    "CancelInitiated",
    "ImmediateOrCancel",
    "IsConditional",
    "Condition",
    "ConditionTarget"
})
public class Result {

    public Result(crypto.soft.cryptongy.feature.shared.json.binance.order.Result r) {
        this.orderUuid = r.getOrderId().toString();
        this.exchange = r.getSymbol();
        this.type = r.getSide();
        this.quantity = Double.valueOf(r.getOrigQty());
        this.limit = Double.valueOf(r.getPrice());
        this.quantityRemaining = Double.valueOf(r.getOrigQty()) - Double.valueOf(r.getExecutedQty());
        this.price = Double.valueOf(r.getOrigQty())* Double.valueOf(r.getExecutedQty());
        this.cancelInitiated = r.getStatus().equals("CANCELED");
        this.isOpen = r.getStatus().equals("NEW");
    }

    @JsonProperty("AccountId")
    private Object accountId;
    @JsonProperty("OrderUuid")
    private String orderUuid;
    @JsonProperty("Exchange")
    private String exchange;
    @JsonProperty("Type")
    private String type;
    @JsonProperty("Quantity")
    private Double quantity;
    @JsonProperty("QuantityRemaining")
    private Double quantityRemaining;
    @JsonProperty("Limit")
    private Double limit;
    @JsonProperty("Reserved")
    private Double reserved;
    @JsonProperty("ReserveRemaining")
    private Double reserveRemaining;
    @JsonProperty("CommissionReserved")
    private Double commissionReserved;
    @JsonProperty("CommissionReserveRemaining")
    private Double commissionReserveRemaining;
    @JsonProperty("CommissionPaid")
    private Double commissionPaid;
    @JsonProperty("Price")
    private Double price;
    @JsonProperty("PricePerUnit")
    private Object pricePerUnit;
    @JsonProperty("Opened")
    private String opened;
    @JsonProperty("Closed")
    private Object closed;
    @JsonProperty("IsOpen")
    private Boolean isOpen;
    @JsonProperty("Sentinel")
    private String sentinel;
    @JsonProperty("CancelInitiated")
    private Boolean cancelInitiated;
    @JsonProperty("ImmediateOrCancel")
    private Boolean immediateOrCancel;
    @JsonProperty("IsConditional")
    private Boolean isConditional;
    @JsonProperty("Condition")
    private String condition;
    @JsonProperty("ConditionTarget")
    private Object conditionTarget;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("AccountId")
    public Object getAccountId() {
        return accountId;
    }

    @JsonProperty("AccountId")
    public void setAccountId(Object accountId) {
        this.accountId = accountId;
    }

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

    @JsonProperty("Type")
    public String getType() {
        return type;
    }

    @JsonProperty("Type")
    public void setType(String type) {
        this.type = type;
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

    @JsonProperty("Limit")
    public Double getLimit() {
        return limit;
    }

    @JsonProperty("Limit")
    public void setLimit(Double limit) {
        this.limit = limit;
    }

    @JsonProperty("Reserved")
    public Double getReserved() {
        return reserved;
    }

    @JsonProperty("Reserved")
    public void setReserved(Double reserved) {
        this.reserved = reserved;
    }

    @JsonProperty("ReserveRemaining")
    public Double getReserveRemaining() {
        return reserveRemaining;
    }

    @JsonProperty("ReserveRemaining")
    public void setReserveRemaining(Double reserveRemaining) {
        this.reserveRemaining = reserveRemaining;
    }

    @JsonProperty("CommissionReserved")
    public Double getCommissionReserved() {
        return commissionReserved;
    }

    @JsonProperty("CommissionReserved")
    public void setCommissionReserved(Double commissionReserved) {
        this.commissionReserved = commissionReserved;
    }

    @JsonProperty("CommissionReserveRemaining")
    public Double getCommissionReserveRemaining() {
        return commissionReserveRemaining;
    }

    @JsonProperty("CommissionReserveRemaining")
    public void setCommissionReserveRemaining(Double commissionReserveRemaining) {
        this.commissionReserveRemaining = commissionReserveRemaining;
    }

    @JsonProperty("CommissionPaid")
    public Double getCommissionPaid() {
        return commissionPaid;
    }

    @JsonProperty("CommissionPaid")
    public void setCommissionPaid(Double commissionPaid) {
        this.commissionPaid = commissionPaid;
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
    public Object getPricePerUnit() {
        return pricePerUnit;
    }

    @JsonProperty("PricePerUnit")
    public void setPricePerUnit(Object pricePerUnit) {
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

    @JsonProperty("Closed")
    public Object getClosed() {
        return closed;
    }

    @JsonProperty("Closed")
    public void setClosed(Object closed) {
        this.closed = closed;
    }

    @JsonProperty("IsOpen")
    public Boolean getIsOpen() {
        return isOpen;
    }

    @JsonProperty("IsOpen")
    public void setIsOpen(Boolean isOpen) {
        this.isOpen = isOpen;
    }

    @JsonProperty("Sentinel")
    public String getSentinel() {
        return sentinel;
    }

    @JsonProperty("Sentinel")
    public void setSentinel(String sentinel) {
        this.sentinel = sentinel;
    }

    @JsonProperty("CancelInitiated")
    public Boolean getCancelInitiated() {
        return cancelInitiated;
    }

    @JsonProperty("CancelInitiated")
    public void setCancelInitiated(Boolean cancelInitiated) {
        this.cancelInitiated = cancelInitiated;
    }

    @JsonProperty("ImmediateOrCancel")
    public Boolean getImmediateOrCancel() {
        return immediateOrCancel;
    }

    @JsonProperty("ImmediateOrCancel")
    public void setImmediateOrCancel(Boolean immediateOrCancel) {
        this.immediateOrCancel = immediateOrCancel;
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
    public String getCondition() {
        return condition;
    }

    @JsonProperty("Condition")
    public void setCondition(String condition) {
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

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
