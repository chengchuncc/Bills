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

    public void deleteAll(){
        SQLiteDatabase db = mySQLiteHelper.getWritableDatabase();
        db.delete(TBNAME,null,null);
        db.close();
    }

    public void addAll(List<DataItem> list) {
        SQLiteDatabase db = mySQLiteHelper.getWritableDatabase();
        for (DataItem item : list) {
            ContentValues values = new ContentValues();
            values.put("INOROUT", item.getInOrOut());
            values.put("TYPE", item.getType());
            values.put("TIME", item.getTime());
            values.put("FEE", item.getFee());
            values.put("REMARKS", item.getRemarks());
            db.insert(TBNAME, null, values);
        }
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

