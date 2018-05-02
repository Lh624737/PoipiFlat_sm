package com.pospi.ipay;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Message;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.pospi.dto.LoginReturnDto;
import com.pospi.util.constant.URL;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.ParseException;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by Qiyan on 2016/4/18.
 */
public class OnLinePay extends Ipay {

    public static final int PAY_SUCESS=2;
    public static final int PAY_FAIUL=3;

    /****
     * 用来接收服务器
     */
    IpayResult result;

    private Context context;

    public OnLinePay(Context context) {
        this.context = context;
    }

    /*********
     * 回调函数
     *
     * @param payParamsMaps 支付的参数
     * @param result        异步时候用来接收服务器的返回结果
     */
    @Override
    public void requestPay(RequestParams payParamsMaps, IpayResult result) {
        this.result = result;
        getConnect(payParamsMaps, new URL().PAYMENT, result);

        //1.请求服务器
        //2.接收服务器返回值
        //3.更具服务器返回值调用
    }

    public void fdfdfd() {
        //根据服务器返回值调用
//        result.paySuccessBlock(EPayType.wx,result);
    }


    public void getConnect(RequestParams params, String URLAddress, final IpayResult result) {
        SharedPreferences preferences = context.getSharedPreferences("token", context.MODE_PRIVATE);
        final String token = preferences.getString("token", "");
//        Log.i("token", token);
        AsyncHttpClient client = new AsyncHttpClient();//实例化


        Header[] headerArr = new Header[1];
        headerArr[0] = new Header() {
            @Override
            public String getName() {
                return "token";
            }

            @Override
            public String getValue() {
                return token;
            }

            @Override
            public HeaderElement[] getElements() throws ParseException {
                return new HeaderElement[0];
            }
        };

        client.post(context, URLAddress, headerArr, params, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  byte[] responseBody) {
                LoginReturnDto returnDto = new LoginReturnDto();

                if (statusCode == 200) {
//                    Log.i("responseBody", new String(responseBody));
                    try {
                        String returnResult = new String(responseBody);
                        JSONObject object = new JSONObject(returnResult);
                        returnDto.setResult(object.getInt("Result"));
                        returnDto.setMessage(object.getString("Message"));
                        returnDto.setValue(String.valueOf(object.getInt("Value")));


                        SharedPreferences.Editor editor = context.getSharedPreferences("payReturn", context.MODE_PRIVATE).edit();
                        editor.putInt("result", object.getInt("Result"));
                        editor.putString("message", object.getString("Message"));
                        editor.commit();

                        Message msg=Message.obtain();
                        if (object.getInt("Result") == 1) {
                            result.paySuccessBlock(object.getString("Message"));
                        } else if (object.getInt("Result") == 0) {

                            result.payFaiullBlock(object.getString("Message"));
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }


            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                throwable.printStackTrace();// 把错误信息打印出轨迹来

            }
        });

    }


}
