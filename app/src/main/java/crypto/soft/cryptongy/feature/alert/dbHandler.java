package crypto.soft.cryptongy.feature.alert;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by maiAjam on 11/23/2017.
 */

public class dbHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "coin_DATABASE";


    // genral table
    private static final String Tabel_Task = "coinInfo";

    private static final String Key_coin_ID = "coin_ID";
    private static final String Key_coinName = "coinName";
    private static final String Key_exchamgeName = "exchamgeName";
    private static final String Key_highValue = "highValue";
    private static final String Key_LowValue = "LowValue";


    public dbHandler(Context context) {
        super(context,DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_Tasks_TABLE = "CREATE TABLE " + Tabel_Task + "("
                + Key_coin_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                + Key_coinName + " TEXT,"
                + Key_exchamgeName + " TEXT,"
                + Key_highValue + " INTEGER,"
                + Key_LowValue + " INTEGER )";


        db.execSQL(CREATE_Tasks_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + Tabel_Task);
    }

    public void AddCoinInfo(CoinInfo coinInfo) {


        SQLiteDatabase db = this.getWritableDatabase();

        String name = coinInfo.getCoinName();

        ContentValues values = new ContentValues();
        values.put(Key_coinName, coinInfo.getCoinName());
        values.put(Key_exchamgeName, coinInfo.getExchangeName());
        values.put(Key_highValue, coinInfo.getHighValue());
        values.put(Key_LowValue, coinInfo.getLowValue());

        db.insert(Tabel_Task, null, values);
        db.close();
    }


    public CoinInfo getCoinInfo(String key_coinName) {
        String selectQ = "SELECT * FROM coinInfo WHERE coinName = '" + key_coinName + "';";
        ;
        SQLiteDatabase db = this.getReadableDatabase();
        CoinInfo coinInfo = new CoinInfo();
        Cursor cursor = db.rawQuery(selectQ, null);

        if (cursor.moveToFirst()) {

            coinInfo.setCoinName(cursor.getString(1));
            coinInfo.setExchangeName(cursor.getString(2));
            coinInfo.setHighValue(Double.valueOf(cursor.getString(3)));
            coinInfo.setLowValue(Double.valueOf(cursor.getString(4)));

        }


        return coinInfo;


    }


    public List<CoinInfo> getAllCoinInfo() {
        String selectQ = "SELECT * FROM coinInfo;";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQ, null);

        List<CoinInfo> coinInfoList = new ArrayList<>();

        if(cursor.getCount() != 0){
            cursor.moveToFirst();

            do{
                String row_values = "";
                CoinInfo coinInfo = new CoinInfo();

                coinInfo.setCoinName(cursor.getString(1));
                coinInfo.setExchangeName(cursor.getString(2));
                coinInfo.setHighValue(Double.valueOf(cursor.getString(3)));
                coinInfo.setLowValue(Double.valueOf(cursor.getString(4)));

                coinInfoList.add(coinInfo);

                for(int i = 0 ; i < cursor.getColumnCount(); i++){
                    row_values = row_values + " || " + cursor.getString(i);
                }

                Log.d("LOG_TAG_HERE", row_values);

            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return coinInfoList;
    }

    public void updateCoinInfo(CoinInfo coinInfo) {

        SQLiteDatabase db = this.getWritableDatabase();

        int taskType = coinInfo.getId();

            // task time

            String addQuery = "UPDATE coinInfo SET highValue = '" + coinInfo.getHighValue() +
                    "', LowValue = '" + coinInfo.lowValue  +
                    "' WHERE coinName = '" + coinInfo.getCoinName() + "';";
            Log.d(TAG, "updatemedcine: " + addQuery);
            db.execSQL(addQuery);
    }

    public void deleteCoin(String coinName) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from coinInfo where coinName = '" + coinName + "';");
    }
}


