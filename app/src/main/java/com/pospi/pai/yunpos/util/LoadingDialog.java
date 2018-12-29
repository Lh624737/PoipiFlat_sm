package com.pospi.pai.yunpos.util;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.TextView;

import com.pospi.pai.yunpos.R;


/**
 * Created by lenovo on 2017/2/5 0005.
 */

public class LoadingDialog extends Dialog {

    private final TextView tv;

    public LoadingDialog(Context context) {
        super(context);
        /**设置对话框背景透明*/
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.loading_dialog);
        tv = (TextView) findViewById(R.id.tv_loadingText);

        setCanceledOnTouchOutside(false);
    }

    public LoadingDialog setMessage(String msg) {
        tv.setText(msg);
        return this;
    }

}
