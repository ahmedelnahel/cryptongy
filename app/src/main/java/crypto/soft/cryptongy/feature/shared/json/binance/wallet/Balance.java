
package crypto.soft.cryptongy.feature.shared.json.binance.wallet;

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
    "asset",
    "free",
    "locked"
})
public class Balance {

    @JsonProperty("asset")
    private String asset;
    @JsonProperty("free")
    private String free;
    @JsonProperty("locked")
    private String locked;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("asset")
    public String getAsset() {
        return asset;
    }

    @JsonProperty("asset")
    public void setAsset(String asset) {
        this.asset = asset;
    }

    @JsonProperty("free")
    public String getFree() {
        return free;
    }

    @JsonProperty("free")
    public void setFree(String free) {
        this.free = free;
    }

    @JsonProperty("locked")
    public String getLocked() {
        return locked;
    }

    @JsonProperty("locked")
    public void setLocked(String locked) {
        this.locked = locked;
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
