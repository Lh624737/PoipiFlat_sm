package com.pospi.pai.pospiflat.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.pax.api.BaseSystemException;
import com.pax.api.BaseSystemManager;
import com.pax.api.PiccException;
import com.pax.api.PiccManager;
import com.pax.baselib.card.Card;
import com.pax.baselib.card.Picc;
import com.pospi.dao.PayWayDao;
import com.pospi.dto.LoginReturnDto;
import com.pospi.dto.ValueCardDto;
import com.pospi.http.Server;
import com.pospi.pai.pospiflat.R;
import com.pospi.pai.pospiflat.base.BaseActivity;
import com.pospi.util.DoubleSave;
import com.pospi.util.constant.URL;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
//import cc.lotuscard.LotusCardDriver;
//import cc.lotuscard.LotusCardParam;

public class ChuZhiKaDialogActivity extends BaseActivity {

    @Bind(R.id.card_pay_rl)
    RelativeLayout cardPayRl;
    @Bind(R.id.card_pay_ye)
    TextView cardPayYe;
    @Bind(R.id.card_pay_sf)
    TextView cardPaySf;
    @Bind(R.id.card_pay_jy)
    TextView cardPayJy;
    @Bind(R.id.card_pay_btn_pay)
    Button cardPayBtnPay;
    @Bind(R.id.ll_card_discount)
    LinearLayout ll_card_discount;
    @Bind(R.id.card_pay_ll)
    LinearLayout cardPayLl;
    @Bind(R.id.order_type)
    TextView order_Type;
    @Bind(R.id.card_no)
    TextView card_no;
    @Bind(R.id.card_discount)
    TextView cardDiscount;

    private ValueCardDto valueCardDto;
    private Picc picc;
    private Card card;
    private PiccManager.PiccCardInfo m1cardInfo;

