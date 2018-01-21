
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
    "transactTime",
    "code",
    "msg"
})
public class BnNewOrder {

    @JsonProperty("symbol")
    private String symbol;
    @JsonProperty("orderId")
    private Long orderId;
    @JsonProperty("clientOrderId")
    private String clientOrderId;
    @JsonProperty("transactTime")
    private Long transactTime;
    @JsonProperty("code")
    private Long code;
    @JsonProperty("msg")
    private String msg;
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

    @JsonProperty("transactTime")
    public Long getTransactTime() {
        return transactTime;
    }

    @JsonProperty("transactTime")
    public void setTransactTime(Long transactTime) {
        this.transactTime = transactTime;
    }

    @JsonProperty("code")
    public Long getCode() {
        return code;
    }

    @JsonProperty("code")
    public void setCode(Long code) {
        this.code = code;
    }

    @JsonProperty("msg")
    public String getMsg() {
        return msg;
    }

    @JsonProperty("msg")
    public void setMsg(String msg) {
        this.msg = msg;
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
