package crypto.soft.cryptongy.feature.shared.listner;

/**
 * Created by tseringwongelgurung on 11/20/17.
 */

public interface OnFinishListner<T> {
    void onComplete(T result);

    void onFail(String error);
}
