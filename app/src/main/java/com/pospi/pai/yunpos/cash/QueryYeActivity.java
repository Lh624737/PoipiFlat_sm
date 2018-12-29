package com.pospi.pai.yunpos.cash;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.pax.api.BaseSystemException;
import com.pax.api.BaseSystemManager;
import com.pax.api.PiccException;
import com.pax.api.PiccManager;
import com.pax.baselib.card.Picc;
import com.pospi.dto.ValueCardDto;
import com.pospi.http.Server;
import com.pospi.pai.yunpos.R;
import com.pospi.pai.yunpos.base.BaseActivity;
import com.pospi.util.constant.URL;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by acer on 2017/5/9.
 */

public class QueryYeActivity extends BaseActivity{
    private ValueCardDto valueCardDto;
    private Picc picc;
    private PiccManager.PiccCardInfo m1cardInfo;
    private boolean tag;
    int code = 99;
    private Boolean isStop = false;
    private AlertDialog mAlertDialog;
    //缤纷
//    private final static byte[] KEY = {(byte) 0x66, (byte) 0x8A, (byte) 0x23, (byte) 0x6E, (byte) 0x1E, (byte) 0x8B};
    //上海吉佳
//   private final static byte[] KEY = {(byte) 0x56, (byte) 0x8A, (byte) 0x23, (byte) 0x6E, (byte) 0xDE, (byte) 0xA8};
    //湖北永旺
    public static final byte[] KEY = {(byte) 0x26, (byte) 0x8A, (byte) 0x23, (byte) 0x6E, (byte) 0x1E, (byte) 0x8B};
//    private final static byte[] KEY = {(byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query);
        valueCardDto = new ValueCardDto();
        InductionCard();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    RequestParams params = new RequestParams();
                    params.put("value", valueCardDto.getCardNo());
                    new Server().getConnect(QueryYeActivity.this, new URL().getCardStatus, params, new AsyncHttpResponseHandler() {
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
                default:
                    break;
            }
        }
    };

    private void showCardInfo() {
        AlertDialog ad = new AlertDialog.Builder(this).setTitle("提示").setMessage("您的卡余额为：" + valueCardDto.getCardAmount())
                .setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                       finish();
                    }
                }).show();

    }

    /**
 * 显示储值卡返回dialog
 *
 * @param msg
 */
    private void showTipDialog(String msg) {
    mAlertDialog = new AlertDialog.Builder(QueryYeActivity.this)
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

}