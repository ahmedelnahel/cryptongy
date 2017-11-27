package crypto.soft.cryptongy.feature.shared.listner;

/**
 * Created by tseringwongelgurung on 11/27/17.
 */

public interface OnMultiFinishListner<U, V> {
    void onComplete(U u, V v);

    void onFail(String error);
}
