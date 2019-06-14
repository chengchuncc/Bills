package swufe.cc.com.bills;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;

import java.util.Calendar;

public class FuncFragment extends Fragment implements View.OnClickListener {

    private RadioButton radioButtonIn;
    private RadioButton radioButtonOut;
    private EditText editTextTime;
    private Spinner mSpinner;
    private EditText editTextFee;
    private EditText editTextRemarks;
    private static final String[] type={"衣","食","住","行","其他"};
    private ArrayAdapter<String> adapter;
    private static final String[] data={};
    private String TAG = "FuncFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frame_func, null);

        radioButtonIn = (RadioButton)view.findViewById(R.id.radioButton);
        radioButtonOut = (RadioButton)view.findViewById(R.id.radioButton2);
        editTextTime = (EditText) view.findViewById(R.id.edit_text_time);
        editTextFee = (EditText) view.findViewById(R.id.editText_fee);
        editTextRemarks = (EditText) view.findViewById(R.id.editText_remarks);
        mSpinner = (Spinner)view.findViewById(R.id.spinner_type);



        adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,type);

        //设置下拉列表的风格
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //将adapter 添加到spinner中
        mSpinner.setAdapter(adapter);

        //添加事件Spinner事件监听
        mSpinner.setOnItemSelectedListener(new SpinnerSelectedListener());

        //设置默认值
        mSpinner.setVisibility(View.VISIBLE);




        editTextTime.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    showDatePickDlg();
                    return true;
                }
                return false;
            }
        });
        editTextTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showDatePickDlg();
                }
            }
        });
        return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        super.onCreate(savedInstanceState);

    }

    public void showDatePickDlg(){
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                monthOfYear=monthOfYear+1;
                FuncFragment.this.editTextTime.setText(year + "-" + monthOfYear + "-" + dayOfMonth);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    @Override
    public void onClick(View v) {
        data[0]="001";
        data[2]=type[(int)mSpinner.getSelectedItemId()];
        data[3]=editTextTime.getText().toString();
        data[4]=editTextFee.getText().toString();
        data[5]=editTextRemarks.getText().toString();

        if(radioButtonIn.isChecked()){
            data[1]="收入";
            //updateData
        }else if(radioButtonOut.isChecked()){
            data[1]="支出";
            //updateData
        }else {
            //不储存数据
        }

    }

    class SpinnerSelectedListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                                   long arg3) {
        }

        public void onNothingSelected(AdapterView<?> arg0) {
        }
    }



}

