package swufe.cc.com.bills;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.TextView;

public class MeFragment extends Fragment {

    TextView income,outcome,balance;
    String TAG = "MeFragment";
    boolean isGetData = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frame_me, null);
        income = (TextView)view.findViewById(R.id.tv_income);
        outcome = (TextView)view.findViewById(R.id.tv_outcome);
        balance = (TextView)view.findViewById(R.id.tv_balance);
        getIncome();

        return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    public void getIncome(){
        DataManager manager = new DataManager(getActivity());
        Log.i(TAG, "收入是"+manager.getIncome()+"。");
        income.setText(manager.getIncome()+"");
        outcome.setText(manager.getOutcome()+"");
        balance.setText(manager.getIncome()-manager.getOutcome()+"");
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        //   进入当前Fragment
        if (enter && !isGetData) {
            isGetData = true;
            //重新获取数据
            getIncome();
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
