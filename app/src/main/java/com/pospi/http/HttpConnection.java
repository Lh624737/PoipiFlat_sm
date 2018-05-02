package com.pospi.http;


import android.content.Context;
import android.util.Log;

import com.pospi.callbacklistener.HttpCallBackListener;
import com.pospi.dto.LoginReturnDto;
import com.pospi.util.constant.URL;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;


/**
 * 空，了，
 * Created by Qiyan on 2016/4/7.
 * <p/>
 * 向服务器发送数据请求并且接收返回来的数据
 */
public class HttpConnection {

    public static final int RETURN = 2;
    private InputStream is;
    private LoginReturnDto loginReturnDto;

    //向服务器发送数据，并且接收服务器返回来的json数据
    public void SendDataToServer(final String Email, final String Password, final String Imei, final HttpCallBackListener listener, final Context context) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection;
                try {
                    Log.i("URL", new URL().LOGIN);
                    java.net.URL url = new java.net.URL(new URL().LOGIN);
//                    Log.i("URL", new URL().LOGIN);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");//设置请求方式为post                    connection.setRequestProperty("Accept-Charset", "utf-8");
//                    connection.setRequestProperty("contentType", "utf-8");
                    DataOutputStream out = new DataOutputStream(connection.getOutputStream());
                    //写入需要传给服务器的值

                    String str = "Email=" + Email + "&Password=" + Password + "&Imei=" + Imei;//
                    out.writeBytes(new String(str.getBytes(), "utf-8"));
                    //设置连接时的参数
                    connection.setConnectTimeout(5000);
                    connection.setReadTimeout(5000);
                    //建立连接获取流对象
                    is = connection.getInputStream();
                    Log.i("urlconnection","getResponseCode:"+connection.getResponseCode());
                    if (connection.getResponseCode() == 200) {
                        //写流对象
                        BufferedReader br = new BufferedReader(new InputStreamReader(is));
                        StringBuilder response = new StringBuilder();
                        String line;
                        while ((line = br.readLine()) != null) {
                            response.append(line);
                        }
                        listener.CallBack(response.toString());
                        br.close();
                    } else {
                        listener.CallBack("error");
                    }
                    is.close();
                    connection.disconnect();
                } catch (Exception e) {
                    Log.i("建立连接获取流对象","捕捉到异常");
                    e.printStackTrace();
                    listener.CallBack("error");
                }
            }
        }).start();
    }

    //解析json数据
    public LoginReturnDto parseJson(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            loginReturnDto = new LoginReturnDto();
            loginReturnDto.setResult(Integer.parseInt(jsonObject.getString("Result")));
            loginReturnDto.setValue(jsonObject.getString("Value"));
            loginReturnDto.setMessage(jsonObject.getString("Message"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return loginReturnDto;
    }

}