    public static final String PAY_MONEY = "getPayMoney";
    private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";
    private double payMoney;
    private double release;//支付完成后结余金额
    private boolean tag;
    int code = 99;
    private Boolean isStop = false;
    private int orderType;
    private String orderNo;
    private String orderTime;
    private double card_discount;
    private String sid;
    private AlertDialog mAlertDialog;
    //缤纷
//    private final static byte[] KEY = {(byte) 0x66, (byte) 0x8A, (byte) 0x23, (byte) 0x6E, (byte) 0x1E, (byte) 0x8B};
    //上海吉佳
//  private final static byte[] KEY = {(byte) 0x56, (byte) 0x8A, (byte) 0x23, (byte) 0x6E, (byte) 0xDE, (byte) 0xA8};
    //湖北永旺
    public static final byte[] KEY = {(byte) 0x26, (byte) 0x8A, (byte) 0x23, (byte) 0x6E, (byte) 0x1E, (byte) 0x8B};
    //空卡
//    private final static byte[] KEY = {(byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff};
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    RequestParams params = new RequestParams();
                    params.put("value", valueCardDto.getCardNo());
                    new Server().getConnect(ChuZhiKaDialogActivity.this, new URL().getCardStatus, params, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int i, Header[] headers, byte[] bytes) {
                            Log.i("cardinfo", new String(bytes));
                            if (i == 200) {
                                try {
                                    JSONObject object = new JSONObject(new String(bytes));
                                    JSONObject object1 = new JSONObject(object.getString("Value"));
                                    Log.i("cardinfo", new String(bytes));
                                    Log.i("cardMsg", object.getString("Message"));
                                    if (object.getInt("Result") == 0) {
                                        showTipDialog("获取卡信息失败！" + "返回信息:" + object.getString("Message"));
                                    } else {
                                        String[] permissions = object1.getString("no").split("\\,");//'充值', '退卡', '消费', '退货'
                                        Log.i("cardAmount_Sys", object1.getString("cardAmount_Sys"));
                                        Log.i("cardStatus", object1.getString("cardStatus"));
                                        Log.i("cardMortgageAmount_Sys", object1.getString("cardMortgageAmount_Sys"));
                                        //获取该卡金额
                                        valueCardDto.setCardAmount(Double.parseDouble(object1.getString("cardAmount_Sys")));

                                        valueCardDto.setCardStatus(object1.getString("cardStatus"));

                                        valueCardDto.setMortgageAmount(Double.parseDouble(object1.getString("cardMortgageAmount_Sys")));
                                        valueCardDto.setCanConsume(permissions[2].equals("1"));
                                        valueCardDto.setCanRefund(permissions[3].equals("1"));
                                        if (object1.getString("cardStatus").equals("D")) {
                                            showTipDialog("该卡已被冻结!" + "返回信息:" + object.getString("Message"));
                                        } else if (valueCardDto.isCanConsume() && valueCardDto.isCanRefund()) {
                                            Log.i("cardinfo", "储值卡可用");
                                            showCardInfo();
                                        } else if (!valueCardDto.isCanConsume()) {
                                            if (orderType != URL.ORDERTYPE_REFUND) {
                                                if (!valueCardDto.isCanRefund()) {
                                                    showTipDialog("该卡不支持消费！");
                                                }
                                            }
                                        } else if (!valueCardDto.isCanRefund()) {
                                            if (orderType == URL.ORDERTYPE_REFUND) {
                                                if (!valueCardDto.isCanRefund()) {
                                                    showTipDialog("该卡不支持退货！");
                                                }
                                            } else {
                                                showCardInfo();
                                            }
                                        } else {
                                            showTipDialog("返回信息:" + object.getString("Message"));
                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                            try {
                                showTipDialog("返回信息:" + new String(bytes));
                                Log.i("查询储值卡信息失败", new String(bytes));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    break;
                case 2:
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(PaySuccessDialogActivity.CARD_INFO, valueCardDto);

                    Log.i("cardInfo", "传之前余额----" + valueCardDto.getCardAmount());
                    Log.i("cardInfo", "传之前卡号----" + valueCardDto.getCardNo());
                    Log.i("cardInfo", "传之前押金----" + valueCardDto.getMortgageAmount());
                    intent.putExtras(bundle);
                    ChuZhiKaDialogActivity.this.setResult(110, intent);
                    finish();
                    break;
                case 3:
                    Toast.makeText(ChuZhiKaDialogActivity.this, msg.obj.toString(), Toast.LENGTH_SHORT).show();
                    finish();
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 显示储值卡返回dialog
     *
     * @param msg
     */
    private void showTipDialog(String msg) {
        mAlertDialog = new AlertDialog.Builder(ChuZhiKaDialogActivity.this)
                .setMessage(msg)
                .setTitle("提示")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mAlertDialog.dismiss();
                        finish();
                    }
                }).setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        mAlertDialog.dismiss();
                        finish();
                    }
                }).create();
        mAlertDialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chu_zhi_ka_dialog);
        ButterKnife.bind(this);
        cardPayRl.setVisibility(View.VISIBLE);
        cardPayLl.setVisibility(View.GONE);

        payMoney = getIntent().getDoubleExtra(PAY_MONEY, 0.01);
        orderType = getIntent().getIntExtra("orderType", URL.ORDERTYPE_SALE);

        card_discount = getSharedPreferences("StoreMessage", MODE_PRIVATE).getFloat("Discount", 1);
        orderNo = getIntent().getStringExtra("orderNo");
        Log.d("orderNo", orderNo+">>>>>>>>>>>>>>>>>>");
        orderTime = getIntent().getStringExtra("orderTime");
        sid = getIntent().getStringExtra("sid");
        valueCardDto = new ValueCardDto();

