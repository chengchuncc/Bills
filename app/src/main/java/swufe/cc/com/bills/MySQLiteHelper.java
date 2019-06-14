package swufe.cc.com.bills;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class MySQLiteHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String DB_NAME = "data.db";
    public static final String TB_NAME = "tb_data";

    public MySQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context, name, factory, version);
    }
    public MySQLiteHelper(Context context){
        super(context,DB_NAME,null,VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+TB_NAME+"(ID TEXT PRIMARY KEY AUTOINCREMENT,INOROUT TEXT,TYPE TEXT,TIME TEXT,FEE TEXT,REMARKS TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}