package com.pospi.pai.yunpos.login;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lany.sp.SPHelper;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.pospi.adapter.SelectStoreAdapter;
import com.pospi.callbacklistener.HttpCallBackListener;
import com.pospi.dao.GoodsDao;
import com.pospi.dao.MemberDao;
import com.pospi.dialog.DownDialog;
import com.pospi.dialog.MyDialog;
import com.pospi.dto.GoodsDto;
import com.pospi.dto.LoginReturnDto;
import com.pospi.dto.MemberDto;
import com.pospi.dto.MenuDto;
import com.pospi.dto.StoreMsgDto;
import com.pospi.dto.Tablebeen;
import com.pospi.greendao.TablebeenDao;
import com.pospi.http.HttpConnection;
import com.pospi.http.Server;
import com.pospi.img.DownLoadImageService;
import com.pospi.img.ImageDownLoadCallBack;
import com.pospi.pai.yunpos.Main_Login;
import com.pospi.pai.yunpos.R;
import com.pospi.pai.yunpos.base.BaseActivity;
import com.pospi.pai.yunpos.been.DzcBeen;
import com.pospi.pai.yunpos.been.PermitonBeen;
import com.pospi.pai.yunpos.been.PrintBeen;
import com.pospi.pai.yunpos.cash.PointActivity;
import com.pospi.pai.yunpos.util.LoadingDialog;
import com.pospi.pai.yunpos.util.LogUtil;
import com.pospi.pai.yunpos.util.PermitionUtil;
import com.pospi.pai.yunpos.util.UrlSettingActivity;
import com.pospi.util.App;
import com.pospi.util.GetData;
import com.pospi.util.SaveMenuInfo;
import com.pospi.util.constant.URL;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static android.R.attr.id;

public class UserLoginActivity extends BaseActivity {

    private static final String TAG = "UserLoginActivity";
    private Button btn_login;
    private EditText et_email;
    private EditText et_password;
    private ProgressBar bar;

    public static final int RETURN = 2;

    private HttpConnection httpConnection;
    private HttpCallBackListener listener;

    private LoginReturnDto loginReturnDto;
    private StoreMsgDto msgDto;
    private WifiManager wifiManager;

    String token;

