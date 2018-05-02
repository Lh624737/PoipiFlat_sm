package com.pospi.util;

import android.app.DatePickerDialog;
import android.content.Context;

/**
 * Created by Qiyan on 2016/5/26.
 */
public class DataTimePicker extends DatePickerDialog{

    public DataTimePicker(Context context, OnDateSetListener callBack, int year, int monthOfYear, int dayOfMonth) {
        super(context, callBack, year, monthOfYear, dayOfMonth);
    }
}