        InductionCard();
    }

    @OnClick(R.id.card_pay_btn_pay)
    public void onClick() {
        try {
            SharedPreferences preferences = this.getSharedPreferences("Token", Context.MODE_PRIVATE);
            String value = preferences.getString("value", "");
            String[] names = value.split(",");
            String deviceNo = names[2];//收银机号
            String uid = names[1];

            preferences = this.getSharedPreferences("StoreMessage", Context.MODE_PRIVATE);
            String Shopid = preferences.getString("Id", "");//得到店铺的ID

            DecimalFormat df = new DecimalFormat("######0.00");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("sid", new PayWayDao(getApplicationContext()).findPaySid("6"));//客户端id
            jsonObject.put("shopId", Shopid);//店铺id
            jsonObject.put("SYYId", uid);//收银员id
            jsonObject.put("deviceNo", deviceNo);//设备id
            jsonObject.put("OrderNo", String.valueOf(orderNo));//订单号
            jsonObject.put("OrderTime", orderTime);//订单时间
            jsonObject.put("cardUid", valueCardDto.getCardUid());//卡原始id
            jsonObject.put("cardNo", valueCardDto.getCardNo());//卡号
            jsonObject.put("cardOldAmount", df.format(valueCardDto.getCardAmount()));//卡交易前金额
            jsonObject.put("cardMortgage", df.format(valueCardDto.getMortgageAmount()));//卡押金
            jsonObject.put("payType", "C");//付款方式，C:餐卡 X:现金，W:微信，Z:支付宝，Y:银行卡，L:礼品券，T:团购，R:人工修正，O:Other
            if (orderType == URL.ORDERTYPE_REFUND) {
                jsonObject.put("saleType", "TH");//交易类型 C:充值 TK:退卡 X:消费 TH:退货
                jsonObject.put("OrderAmount", df.format(-payMoney * card_discount));//
                jsonObject.put("cardNowAmount", df.format(valueCardDto.getCardAmount() - payMoney * card_discount));//
            } else {
                jsonObject.put("saleType", "X");//交易类型 C:充值 TK:退卡 X:消费 TH:退货
                jsonObject.put("OrderAmount", df.format(-payMoney * card_discount));//
                jsonObject.put("cardNowAmount", df.format(valueCardDto.getCardAmount() - payMoney * card_discount));//
            }

            JSONArray jsonArrayOrder = new JSONArray();
            jsonArrayOrder.put(jsonObject);

            RequestParams params = new RequestParams();
            params.put("value", jsonArrayOrder.toString());

            Log.i("card订单", params.toString());

            new Server().getConnect(ChuZhiKaDialogActivity.this, new URL().AddCardRecord, params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int i, Header[] headers, byte[] bytes) {
                    try {
                        Log.i("card订单", "请求成功" + new String(bytes));
                        if (i == 200) {
                            Log.i("card订单", "上传成功" + new String(bytes));
                            JSONObject object = new JSONObject(new String(bytes));
                            LoginReturnDto returnDto = new LoginReturnDto();
                            returnDto.setResult(object.getInt("Result"));
                            returnDto.setMessage(object.getString("Message"));
                            returnDto.setValue(String.valueOf(object.getString("Value")));
                            if (returnDto.getResult() == 1) {
                                Message message = new Message();
                                message.what = 2;
                                handler.sendMessage(message);
                            } else {
                                Toast.makeText(ChuZhiKaDialogActivity.this, "返回信息：" + returnDto.getMessage(), Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        } else {
                            Toast.makeText(ChuZhiKaDialogActivity.this, "网络请求失败！", Toast.LENGTH_SHORT).show();
                            finish();
                            Log.i("card订单失败", new String(bytes));
                        }
                    } catch (Exception e) {
                        Toast.makeText(ChuZhiKaDialogActivity.this, "网络请求失败！", Toast.LENGTH_SHORT).show();
                        finish();
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                    try {
                        Log.i("card订单", "上传订单失败" + new String(bytes));
                        Toast.makeText(ChuZhiKaDialogActivity.this, "网络请求失败！", Toast.LENGTH_SHORT).show();
                        finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 感应并返回给Handle卡号
     */
    public void InductionCard() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    picc = Picc.getInstance();
                    while (!isStop) { // 不断的寻卡操作，直到找到为止
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        try {
                            PiccManager.getInstance().piccOpen();
                            m1cardInfo = picc.piccDetect((byte) 'M');
                            if (m1cardInfo != null) {
                                try {
                                    Log.i("cardInfo:", "SerialInfo:" + bcd2Strs(m1cardInfo.SerialInfo));
                                    valueCardDto.setCardUid(bcd2Strs(m1cardInfo.SerialInfo));
                                } catch (NumberFormatException e) {
                                    Message message = new Message();
                                    message.what = 3;
                                    message.obj = "读卡序列号失败！";
                                    handler.sendMessage(message);
                                    e.printStackTrace();
                                }
                                isStop = true;
                                code = 0;
                            }
                        } catch (PiccException e1) {
                            e1.printStackTrace();
                        }
                    }
                    // 检卡
                    if (null != m1cardInfo) {
                        // 访问相应的块验证密码
                        try {
                            if (null != m1cardInfo.SerialInfo) {
                                picc.m1Authority((byte) 'A', (byte) 0x04, KEY, m1cardInfo.SerialInfo);
                                Log.i("cardInfo", "关联成功----");
                                tag = true;//
                            }
                        } catch (PiccException e) {
                            Log.i("cardInfo", "关联失败----");
                            e.printStackTrace();
                        }
                        // read
                        try {
                            byte[] value1 = picc.m1ReadBlock((byte) 0x04);//读取卡号
                            if (value1 != null) {
                                Log.i("cardInfo", "读成功----" + byteToString(value1));
                                valueCardDto.setCardNo(byteToString(value1));
                                Message message = new Message();
                                message.what = 1;
                                handler.sendMessage(message);
                            }
                            // 关闭跟卡的关联
                            if (tag) {
                                try {
                                    picc.piccRemove((byte) 'R', (byte) 0);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        } catch (PiccException e) {
                            Message message = new Message();
                            message.what = 3;
                            message.obj = "读卡失败！";
                            handler.sendMessage(message);
                        }
                        isStop = false;
                        BaseSystemManager.getInstance().beep();   // 关闭manager
                    }
                } catch (BaseSystemException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    //给卡片写入数据
    public void WriteInCard(final double balance) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    picc = Picc.getInstance();
                    isStop = false;
//                     lightInit();
//                     蓝灯
//                     light((byte) 0x08, (byte) 1);
                    while (!isStop) { // 不断的寻卡操作，直到找到为止
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        try {
                            PiccManager.getInstance().piccOpen();
                            m1cardInfo = picc.piccDetect((byte) 'M');
                            if (m1cardInfo != null) {
                                isStop = true;
                                code = 0;
                            }
                        } catch (PiccException e1) {
                            e1.printStackTrace();
                        }
                    }
                    // 检卡
                    if (null != m1cardInfo) {
                        // 访问相应的块验证密码
                        try {
                            if (null != m1cardInfo.SerialInfo) {
                                picc.m1Authority((byte) 'A', (byte) 0x05, KEY, m1cardInfo.SerialInfo);
                                tag = true;//
                            }
                        } catch (PiccException e) {
                            e.printStackTrace();
                        }
                        try {
                            Log.i("cardInfo", "结余----" + balance);
                            picc.m1WriteBlock((byte) 0x05, doubleToByte(balance));
                            Log.i("cardInfo", "写成功----");
                            Message message = new Message();
                            message.what = 2;
                            handler.sendMessage(message);
                            isStop = true;
                            if (tag) {
                                picc.piccRemove((byte) 'R', (byte) 0);
                            }
                        } catch (PiccException e) {
                            Log.i("cardInfo", "写失败----");
                            e.printStackTrace();
                        }
                        BaseSystemManager.getInstance().beep();
                    }
                } catch (BaseSystemException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 显示储值卡信息界面
     */
    public void showCardInfo() {
        release = valueCardDto.getCardAmount() - payMoney * card_discount;
        if (release >= 0) {
            cardPayRl.setVisibility(View.GONE);
            cardPayLl.setVisibility(View.VISIBLE);
            if (card_discount < 1) {
                ll_card_discount.setVisibility(View.VISIBLE);
                cardDiscount.setText(String.valueOf(DoubleSave.doubleSaveTwo(payMoney * (1 - card_discount))));
            }
            card_no.setText(valueCardDto.getCardNo());
            cardPayYe.setText(String.valueOf(valueCardDto.getCardAmount()));

            if (orderType == URL.ORDERTYPE_REFUND) {
                order_Type.setText("退款：");
                cardPaySf.setText(String.valueOf(DoubleSave.doubleSaveTwo(-payMoney* card_discount)));
                cardPayBtnPay.setText("确定退款");
            } else {
                cardPaySf.setText(String.valueOf(DoubleSave.doubleSaveTwo(payMoney* card_discount)));
            }

            cardPayJy.setText(String.valueOf(DoubleSave.doubleSaveTwo(release)));
        } else {
            showTipDialog("卡内余额：" + valueCardDto.getCardAmount() + " ,余额不足，无法支付!");
        }

    }

    //将数值转换成byte[]数组
    public byte[] doubleToByte(double dou) {
        byte[] bytes = new byte[16];
        char ch[] = String.valueOf(dou).toCharArray();
        String str = "";
        for (int i = 0; i < ch.length; i++) {
            str = str + Integer.toHexString((int) ch[i]);
        }
        byte[] b = HexString2Bytes(str);
        for (int i = 0; i < 16; i++) {
            if (i < b.length) {
                bytes[i] = b[i];
            } else {
                bytes[i] = (byte) 0x00;
            }
        }
        return bytes;
    }

    public byte[] HexString2Bytes(String src) {
        if (null == src || 0 == src.length()) {
            return null;
        }
        byte[] ret = new byte[src.length() / 2];
        byte[] tmp = src.getBytes();
        for (int i = 0; i < (tmp.length / 2); i++) {
            ret[i] = uniteBytes(tmp[i * 2], tmp[i * 2 + 1]);
        }
        return ret;
    }

    public byte uniteBytes(byte src0, byte src1) {
        byte _b0 = Byte.decode("0x" + new String(new byte[]{src0})).byteValue();
        _b0 = (byte) (_b0 << 4);
        byte _b1 = Byte.decode("0x" + new String(new byte[]{src1})).byteValue();
        byte ret = (byte) (_b0 ^ _b1);
        return ret;
    }

    //将读取到的byte[]数组转换成数值
    public double byteToDouble(byte[] b) {
        char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'A', 'B', 'C', 'D', 'E', 'F'};
        StringBuilder sb = new StringBuilder(b.length * 2);
        for (int i = 0; i < b.length; i++) {
            sb.append(HEX_DIGITS[((b[i] & 0xF0) >>> 4)]);
            sb.append(HEX_DIGITS[(b[i] & 0xF)]);
        }
        String str = sb.toString();
        String str2 = "";
        for (int i = 0; i < str.length(); i++) {
            if (i % 2 == 0) {
                if (!str.substring(i, i + 2).equals("00")) {
                    str2 = str2 + (char) Integer.parseInt(str.substring(i, i + 2), 16);
                } else {
                    break;
                }
            }
        }
        if (str2 == "") {
            return 0.00;
        } else {
            return DoubleSave.doubleSaveTwo(Double.parseDouble(str2));
        }
    }

    //将读取到的byte[]数组转换成String
    public String byteToString(byte[] b) {
        Log.i("cardno", "byteToString: " + b);
        char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'A', 'B', 'C', 'D', 'E', 'F'};
        StringBuilder sb = new StringBuilder(b.length * 2);
        for (int i = 0; i < b.length; i++) {
            sb.append(HEX_DIGITS[((b[i] & 0xF0) >>> 4)]);
            sb.append(HEX_DIGITS[(b[i] & 0xF)]);
        }
        String str = sb.toString();
        String str2 = "";
        for (int i = 0; i < str.length(); i++) {
            if (i % 2 == 0) {
                if (!str.substring(i, i + 2).equals("00")) {
                    str2 = str2 + (char) Integer.parseInt(str.substring(i, i + 2), 16);
                } else {
                    break;
                }
            }
        }
        Log.i("cardno2", "byteToString: " + str2);
        return str2;
    }

    public String bcd2Strs(byte[] b) {
        char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'A', 'B', 'C', 'D', 'E', 'F'};
        StringBuilder sb = new StringBuilder(b.length * 2);
        for (int i = 0; i < b.length; i++) {
            sb.append(HEX_DIGITS[((b[i] & 0xF0) >>> 4)]);
            sb.append(HEX_DIGITS[(b[i] & 0xF)]);
        }
        return sb.toString();
    }

    @Override
    protected void onDestroy() {
        isStop = true;
        
        super.onDestroy();
    }
}
