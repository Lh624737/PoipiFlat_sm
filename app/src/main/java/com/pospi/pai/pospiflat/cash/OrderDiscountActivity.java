package com.pospi.pai.pospiflat.cash;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.pospi.pai.pospiflat.R;
import com.pospi.pai.pospiflat.base.BaseActivity;
import com.pospi.util.constant.URL;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OrderDiscountActivity extends BaseActivity {

    @Bind(R.id.discount_cancel)
    TextView discountCancel;
    @Bind(R.id.discount_sure)
    TextView discountSure;
    @Bind(R.id.zhekou)
    TextView zhekou;
    @Bind(R.id.zherang)
    TextView zherang;
    @Bind(R.id.discount_number)
    TextView discountNumber;
    @Bind(R.id.discount_7)
    TextView discount7;
    @Bind(R.id.discount_8)
    TextView discount8;
    @Bind(R.id.discount_9)
    TextView discount9;
    @Bind(R.id.discount_free)
    TextView discountFree;
    @Bind(R.id.discount_4)
    TextView discount4;
    @Bind(R.id.discount_5)
    TextView discount5;
    @Bind(R.id.discount_6)
    TextView discount6;
    @Bind(R.id.discount_5zhe)
    TextView discount5zhe;
    @Bind(R.id.discount_6zhe)
    TextView discount6zhe;
    @Bind(R.id.discount_1)
    TextView discount1;
    @Bind(R.id.discount_2)
    TextView discount2;
    @Bind(R.id.discount_3)
    TextView discount3;
    @Bind(R.id.discount_7zhe)
    TextView discount7zhe;
    @Bind(R.id.discount_8zhe)
    TextView discount8zhe;
    @Bind(R.id.discount_dian)
    TextView discountDian;
    @Bind(R.id.discount_0)
    TextView discount0;
    @Bind(R.id.discount_clear)
    TextView discountClear;
    @Bind(R.id.discount_9zhe)
    TextView discount9zhe;
    @Bind(R.id.discount_95zhe)
    TextView discount95zhe;
    @Bind(R.id.discount_left)
    TextView discountLeft;
    @Bind(R.id.discount_right)
    TextView discountRight;

    private int tag = 1;
    private double number = 0;
    private String str_num = "";
    private double order_money;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_discount);
        ButterKnife.bind(this);
        Window window = getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        //设置窗口的大小及透明度
        layoutParams.height = URL.getScreemHeight() * 4 / 5;
        layoutParams.width = URL.getScreemWidth() * 3 / 5;
        order_money = getIntent().getDoubleExtra("money", 0);
        window.setAttributes(layoutParams);
    }

    @OnClick({R.id.discount_cancel, R.id.discount_sure, R.id.zhekou, R.id.zherang, R.id.discount_7, R.id.discount_8, R.id.discount_9, R.id.discount_free, R.id.discount_4, R.id.discount_5, R.id.discount_6, R.id.discount_5zhe, R.id.discount_6zhe, R.id.discount_1, R.id.discount_2, R.id.discount_3, R.id.discount_7zhe, R.id.discount_8zhe, R.id.discount_dian, R.id.discount_0, R.id.discount_clear, R.id.discount_9zhe, R.id.discount_95zhe})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.discount_cancel:
                this.setResult(0);
                finish();
                break;
            case R.id.discount_sure:
                Intent intent = new Intent();
                if (tag == 1) {
                    intent.putExtra("discount_type", "折扣");
                    intent.putExtra("discount_num", number);
                } else {
                    intent.putExtra("discount_type", "折让");
                    intent.putExtra("discount_num", number);
                }
                setResult(1, intent);
                finish();
                break;
            case R.id.zhekou:
                if (tag != 1) {
                    tag = 1;
                    discountLeft.setText("折扣");
                    discountRight.setText("%");
                    discountNumber.setText("0");
                    discount5zhe.setText("5折");
                    discount6zhe.setText("6折");
                    discount7zhe.setText("7折");
                    discount8zhe.setText("8折");
                    discount9zhe.setText("9折");
                    discount95zhe.setText("95折");
                    zhekou.setBackgroundColor(getResources().getColor(R.color.blue));
                    zhekou.setTextColor(getResources().getColor(R.color.white));
                    zhekou.setTextSize(24);
                    zherang.setBackgroundColor(getResources().getColor(R.color.gray));
                    zherang.setTextColor(getResources().getColor(R.color.black));
                    zherang.setTextSize(20);
                }
                break;
            case R.id.zherang:
                if (tag == 1) {
                    tag = 2;
                    discountLeft.setText("折让");
                    discountRight.setText("元");
                    discountNumber.setText("0");
                    discount5zhe.setText("5");
                    discount6zhe.setText("10");
                    discount7zhe.setText("20");
                    discount8zhe.setText("40");
                    discount9zhe.setText("50");
                    discount95zhe.setText("100");
                    zherang.setBackgroundColor(getResources().getColor(R.color.blue));
                    zherang.setTextColor(getResources().getColor(R.color.white));
                    zherang.setTextSize(24);
                    zhekou.setBackgroundColor(getResources().getColor(R.color.gray));
                    zhekou.setTextColor(getResources().getColor(R.color.black));
                    zhekou.setTextSize(20);
                }
                break;
            case R.id.discount_7:
                setTextNumber("7");
                break;
            case R.id.discount_8:
                setTextNumber("8");
                break;
            case R.id.discount_9:
                setTextNumber("9");
                break;
            case R.id.discount_free:
                if (tag == 1) {
                    setTextNumber2("100");
                } else {
                    setTextNumber3(String.valueOf(order_money));
                }
                break;
            case R.id.discount_4:
                setTextNumber("4");
                break;
            case R.id.discount_5:
                setTextNumber("5");
                break;
            case R.id.discount_6:
                setTextNumber("6");
                break;
            case R.id.discount_5zhe:
                if (tag == 1) {
                    setTextNumber2("50");
                } else {
                    setTextNumber3("5");
                }
                break;
            case R.id.discount_6zhe:
                if (tag == 1) {
                    setTextNumber2("40");
                } else {
                    setTextNumber3("10");
                }
                break;
            case R.id.discount_1:
                setTextNumber("1");
                break;
            case R.id.discount_2:
                setTextNumber("2");
                break;
            case R.id.discount_3:
                setTextNumber("3");
                break;
            case R.id.discount_7zhe:
                if (tag == 1) {
                    setTextNumber2("30");
                } else {
                    setTextNumber3("20");
                }
                break;
            case R.id.discount_8zhe:
                if (tag == 1) {
                    setTextNumber2("20");
                } else {
                    setTextNumber3("40");
                }
                break;
            case R.id.discount_dian:
