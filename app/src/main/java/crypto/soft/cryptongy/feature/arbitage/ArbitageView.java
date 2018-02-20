package crypto.soft.cryptongy.feature.arbitage;

import com.hannesdorfmann.mosby.mvp.MvpView;

import java.util.List;

/**
 * Created by tseringwongelgurung on 11/27/17.
 */

public interface ArbitageView extends MvpView {
    void initRecycler();

    void setAdapter(List<AribitaryTableResult> results);

    void hideProgressBar();
    void showProgressBar();
    void setCoinInTable(List<AribitaryTableResult> marketSummaries);
    void setList(List<AribitaryTableResult> marketSummaries);


}