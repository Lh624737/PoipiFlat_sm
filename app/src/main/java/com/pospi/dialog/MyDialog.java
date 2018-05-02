package com.pospi.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pospi.pai.pospiflat.R;

/**
 * Created by Qiyan on 2016/5/11.
 */
public class MyDialog {
    public static Dialog createLoadingDialog(Activity context, String msg) {

        // 首先得到整个View
        View view = LayoutInflater.from(context).inflate(
                R.layout.waitdialog, null);
        // 获取整个布局
        RelativeLayout layout = (RelativeLayout) view
                .findViewById(R.id.dialog_view);
        // 页面中的Img
        ImageView img = (ImageView) view.findViewById(R.id.img);
        // 页面中显示文本
        TextView tipText = (TextView) view.findViewById(R.id.tipTextView);

        // 加载动画，动画用户使img图片不停的旋转
        Animation animation = AnimationUtils.loadAnimation(context,
                R.anim.waitingdialog);
        // 显示动画
        img.startAnimation(animation);
        // 显示文本
        tipText.setText(msg);

        // 创建自定义样式的Dialog
        Dialog loadingDialog = new Dialog(context, R.style.loading_dialog);
        // 设置返回键无效
        loadingDialog.setCancelable(false);
        loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));

        return loadingDialog;
    }


}
