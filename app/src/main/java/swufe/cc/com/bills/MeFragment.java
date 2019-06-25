package swufe.cc.com.bills;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MeFragment extends Fragment{

    TextView income,outcome,balance,timeSlot;
    EditText startTime,endTime;
    Button cal;
    private String start,end;
    private Date startDate,endDate;
    String TAG = "MeFragment";
    boolean isGetData = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frame_me, null);
        startTime = (EditText) view.findViewById(R.id.start_time);
        endTime = (EditText) view.findViewById(R.id.end_time);
        cal = (Button)view.findViewById(R.id.cal);
        income = (TextView)view.findViewById(R.id.tv_income);
        outcome = (TextView)view.findViewById(R.id.tv_outcome);
        balance = (TextView)view.findViewById(R.id.tv_balance);
        timeSlot = (TextView)view.findViewById(R.id.time_slot);

        startTime.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    showDatePickStartDlg();
                    return true;
                }
                return false;
            }
        });
        startTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showDatePickStartDlg();
                }
            }
        });
        endTime.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    showDatePickEndDlg();
                    return true;
                }
                return false;
            }
        });
        endTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showDatePickEndDlg();
                }
            }
        });

        cal.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                start = startTime.getText().toString();
                end = endTime.getText().toString();
                if (TextUtils.isEmpty(startTime.getText())) {
                    Toast.makeText(getActivity(), "请输入开始时间", Toast.LENGTH_SHORT)
                            .show();
                }
                if (TextUtils.isEmpty(endTime.getText())) {
                    Toast.makeText(getActivity(), "请输入结束时间", Toast.LENGTH_SHORT)
                            .show();
                }

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    startDate = dateFormat.parse(startTime.getText().toString());
                    endDate = dateFormat.parse(endTime.getText().toString());
                    if (startDate.getTime() > endDate.getTime()) {
                        Toast.makeText(getActivity(), "开始时间需小于结束时间",
                                Toast.LENGTH_SHORT).show();
                    } else if (startDate.getTime() > new Date().getTime()) {
                        Toast.makeText(getActivity(), "开始时间大于现在时间",
                                Toast.LENGTH_SHORT).show();
                    } else if (endDate.getTime() > new Date().getTime()) {
                        Toast.makeText(getActivity(), "结束时间大于现在时间",
                                Toast.LENGTH_SHORT).show();
                    }else if(TextUtils.isEmpty(startTime.getText())||TextUtils.isEmpty(endTime.getText())){
                        Toast.makeText(getActivity(), "请先选择时间!", Toast.LENGTH_SHORT).show();
                    }else {
                        getData(startDate, endDate);
                        timeSlot.setText("从" + startTime.getText().toString() + "到" + endTime.getText().toString() + "的合计");
                        startTime.setText("");
                        endTime.setText("");
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "数据格式有误！", Toast.LENGTH_SHORT)
                            .show();
                }

            }
        });

        getData();

        return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    public void getData(){
        DataManager manager = new DataManager(getActivity());
        income.setText(manager.getIncome()+"");
        outcome.setText(manager.getOutcome()+"");
        balance.setText(manager.getIncome()-manager.getOutcome()+"");
    }
    public void getData(Date startDate,Date endDate){
        DataManager manager = new DataManager(getActivity());
        income.setText(manager.getIncome(startDate,endDate)+"");
        outcome.setText(manager.getOutcome(startDate,endDate)+"");
        balance.setText(manager.getIncome(startDate,endDate)-manager.getOutcome(startDate,endDate)+"");
    }
    public void showDatePickStartDlg(){
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                monthOfYear=monthOfYear+1;
                MeFragment.this.startTime.setText(year + "-" + monthOfYear + "-" + dayOfMonth);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }
    public void showDatePickEndDlg(){
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                monthOfYear=monthOfYear+1;
                MeFragment.this.endTime.setText(year + "-" + monthOfYear + "-" + dayOfMonth);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }
/*    public static Date stringToDate(String checktime, String formatType)
            throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(formatType);
        Date date = null;
        date = formatter.parse(checktime);
        return date;
    }*/

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        //   进入当前Fragment
        if (enter && !isGetData) {
            isGetData = true;
            //重新获取数据
            getData();
            timeSlot.setText("所有时间的合计");
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
