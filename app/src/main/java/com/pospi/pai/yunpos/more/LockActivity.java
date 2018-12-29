package com.pospi.pai.yunpos.more;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lany.sp.SPHelper;
import com.pospi.pai.yunpos.R;
import com.pospi.pai.yunpos.base.BaseActivity;
import com.pospi.pai.yunpos.cash.PointActivity;
import com.pospi.pai.yunpos.login.Constant;
import com.pospi.pai.yunpos.login.MainPospiActivity;
import com.pospi.util.constant.URL;

import java.text.SimpleDateFormat;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LockActivity extends BaseActivity {

    @Bind(R.id.et_pwd)
    EditText et_pwd;
    @Bind(R.id.btn_unlock)
    Button btn_unlock;
    private int recLen = 0;
    private Thread myThread;
    private SimpleDateFormat sdf;
    private int hour = 0;
    private int minute = 0;
    private int second = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock);
        ButterKnife.bind(this);

//        LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) lock1.getLayoutParams(); //取控件textView当前的布局参数
//        int height = URL.getScreemHeight();
//        linearParams.height = height / 2;// 控件的高强制设成20
//        lock1.setLayoutParams(linearParams); //使设置好的布局参数应用到控件</pre>
//        lock2.setLayoutParams(linearParams);
//
//        sdf = new SimpleDateFormat("HHmmss", Locale.getDefault());
//        myThread = new Thread(new MyThread());
//        myThread.start();         // start thread
    }

//    final Handler handler = new Handler() {          // handle
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//                case 1:
//                    recLen++;
//                    minute = recLen / 60;
//                    if (minute < 60) {
//                        second = recLen % 60;
//
//                        timer.setText(getNumber(hour) + ":" + getNumber(minute) + ":" + getNumber(second));
//                    } else {
//                        hour = minute / 60;
//                        if (hour > 99)
//                            timer.setText("99:59:59");
//                        minute = minute % 60;
//                        second = recLen - hour * 3600 - minute * 60;
//                        timer.setText(getNumber(hour) + ":" + getNumber(minute) + ":" + getNumber(second));
//                    }
//            }
//            super.handleMessage(msg);
//        }
//    };

    /**
     * 当数字小于10时在前面加个0
     *
     * @param number
     * @return
     */
    public String getNumber(int number) {
        if (number < 10) {
            return "0" + number;
        }
        return String.valueOf(number);
    }

    @OnClick(R.id.btn_unlock)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_unlock:
                if (SPHelper.getInstance().getString(Constant.USER_PWD).equals(et_pwd.getText().toString().trim())) {
                    Intent intent = new Intent(LockActivity.this, PointActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(this, "密码错误", Toast.LENGTH_SHORT).show();
                }
                break;
//            case R.id.lock:
//                Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.lock1_out);
////                animation1.setFillAfter(true);
//                lock1.setAnimation(animation1);
//                Animation animation2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.lock2_out);
////                animation2.setFillAfter(true);
//                lock2.setAnimation(animation2);
//
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            Thread.sleep(1000);
//                            Intent intent = new Intent(LockActivity.this, PointActivity.class);
////                            Log.i("which",getSharedPreferences("islogin", Context.MODE_PRIVATE).getInt("which", 0)+"");
////                            intent.putExtra("cashier", getSharedPreferences("islogin", Context.MODE_PRIVATE).getInt("which", 0));
//                            startActivity(intent);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }).start();
//                break;
        }
    }

    //    public class MyThread implements Runnable {      // thread
//        @Override
//        public void run() {
//            do try {
//                Thread.sleep(1000);     // sleep 1000ms
//                Message message = new Message();
//                message.what = 1;
//                handler.sendMessage(message);
//            } catch (Exception e) {
//            }
//            while (true);
//        }
//    }
////
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        if (myThread != null) {
//            myThread.destroy();
//        }
//    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);

    }
}
