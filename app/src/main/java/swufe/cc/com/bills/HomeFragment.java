package swufe.cc.com.bills;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class HomeFragment extends Fragment implements AdapterView.OnItemLongClickListener{

    ListView bills;
    String TAG = "HomeFragment";
    private ArrayList<HashMap<String, String>> listItems; // 存放文字、图片信息
    private SimpleAdapter listItemAdapter; // 适配器
    private boolean isGetData = false;

    ListView listView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frame_home, container, false);
        listView = (ListView)view.findViewById(R.id.bills);
        initListView();
        listView.setOnItemLongClickListener(this);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        super.onCreate(savedInstanceState);
    }

    private void initListView(){
        DataManager manager = new DataManager(getActivity());
        listItems = new ArrayList<HashMap<String,String>>();

        for (DataItem dataItem : manager.listAll()) {
            HashMap<String,String> map = new HashMap<String,String>();
            map.put("ItemTitle",dataItem.getInOrOut());
            map.put("ItemDetailType",dataItem.getType());
            map.put("ItemDetailTime",dataItem.getTime());
            map.put("ItemDetailFee","¥"+dataItem.getFee());
            map.put("ItemDetailRemarks",dataItem.getRemarks());
            listItems.add(map);
        }


        //生成适配器的 Item 和动态数组对应的元素
        listItemAdapter = new SimpleAdapter(getActivity(),listItems,//listItems 数据源
                R.layout.list_item,//listItem的XML布局实现
                new String[]{"ItemTitle","ItemDetailType","ItemDetailTime","ItemDetailFee","ItemDetailRemarks"},
                new int[]{R.id.itemTitle,R.id.itemDetailType,R.id.itemDetailTime,R.id.itemDetailFee,R.id.itemDetailRemarks }//确定数据与布局位置的对应关系
        );
        listView.setAdapter(listItemAdapter);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view,final int position, long id) {
        Log.i(TAG, "onItemLongClick: 长按列表项position="+ position);
        final DataManager manager = new DataManager(getActivity());
        //删除操作
        //listItems.remove(position);
        //listItemAdapter.notifyDataSetChanged();
        //构造对话框进行确定操作
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setTitle("提示").setMessage("请确认是否删除当前数据").setPositiveButton("是", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.i(TAG, "onClick: 对话框事件处理");
                listItems.remove(position);
                listItemAdapter.notifyDataSetChanged();
                //删除数据库数据
                DataManager manager = new DataManager(getActivity());
                manager.delete(position);

            }
        }).setNegativeButton("否",null);
        builder.create().show();



        Log.i(TAG, "onItemLongClick: size="+ listItems.size());

        return true;
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        //   进入当前Fragment
        if (enter && !isGetData) {
            isGetData = true;
            //重新获取数据
            initListView();
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

}