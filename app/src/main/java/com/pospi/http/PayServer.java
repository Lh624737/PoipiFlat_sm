package com.pospi.http;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.ParseException;

public class PayServer {


    /**
     * 连接服务器
     *
     * @param params
     */
    public void getConnect(final Context context, String address, RequestParams params, final AsyncHttpResponseHandler handler) {
        SharedPreferences preferences = context.getSharedPreferences("token", Context.MODE_PRIVATE);
        final String token = preferences.getString("token", "");

//        Log.i("goods", "url: " +address);
//        Log.i("token", token);
        final AsyncHttpClient client = new AsyncHttpClient();//实例化
        client.setTimeout(5000);

        Header[] headerArr = new Header[1];
        headerArr[0] = new Header() {
            @Override
            public String getName() {
                return "token";
            }

            @Override
            public String getValue() {
//                return "245148d996d5404c9d37a4cccd2e791b,f17851cbccb84ce0b55ac6f464d9e13f";
                return token;
            }

            @Override
            public HeaderElement[] getElements() throws ParseException {
                return new HeaderElement[0];
            }
        };

        client.post(context, address, headerArr, params, null, new AsyncHttpResponseHandler() {
            public void onSuccess(int statusCode, Header[] headers,
                                  byte[] responseBody) {

                handler.onSuccess(statusCode, headers, responseBody);
//                Log.i("responseBody", new String(responseBody));
            }


            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                handler.onFailure(i, headers, bytes, throwable);
                Toast.makeText(context, "错误代码："+i, Toast.LENGTH_SHORT).show();
//                try {
//                    Log.i("responseBody", "bytes:" + new String(bytes));
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                handler.onFinish();
            }
        });

    }
}
