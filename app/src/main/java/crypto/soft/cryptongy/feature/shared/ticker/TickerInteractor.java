package crypto.soft.cryptongy.feature.shared.ticker;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

import crypto.soft.cryptongy.feature.shared.json.ticker.Ticker;
import crypto.soft.cryptongy.feature.shared.listner.OnFinishListner;
import crypto.soft.cryptongy.network.BinanceServices;
import crypto.soft.cryptongy.network.BittrexServices;
import crypto.soft.cryptongy.utils.GlobalConstant;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

import static android.content.ContentValues.TAG;

/**
 * Created by tseringwongelgurung on 12/18/17.
 */

public class TickerInteractor {

    final BinanceServices binanceServices=new BinanceServices();
    Disposable disposable;

    public void getTicker(final String coinName, final String exchangeValue, final OnFinishListner<Ticker> listner) {


        new AsyncTask<Void, Void, Ticker>() {

            @Override
            protected Ticker doInBackground(Void... voids) {
                try {
                    if(exchangeValue.equalsIgnoreCase(GlobalConstant.Exchanges.BITTREX)){

                        return new BittrexServices().getTicker(coinName);
                    }
                    if(exchangeValue.equalsIgnoreCase(GlobalConstant.Exchanges.BINANCE)){

                        try {
                            binanceServices.getTickerConnectSocket(coinName);

                      disposable=      binanceServices.sourceTickerWebsocket.observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Ticker>() {
                                @Override
                                public void accept(Ticker ticker) throws Exception {

                                    disposable.dispose();
                                    closeWebSocket();
                                    Log.d(TAG, "this is ticker  " + ticker);

                                  listner.onComplete(ticker);

                                }
                            });

                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Ticker ticker) {
                super.onPostExecute(ticker);
                if (ticker == null)
                    listner.onFail("Failed to fetch data");
                else if (ticker.getSuccess().booleanValue())
                    listner.onComplete(ticker);
                else
                    listner.onFail(ticker.getMessage());
            }
        }.execute();
    }



    public void closeWebSocket(){


        binanceServices.closeWebSocket();

    }




}
