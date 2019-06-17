package swufe.cc.com.bills;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements AdapterView.OnItemLongClickListener{

    TextView id,title,detailType,detailTime,detailFee,detailRemarks;
    ListView bills;
    String TAG = "HomeFragment";
    private SQLiteDatabase db;
    MySQLiteHelper helper;
    private ArrayList<DataItem> listData;
    private MyAdapter adapter;
    private boolean isGetData = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frame_home, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        super.onCreate(savedInstanceState);


    }
    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        //   进入当前Fragment
        if (enter && !isGetData) {
            isGetData = true;

            title = (TextView)getActivity().findViewById(R.id.itemTitle);
            detailType = (TextView)getActivity().findViewById(R.id.itemDetailType);
            detailTime = (TextView)getActivity().findViewById(R.id.itemDetailTime);
            detailFee = (TextView)getActivity().findViewById(R.id.itemDetailFee);
            detailRemarks = (TextView)getActivity().findViewById(R.id.itemDetailRemarks);

            bills = (ListView)getActivity().findViewById(R.id.bills);
            listData = new ArrayList<DataItem>();
            //创建MySQLiteHelper实例
            helper = new MySQLiteHelper(getActivity(),"bills",null,1);
            //得到数据库
            db = helper.getWritableDatabase();
            //查询数据
            Cursor cursor= db.query("tb_data",null,null,null,null,null,null);
            while(cursor.moveToNext()){
                DataItem item = new DataItem();
                item.setInOrOut(cursor.getString(cursor.getColumnIndex("INOROUT")));
                item.setType(cursor.getString(cursor.getColumnIndex("TYPE")));
                item.setTime(cursor.getString(cursor.getColumnIndex("TIME")));
                item.setFee(cursor.getString(cursor.getColumnIndex("FEE")));
                item.setRemarks(cursor.getString(cursor.getColumnIndex("REMARKS")));
                listData.add(item);
            }
            adapter = new MyAdapter(this.getActivity(),listData);
            bills.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        } else {
            isGetData = false;
        }
        return super.onCreateAnimation(transit, enter, nextAnim);
    }
    @Override
    public void onPause() {
        super.onPause();
        isGetData = false;
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        Log.i(TAG, "onItemLongClick: 长按列表项position="+ position);
        //删除操作
        //listItems.remove(position);
        //listItemAdapter.notifyDataSetChanged();
        //构造对话框进行确定操作
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setTitle("提示").setMessage("请确认是否删除当前数据").setPositiveButton("是", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.i(TAG, "onClick: 对话框事件处理");
                listData.remove(position);
                adapter.notifyDataSetChanged();
            }
        }).setNegativeButton("否",null);

        builder.create().show();


        Log.i(TAG, "onItemLongClick: size="+ listData.size());

        return true;
    }
}