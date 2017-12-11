package crypto.soft.cryptongy.feature.shared.listner;

/**
 * Created by tseringwongelgurung on 12/11/17.
 */

public interface OnFinishMultliListner<S, F> extends OnFinishListner<S> {
    void onFail(F error);
}