//                if (number < 100) {
                    if (!str_num.contains(".")) {
                        str_num += ".";
                        discountNumber.setText(str_num);
                    }
//                }
                break;
            case R.id.discount_0:
                setTextNumber("0");
                break;
            case R.id.discount_clear:
                str_num = "";
                number = 0;
                discountNumber.setText("0");
                break;
            case R.id.discount_9zhe:
                if (tag == 1) {
                    setTextNumber2("10");
                } else {
                    setTextNumber3("50");
                }
                break;
            case R.id.discount_95zhe:
                if (tag == 1) {
                    setTextNumber2("5");
                } else {
                    setTextNumber3("100");
                }
                break;
        }
    }

    private void setTextNumber2(String num) {
        str_num = num;
        number = Double.parseDouble(str_num);
        discountNumber.setText(num);
    }

    private void setTextNumber3(String num) {
        str_num = num;
        number = Double.parseDouble(str_num);
        if (number > order_money) {
            number = 0;
            str_num = "";
            new AlertDialog.Builder(OrderDiscountActivity.this)
                    .setTitle("提示")
                    .setMessage("折让金额不能大于订单总金额！").setNegativeButton("确认", null).create().show();
            discountNumber.setText("0");
        } else {
            discountNumber.setText(str_num);
        }
    }

    private void setTextNumber(String num) {
        if (tag == 1) {
            if (number < 100) {
                if (str_num.contains(".")) {
                    int len = str_num.indexOf(".");
                    String str = str_num.substring(len + 1);
                    Log.i("lenlen", "setTextNumber: " + str);
                    if (str.length() < 2) {
                        str_num += num;
                        number = Double.parseDouble(str_num);
                    }
                } else {
                    str_num += num;
                    number = Double.parseDouble(str_num);
                    if (number > 100) {
                        number = 100;
                        str_num = "100";
                    }
                }
            }
            discountNumber.setText(str_num);
        } else {
            if (str_num.contains(".")) {
                int len = str_num.indexOf(".");
                String str = str_num.substring(len + 1);
                Log.i("lenlen", "setTextNumber: " + str);
                if (str.length() < 2) {
                    str_num += num;
                    number = Double.parseDouble(str_num);
                }
                discountNumber.setText(str_num);
            } else {
                str_num += num;
                number = Double.parseDouble(str_num);
                if (number > order_money) {
                    number = 0;
                    str_num = "";
                    new AlertDialog.Builder(OrderDiscountActivity.this)
                            .setTitle("提示")
                            .setMessage("折让金额不能大于订单总金额！").setNegativeButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }).create().show();
                    discountNumber.setText("0.00");
                } else {
                    discountNumber.setText(str_num);
                }
            }
        }
    }
}