    private Dialog dialog;

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case RETURN:
                    closePayDialog();
                    if (msg.obj.toString().equals("error")) {
                        showToast("网络请求失败");
                    } else {
                        bar.setVisibility(View.INVISIBLE);
                        loginReturnDto = httpConnection.parseJson(msg.obj.toString());

                        if (loginReturnDto.getErrCode().equals("100")) {
                            //存储信息
                            JSONObject resultObject = null;
                            try {
                                SPHelper.getInstance().putString(Constant.USER_QYH, userlogin_qyh.getText().toString().trim());
                                SPHelper.getInstance().putString(Constant.USER_GH, et_email.getText().toString().trim());
                                SPHelper.getInstance().putString(Constant.USER_PWD, et_password.getText().toString().trim());
                                resultObject = new JSONObject(loginReturnDto.getResult());
                                JSONObject object1 = resultObject.getJSONObject("user");
                                JSONObject object2 = resultObject.getJSONObject("sys");
                                JSONObject object = resultObject.getJSONObject("customer");
                                SPHelper.getInstance().putString(Constant.USER_ID, object1.getString("logid"));
                                SPHelper.getInstance().putString(Constant.SYS_ID, object2.getString("id"));
                                SPHelper.getInstance().putString(Constant.CUSTOMER, object1.getString("id"));
                                SPHelper.getInstance().putString(Constant.CUSTOMER_name, object1.getString("cname"));
                                SPHelper.getInstance().putString(Constant.SYJH, resultObject.getJSONObject("pos").getString("syjh"));
                                markeid = resultObject.getJSONObject("pos").getString("market");
                                JSONObject pos_rule = resultObject.getJSONObject("pos_rule");
                                setPermiton(pos_rule);
                                downPosls();
                                downPosdzc();
                                downPosprint();
                                downShop();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        } else {
                            Toast.makeText(getApplicationContext(), loginReturnDto.getErrMsg(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                case 4:
                    pro = 0;
                    count = 0;
                    closeDownDialog();
                    SPHelper.getInstance().putBoolean("login", true);
                    new LogUtil().save(Constant.LOG_LOGIN);
                    startActivity(PointActivity.class);
                    finish();
                    break;
            }
        }
    };


    private void setPermiton(JSONObject object) {
        PermitonBeen been = new PermitonBeen();
        try {
            been.setZkl(0);
            been.setCanTh(object.getString("th").equals("0") ? false : true);
            been.setCanPrint(object.getString("cyxp").equals("0") ? false : true);
            been.setCanDelete(object.getString("scsp").equals("0") ? false : true);
            been.setCanLx(object.getString("sylx").equals("0") ? false : true);
            been.setCanGd(object.getString("gd").equals("0") ? false : true);
            been.setCanSz(object.getString("szsyj").equals("0") ? false : true);
            been.setCanKqx(object.getString("fjzkqx").equals("0") ? false : true);

            been.setCgdd(object.getString("cgdd").equals("0") ? false : true);
            been.setCgsh(object.getString("cgsh").equals("0") ? false : true);
            been.setCgtc(object.getString("cgtc").equals("0") ? false : true);

            been.setSpsy(object.getString("spsy").equals("0") ? false : true);
            been.setDbck(object.getString("dbck").equals("0") ? false : true);
            been.setDbrk(object.getString("dbrk").equals("0") ? false : true);
            been.setDbcy(object.getString("dbcy").equals("0") ? false : true);
            been.setPsck(object.getString("psck").equals("0") ? false : true);
            been.setPsrk(object.getString("psrk").equals("0") ? false : true);
            been.setPscy(object.getString("pscy").equals("0") ? false : true);
            been.setXsdd(object.getString("xsdd").equals("0") ? false : true);
            been.setXsck(object.getString("xsck").equals("0") ? false : true);
            been.setXsth(object.getString("xsth").equals("0") ? false : true);
            been.setMdbh(object.getString("mdbh").equals("0") ? false : true);
            been.setSspd(object.getString("sspd").equals("0") ? false : true);
            been.setPdlr(object.getString("pdlr").equals("0") ? false : true);
            been.setBhjy(object.getString("bhjy").equals("0") ? false : true);

            been.setXshz(object.getString("xshz").equals("0") ? false : true);
            been.setSkhz(object.getString("skhz").equals("0") ? false : true);
            been.setXsxp(object.getString("xsxp").equals("0") ? false : true);
            been.setXsmx(object.getString("xsmx").equals("0") ? false : true);
            been.setKccn(object.getString("kccn").equals("0") ? false : true);
            been.setMdjy(object.getString("mdjy").equals("0") ? false : true);
            been.setSpph(object.getString("spph").equals("0") ? false : true);
            been.setXsfl(object.getString("xsfl").equals("0") ? false : true);


            been.setKhwl(object.getString("khwl").equals("0") ? false : true);
            been.setYwywl(object.getString("ywywl").equals("0") ? false : true);
            been.setCgmx(object.getString("cgmx").equals("0") ? false : true);
            been.setDbmx(object.getString("dbmx").equals("0") ? false : true);
            been.setPsmx(object.getString("psmx").equals("0") ? false : true);
            been.setPdmx(object.getString("pdmx").equals("0") ? false : true);
            been.setJxcmx(object.getString("jxcmx").equals("0") ? false : true);


        } catch (JSONException e) {
            e.printStackTrace();
        }
//        new PermitionUtil().initPermiton(been);
        new PermitionUtil().setPermitonHigh();

    }

    private boolean getPer(String value,JSONObject object) {
        try {
            return object.getString(value).equals("0") ? false : true;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    private String markeid = "";

    private void downShop() {
        Map<String, String> params = new HashMap<>();
        params.put("model", "base.mselect");
        params.put("fun", "getlastmarket");
//        params.put("model", "base.mbase");
//        params.put("fun", "find");
//        params.put("table", "base_market");
        params.put("logid", SPHelper.getInstance().getString(Constant.USER_ID));
        params.put("sysid", SPHelper.getInstance().getString(Constant.SYS_ID));
        params.put("page", "");
        params.put("limit", "");
        httpConnection.postNet(params, new HttpCallBackListener() {
            @Override
            public void CallBack(String Response) {
                saveShopMsg(Response);
            }
        });
    }

    private void downPosls() {
        Map<String, String> params = new HashMap<>();
        params.put("model", "syspara.msz");
        params.put("fun", "getsyspara");
        params.put("logid", SPHelper.getInstance().getString(Constant.USER_ID));
        params.put("sysid", SPHelper.getInstance().getString(Constant.SYS_ID));
        JSONObject object = new JSONObject();
        try {
            object.put("type", "lsxssz");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        params.put("pds", object.toString());
        Log.i("pos", params.toString());
        httpConnection.postNet(params, new HttpCallBackListener() {
            @Override
            public void CallBack(String Response) {
                Log.i("pos", Response);
                try {
                    JSONObject jsonObject = new JSONObject(Response);
                    String mlsx = jsonObject.getJSONObject("result").getString("mlsx");
                    SPHelper.getInstance().putFloat(Constant.POS_MLSX, Integer.parseInt(mlsx));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private void downPosdzc() {
        Map<String, String> params = new HashMap<>();
        params.put("model", "syspara.msz");
        params.put("fun", "getsyspara");
        params.put("logid", SPHelper.getInstance().getString(Constant.USER_ID));
        params.put("sysid", SPHelper.getInstance().getString(Constant.SYS_ID));
        JSONObject object = new JSONObject();
        try {
            object.put("type", "tmsz");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        params.put("pds", object.toString());
        Log.i("pos", params.toString());
        httpConnection.postNet(params, new HttpCallBackListener() {
            @Override
            public void CallBack(String Response) {
                Log.i("pos", Response);
                try {
                    JSONObject jsonObject = new JSONObject(Response);
                    SPHelper.getInstance().putString(Constant.POS_DZC, jsonObject.getJSONObject("result").getString("tmgs"));
                    SPHelper.getInstance().putString(Constant.POS_DZC_FLAG,jsonObject.getJSONObject("result").getString("bzf"));
                    SPHelper.getInstance().putInt(Constant.POS_DZC_NUM, Integer.parseInt(jsonObject.getJSONObject("result").getString("xsd")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private void downPosprint() {
        Map<String, String> params = new HashMap<>();
        params.put("model", "syspara.msz");
        params.put("fun", "getsyspara");
        params.put("logid", SPHelper.getInstance().getString(Constant.USER_ID));
        params.put("sysid", SPHelper.getInstance().getString(Constant.SYS_ID));
        JSONObject object = new JSONObject();
        try {
            object.put("type", "xpsz");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        params.put("pds", object.toString());
        Log.i("pos", params.toString());
        httpConnection.postNet(params, new HttpCallBackListener() {
            @Override
            public void CallBack(String Response) {
                Log.i("pos", Response);
                try {
                    JSONObject jsonObject = new JSONObject(Response);
                    PrintBeen been = new PrintBeen();
                    been.setHead(jsonObject.getJSONObject("result").getString("xpgsmc"));
                    been.setFoot1(jsonObject.getJSONObject("result").getString("xppw1"));
                    been.setFoot2(jsonObject.getJSONObject("result").getString("xppw2"));
                    been.setFoot3(jsonObject.getJSONObject("result").getString("xppw3"));
                    been.setFoot4(jsonObject.getJSONObject("result").getString("xppw4"));
                    SPHelper.getInstance().putString(Constant.POS_PRINT, new Gson().toJson(been, PrintBeen.class));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private EditText userlogin_qyh;

    /*
    店铺信息
     */
    private void saveShopMsg(String value) {
        LoginReturnDto dto = httpConnection.parseJson2(value);
        Log.i("msg", value);
        List<StoreMsgDto> msgDtos = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(dto.getResult());
            for (int j = 0; j < array.length(); j++) {
                JSONObject object1 = (JSONObject) array.opt(j);
                msgDto = new StoreMsgDto();
                msgDto.setId(object1.getString("id"));
                msgDto.setName(object1.getString("name"));

//                msgDto.setAddress(object1.getString("address"));
//                msgDto.setPhone(object1.getString("tel"));
//                msgDto.setContacts(object1.getString("Contacts"));
//                msgDto.setDiscount((float) object1.getDouble("Discount"));
                msgDtos.add(msgDto);
            }
            selectStore(msgDtos);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private TablebeenDao tablebeenDao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userlogin);
        tablebeenDao = App.getInstance().getDaoSession().getTablebeenDao();
        rePermition();
        initWidgets();//实例化所有的控件

        //通过回调来获得网络上返回来的json
        listener = new HttpCallBackListener() {
            @Override
            public void CallBack(String Response) {
                Message message = Message.obtain();
                message.what = RETURN;
                message.obj = Response;
                handler.sendMessage(message);
            }
        };
        Log.i("URL", new URL().LOGIN);

        //点击按钮之后会向服务器发送数据并且会接收到返回值，解析返回的json数据判断登录是否成功
        btn_login.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        login();
                    }
                }
        );

        et_password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                            /*判断是否是“GO”键*/
                if (actionId == EditorInfo.IME_ACTION_GO) {
                                    /*隐藏软键盘*/
                    InputMethodManager imm = (InputMethodManager) v
                            .getContext().getSystemService(
                                    Context.INPUT_METHOD_SERVICE);
                    if (imm.isActive()) {
                        imm.hideSoftInputFromWindow(
                                v.getApplicationWindowToken(), 0);
                    }
                    login();
                    return true;
                }
                return false;
            }
        });
    }


    public void login() {

        if (et_email.getText().toString().trim().equals("vip") && et_password.getText().toString().isEmpty()) {
            //Toast.makeText(getApplicationContext(), "请输入密码！", Toast.LENGTH_SHORT).show();
            startActivity(UrlSettingActivity.class);

        } else if (et_email.getText().toString().isEmpty() || et_password.getText().toString().isEmpty() || userlogin_qyh.getText().toString().trim().isEmpty()) {
            Toast.makeText(getApplicationContext(), "请补全用户信息！", Toast.LENGTH_SHORT).show();
        } else {
            if (new MainPospiActivity().isNetworkAvailable(getApplicationContext())) {
                showPayDialog("正在验证登录信息");
//
                httpConnection.SendDataToServer(userlogin_qyh.getText().toString(), et_email.getText().toString(),
                        et_password.getText().toString(), listener, getApplicationContext(), (getSN().isEmpty() ? getAndroidId() : getSN()));
            } else {
                showToast("网络不可用,请检查网络设置！");
            }
        }
    }

    public void showDialog(String msg) {
        if (dialog == null) {
            dialog = MyDialog.createLoadingDialog(UserLoginActivity.this, msg);
            dialog.show();
        }
    }

    public void closeDialog() {
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }

    //对所有的控件进行实例化
    public void initWidgets() {
        btn_login = (Button) findViewById(R.id.userlogin_btn_login);
        et_email = (EditText) findViewById(R.id.userlogin_uesr);
        et_password = (EditText) findViewById(R.id.userlogin_pwd);
        httpConnection = new HttpConnection();
        bar = (ProgressBar) findViewById(R.id.userlogin_pro);
        userlogin_qyh = findViewById(R.id.userlogin_qyh);
        bar.setVisibility(View.INVISIBLE);
        userlogin_qyh.setText(SPHelper.getInstance().getString(Constant.USER_QYH, ""));
        et_email.setText(SPHelper.getInstance().getString(Constant.USER_GH, ""));
        if (!SPHelper.getInstance().getString(Constant.USER_QYH, "").equals("")) {
            et_password.setFocusableInTouchMode(true);
            et_password.requestFocus();
        }
    }


    /**
     * 弹出选择店铺的dialog
     */

    private AlertDialog selectStoreDialog;

    public void selectStore(final List<StoreMsgDto> storename) {
            boolean tag = true;
            for (StoreMsgDto storeMsgDto : storename) {
                if (markeid.equals(storeMsgDto.getId())) {
                    SPHelper.getInstance().putString(Constant.STORE_ID, storeMsgDto.getId());
                    SPHelper.getInstance().putString(Constant.STORE_NAME, storeMsgDto.getName());
                    tag = false;
                    break;
                }
            }
        if (tag) {
            if (storename.size() == 1) {
                SPHelper.getInstance().putString(Constant.STORE_ID, storename.get(0).getId());
                SPHelper.getInstance().putString(Constant.STORE_NAME, storename.get(0).getName());
                downMenu();
                downGoods();
            } else {
                @SuppressLint("InflateParams")
                View store_lv = getLayoutInflater().inflate(R.layout.store_select_dialog, null);

                final ListView lv = (ListView) store_lv.findViewById(R.id.store_select_dialog_lv);
                SelectStoreAdapter adapter = new SelectStoreAdapter(getApplicationContext(), storename);
                lv.setAdapter(adapter);
                selectStoreDialog = new AlertDialog.Builder(this)
                        .setView(store_lv)
                        .create();

                selectStoreDialog.show();
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        selectStoreDialog.dismiss();
                        downMenu();
                        downGoods();

//                Intent intent = new Intent(UserLoginActivity.this, MainPospiActivity.class);
//                startActivity(intent);
//                finish();

                        SharedPreferences.Editor edit = getSharedPreferences("login_saveInfo", MODE_PRIVATE).edit();
                        edit.putBoolean("login", true);
                        edit.apply();

                        SharedPreferences.Editor editor = getSharedPreferences("StoreMessage", MODE_PRIVATE).edit();
                        editor.putString("Id", storename.get(position).getId());
                        editor.putString("Name", storename.get(position).getName());
                        SPHelper.getInstance().putString(Constant.STORE_ID, storename.get(position).getId());
                        SPHelper.getInstance().putString(Constant.STORE_NAME, storename.get(position).getName());
//                editor.putString("Address", storename.get(position).getAddress());
////                editor.putString("Contacts", storename.get(position).getContacts());
////                editor.putString("Phone", storename.get(position).getPhone());
////                editor.putFloat("Discount", storename.get(position).getDiscount());
//                editor.apply();
                    }
                });
            }

        } else {
            downMenu();
            downGoods();
            downStore();
        }
    }

    //下载门店
    private void downStore() {
        Map<String, String> params = new HashMap<>();
        params.put("logid", SPHelper.getInstance().getString(Constant.USER_ID));
        params.put("sysid", SPHelper.getInstance().getString(Constant.SYS_ID));
        params.put("model", "base.mselect");
        params.put("fun", "getlastmarket");
        Log.i("goods", params.toString());
        new HttpConnection().postNet(params, new HttpCallBackListener() {
            @Override
            public void CallBack(String Response) {
                Log.i("md", Response);
                try {
                    JSONObject jsonObject = new JSONObject(Response);
                    JSONArray result = jsonObject.getJSONArray("result");
                    SPHelper.getInstance().putString(Constant.STORE_LIST, result.toString());
                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }
        });


    }



    float pro = 0;
    float count =0;
    private void downGoods() {
        showDownDialog();
        Map<String, String> params = new HashMap<>();
        params.put("logid", SPHelper.getInstance().getString(Constant.USER_ID));
        params.put("sysid", SPHelper.getInstance().getString(Constant.SYS_ID));
        params.put("model", "base.mgoodsbase");
        params.put("table", "base_goodsbase");
        params.put("fun", "find");
        params.put("page", "");
        params.put("limit", "");
        httpConnection.postNet(params, new HttpCallBackListener() {
            @Override
            public void CallBack(final String Response) {
                        saveGoods(Response);
            }
        });
    }
    private void setpro(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (pro<count) {
                    try {
                        Thread.sleep(40);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            downDialog.setProgress(pro / count*100);
                        }
                    });
                    pro +=10;
                    if (pro >= count) {
                        pro = count;
                    }

                }
                try {
                    Thread.sleep(1000);
                    Message message = Message.obtain();
                    message.what = 4;
                    handler.sendMessage(message);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            }
        }).start();
    }

    private void saveGoods(String response) {
        final GoodsDao goodsDao = new GoodsDao(getApplicationContext());
        final List<GoodsDto> dtos = new ArrayList<>();
        try {
            JSONObject object = new JSONObject(response);
            JSONArray goodsJson = object.getJSONArray("result");
            String num = object.getString("count");
            count = Float.parseFloat(num);
            setpro();

            Log.i("data", "商品数量：" + num);
            for (int i = 0; i < goodsJson.length(); i++) {
                GoodsDto dto = new GoodsDto();
                JSONObject data = goodsJson.getJSONObject(i);
                dto.setSid(data.getString("id"));//商品id
                dto.setName(data.getString("name"));//商品名
                dto.setPrice(Double.parseDouble(data.getString("lsj")));//零售价
                dto.setCode(data.getString("barcode"));//商品条码
                dto.setCategory_sid(data.getString("catid"));//分类
                dto.setUnit(data.getString("unit"));//单位
                dto.setSpecification(data.getString("spec"));//规格
                dto.setDzc(data.getString("dzc"));//电子称
                dto.setImage(data.getString("pict"));
                dto.setOldPrice(Double.parseDouble(data.getString("lsj")));

                dto.setHyj(Double.parseDouble(data.getString("hyj")));
                dto.setHyj1(Double.parseDouble(data.getString("hyj1")));
                dto.setHyj2(Double.parseDouble(data.getString("hyj2")));
                dto.setHyj3(Double.parseDouble(data.getString("hyj3")));
                dto.setMinzkl(Double.parseDouble(data.getString("minzkl")));
                dto.setUsejf(data.getString("hyjf"));
                dto.setUsezk(data.getString("hyzk"));
                dto.setPlu(data.getString("plu"));//电子秤商品码
                dto.setBzts(data.getString("bzts"));
                Log.i("data", data.getString("hyjf") + "-----" + data.getString("hyzk"));


                dtos.add(dto);
            }
            new Thread(new Runnable() {
                @Override
                public void run() {
                    goodsDao.addGoods(dtos);//存储商品
                }
            }).start();

            Log.i("login", "商品存储成功");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void downMenu() {
        Map<String, String> params = new HashMap<>();
        params.put("logid", SPHelper.getInstance().getString(Constant.USER_ID));
        params.put("sysid", SPHelper.getInstance().getString(Constant.SYS_ID));
        params.put("model", "base.mbase");
        params.put("table", "base_goodscat");
        params.put("fun", "find");
        httpConnection.postNet(params, new HttpCallBackListener() {
            @Override
            public void CallBack(final String Response) {
                LoginReturnDto dto = httpConnection.parseJson2(Response);
                JSONArray result = null;
                try {
                    result = new JSONArray(dto.getResult());
                    List<MenuDto> menuDtos = new ArrayList<MenuDto>();
                    for (int j = 0; j < result.length(); j++) {
                        MenuDto menuDto = new MenuDto();
                        menuDto.setSid(result.getJSONObject(j).getString("id"));
                        menuDto.setName(result.getJSONObject(j).getString("name"));
                        menuDtos.add(menuDto);
                    }
                    SaveMenuInfo.saveAsJson(UserLoginActivity.this, menuDtos);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }


    @Override
    protected void onDestroy() {
        if (selectStoreDialog != null) {
            selectStoreDialog.dismiss();
            selectStoreDialog = null;
        }
        super.onDestroy();
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

    private LoadingDialog loadingDialog;

    //支付等待提示
    private void showPayDialog(String msg) {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(this);
            loadingDialog.setMessage(msg);
            loadingDialog.show();
        }
    }

    //关闭支付等待提示
    private void closePayDialog() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
            loadingDialog = null;
        }
    }

    //支付等待提示
    private DownDialog downDialog;
    private void showDownDialog() {
        if (downDialog == null) {
            downDialog = new DownDialog(this);
            downDialog.show();
        }
    }

    //关闭支付等待提示
    private void closeDownDialog() {
        if (downDialog != null) {
            downDialog.dismiss();
            downDialog = null;
        }
    }
    //读写权限
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    //请求状态码
    private static int REQUEST_PERMISSION_CODE = 1;
    private void rePermition(){
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_PERMISSION_CODE);
            }
        }
    }
    private String getSN() {
        String sn = "";
        try {
            Class c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", String.class);
            Log.i("sunmi", "the sn:" + (String) get.invoke(c, "ro.serialno"));
            sn = (String) get.invoke(c, "ro.serialno");
//            Log.i("sunmi", "First four characters:"
//                    + (String) get.invoke(c, "ro.serialno").substring(0, 4));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sn;
    }
    private String getAndroidId(){
        String m_szAndroidID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        return m_szAndroidID;
    }
}