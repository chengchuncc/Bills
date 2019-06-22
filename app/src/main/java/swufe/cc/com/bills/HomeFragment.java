package swufe.cc.com.bills;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment implements View.OnClickListener {

    ListView bills;
    String TAG = "HomeFragment";
    private MyAdapter myAdapter;
    private boolean isGetData = false;
    private static final int NOSELECT_STATE = -1;
    private ListView listView;
    private Button bt_cancel, bt_delete;
    private TextView tv_sum;
    private LinearLayout linearLayout;
    private List<Map<String,String>> listItems = new ArrayList<Map<String,String>>();;//数据
    private List<Map<String,String>> list_delete = new ArrayList<Map<String,String>>();// 需要删除的数据
    private List<Integer> delete_position;
    private boolean isMultiSelect = false;// 是否处于多选状态


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frame_home, container, false);
        listView = (ListView)view.findViewById(R.id.bills);
        bt_cancel = (Button)view.findViewById(R.id.bt_cancel);
        bt_delete = (Button)view.findViewById(R.id.bt_delete);
        tv_sum = (TextView)view.findViewById(R.id.tv_sum);
        linearLayout = (LinearLayout)view.findViewById(R.id.linearLayout);
        bt_cancel.setOnClickListener(this);
        bt_delete.setOnClickListener(this);
        DataManager manager = new DataManager(getActivity());
        for (DataItem dataItem : manager.listAll()) {
            HashMap<String,String> map = new HashMap<String,String>();
            map.put("ItemTitle",dataItem.getInOrOut());
            map.put("ItemDetailType",dataItem.getType());
            map.put("ItemDetailTime",dataItem.getTime());
            map.put("ItemDetailFee","¥"+dataItem.getFee());
            map.put("ItemDetailRemarks",dataItem.getRemarks());
            listItems.add(map);
        }
            myAdapter = new MyAdapter(getContext(),listItems,NOSELECT_STATE);
            listView.setAdapter(myAdapter);
        return view;
    }


    public class MyAdapter extends BaseAdapter {

        private LayoutInflater inflater;
        private List<Map<String,String>> listItems;
        private HashMap<Integer, Integer> isCheckBoxVisible;
        private HashMap<Integer, Boolean> isChecked;

        public MyAdapter(Context context, List<Map<String,String>> listItems, int position)
        {
            inflater = LayoutInflater.from(context);
            this.listItems = listItems;
            isCheckBoxVisible = new HashMap<Integer, Integer>();
            isChecked = new HashMap<Integer, Boolean>();
            // 如果处于多选状态，则显示CheckBox，否则不显示
            if (isMultiSelect) {
                for (int i = 0; i < listItems.size(); i++) {
                    isCheckBoxVisible.put(i, CheckBox.VISIBLE);
                    isChecked.put(i, false);
                }
            } else {
                for (int i = 0; i < listItems.size(); i++) {
                    isCheckBoxVisible.put(i, CheckBox.INVISIBLE);
                    isChecked.put(i, false);
                }
            }
            // 如果长按Item，则设置长按的Item中的CheckBox为选中状态
            if (isMultiSelect && position >= 0) {
                isChecked.put(position, true);
            }
        }
        @Override
        public int getCount() {
            return listItems.size();
        }
        @Override
        public Map<String, String> getItem(int position) {
            return listItems.get(position);
        }
        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder viewholder;
            if(convertView == null)
            {
                viewholder = new ViewHolder();
                convertView = inflater.inflate(R.layout.list_item, null);
                viewholder.title= (TextView)convertView.findViewById(R.id.itemTitle);
                viewholder.detailType = (TextView)convertView.findViewById(R.id.itemDetailType);
                viewholder.detailTime = (TextView)convertView.findViewById(R.id.itemDetailTime);
                viewholder.detailFee = (TextView)convertView.findViewById(R.id.itemDetailFee);
                viewholder.detailRemarks = (TextView)convertView.findViewById(R.id.itemDetailRemarks);
                viewholder.checkBox = (CheckBox) convertView.findViewById(R.id.cb_select);
                convertView.setTag(viewholder);
            }else {
                viewholder = (ViewHolder)convertView.getTag();
            }
            final Map str= listItems.get(position);
            viewholder.title.setText((String)listItems.get(position).get("ItemTitle"));
            viewholder.detailType.setText((String)listItems.get(position).get("ItemDetailType"));
            viewholder.detailTime.setText((String)listItems.get(position).get("ItemDetailTime"));
            viewholder.detailFee.setText((String)listItems.get(position).get("ItemDetailFee"));
            viewholder.detailRemarks.setText((String)listItems.get(position).get("ItemDetailRemarks"));
            viewholder.checkBox.setChecked(isChecked.get(position));
            viewholder.checkBox.setVisibility(isCheckBoxVisible.get(position));
            convertView.setOnLongClickListener(new onMyLongClick(position, listItems));
            /*
             * 在ListView中点击每一项的处理
             * 如果CheckBox未选中，则点击后选中CheckBox，并将数据添加到list_delete中
             * 如果CheckBox选中，则点击后取消选中CheckBox，并将数据从list_delete中移除
             */
            convertView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // 处于多选模式
                    if (isMultiSelect) {
                        if (viewholder.checkBox.isChecked()) {
                            viewholder.checkBox.setChecked(false);
                            list_delete.remove(str);
                        } else {
                            viewholder.checkBox.setChecked(true);
                            list_delete.add(str);
                        }
                        tv_sum.setText("共选择了" + list_delete.size() + "项");
                    }
                }
            });
            return convertView;
        }

        class ViewHolder
        {
            private TextView title;
            private TextView detailType;
            private TextView detailTime;
            private TextView detailFee;
            private TextView detailRemarks;
            private CheckBox checkBox;
        }

        // 自定义长按事件
        class onMyLongClick implements View.OnLongClickListener {

            private int position;
            private List<Map<String,String>> listItems;

            // 获取数据，与长按Item的position
            public onMyLongClick(int position, List<Map<String,String>> listItems) {
                this.position = position;
                this.listItems = listItems;
            }

            // 在长按监听时候，切记将监听事件返回ture
            @Override
            public boolean onLongClick(View v) {
                isMultiSelect = true;
                list_delete.clear();
                // 添加长按Item到删除数据list中
                list_delete.add(listItems.get(position));
                linearLayout.setVisibility(View.VISIBLE);
                tv_sum.setText("共选择了" + list_delete.size() + "项");
                for (int i = 0; i < listItems.size(); i++) {
                    myAdapter.isCheckBoxVisible.put(i, CheckBox.VISIBLE);
                }
                // 根据position，设置ListView中对应的CheckBox为选中状态
                myAdapter = new MyAdapter(getActivity(), listItems, position);
                listView.setAdapter(myAdapter);
                return true;
            }
        }
    }
    @Override
    public void onClick(View v) {
        DataManager manager = new DataManager(getActivity());
        switch (v.getId()) {
            // 取消选择
            case R.id.bt_cancel:
                isMultiSelect = false;// 退出多选模式
                list_delete.clear();// 清楚选中的数据
                // 重新加载Adapter
                myAdapter = new MyAdapter(getActivity(), listItems, NOSELECT_STATE);
                listView.setAdapter(myAdapter);
                linearLayout.setVisibility(View.GONE);
                break;

            // 删除
            case R.id.bt_delete:
                isMultiSelect = false;
                // 将数据从list中移除
                for (int i = 0; i < listItems.size(); i++) {
                    for (int j = 0; j < list_delete.size(); j++) {
                        if (listItems.get(i).equals(list_delete.get(j))) {
                            listItems.remove(i);
                        }
                    }
                }
                list_delete.clear();
                // 重新加载Adapter
                myAdapter = new MyAdapter(getActivity(), listItems, NOSELECT_STATE);
                listView.setAdapter(myAdapter);
                linearLayout.setVisibility(View.GONE);
                break;

            default:
                break;
        }
    }






    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        super.onCreate(savedInstanceState);
    }
    @Override
    public void onStart() {
        super.onStart();
        initListView();
        myAdapter.notifyDataSetChanged();
    }

    @Override
    public void onStop() {
        super.onStop();
        listItems.clear();
    }


    private void initListView(){
        DataManager manager = new DataManager(getActivity());
        for (DataItem dataItem : manager.listAll()) {
            HashMap<String,String> map = new HashMap<String,String>();
            map.put("ItemTitle",dataItem.getInOrOut());
            map.put("ItemDetailType",dataItem.getType());
            map.put("ItemDetailTime",dataItem.getTime());
            map.put("ItemDetailFee","¥"+dataItem.getFee());
            map.put("ItemDetailRemarks",dataItem.getRemarks());
            listItems.add(map);
        }

        myAdapter = new MyAdapter(getContext(),listItems,NOSELECT_STATE);
        listView.setAdapter(myAdapter);
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