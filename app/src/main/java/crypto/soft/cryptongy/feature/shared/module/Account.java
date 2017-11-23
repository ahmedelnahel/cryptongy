package crypto.soft.cryptongy.feature.shared.module;


import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by tseringwongelgurung on 11/20/17.
 */

public class Account extends RealmObject {
    @PrimaryKey
    private Integer id;
    private String exchange;
    private String label;
    private String apiKey;
    private String secret;

    public Account() {

    }

    public Account(String exchange, String label, String apiKey, String secret) {
        this.exchange = exchange;
        this.label = label;
        this.apiKey = apiKey;
        this.secret = secret;
    }

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }
}
