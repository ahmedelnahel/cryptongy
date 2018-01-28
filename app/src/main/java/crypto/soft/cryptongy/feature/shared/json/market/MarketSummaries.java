package crypto.soft.cryptongy.feature.shared.json.market;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MarketSummaries {
    private String json;

    private HashMap<String, Result> coinsMap;

    public HashMap<String, Result> getCoinsMap() {
        return coinsMap;
    }

    public void setCoinsMap(HashMap<String, Result> coinsMap) {
        this.coinsMap = coinsMap;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    private Boolean success;
    private String message;
    private List<Result> result = null;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Boolean getSuccess() {
        if (success == null)
            success = new Boolean(false);
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Result> getResult() {
        return result;
    }

    public void setResult(List<Result> result) {

        this.result = result;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
