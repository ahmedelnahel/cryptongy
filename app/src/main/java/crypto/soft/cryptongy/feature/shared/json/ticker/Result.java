
package crypto.soft.cryptongy.feature.shared.json.ticker;

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
    "Bid",
    "Ask",
    "Last"
})
public class Result {

    @JsonProperty("Bid")
    private Double bid;
    @JsonProperty("Ask")
    private Double ask;
    @JsonProperty("Last")
    private Double last;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

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

    @JsonProperty("Last")
    public Double getLast() {
        return last;
    }

    @JsonProperty("Last")
    public void setLast(Double last) {
        this.last = last;
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
