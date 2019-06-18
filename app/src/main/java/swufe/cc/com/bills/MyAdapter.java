package swufe.cc.com.bills;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyAdapter extends ArrayAdapter{
    private  static  final String TAG = "MyAdapter";

    public MyAdapter(Context context, int resource, ArrayList<HashMap<String,String>> list) {
        super(context, resource, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = convertView;
        if (itemView == null) {
            itemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        Map<String,String> map = (Map<String,String>) getItem(position);
        TextView title = (TextView) itemView.findViewById(R.id.itemTitle);
        TextView detailType = (TextView) itemView.findViewById(R.id.itemDetailType);
        TextView detailTime = (TextView) itemView.findViewById(R.id.itemDetailTime);
        TextView detailFee = (TextView) itemView.findViewById(R.id.itemDetailFee);
        TextView detailRemarks = (TextView) itemView.findViewById(R.id.itemDetailRemarks);

        title.setText("Title:"+ map.get("ItemTitle"));
        detailType.setText("detailType:"+ map.get("ItemDetailType"));
        detailTime.setText("detailTime:"+ map.get("ItemDetailType"));
        detailFee.setText("detailFee:"+ map.get("ItemDetailFee"));
        detailRemarks.setText("detailRemarks:"+ map.get("ItemDetailRemarks"));

        return itemView;
    }
}

