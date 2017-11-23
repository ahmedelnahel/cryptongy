package crypto.soft.cryptongy.feature.order;

import android.os.AsyncTask;

import java.io.IOException;

import crypto.soft.cryptongy.feature.shared.json.openorder.OpenOrder;
import crypto.soft.cryptongy.feature.shared.listner.OnFinishListner;
import crypto.soft.cryptongy.network.BittrexServices;

/**
 * Created by tseringwongelgurung on 11/24/17.
 */

public class OrderInteractor {
    void getOpenOrder(final OnFinishListner<OpenOrder> listner) {
        new AsyncTask<Void, Void, OpenOrder>() {

            @Override
            protected OpenOrder doInBackground(Void... voids) {
                try {
                    return new BittrexServices().getOpnOrdersMock();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(OpenOrder openOrder) {
                super.onPostExecute(openOrder);
                listner.onComplete(openOrder);
            }
        }.execute();
    }
}
