package swufe.cc.com.bills;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class DataManager {

    private MySQLiteHelper mySQLiteHelper;
    private String TBNAME;

    public DataManager(Context context) {
        mySQLiteHelper = new MySQLiteHelper(context);
        TBNAME = MySQLiteHelper.TB_NAME;
    }

    public void add(String data[]){
        SQLiteDatabase db = mySQLiteHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("INOROUT", data[0]);
        values.put("TYPE", data[1]);
        values.put("TIME", data[2]);
        values.put("FEE", data[3]);
        values.put("REMARKS", data[4]);
        db.insert(TBNAME, null, values);
        db.close();
    }
    public void deleteAll(){
        SQLiteDatabase db = mySQLiteHelper.getWritableDatabase();
        db.delete(TBNAME,null,null);
        db.close();
    }

    public List<DataItem> listAll(){
        List<DataItem> rateList = null;
        SQLiteDatabase db = mySQLiteHelper.getReadableDatabase();
        Cursor cursor = db.query(TBNAME, null, null, null, null, null, null);
        if(cursor!=null){
            rateList = new ArrayList<DataItem>();
            while(cursor.moveToNext()){
                DataItem item = new DataItem();
                item.setId(cursor.getInt(cursor.getColumnIndex("ID")));
                item.setInOrOut(cursor.getString(cursor.getColumnIndex("INOROUT")));
                item.setType(cursor.getString(cursor.getColumnIndex("TYPE")));
                item.setTime(cursor.getString(cursor.getColumnIndex("TIME")));
                item.setFee(cursor.getString(cursor.getColumnIndex("FEE")));
                item.setRemarks(cursor.getString(cursor.getColumnIndex("REMARKS")));
                rateList.add(item);
            }
            cursor.close();
        }
        db.close();
        return rateList;
    }
    public void delete(int id){
        SQLiteDatabase db = mySQLiteHelper.getWritableDatabase();
        db.delete(TBNAME, "ID=?", new String[]{String.valueOf(id)});
        db.close();
    }
    public void update(DataItem item){
        SQLiteDatabase db = mySQLiteHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("inorout", item.getInOrOut());
        values.put("type", item.getType());
        values.put("time", item.getTime());
        values.put("fee", item.getFee());
        values.put("remarks", item.getRemarks());
        db.update(TBNAME, values, "ID=?", new String[]{String.valueOf(item.getId())});
        db.close();
    }
    public DataItem findById(int id){
        SQLiteDatabase db = mySQLiteHelper.getReadableDatabase();
        Cursor cursor = db.query(TBNAME, null, "ID=?", new String[]{String.valueOf(id)}, null,
                null, null);
        DataItem dataItem = null;
        if(cursor!=null && cursor.moveToFirst()){
            dataItem = new DataItem();
            dataItem.setId(cursor.getInt(cursor.getColumnIndex("ID")));
            dataItem.setInOrOut(cursor.getString(cursor.getColumnIndex("INOROUT")));
            dataItem.setType(cursor.getString(cursor.getColumnIndex("TYPE")));
            dataItem.setTime(cursor.getString(cursor.getColumnIndex("TIME")));
            dataItem.setFee(cursor.getString(cursor.getColumnIndex("FEE")));
            dataItem.setRemarks(cursor.getString(cursor.getColumnIndex("REMARKS")));
            cursor.close();
        }
        db.close();
        return dataItem;
    }

}

