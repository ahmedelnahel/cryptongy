
package crypto.soft.cryptongy.feature.shared.json.binance.wallet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "makerCommission",
    "takerCommission",
    "buyerCommission",
    "sellerCommission",
    "canTrade",
    "canWithdraw",
    "canDeposit",
    "balances"
})
public class BnWallet {
    @JsonProperty("code")
    private Long code;
    @JsonProperty("msg")
    private String msg;
    @JsonProperty("makerCommission")
    private Integer makerCommission;
    @JsonProperty("takerCommission")
    private Integer takerCommission;
    @JsonProperty("buyerCommission")
    private Integer buyerCommission;
    @JsonProperty("sellerCommission")
    private Integer sellerCommission;
    @JsonProperty("canTrade")
    private Boolean canTrade;
    @JsonProperty("canWithdraw")
    private Boolean canWithdraw;
    @JsonProperty("canDeposit")
    private Boolean canDeposit;
    @JsonProperty("balances")
    private List<Balance> balances = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("makerCommission")
    public Integer getMakerCommission() {
        return makerCommission;
    }

    @JsonProperty("makerCommission")
    public void setMakerCommission(Integer makerCommission) {
        this.makerCommission = makerCommission;
    }

    @JsonProperty("takerCommission")
    public Integer getTakerCommission() {
        return takerCommission;
    }

    @JsonProperty("takerCommission")
    public void setTakerCommission(Integer takerCommission) {
        this.takerCommission = takerCommission;
    }

    @JsonProperty("buyerCommission")
    public Integer getBuyerCommission() {
        return buyerCommission;
    }

    @JsonProperty("buyerCommission")
    public void setBuyerCommission(Integer buyerCommission) {
        this.buyerCommission = buyerCommission;
    }

    @JsonProperty("sellerCommission")
    public Integer getSellerCommission() {
        return sellerCommission;
    }

    @JsonProperty("sellerCommission")
    public void setSellerCommission(Integer sellerCommission) {
        this.sellerCommission = sellerCommission;
    }

    @JsonProperty("canTrade")
    public Boolean getCanTrade() {
        return canTrade;
    }

    @JsonProperty("canTrade")
    public void setCanTrade(Boolean canTrade) {
        this.canTrade = canTrade;
    }

    @JsonProperty("canWithdraw")
    public Boolean getCanWithdraw() {
        return canWithdraw;
    }

    @JsonProperty("canWithdraw")
    public void setCanWithdraw(Boolean canWithdraw) {
        this.canWithdraw = canWithdraw;
    }

    @JsonProperty("canDeposit")
    public Boolean getCanDeposit() {
        return canDeposit;
    }

    @JsonProperty("canDeposit")
    public void setCanDeposit(Boolean canDeposit) {
        this.canDeposit = canDeposit;
    }

    @JsonProperty("balances")
    public List<Balance> getBalances() {
        return balances;
    }

    @JsonProperty("balances")
    public void setBalances(List<Balance> balances) {
        this.balances = balances;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
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
}
