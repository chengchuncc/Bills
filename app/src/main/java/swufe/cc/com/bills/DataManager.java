package swufe.cc.com.bills;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.Date;
import java.util.List;

public class DataManager {

    private MySQLiteHelper mySQLiteHelper;
    private String TBNAME;

    public DataManager(Context context) {
        mySQLiteHelper = new MySQLiteHelper(context);
        TBNAME = MySQLiteHelper.TB_NAME;
    }

    public void add(DataItem dataItem){
        SQLiteDatabase db = mySQLiteHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("INOROUT", dataItem.getInOrOut());
        values.put("TYPE", dataItem.getType());
        values.put("TIME", dataItem.getTime());
        values.put("FEE", dataItem.getFee());
        values.put("REMARKS", dataItem.getRemarks());
        db.insert(TBNAME, null, values);
        db.close();
    }



    public List<DataItem> listAll(){
        List<DataItem> dataItemList = null;
        SQLiteDatabase db = mySQLiteHelper.getReadableDatabase();
        Cursor cursor = db.query(TBNAME, null, null, null, null, null, null);
        if(cursor!=null){
            dataItemList = new ArrayList<DataItem>();
            while(cursor.moveToNext()){
                DataItem item = new DataItem();
                item.setId(cursor.getInt(cursor.getColumnIndex("ID")));
                item.setInOrOut(cursor.getString(cursor.getColumnIndex("INOROUT")));
                item.setType(cursor.getString(cursor.getColumnIndex("TYPE")));
                item.setTime(cursor.getString(cursor.getColumnIndex("TIME")));
                item.setFee(cursor.getString(cursor.getColumnIndex("FEE")));
                item.setRemarks(cursor.getString(cursor.getColumnIndex("REMARKS")));
                dataItemList.add(item);
            }
            cursor.close();
        }
        db.close();
        return dataItemList;
    }

    public void delete(int positon) {
        SQLiteDatabase db = mySQLiteHelper.getWritableDatabase();
        Cursor cursor = db.query(TBNAME, null, null, null, null, null, null);
        cursor.moveToPosition(positon);
        int itemId = cursor.getInt(cursor.getColumnIndex("ID"));
        db.delete(TBNAME, "ID=?", new String[]{itemId + ""});
    }
    public float getIncome(){
        float income = 0;
        String inOrOut;
        String fee;
        SQLiteDatabase db = mySQLiteHelper.getReadableDatabase();
        Cursor cursor = db.query(TBNAME, null, null, null, null, null, null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            for (int i = 0; i < cursor.getCount(); i++) {

                inOrOut = cursor.getString(cursor.getColumnIndex("INOROUT"));
                fee = cursor.getString(cursor.getColumnIndex("FEE"));
                if (inOrOut.equals("收入") ) {
                    income += Float.parseFloat(fee);
                }
                cursor.moveToNext();
            }
            cursor.close();
        }
        db.close();
        return income;
    }

    public float getIncome(Date startDate, Date endDate){
        float income = 0;
        String time;
        String inOrOut;
        String fee;
        Date date=null;
        SQLiteDatabase db = mySQLiteHelper.getReadableDatabase();
        Cursor cursor = db.query(TBNAME, null, null, null, null, null, null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            for (int i = 0; i < cursor.getCount(); i++) {
                time = cursor.getString(cursor.getColumnIndex("TIME"));
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    date = dateFormat.parse(time);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Log.i("这里", "getIncome: "+date+","+date.getTime());
                inOrOut = cursor.getString(cursor.getColumnIndex("INOROUT"));
                fee = cursor.getString(cursor.getColumnIndex("FEE"));
                if (inOrOut.equals("收入") && date.getTime()>=startDate.getTime() && date.getTime()<=endDate.getTime()) {
                    income += Float.parseFloat(fee);
                }
                cursor.moveToNext();
            }
            cursor.close();
        }
        db.close();
        return income;
    }

    public float getOutcome(){
        float outcome = 0;
        String inOrOut;
        String fee;
        SQLiteDatabase db = mySQLiteHelper.getReadableDatabase();
        Cursor cursor = db.query(TBNAME, null, null, null, null, null, null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            for (int i = 0; i < cursor.getCount(); i++) {
                inOrOut = cursor.getString(cursor.getColumnIndex("INOROUT"));
                fee = cursor.getString(cursor.getColumnIndex("FEE"));
                if (inOrOut.equals("支出")) {
                    outcome += Float.parseFloat(fee);
                }
                cursor.moveToNext();
            }
            cursor.close();
        }
        db.close();
        return outcome;
    }
    public float getOutcome(Date startDate, Date endDate){
        float outcome = 0;
        String time;
        String inOrOut;
        String fee;
        Date date=null;
        SQLiteDatabase db = mySQLiteHelper.getReadableDatabase();
        Cursor cursor = db.query(TBNAME, null, null, null, null, null, null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            for (int i = 0; i < cursor.getCount(); i++) {
                time = cursor.getString(cursor.getColumnIndex("TIME"));
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    date = dateFormat.parse(time);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                inOrOut = cursor.getString(cursor.getColumnIndex("INOROUT"));
                fee = cursor.getString(cursor.getColumnIndex("FEE"));
                if (inOrOut.equals("支出") && date.getTime()>=startDate.getTime() && date.getTime()<=endDate.getTime()) {
                    outcome += Float.parseFloat(fee);
                }
                cursor.moveToNext();
            }
            cursor.close();
        }
        db.close();
        return outcome;
    }

}

