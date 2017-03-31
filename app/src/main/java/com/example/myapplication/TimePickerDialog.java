package com.example.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

import java.util.Calendar;

public class TimePickerDialog extends AlertDialog implements
        OnClickListener {
    private TimePicker mDateTimePicker;
    private Calendar mDate = Calendar.getInstance();
    private OnDateTimeSetListener mOnDateTimeSetListener;
    @SuppressWarnings("deprecation")
    public TimePickerDialog(Context context, long date) {
        super(context);
        mDateTimePicker = new TimePicker(context);
        setView(mDateTimePicker);
        /*
         *实现接口，实现里面的方法
         */


        setButton("设置", this);
        setButton2("取消", (OnClickListener) null);
        mDate.setTimeInMillis(date);

    }
    /*
     *接口回調
     *控件 秒数
     */
    public interface OnDateTimeSetListener {
        void OnDateTimeSet(AlertDialog dialog, long date);
    }


    /*
     * 对外公开方法让Activity实现
     */
    public void setOnDateTimeSetListener(OnDateTimeSetListener callBack) {
        mOnDateTimeSetListener = callBack;
    }

    public void onClick(DialogInterface arg0, int arg1) {
        if (mOnDateTimeSetListener != null) {
            mOnDateTimeSetListener.OnDateTimeSet(this, mDate.getTimeInMillis());
        }
    }
}