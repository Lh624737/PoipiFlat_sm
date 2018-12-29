package com.pospi.pai.yunpos.util;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.pospi.dto.GoodsDto;
import com.pospi.pai.yunpos.base.BaseActivity;
import com.pospi.pai.yunpos.R;
import com.pospi.util.DoubleSave;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DiscountDialogActivity extends BaseActivity {
    @Bind(R.id.name)
    TextView name;
    @Bind(R.id.discount_zr)
    TextView discountZr;
    @Bind(R.id.discount_zc)
    TextView discountZc;
    @Bind(R.id.iv_jian)
    ImageView ivJian;
    @Bind(R.id.tv_num)
    TextView tvNum;
    @Bind(R.id.iv_jia)
    ImageView ivJia;
    @Bind(R.id.delete)
    ImageView delete;
    @Bind(R.id.et_discount)
    EditText etDiscount;
    @Bind(R.id.unit)
    TextView unit;
    @Bind(R.id.discount_dian)
    TextView discountDian;
    @Bind(R.id.d_zero)
    TextView zero;
    @Bind(R.id.discount_sure)
    TextView discountSure;
    @Bind(R.id.cancle)
    Button cancle;
    @Bind(R.id.sure)
    Button sure;

    private GoodsDto goodsDto;

    public static final String DISCOUNT = "discount";
    public static final int DISCOUNTDIALOGACTIVITY_REQUEST = 1001;
    public static final int DISCOUNTDIALOGACTIVITY_RESULT_OK = 1111;
    public static final int DISCOUNTDIALOGACTIVITY_RESULT_CANCEL = 2222;

    private double num;

    /**
     * 当为1是折扣
     * 2是为折让
     */
    private int zc;
    private Intent mIntent;
    private String numberAdd = "";
    private double discount = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getAttributes().gravity = Gravity.RIGHT;
//        setContentView(R.layout.discount);
        setContentView(R.layout.dialog_zk_single);
//        ButterKnife.bind(this);
//        clickZR();
//        mIntent = getIntent();
//        goodsDto = (GoodsDto) mIntent.getSerializableExtra(DISCOUNT);
//        name.setText(goodsDto.getName());
//        num = goodsDto.getNum();
//        tvNum.setText(String.valueOf(num));
//
//        etDiscount.setInputType(InputType.TYPE_NULL);
    }

//    public void discountNumberClick(View view) {
//        TextView tv = (TextView) view;
//        if (numberAdd.isEmpty()) {
//            if (view.getId() != R.id.discount_dian) {
//                numberAdd += tv.getText().toString();
//            }
//        } else {
//            if (view.getId() != R.id.discount_dian) {
//                if (!numberAdd.equals("0")){
//                    numberAdd += tv.getText().toString();
//                }
//            }else{
//                if(!numberAdd.contains(".")){
//                    numberAdd += tv.getText().toString();
//                }
//            }
////            if (numberAdd.equals("0") && view.getId() == R.id.dian) {
////                numberAdd = tv.getText().toString();
////            } else {
////                numberAdd += tv.getText().toString();
////            }
//        }
//        etDiscount.setText(numberAdd);
//
//    }
//
//    @OnClick({R.id.discount_zr, R.id.discount_zc, R.id.iv_jian, R.id.iv_jia, R.id.delete, R.id.discount_sure, R.id.cancle, R.id.sure})
//    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.discount_zr:
//                clickZR();
//                break;
//            case R.id.discount_zc:
//                clcikZC();
//
//                break;
//            case R.id.iv_jian:
//                if (num > 1) {
//                    num -= 1;
//                    tvNum.setText(String.valueOf(num));
//                } else {
//                    showToast("商品数量不可再少了");
//                }
//                break;
//            case R.id.iv_jia:
//                num += 1;
//                tvNum.setText(String.valueOf(num));
//                break;
//            case R.id.delete:
//                this.setResult(DISCOUNTDIALOGACTIVITY_RESULT_CANCEL, mIntent);
//                //关闭Activity
//                finish();
//                break;
//            case R.id.discount_sure://清除键
//                numberAdd = "";
//                etDiscount.setText(numberAdd);
//                break;
//            case R.id.cancle:
//                finish();
//                break;
//            case R.id.sure:
//                if (zc == 1) {
//                    if (!TextUtils.isEmpty(etDiscount.getText().toString())) {
//                        discount = DoubleSave.doubleSaveTwo(Double.parseDouble(etDiscount.getText().toString()) / 100 * goodsDto.getPrice() * num);
//                    }
//                } else {
//                    if (!TextUtils.isEmpty(etDiscount.getText().toString())) {
//                        discount = DoubleSave.doubleSaveTwo(Double.parseDouble(etDiscount.getText().toString()));
//                    }
//                }
//
//                if (discount > goodsDto.getPrice() * num) {
//                    showToast("您折让金额已超过销售金额，无法提交！");
//                } else {
//                    goodsDto.setNum(num);
//                    Intent intent = new Intent();
//                    Log.i("before_discount", "" + discount);
//                    intent.putExtra("discount", discount);
//                    intent.putExtra("num", num);
//                    this.setResult(DISCOUNTDIALOGACTIVITY_RESULT_OK, intent);
//                    finish();
//                }
//                break;
//        }
//    }
//
//    public void clcikZC() {
//        numberAdd = "";
//        discount = 0.0;
//        unit.setText("%");
//        discountZc.setBackgroundColor(getResources().getColor(R.color.pay_title));
//        discountZc.setTextColor(getResources().getColor(R.color.white));
//        discountZr.setBackgroundColor(getResources().getColor(R.color.white));
//        discountZr.setTextColor(getResources().getColor(R.color.pay_title));
//        zc = 1;
//        if (!TextUtils.isEmpty(etDiscount.getText().toString())) {
//            discount = Double.parseDouble(etDiscount.getText().toString()) / 100 * goodsDto.getPrice() * num;
//        }
//    }
//
//    public void clickZR() {
//        numberAdd = "";
//        discount = 0.0;
//        unit.setText("¥");
//        discountZr.setBackgroundColor(getResources().getColor(R.color.pay_title));
//        discountZr.setTextColor(getResources().getColor(R.color.white));
//        discountZc.setBackgroundColor(getResources().getColor(R.color.white));
//        discountZc.setTextColor(getResources().getColor(R.color.pay_title));
//        zc = 2;
//
//        if (!TextUtils.isEmpty(etDiscount.getText().toString())) {
//            discount = Double.parseDouble(etDiscount.getText().toString());
//        }
//    }
}
