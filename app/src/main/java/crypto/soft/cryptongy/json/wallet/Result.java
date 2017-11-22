package crypto.soft.cryptongy.json.wallet;

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
        "Currency",
        "Balance",
        "Available",
        "Pending",
        "CryptoAddress",
        "Requested",
        "Uuid"
})
public class Result {

    @JsonProperty("Currency")
    private String currency;
    @JsonProperty("Balance")
    private Double balance;
    @JsonProperty("Available")
    private Double available;
    @JsonProperty("Pending")
    private Double pending;
    @JsonProperty("CryptoAddress")
    private String cryptoAddress;
    @JsonProperty("Requested")
    private Boolean requested;
    @JsonProperty("Uuid")
    private Object uuid;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("Currency")
    public String getCurrency() {
        return currency;
    }

    @JsonProperty("Currency")
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @JsonProperty("Balance")
    public Double getBalance() {
        return balance;
    }

    @JsonProperty("Balance")
    public void setBalance(Double balance) {
        this.balance = balance;
    }

    @JsonProperty("Available")
    public Double getAvailable() {
        return available;
    }

    @JsonProperty("Available")
    public void setAvailable(Double available) {
        this.available = available;
    }

    @JsonProperty("Pending")
    public Double getPending() {
        return pending;
    }

    @JsonProperty("Pending")
    public void setPending(Double pending) {
        this.pending = pending;
    }

    @JsonProperty("CryptoAddress")
    public String getCryptoAddress() {
        return cryptoAddress;
    }

    @JsonProperty("CryptoAddress")
    public void setCryptoAddress(String cryptoAddress) {
        this.cryptoAddress = cryptoAddress;
    }

    @JsonProperty("Requested")
    public Boolean getRequested() {
        return requested;
    }

    @JsonProperty("Requested")
    public void setRequested(Boolean requested) {
        this.requested = requested;
    }

    @JsonProperty("Uuid")
    public Object getUuid() {
        return uuid;
    }

    @JsonProperty("Uuid")
    public void setUuid(Object uuid) {
        this.uuid = uuid;
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