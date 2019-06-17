package swufe.cc.com.bills;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import java.util.Calendar;

public class FuncFragment extends Fragment {

    private RadioGroup radioGroup;
    private EditText editTextTime;
    private Spinner mSpinner;
    private EditText editTextFee;
    private EditText editTextRemarks;
    private Button btnSave;
    private static final String[] type={"衣","食","住","行","其他"};
    private String inOrOut="",contentType="",time="",fee="",remarks="";
    private ArrayAdapter<String> adapter;
    private static final String[] data={"","","","",""};
    private String TAG = "FuncFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frame_func, null);

        radioGroup = (RadioGroup)view.findViewById(R.id.radioGroup);
        editTextTime = (EditText) view.findViewById(R.id.edit_text_time);
        editTextFee = (EditText) view.findViewById(R.id.editText_fee);
        editTextRemarks = (EditText) view.findViewById(R.id.editText_remarks);
        mSpinner = (Spinner) view.findViewById(R.id.spinner_type);
        btnSave = (Button)view.findViewById(R.id.plan_sure);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                selectRadioButton();
            }
        });


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time=editTextTime.getText().toString();
                fee=editTextFee.getText().toString();
                remarks=editTextRemarks.getText().toString();

                data[0] = inOrOut;
                data[1] = contentType;
                data[2] = time;
                data[3] = fee;
                data[4] = remarks;

                final DataManager manager = new DataManager(getActivity());

                if (data[0] == "" || data[1] == "" || TextUtils.isEmpty(editTextTime.getText()) || TextUtils.isEmpty(editTextFee.getText()) || TextUtils.isEmpty(editTextRemarks.getText())) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("提示").setMessage("当前页面有信息未填写，是否继续保存？").setPositiveButton("是", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Log.i(TAG, "onClick: 对话框事件处理");
                            //上传数据到SQLite
                            manager.add(data);
                            editTextTime.setText("");
                            editTextFee.setText("");
                            editTextRemarks.setText("");
                        }
                    }).setNegativeButton("否", null);
                    builder.create().show();
                } else {
                    //上传数据到SQLite
                    manager.add(data);
                    editTextTime.setText("");
                    editTextFee.setText("");
                    editTextRemarks.setText("");

                }
            }
        });


        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, type);

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



    class SpinnerSelectedListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                                   long arg3) {
            contentType = mSpinner.getSelectedItem().toString();
        }

        public void onNothingSelected(AdapterView<?> arg0) {
        }
    }

    private void selectRadioButton() {
        RadioButton rb = (RadioButton)getActivity().findViewById(radioGroup.getCheckedRadioButtonId());
        inOrOut = rb.getText().toString();
    }



}

