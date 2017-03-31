package com.example.myapplication;

import java.util.Calendar;

import com.example.myapplication.R;


import android.content.Context;
import android.text.format.DateFormat;
import android.widget.FrameLayout;
import android.widget.NumberPicker;
import android.widget.NumberPicker.OnValueChangeListener;

public class TimePicker extends FrameLayout {

    private final NumberPicker mHourSpinner;
    private final NumberPicker mMinuteSpinner;
    private final static String[] xuenian = {"2004-2005", "2005-2006", "2006-2007", "2007-2008", "2008-2009", "2010-2011"};
    private final static String[] xueqi = {"第一学期", "第二学期", "第三学期"};
    private Calendar mDate;
    private int mHour, mMinute;
    private String[] mDateDisplayValues = new String[7];
    private OnDateTimeChangedListener mOnDateTimeChangedListener;

    public TimePicker(Context context) {
        super(context);
        /*
         *獲取系統時間
         */
        mDate = Calendar.getInstance();
        mHour = mDate.get(Calendar.HOUR_OF_DAY);
        mMinute = mDate.get(Calendar.MINUTE);
        /**
         * 加载布局
         */
        inflate(context, R.layout.numberpicker1, this);
        /**
         * 初始化控件
         */

        mHourSpinner = (NumberPicker) this.findViewById(R.id.np_hour);
        mHourSpinner.setMaxValue(xuenian.length-1);
        mHourSpinner.setMinValue(0);
        mHourSpinner.setDisplayedValues(xuenian);
        mHourSpinner.setOnValueChangedListener(mOnHourChangedListener);

        mMinuteSpinner = (NumberPicker) this.findViewById(R.id.np_minute);
        mMinuteSpinner.setMaxValue(xueqi.length-1);
        mMinuteSpinner.setMinValue(0);
        mMinuteSpinner.setDisplayedValues(xueqi);
        mMinuteSpinner.setOnValueChangedListener(mOnMinuteChangedListener);
    }
    /**
     *
     * 控件监听器
     */

    private NumberPicker.OnValueChangeListener mOnHourChangedListener = new OnValueChangeListener() {
        @Override
        public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
            mHour = mHourSpinner.getValue();
            onDateTimeChanged();
        }
    };

    private NumberPicker.OnValueChangeListener mOnMinuteChangedListener = new OnValueChangeListener() {
        @Override
        public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
            mMinute = mMinuteSpinner.getValue();
            onDateTimeChanged();
        }
    };



    /*
     *接口回调 参数是当前的View 年月日小时分钟
     */
    public interface OnDateTimeChangedListener {
        void onDateTimeChanged(TimePicker view, int year, int month,
                               int day, int hour, int minute);
    }
    /*
     *对外的公开方法
     */
    public void setOnDateTimeChangedListener(OnDateTimeChangedListener callback) {
        mOnDateTimeChangedListener = callback;
    }

    private void onDateTimeChanged() {
        if (mOnDateTimeChangedListener != null) {
            mOnDateTimeChangedListener.onDateTimeChanged(this,mDate.get(Calendar.YEAR), mDate.get(Calendar.MONTH),
                    mDate.get(Calendar.DAY_OF_MONTH), mHour, mMinute);
        }
    }
}