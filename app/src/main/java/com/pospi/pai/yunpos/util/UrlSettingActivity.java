package com.pospi.pai.yunpos.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.pospi.pai.yunpos.R;
import com.pospi.pai.yunpos.base.BaseActivity;

public class UrlSettingActivity extends BaseActivity {

    private EditText et_basicURl;
    private EditText et_payURL;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_url_setting);
        initWeidgets();
    }

    public void initWeidgets() {
        et_basicURl = (EditText) findViewById(R.id.website_url_basic);
        SharedPreferences sharedPreferences = this.getSharedPreferences("url", Context.MODE_PRIVATE);
        String url = sharedPreferences.getString("url", "");
        if(!url.equals("")){
            et_basicURl.setText(url);
        }

        et_payURL = (EditText) findViewById(R.id.website_url_pay);
        String pay_url = sharedPreferences.getString("pay_url", "");
        if(!pay_url.equals("")){
            et_payURL.setText(pay_url);
        }

    }

    public void click(View view) {
        switch (view.getId()) {
            case R.id.website_cancle:
                finish();
                break;

            case R.id.website_finish:
                //当两个空都为空的时候
                if (et_payURL.getText().toString().isEmpty() && et_basicURl.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "设置完成\n服务器将采用默认网址", Toast.LENGTH_SHORT).show();
                    SharedPreferences.Editor editor = getSharedPreferences("url", Context.MODE_PRIVATE).edit();
                    editor.putString("url", "http://pos.pospi.com");
                    editor.putString("pay_url", "");
                    editor.apply();
                    finish();
                    // 当basicURl为空且payURL不为空的时候
                } else if (et_basicURl.getText().toString().isEmpty() && !et_payURL.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "基础服务器URL未填写，不能提交！", Toast.LENGTH_SHORT).show();
                } else if (!et_basicURl.getText().toString().isEmpty() && et_payURL.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "设置完成\n系统将采用当前网址", Toast.LENGTH_SHORT).show();
                    SharedPreferences.Editor editor = getSharedPreferences("url", Context.MODE_PRIVATE).edit();
                    editor.putString("url", et_basicURl.getText().toString());
                    editor.putString("pay_url", "");
                    editor.apply();
                    finish();
                } else if (!et_basicURl.getText().toString().isEmpty() && !et_payURL.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "设置完成\n系统将采用当前网址", Toast.LENGTH_SHORT).show();
                    SharedPreferences.Editor editor = getSharedPreferences("url", Context.MODE_PRIVATE).edit();
                    editor.putString("url", et_basicURl.getText().toString());
                    editor.putString("pay_url", et_payURL.getText().toString());
                    editor.apply();
                    finish();

                }


                break;
        }
    }
}
