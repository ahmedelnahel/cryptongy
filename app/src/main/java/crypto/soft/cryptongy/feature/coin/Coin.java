package crypto.soft.cryptongy.feature.coin;

import crypto.soft.cryptongy.feature.shared.json.market.Result;

/**
 * Created by tseringwongelgurung on 12/19/17.
 */

public class Coin {
    private Result result;
    private int drawable;

    public Coin(Result result, int drawable) {
        this.result = result;
        this.drawable = drawable;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public int getDrawable() {
        return drawable;
    }

    public void setDrawable(int drawable) {
        this.drawable = drawable;
    }
}
