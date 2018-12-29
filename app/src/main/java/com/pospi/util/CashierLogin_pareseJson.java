package com.pospi.util;

import android.util.Log;
import android.widget.Toast;

import com.pospi.dto.CashierMsgDto;
import com.pospi.dto.LoginReturnDto;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Qiyan on 2016/4/19.
 */
public class CashierLogin_pareseJson {

    private CashierMsgDto cashierMsgDto;
    private List<CashierMsgDto> cashierMsgDtos;
    private LoginReturnDto returnDto = new LoginReturnDto();

    //解析json数据
    public List<CashierMsgDto> parese(String response) {
        cashierMsgDtos = new ArrayList<>();

        try {
            JSONObject object = new JSONObject(response);
            Log.i("Cashier", "Value: " +object.getString("data"));
            if (object.getString("data") == null) {
                Log.i("CashierMsgDto", "msg: " + object.getString("msg"));
                Toast.makeText(App.getContext(), "获取收银员失败！，请检查网络。", Toast.LENGTH_SHORT).show();
            }
//            returnDto.setResult(object.getInt("code"));
//            returnDto.setMessage(object.getString("msg"));
            JSONArray array = object.getJSONArray("data");
            for (int i = 0; i < array.length(); i++) {
                JSONObject object1 = (JSONObject) array.opt(i);

                cashierMsgDto = new CashierMsgDto();
//                cashierMsgDto.setSid(object1.getString("Sid"));
                cashierMsgDto.setUid(object1.getString("id"));
                cashierMsgDto.setNumber(object1.getString("num"));
                cashierMsgDto.setName(object1.getString("ca_name"));
                cashierMsgDto.setPwd(object1.getString("password"));
//                cashierMsgDto.setPhone(object1.getString("Phone"));
//                cashierMsgDto.setIsAdmin(object1.getInt("IsAdmin"));
//                cashierMsgDto.setIsdel(object1.getBoolean("IsDel"));
//                cashierMsgDto.setShopId(object1.getString("ShopId"));
//                cashierMsgDto.setIsAdmin_bool(object1.getBoolean("IsAdmin_bool"));
//                cashierMsgDto.setConfirm(object1.getString("Confirm"));
                cashierMsgDtos.add(cashierMsgDto);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return cashierMsgDtos;
    }
}
