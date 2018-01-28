package crypto.soft.cryptongy.feature.shared.listner;

/**
 * Created by tseringwongelgurung on 11/27/17.
 */

public interface OnMultiFinishListner<U, V, X> {
    void onComplete(U u, V v, X x);

    void onFail(String error);
}
