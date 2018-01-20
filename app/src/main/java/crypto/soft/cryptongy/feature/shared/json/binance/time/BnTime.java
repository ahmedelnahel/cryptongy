
package crypto.soft.cryptongy.feature.shared.json.binance.time;

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
    "serverTime",
    "code",
    "msg"
})
public class BnTime {

    @JsonProperty("serverTime")
    private Long serverTime;
    @JsonProperty("code")
    private Long code;
    @JsonProperty("msg")
    private String msg;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("serverTime")
    public Long getServerTime() {
        return serverTime;
    }

    @JsonProperty("serverTime")
    public void setServerTime(Long serverTime) {
        this.serverTime = serverTime;
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
