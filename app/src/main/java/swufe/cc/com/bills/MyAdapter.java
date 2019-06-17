package swufe.cc.com.bills;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MyAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<DataItem> listData;


    public MyAdapter(Context context,ArrayList<DataItem> listData) {
        this.context = context;
        this.listData = listData;

    }

    @Override
    public int getCount() {
        return listData.size();
    }
    @Override
    public Object getItem(int pos) {
        return listData.get(pos);
    }
    @Override
    public long getItemId(int pos) {
        return pos;
    }

    @Override
    public View getView(int pos, View view, ViewGroup arg2) {
        ViewHolder viewholder = null;
        if(view == null){
            LayoutInflater inflate=LayoutInflater.from(context);
            view=inflate.inflate(R.layout.list_item,null);
            viewholder = new ViewHolder();
            viewholder.title= (TextView)view.findViewById(R.id.itemTitle);
            viewholder.detailType = (TextView)view.findViewById(R.id.itemDetailType);
            viewholder.detailTime = (TextView)view.findViewById(R.id.itemDetailTime);
            viewholder.detailFee = (TextView)view.findViewById(R.id.itemDetailFee);
            viewholder.detailRemarks = (TextView)view.findViewById(R.id.itemDetailRemarks);
            view.setTag(viewholder);
        }
        viewholder = (ViewHolder)view.getTag();
        DataItem model = listData.get(pos);
        viewholder.title.setText(model.getInOrOut());
        viewholder.detailType.setText(model.getType());
        viewholder.detailTime.setText(model.getTime());
        viewholder.detailFee.setText(model.getFee());
        viewholder.detailRemarks.setText(model.getRemarks());

        return view;
    }

    class ViewHolder {
        private TextView title;
        private TextView detailType;
        private TextView detailTime;
        private TextView detailFee;
        private TextView detailRemarks;
    }

}
