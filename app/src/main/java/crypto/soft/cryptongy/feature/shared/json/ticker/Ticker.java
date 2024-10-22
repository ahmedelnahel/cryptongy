
package crypto.soft.cryptongy.feature.shared.json.ticker;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.HashMap;
import java.util.Map;

import crypto.soft.cryptongy.feature.coin.BnSocketOrders;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "success",
    "message",
    "result"
})
public class Ticker {

    @JsonProperty("success")
    private Boolean success;
    @JsonProperty("message")
    private String message;
    @JsonProperty("result")
    private Result result;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    private String json;

    public String getJson() {
        return json;
    }


    public void setBinanceResult(crypto.soft.cryptongy.feature.shared.json.binance.marketsummary.Result r)
    {
     this.result = new Result();
     result.setLast(r.getLastPrice()==null?0.0:Double.valueOf(r.getLastPrice()));
     result.setAsk(r.getAskPrice()==null?0.0:Double.valueOf(r.getAskPrice()));
     result.setBid(r.getBidPrice()==null?0.0:Double.valueOf(r.getBidPrice()));
    }

    public void setBinanceResultWebSocket(BnSocketOrders r)
    {
        this.result = new Result();
        result.setLast(r.getLastPrice()==null?0.0:Double.valueOf(r.getLastPrice()));
        result.setAsk(r.getAskPrice()==null?0.0:Double.valueOf(r.getAskPrice()));
        result.setBid(r.getBidPrice()==null?0.0:Double.valueOf(r.getBidPrice()));
    }
    public void setJson(String json) {
        this.json = json;
    }
    @JsonProperty("success")
    public Boolean getSuccess() {
        return success;
    }

    @JsonProperty("success")
    public void setSuccess(Boolean success) {
        this.success = success;
    }

    @JsonProperty("message")
    public String getMessage() {
        return message;
    }

    @JsonProperty("message")
    public void setMessage(String message) {
        this.message = message;
    }

    @JsonProperty("result")
    public Result getResult() {
        return result;
    }

    @JsonProperty("result")
    public void setResult(Result result) {
        this.result = result;
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
