package com.pospi.http;


import android.content.Context;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.pospi.callbacklistener.HttpCallBackListener;
import com.pospi.dto.LoginReturnDto;
import com.pospi.util.App;
import com.pospi.util.constant.URL;
import com.tsy.sdk.myokhttp.response.RawResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;


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
    public void SendDataToServer(final String qyh, final String gh, final String pass, final HttpCallBackListener listener, final Context context,String sn) {
        Map<String, String> map = new HashMap<>();
        map.put("fun", "login");
        map.put("model", "system.mlogin");
        map.put("logid","0");
        final JSONObject object = new JSONObject();
        try {
            object.put("code", qyh);
            object.put("gh", gh);
            object.put("pwd", pass);
            object.put("logtype", "app");
            object.put("clientid", sn);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        map.put("pds", object.toString());
//            params.put("Imei", getPhoneMac());
        Log.i("login", map.toString());
        App.getInstance().getMyOkHttp().post()
                .url(URL.HOST)
                .params(map)
                .enqueue(new RawResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, String response) {
                        Log.i("login", response);
                        listener.CallBack(response.toString());
                    }

                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                        listener.CallBack("error");
                    }
                });


    }

    public void postNet(Map<String, String> map, final HttpCallBackListener listener) {
        App.getInstance().getMyOkHttp().post()
                .url(URL.HOST)
                .params(map)
                .enqueue(new RawResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, String response) {
                        Log.i("login", response);
                        listener.CallBack(response.toString());
                    }

                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                        listener.CallBack("error");
                    }
                });
    }
    public void Net(Map<String, String> map, final HttpCallBackListener listener) {
        App.getInstance().getMyOkHttp().post()
                .url("http://se.pospi.com/downloadapp.php")
                .params(map)
                .enqueue(new RawResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, String response) {
                        Log.i("login", response);
                        listener.CallBack(response.toString());
                    }

                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                        listener.CallBack("error");
                    }
                });
    }


//    //解析json数据
    public LoginReturnDto parseJson(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            loginReturnDto = new LoginReturnDto();
            loginReturnDto.setFlag(jsonObject.getString("flag"));
            loginReturnDto.setErrMsg(jsonObject.getString("errMsg"));
            loginReturnDto.setErrCode(jsonObject.getString("errCode"));
            loginReturnDto.setResult(jsonObject.getJSONObject("result").toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return loginReturnDto;
    }
    public LoginReturnDto parseJson2(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            loginReturnDto = new LoginReturnDto();
            loginReturnDto.setFlag(jsonObject.getString("flag"));
            loginReturnDto.setErrMsg(jsonObject.getString("errMsg"));
            loginReturnDto.setErrCode(jsonObject.getString("errCode"));
            loginReturnDto.setResult(jsonObject.getJSONArray("result").toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return loginReturnDto;
    }


}