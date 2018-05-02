package com.pospi.pai.pospiflat.login;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.pax.api.PortException;
import com.pax.api.PortManager;
import com.pospi.adapter.CashierSelectionAdapter;
import com.pospi.dao.GoodsDao;
import com.pospi.dao.ModifiedDao;
import com.pospi.dao.ModifiedGroupDao;
import com.pospi.dao.PayWayDao;
import com.pospi.dao.TableDao;
import com.pospi.dto.CashierMsgDto;
import com.pospi.dto.GoodsDto;
import com.pospi.dto.LoginReturnDto;
import com.pospi.dto.MenuDto;
import com.pospi.dto.ModifiedDto;
import com.pospi.dto.ModifiedGroupDto;
import com.pospi.dto.PayWayDto;
import com.pospi.dto.Tabledto;
import com.pospi.http.Server;
import com.pospi.pai.pospiflat.Main_Login;
import com.pospi.pai.pospiflat.R;
import com.pospi.pai.pospiflat.base.BaseActivity;
import com.pospi.util.CashierLogin_pareseJson;
import com.pospi.util.SaveMenuInfo;
import com.pospi.util.constant.URL;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainPospiActivity extends BaseActivity implements View.OnClickListener {
    private TextView tv_user;
    private EditText et_pwd;
    private ListView lv;
    private ImageView iv_cacle, iv_sure, img_back;
    private LinearLayout layout;
    private List<CashierMsgDto> cashierMsgDtos;
    private LinearLayout ll_pwd;
    private String password = "";
    private SharedPreferences preferences;
    private String token;
    private String Id;
    private LoginReturnDto returnDto;
    private CashierSelectionAdapter adapter;
    public int whichOne;
    private GoodsDao goodsDao;

    private Handler m_handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    downModified();
                    break;
                case 2:
//                    findTable();
                    break;
                case 3:
                    getMenuData();
                    break;
                case 4:
                    getSelectMenuData();
                    break;
                case 5:
                    getData();
                    break;
                case 6:
                    downPayWay();
                    break;
                case 7:
                    downModifiedGroup();
                    break;
            }
        }
    };
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK) { //监控/拦截/屏蔽返回键
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cashierin);
        initWidgets();
        getSavedData();
        getDataIfNOIntnet();
    }

    @Override
    protected void onStart() {
        super.onStart();
//        Log.i("DatabasePath", getDatabasePath("orderinfo.db").getPath());
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                Utils.initAddressDB(MainPospiActivity.this, "/data/data/com.pospi.pai.pospiflat/databases/orderinfo.db");
//            }
//        }).start();
//        KeChuanV61.sendData();

        int cashier_number = getIntent().getIntExtra("cashier", -1);
        Log.i("cashier_number", cashier_number + "");
        if (cashier_number != -1) {
            whichOne = cashier_number;
            tv_user.setText(new CashierLogin_pareseJson().parese(
                    getSharedPreferences("cashierMsgDtos", Context.MODE_PRIVATE)
                            .getString("cashierMsgDtos", ""))
                    .get(whichOne).getNumber());
            lv.setVisibility(View.GONE);
            img_back.setVisibility(View.VISIBLE);
            layout.setVisibility(View.VISIBLE);
            ll_pwd.setVisibility(View.VISIBLE);
        }

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tv_user.setText(cashierMsgDtos.get(position).getNumber());
                lv.setVisibility(View.GONE);
                layout.setVisibility(View.VISIBLE);
                ll_pwd.setVisibility(View.VISIBLE);
                whichOne = position;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkInternet();//得到网络状态,网络正常则开启子线程下载
    }

    /**
     * 下载支付方式
     */
    public void downPayWay() {
        final RequestParams params = new RequestParams();//实例化后存入键值对
        params.put("value", Id);

        new Server().getConnect(getApplicationContext(), new URL().SyncPayType, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                try {
                    Log.i("SyncPayType", new String(bytes));
                    JSONObject object = new JSONObject(new String(bytes));
                    Gson gson = new Gson();
                    List<PayWayDto> dtos = gson.fromJson(object.getString("Value"), new TypeToken<List<PayWayDto>>() {
                    }.getType());
                    new PayWayDao(getApplicationContext()).addPayWay(dtos);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                try {
//                    Log.i("SyncPayTypeF", new String(bytes));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 下载做法
     */
    public void downModified() {
        final RequestParams params = new RequestParams();//实例化后存入键值对
        params.put("value", Id);
        new Server().getConnect(getApplicationContext(), new URL().SyncModified, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                Log.i("SyncModified", new String(bytes));
                try {
                    JSONObject object = new JSONObject(new String(bytes));
                    Gson gson = new Gson();
                    List<ModifiedDto> dtos = gson.fromJson(object.getString("Value"), new TypeToken<List<ModifiedDto>>() {
                    }.getType());
                    new ModifiedDao(getApplicationContext()).addModified(dtos);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
            }
        });
    }

    /**
     * 下载做法
     */
    public void downModifiedGroup() {
        final RequestParams params = new RequestParams();//实例化后存入键值对
        params.put("value", Id);
        new Server().getConnect(getApplicationContext(), new URL().SyncModifiedGroup, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                Log.i("SyncModifiedGroup", new String(bytes));
                try {
                    JSONObject object = new JSONObject(new String(bytes));
                    Gson gson = new Gson();
                    List<ModifiedGroupDto> dtos = gson.fromJson(object.getString("Value"), new TypeToken<List<ModifiedGroupDto>>() {
                    }.getType());
                    new ModifiedGroupDao(MainPospiActivity.this).addModifiedGroup(dtos);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
            }
        });
    }

    public void findTable() {
        final RequestParams params = new RequestParams();//实例化后存入键值对
        params.put("value", Id);
        new Server().getConnect(getApplicationContext(), new URL().SYNCTable, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
//                Log.i("SYNCTable", new String(bytes));
                try {
                    JSONObject object = new JSONObject(new String(bytes));
                    Gson gson = new Gson();
                    List<Tabledto> dtos = gson.fromJson(object.getString("Value"), new TypeToken<List<Tabledto>>() {
                    }.getType());
                    new TableDao(getApplicationContext()).downLoadTableinfo(dtos);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
            }
        });

        Log.i("value", Id);
    }

    /**
     * 检查当前网络是否可用
     *
     * @return
     */
    public boolean isNetworkAvailable(Context context) {
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null) {
            return false;
        } else {
            // 获取NetworkInfo对象
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

            if (networkInfo != null && networkInfo.length > 0) {
                for (NetworkInfo aNetworkInfo : networkInfo) {
                    // 判断当前网络状态是否为连接状态
                    if (aNetworkInfo.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 得到网络状态,网络正常则开启子线程下载
     */
    private void checkInternet() {
        if (!isNetworkAvailable(MainPospiActivity.this.getApplicationContext())) {
            Toast.makeText(this, "网络不可用,请检查网络设置！", Toast.LENGTH_LONG).show();
        } else {
            startThread();
        }
    }

    /**
     * 进入界面之后开启子线程去加载收银员的列表,去下载菜单
     */
    private void startThread() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg = m_handler.obtainMessage();
                msg.what = 1;
                m_handler.sendMessage(msg);
            }
        }).start();


        new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg = m_handler.obtainMessage();
                msg.what = 2;
                m_handler.sendMessage(msg);
            }
        }).start();


        new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg = m_handler.obtainMessage();
                msg.what = 3;
                m_handler.sendMessage(msg);
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg = m_handler.obtainMessage();
                msg.what = 4;
                m_handler.sendMessage(msg);
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg = m_handler.obtainMessage();
                msg.what = 5;
                m_handler.sendMessage(msg);
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg = m_handler.obtainMessage();
                msg.what = 6;
                m_handler.sendMessage(msg);
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg = m_handler.obtainMessage();
                msg.what = 7;
                m_handler.sendMessage(msg);
            }
        }).start();
    }

    public void initWidgets() {
        lv = (ListView) findViewById(R.id.listView22);
        cashierMsgDtos = new ArrayList<>();
        img_back = (ImageView) findViewById(R.id.img_back);
        img_back.setOnClickListener(this);
        tv_user = (TextView) findViewById(R.id.tv_choose_cashier);
        tv_user.setOnClickListener(this);
        layout = (LinearLayout) findViewById(R.id.liner_number);

        iv_cacle = (ImageView) findViewById(R.id.iv_cancle);
        iv_sure = (ImageView) findViewById(R.id.iv_sure);
        iv_cacle.setOnClickListener(this);
        iv_sure.setOnClickListener(this);

        et_pwd = (EditText) findViewById(R.id.et_pwd);
//        et_pwd.setInputType(InputType.TYPE_NULL);
        ll_pwd = (LinearLayout) findViewById(R.id.ll_pwd);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_choose_cashier:
                lv.setVisibility(View.VISIBLE);
                layout.setVisibility(View.GONE);
                tv_user.setText("请选择收银员");
                ll_pwd.setVisibility(View.INVISIBLE);
                getData();
                break;
            case R.id.iv_cancle:
                et_pwd.setText("");
                password = "";
                break;
            case R.id.iv_sure:
//                try {
//                    PortManager portManager = PortManager.getInstance();
//                    String[] coms = new String[]{"mount -o rw,remount /sys", "echo \"1\" > /sys/devices/platform/power_ctrl/printer"};
//                    portManager.portOpen();
//                } catch (PortException e) {
//                    e.printStackTrace();
//                }
                judgePwd(et_pwd.getText().toString(), whichOne);
                break;
            case R.id.img_back:
                finish();
                break;
        }
    }

    /**
     * 得到各个存储的SharedPreferences的值
     */
    public void getSavedData() {
        preferences = getSharedPreferences("token", MODE_PRIVATE);
        token = preferences.getString("token", "");
        preferences = getSharedPreferences("StoreMessage", MODE_PRIVATE);
        Id = preferences.getString("Id", "");
    }

    public void showPWD(View v) {
        Button btn = (Button) v;
        password = password + btn.getText().toString();
        et_pwd.setText(password);
    }

    //收银员登录的时候判断账号密码是否正确
    public void judgePwd(String text, int position) {
        if (cashierMsgDtos.get(position).getPwd().equals(text)) {
//            Toast.makeText(getApplicationContext(), "收银员登录成功", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainPospiActivity.this, Main_Login.class);
            startActivity(intent);

            SharedPreferences.Editor editor = getSharedPreferences("islogin", MODE_PRIVATE).edit();
            editor.putBoolean("islogin", true);
            editor.putInt("which", position);
            editor.apply();
            finish();
        } else {
            Toast.makeText(getApplicationContext(), "密码错误请重新输入！", Toast.LENGTH_SHORT).show();
        }
    }

    public void getDataIfNOIntnet() {
        String cashierMsg = getSharedPreferences("cashierMsgDtos", MODE_PRIVATE).getString("cashierMsgDtos", null);
        if (cashierMsg != null) {
            cashierMsgDtos = new CashierLogin_pareseJson().parese(cashierMsg);
//            Log.i("cashierMsgDtos", "" + cashierMsgDtos.size());
            adapter = new CashierSelectionAdapter(getApplicationContext(), cashierMsgDtos);
            lv.setAdapter(adapter);
        }
    }

    /**
     * 下载收银员信息
     */
    public void getData() {
        final RequestParams params = new RequestParams();//实例化后存入键值对
        params.put("value", Id);
//        Log.i("cashierMsgDtos.url", new URL(getApplicationContext()).SYNCCASHIER);
        new Server().getConnect(getApplicationContext(), new URL().SYNCCASHIER, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                try {
//                    showMenuDialog(new String(bytes));
//                    Log.i("cashierMsgDtosS", new String(bytes));
                    cashierMsgDtos = new CashierLogin_pareseJson().parese(new String(bytes));
                    SharedPreferences.Editor editor = getSharedPreferences("cashierMsgDtos", MODE_PRIVATE).edit();
                    editor.putString("cashierMsgDtos", new String(bytes));
                    editor.apply();
//                    Log.i("cashierMsgDtos", new String(bytes));
                    adapter = new CashierSelectionAdapter(getApplicationContext(), cashierMsgDtos);
                    lv.setAdapter(adapter);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
//                try {
//                    showMenuDialog(new String(bytes));
//                    Log.i("cashierMsgDtosF", new String(bytes));
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
            }
        });
        Log.i("value", Id);
    }

    public void getSelectMenuData() {
        RequestParams params = new RequestParams();//实例化后存入键值对

        SharedPreferences preferences = getSharedPreferences("StoreMessage", MODE_PRIVATE);
        String shopid = preferences.getString("Id", "");
        params.put("value", shopid);
        new Server().getConnect(getApplicationContext(), new URL().SYNCCATEGORY, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int code, Header[] headers, byte[] bytes) {
                if (code == 200) {
                    try {
                        JSONObject object = new JSONObject(new String(bytes));
                        Gson gson = new Gson();
//                        Log.i("MenuDto", object.getString("Value") + "");
                        List<MenuDto> dtos = gson.fromJson(object.getString("Value"), new TypeToken<List<MenuDto>>() {
                        }.getType());
                        SaveMenuInfo.saveAsJson(getApplication(), dtos);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
//                try {
//                    Log.i("onFailure", new String(bytes) + "");
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
            }
        });
    }

    /**
     * 进入界面之后联网获取数据下载商品数据库
     */
    List<GoodsDto> goodsDtosInfo = new ArrayList<>();

    public void getMenuData() {
        goodsDao = new GoodsDao(getApplicationContext());
        final RequestParams params = new RequestParams();//实例化后存入键值对
        new Server().getConnect(getApplicationContext(), new URL().SYNC_MENUS, params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int code, Header[] headers, byte[] bytes) {
                        if (code == 200) {
                            Log.i("goodsDtosInfo", new String(bytes) + "");
                            try {
                                JSONObject object = new JSONObject(new String(bytes));
                                LoginReturnDto loginReturnDto = new LoginReturnDto();
                                loginReturnDto.setResult(object.getInt("Result"));
                                loginReturnDto.setMessage(object.getString("Message"));
                                Gson gson = new Gson();
                                goodsDtosInfo = gson.fromJson(object.getString("Value"), new TypeToken<List<GoodsDto>>() {
                                }.getType());
                                goodsDao.addGoods(goodsDtosInfo);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {

                    }
                }
        );
    }

    public void showMenuDialog(String str) {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("下载的收银员信息")
                .setMessage(str)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();
        dialog.show();
    }
}