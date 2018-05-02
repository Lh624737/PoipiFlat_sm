package com.pospi.pai.pospiflat;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.gprinter.aidl.GpService;
import com.gprinter.io.GpDevice;
import com.gprinter.service.GpPrintService;
import com.kyleduo.switchbutton.SwitchButton;
import com.pax.api.PortException;
import com.pax.api.PortManager;
import com.pax.api.scanner.BarcodeParam;
import com.pax.api.scanner.ScanResult;
import com.pax.api.scanner.ScannerListener;
import com.pax.api.scanner.ScannerManager;
import com.pospi.dao.PayWayDao;
import com.pospi.pai.pospiflat.base.BaseActivity;
import com.pospi.pai.pospiflat.cash.PoiSelectActivity;
import com.pospi.pai.pospiflat.cash.PointActivity;
import com.pospi.pai.pospiflat.cash.QueryYeActivity;
import com.pospi.pai.pospiflat.cash.ScanActivity;
import com.pospi.pai.pospiflat.login.MainPospiActivity;
import com.pospi.pai.pospiflat.login.UserLoginActivity;
import com.pospi.pai.pospiflat.more.RestOrderActivity;
import com.pospi.pai.pospiflat.more.StatisticsActivity;
import com.pospi.pai.pospiflat.pay.PayActivity;
import com.pospi.pai.pospiflat.util.DeletFile;
import com.pospi.pai.pospiflat.util.PrinterConnectDialog;
import com.pospi.pai.pospiflat.wifi_printer.PrintActivity;
import com.pospi.service.ERPService;
import com.pospi.service.UpLoadService;
import com.pospi.util.App;
import com.pospi.util.CashierLogin_pareseJson;
import com.pospi.util.LogCatHelper;
import com.pospi.util.constant.URL;

import java.io.File;
import java.net.HttpURLConnection;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.xiaopan.android.preference.PreferencesUtils;

public class Main_Login extends BaseActivity {
    @Bind(R.id.iv_cashier)
    ImageView ivCashier;
    @Bind(R.id.tv_cashier)
    TextView tvCashier;
    @Bind(R.id.ll_cashier)
    LinearLayout llCashier;
    @Bind(R.id.iv_member)
    ImageView ivMember;
    @Bind(R.id.tv_member)
    TextView tvMember;
    @Bind(R.id.ll_poiselect)
    LinearLayout llMember;
    @Bind(R.id.iv_statistics)
    ImageView ivStatistics;
    @Bind(R.id.tv_statistics)
    TextView tvStatistics;
    @Bind(R.id.ll_statistics)
    LinearLayout llStatistics;
    @Bind(R.id.iv_shift)
    ImageView ivShift;
    @Bind(R.id.tv_shift)
    TextView tvShift;
    @Bind(R.id.ll_shift)
    LinearLayout llShift;
    @Bind(R.id.login_sz)
    ImageView loginSz;

    private final static String TAG = "Main_Login";
    private Dialog setting_dialog;
    private GpService mGpService = null;
    public static final String CONNECT_STATUS = "connect.status";
    private static final String DEBUG_TAG = "MainActivity";
    private PrinterServiceConnection conn = null;
    private int erp_position = 0;
    private EditText pay_shopId;
    private EditText pay_jiju;
    private EditText pay_opid;
    private TextView deleteMiYa;
    private EditText receipt_num;
    private CheckBox checkBox;
    private boolean isChecked;

    class PrinterServiceConnection implements ServiceConnection {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i("ServiceConnection", "Main_Login onServiceDisconnected()");
            mGpService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i("ServiceConnection", "Main_Login onServiceConnected()");
            mGpService = GpService.Stub.asInterface(service);
            App.setmGpService(mGpService);
            try {
                mGpService.openPort(0, 2, "/dev/bus/usb/001/003", 0);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_login);
        ButterKnife.bind(this);

        new PayWayDao(getApplicationContext()).findAllPayWay();
//        Log.i("Build.MODEL", Build.MODEL);
        if (Build.MODEL.equalsIgnoreCase(URL.MODEL_DT92)) {
            connection();
        }
        //动态申请存储权限
        App.verifyStoragePermissions(Main_Login.this);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK) { //监控/拦截/屏蔽返回键
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        recover();
    }

    @OnClick({R.id.ll_cashier, R.id.ll_poiselect, R.id.ll_statistics, R.id.ll_shift, R.id.login_sz})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_cashier:
               // Toast.makeText(this, "无权限访问", Toast.LENGTH_SHORT).show();

                clickCash();
                startActivity(ScanActivity.class);
//                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                unclickmen();
                unclickshift();
                unclickStatis();
                break;
            case R.id.ll_poiselect:
                clickmem();
                startActivity(PointActivity.class);
                unclickCash();
                unclickshift();
                unclickStatis();
                break;
            case R.id.ll_statistics:
                clickStatis();
                unclickmen();
                unclickshift();
                unclickCash();
                startActivity(StatisticsActivity.class);
                break;
            case R.id.ll_shift:
                clickshift();
                unclickmen();
                unclickCash();
                unclickStatis();
                startActivity(RestOrderActivity.class);
                break;
            case R.id.login_sz:
//                final ScannerManager scannerManager = ScannerManager.getInstance(PayActivity.this);
//                BarcodeParam param = scannerManager.getBarcodeParam();
//
//                param.paramMap.put("preferences_use_front_ccd", true);
//                scannerManager.setBarcodeParam(param);
//                boolean ret = scannerManager.scanOpen();
//                Log.i(TAG, "ret: " + ret);
//                scannerManager.scanStart(new ScannerListener() {
//                    @Override
//                    public void scanOnRead(final ScanResult arg0) {
//                        if (arg0 != null) {
//                            //requestPay(arg0.getContent(), way, wayName, status);
//                            if (way.equals("ali")) {
//                                payType = "3";
//                            } else if (way.equals("wx")) {
//                                payType = "1";
//                            }
//                            if (shopId.equals("") || jiju.equals("") || opid.equals("")) {
//                                Toast.makeText(PayActivity.this, "配置参数不能为空", Toast.LENGTH_SHORT).show();
//                            }
//                            new Thread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    boolean isSuccess = payToMiYa(saasid, arg0.getContent(), shopId, jiju, opid, String.valueOf(maxNO), String.valueOf(Double.parseDouble(shishou.getText().toString()) * 100), payType, "");
//                                    if (way.equals("ali")) {
//                                        payType = "3";
//                                    } else if (way.equals("wx")) {
//                                        payType = "1";
//                                    }
//                                    if (isSuccess) {
//                                        runOnUiThread(new Runnable() {
//                                            @Override
//                                            public void run() {
//                                                getCodeDialog.dismiss();
//                                                print(shishou.getText().toString(), String.valueOf(whatPay));
//                                                showFinshDialog(wayName, whatPay, shishou.getText().toString(), true, null, null);
//                                                sendIntent(wayName, shishou.getText().toString(), zhaoling.getText().toString());
//                                                //InductionCard();
//                                                // Toast.makeText(PayActivity.this, "操作成功", Toast.LENGTH_SHORT).show();
//                                            }
//                                        });
//                                    } else {
//                                        runOnUiThread(new Runnable() {
//                                            @Override
//                                            public void run() {
//                                                closeDialog();
//                                                Toast.makeText(PayActivity.this, "操作失败", Toast.LENGTH_SHORT).show();
//                                            }
//                                        });
//                                    }
//                                }
//                            }).start();
//                        }
//                    }
//
//                    @Override
//                    public void scanOnComplete() {
//                        scannerManager.scanClose();
//                    }
//
//                    @Override
//                    public void scanOnCancel() {
//                        scannerManager.scanClose();
//                    }
//                });

//                startActivity(TableActivity.class);
                showDialog();
                break;

        }
    }

    /**
     * 设置dialog
     * @return
     */
    public Dialog createLoadingDialog() {

        MyclickListener listener = new MyclickListener();

        // 首先得到整个View
        @SuppressLint("InflateParams")
        View view = getLayoutInflater().inflate(R.layout.setting_dialog, null);
        // 获取整个布局
        LinearLayout layout = (LinearLayout) view.findViewById(R.id.setting_dialog);

        /**
         * 退出按钮的点击事件
         */
        Button exit = (Button) view.findViewById(R.id.setting_exit);
        LinearLayout hb = (LinearLayout) view.findViewById(R.id.huanban);
        //无图模式开关
        final Switch aSwitch = (Switch) view.findViewById(R.id.wutu_switch);

        aSwitch.setChecked(getWuTu());
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                saveInfo(b);

                Log.i("onCheckedChanged", b + ">>>>>>>>>");
            }
        });

        TextView back = (TextView) view.findViewById(R.id.setting_back);
//        TextView printer_text = (TextView) view.findViewById(R.id.printer_text);
//        if (!Build.MODEL.equalsIgnoreCase(URL.MODEL_DT92)) {
//            printer_text.setText("商品设置");
//        }
//        LinearLayout about = (LinearLayout) view.findViewById(R.id.setting_about);
        LinearLayout receipt_setting = (LinearLayout) view.findViewById(R.id.receipt_setting);
        LinearLayout account = (LinearLayout) view.findViewById(R.id.setting_account);
//        LinearLayout check = (LinearLayout) view.findViewById(R.id.setting_check);
        LinearLayout print = (LinearLayout) view.findViewById(R.id.printer);
        LinearLayout erp = (LinearLayout) view.findViewById(R.id.erp);

        receipt_setting.setOnClickListener(listener);
        print.setOnClickListener(listener);
        hb.setOnClickListener(listener);
        exit.setOnClickListener(listener);
        back.setOnClickListener(listener);
//        about.setOnClickListener(listener);
        account.setOnClickListener(listener);

        erp.setOnClickListener(listener);
//        check.setOnClickListener(listener);

        // 创建自定义样式的Dialog
        Dialog loadingDialog = new Dialog(this, R.style.setting_dialog);
        // 设置返回键无效
        loadingDialog.setCancelable(false);

        int height = URL.getScreemHeight();
        int width = URL.getScreemWidth();

        loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(width * 3 / 5, height * 4 / 5));

        return loadingDialog;
    }
    //获取无图模式设置
    private boolean getWuTu() {
        SharedPreferences sp = getSharedPreferences("wutu", Context.MODE_PRIVATE);
        return sp.getBoolean("wutuModle", false);
    }
    /**
     * 存储无图模式设置
     * @param c
     */
    private void saveInfo(boolean c) {
        SharedPreferences sp = getSharedPreferences("wutu", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();//获取编辑器
        editor.putBoolean("wutuModle",c);
        editor.commit();


    }

    /**
     * 账号信息Dialog
     */
    public void accountInfoDialog() {
        // 首先得到整个View
        @SuppressLint("InflateParams")
        View view = getLayoutInflater().inflate(R.layout.account_info_dialog, null);
        // 获取整个布局
        LinearLayout layout = (LinearLayout) view.findViewById(R.id.account_dialog);

        TextView account = (TextView) view.findViewById(R.id.account);
        TextView shop_id = (TextView) view.findViewById(R.id.shop_id);
        TextView cash_register_id = (TextView) view.findViewById(R.id.cash_register_id);
        TextView cashier_id = (TextView) view.findViewById(R.id.cashier_id);

        int whichOne = getSharedPreferences("islogin", Context.MODE_PRIVATE).getInt("which", 0);
        cashier_id.setText(new CashierLogin_pareseJson().parese(
                getSharedPreferences("cashierMsgDtos", Context.MODE_PRIVATE)
                        .getString("cashierMsgDtos", ""))
                .get(whichOne).getName());

        account.setText(getApplicationContext()
                .getSharedPreferences("shop_user", Context.MODE_PRIVATE).getString("shop_user", ""));

        SharedPreferences preferences = getApplicationContext().getSharedPreferences("Token", Context.MODE_PRIVATE);//得到第一个登陆之后存储的Token  里面存储的有机器的设备号 店铺的id
        String value = preferences.getString("value", "");
        String[] names = value.split("\\,");
        shop_id.setText(names[3]);

        cash_register_id.setText("100125");//未设置动态参数

        // 创建自定义样式的Dialog
        final Dialog accountDialog = new Dialog(this, R.style.setting_dialog);
        // 设置返回键无效
        accountDialog.setCancelable(false);

        int height = URL.getScreemHeight();
        int width = URL.getScreemWidth();

        accountDialog.setContentView(layout, new LinearLayout.LayoutParams(width * 3 / 5, height * 4 / 5));

        accountDialog.show();
        /**
         * 返回按钮的点击事件
         */
        TextView back = (TextView) view.findViewById(R.id.set_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accountDialog.dismiss();
            }
        });
    }

    /**
     * 关于POSPI Dialog
     */
    public void aboutPospiDialog() {
        // 首先得到整个View
        @SuppressLint("InflateParams")
        View view = getLayoutInflater().inflate(R.layout.about_dialog, null);
        // 获取整个布局
        LinearLayout layout = (LinearLayout) view.findViewById(R.id.about_dialog);

        TextView tv_version = (TextView) view.findViewById(R.id.tv_version);
        TextView btn_jump = (TextView) view.findViewById(R.id.btn_jump);

        btn_jump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("http://pos.pospi.com/");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        TextView txt_back = (TextView) view.findViewById(R.id.txt_back);

        String versionName;
        try {
            PackageManager pm = getApplicationContext().getPackageManager();
            PackageInfo pi = pm.getPackageInfo(getApplicationContext().getPackageName(), 0);
            versionName = pi.versionName;
            if (versionName != null) {
                tv_version.setText(String.format("POSPi v%s", versionName));
            }
        } catch (Exception e) {
            Log.e("VersionInfo", "Exception", e);
        }

        // 创建自定义样式的Dialog
        final Dialog aboutDialog = new Dialog(this, R.style.setting_dialog);
        // 设置返回键无效
        aboutDialog.setCancelable(false);

        int height = URL.getScreemHeight();
        int width = URL.getScreemWidth();

        aboutDialog.setContentView(layout, new LinearLayout.LayoutParams(width * 3 / 5, height * 4 / 5));

//        Window window = aboutDialog.getWindow();
        //设置显示动画
//        window.setWindowAnimations(R.style.dialog_anim);

        aboutDialog.show();
        /**
         * 返回按钮的点击事件
         */
        txt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aboutDialog.dismiss();
            }
        });
    }

    /**
     * 获取米雅配置参数
     *
     * @param param
     * @return
     */
    private String getParam(String param) {
        SharedPreferences sp = getSharedPreferences("receipt_num", Context.MODE_PRIVATE);
        return sp.getString(param, "");
    }
    /**
     * 设置小票 Dialog
     */
    public void setReceiptDialog() {
        // 首先得到整个View
        @SuppressLint("InflateParams")
        View view = getLayoutInflater().inflate(R.layout.receipt_setting_dialog, null);
        // 获取整个布局
        LinearLayout layout = (LinearLayout) view.findViewById(R.id.receipt_dialog);

        TextView txt_back = (TextView) view.findViewById(R.id.receipt_back);
        receipt_num = (EditText) view.findViewById(R.id.receipt_num);
        receipt_num.setText(String.valueOf(this.getSharedPreferences("receipt_num", Context.MODE_PRIVATE).getInt("receipt_num", 1)));
        Button btn_sure = (Button) view.findViewById(R.id.receipt_btn_sure);
        checkBox = (CheckBox) view.findViewById(R.id.scanner_box);
        checkBox.setChecked(getSharedPreferences("receipt_num", Context.MODE_PRIVATE).getBoolean("scannerConfig",false));
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isChecked = b;
            }
        });

        //米雅支付参数配置
        pay_shopId = (EditText) view.findViewById(R.id.pay_shopId);
        pay_jiju = (EditText) view.findViewById(R.id.pay_jiju);
        pay_opid = (EditText) view.findViewById(R.id.pay_opid);
        pay_shopId.setText(getParam("pay_shopId"));
        pay_jiju.setText(getParam("pay_jiju"));
        pay_opid.setText(getParam("pay_opid"));


//        deleteMiYa = (TextView) view.findViewById(R.id.delete_miya);

//        SharedPreferences sharedPreferences = getSharedPreferences("receipt_num", Context.MODE_PRIVATE);
//        receipt_num.setText(String.valueOf(sharedPreferences.getInt("receipt_num", 1)));
        SwitchButton switchButton = (SwitchButton) view.findViewById(R.id.switchButton);
        switchButton.setChecked(App.isUpLoad());
        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                App.setIsUpLoad(isChecked);
                PreferencesUtils.putBoolean(App.getContext(), "IsUpLoad", isChecked);
                if (App.isUpLoad()) {//如果打开上传订单开关。则开启上传服务
                    Intent i = new Intent(getApplicationContext(), UpLoadService.class);
                    startService(i);
                    SharedPreferences preferences = App.getContext().getSharedPreferences("ERP", Context.MODE_PRIVATE);
                    String url = preferences.getString("url", "");//地址
                    if (!TextUtils.isEmpty(url)) {
                        Intent erp = new Intent(getApplicationContext(), ERPService.class);
                        startService(erp);
                    }
                }
            }
        });
        // 创建自定义样式的Dialog
        final Dialog aboutDialog = new Dialog(this, R.style.setting_dialog);
        // 设置返回键无效
        aboutDialog.setCancelable(false);

        int height = URL.getScreemHeight();
        int width = URL.getScreemWidth();

        aboutDialog.setContentView(layout, new LinearLayout.LayoutParams(width * 3 / 5, height * 4 / 5));

//        Window window = aboutDialog.getWindow();
        //设置显示动画
//        window.setWindowAnimations(R.style.dialog_anim);

        aboutDialog.show();
        /**
         * 返回按钮的点击事件
         */
        txt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aboutDialog.dismiss();
            }
        });

        btn_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //存储本地
                String num = receipt_num.getText().toString().trim();
                String shopId = pay_shopId.getText().toString().trim();
                String jiju = pay_jiju.getText().toString().trim();
                String opid = pay_opid.getText().toString().trim();
                if (TextUtils.isEmpty(shopId) || TextUtils.isEmpty(jiju) || TextUtils.isEmpty(opid)) {
                    showToast("配置参数不能为空");
                    return;
                }
                if (TextUtils.isEmpty(num) ) {
                    showToast("小票数不能为空！");
                } else {
                    SharedPreferences sharedPreferences = getSharedPreferences("receipt_num", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
                    editor.putInt("receipt_num", Integer.parseInt(num));
                    //保存米雅支付配置
                    editor.putString("pay_shopId" , shopId);
                    editor.putString("pay_jiju" , jiju);
                    editor.putString("pay_opid" , opid);
                    //保存扫码枪选择
                    editor.putBoolean("scannerConfig", isChecked);


                    editor.apply();
                    aboutDialog.dismiss();
                }
            }
        });
       /* deleteMiYa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(Main_Login.this).setTitle("提示")//设置对话框标题  
                        .setMessage("确定删除配置文件？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String filePath = Environment.getExternalStorageDirectory().getPath() + "/miyajpos";
                        File f = new File(filePath );
                        DeletFile.deleteAllFiles(f);
                        Toast.makeText(Main_Login.this, "删除成功", Toast.LENGTH_SHORT).show();
                    }
                }).setNegativeButton("取消", null).show();
            }
        });*/
    }

    /**
     * 关于Ping Dialog
     */
    public void pingDialog() {
        // 首先得到整个View
        @SuppressLint("InflateParams")
        View view = getLayoutInflater().inflate(R.layout.ping_dialog, null);
        // 获取整个布局
        LinearLayout layout = (LinearLayout) view.findViewById(R.id.ping_dialog);
        TextView tv_back = (TextView) view.findViewById(R.id.tv_back);
//        tv_back.setClickable(false);
        TextView tv_router = (TextView) view.findViewById(R.id.tv_router);
        TextView tv_router_state = (TextView) view.findViewById(R.id.tv_router_state);
        final TextView server_state = (TextView) view.findViewById(R.id.server_state);
//        TextView run_state = (TextView) view.findViewById(R.id.run_state);


        //设置WIFI名称给tv_router的值
        WifiManager wifiMgr = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        int wifiState = wifiMgr.getWifiState();//3为wifi连接
//        Log.i("wifiState", "" + wifiState);
        if (wifiState == 3) {
            WifiInfo info = wifiMgr.getConnectionInfo();
            String wifiId = info != null ? info.getSSID() : "";
            tv_router.setText(wifiId.replace("\"", ""));
            tv_router_state.setText(R.string.connect_well);
        }

        // 创建自定义样式的Dialog
        final Dialog dialog = new Dialog(this, R.style.setting_dialog);
        // 设置返回键无效
        dialog.setCancelable(false);

        int height = URL.getScreemHeight();
        int width = URL.getScreemWidth();

        dialog.setContentView(layout, new LinearLayout.LayoutParams(width * 3 / 5, height * 4 / 5));

//        Window window = dialog.getWindow();
        //设置显示动画
//        window.setWindowAnimations(R.style.dialog_anim);

        try {
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpURLConnection connection;
                    java.net.URL url = new java.net.URL("http://pos.pospi.com/");
                    connection = (HttpURLConnection) url.openConnection();
                    connection.connect();
                    int code = connection.getResponseCode();
                    Log.i("code", "code:" + code);
                    if (code == 200) {
                        server_state.setText(R.string.connect_well);//子线程中对UI进行操作！！！！
                    } else {
                        server_state.setText("连接不稳定");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
        /**
         * 返回按钮的点击事件
         */
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    /**
     * 关于ERP Dialog
     */
    public void erpDialog() {
        try {
            // 首先得到整个View
            @SuppressLint("InflateParams")
            View view = getLayoutInflater().inflate(R.layout.erp_dialog, null);

            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);//设置软键盘不弹

            // 获取整个布局
            LinearLayout layout = (LinearLayout) view.findViewById(R.id.erp_dialog);
            TextView erp_back = (TextView) view.findViewById(R.id.erp_back);
            Spinner erp_spinner = (Spinner) view.findViewById(R.id.erp_spinner);
            final EditText erp_url = (EditText) view.findViewById(R.id.erp_url);
            final EditText erp_mallid = (EditText) view.findViewById(R.id.erp_mallid);
            final EditText erp_storecode = (EditText) view.findViewById(R.id.erp_storecode);
            final EditText erp_itemcode = (EditText) view.findViewById(R.id.erp_itemcode);
            final EditText erp_id = (EditText) view.findViewById(R.id.erp_id);
            final EditText erp_pwd = (EditText) view.findViewById(R.id.erp_pwd);
            Button erp_btn_sure = (Button) view.findViewById(R.id.erp_btn_sure);

            SharedPreferences preferences = getSharedPreferences("ERP", Context.MODE_PRIVATE);
            if (!preferences.getString("url", "").equals("")) {
                erp_url.setText(preferences.getString("url", ""));
                erp_mallid.setText(preferences.getString("mallid", ""));
                erp_storecode.setText(preferences.getString("storecode", ""));
                erp_itemcode.setText(preferences.getString("itemcode", ""));
                erp_id.setText(preferences.getString("id", ""));
                erp_pwd.setText(preferences.getString("pwd", ""));
            }

            erp_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    erp_position = position;
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            // 创建自定义样式的Dialog
            final Dialog dialog = new Dialog(this, R.style.setting_dialog);
            // 设置返回键无效
            dialog.setCancelable(false);

            int height = URL.getScreemHeight();
            int width = URL.getScreemWidth();

            dialog.setContentView(layout, new LinearLayout.LayoutParams(width * 3 / 5, height * 4 / 5));

//        Window window = dialog.getWindow();
            //设置显示动画
//        window.setWindowAnimations(R.style.dialog_anim);

            dialog.show();
            /**
             * 返回按钮的点击事件
             */
            erp_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            /**
             * 确定按钮的点击事件
             */
            erp_btn_sure.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (erp_url.getText().toString().isEmpty() || erp_storecode.getText().toString().isEmpty()
                            || erp_itemcode.getText().toString().isEmpty()) {
                        showToast("请完善信息");
                    } else {
                        SharedPreferences sharedPreferences = getSharedPreferences("ERP", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
                        editor.putInt("position", erp_position);
                        editor.putString("url", erp_url.getText().toString().trim());
                        editor.putString("mallid", erp_mallid.getText().toString().trim());
                        editor.putString("storecode", erp_storecode.getText().toString().trim());
                        editor.putString("id", erp_id.getText().toString().trim());
                        editor.putString("pwd", erp_pwd.getText().toString().trim());
                        editor.putString("itemcode", erp_itemcode.getText().toString().trim());
                        editor.apply();//提交修改
                        if (App.isUpLoad()) {//如果打开上传订单开关。则开启上传服务
                            Intent erp = new Intent(getApplicationContext(), ERPService.class);
                            startService(erp);
                        }
                        erp_url.setText("");
                        erp_mallid.setText("");
                        erp_storecode.setText("");
                        erp_id.setText("");
                        erp_pwd.setText("");
                        erp_itemcode.setText("");
                        showToast("保存成功!");
                        dialog.dismiss();
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showDialog() {
        if (setting_dialog == null) {
            setting_dialog = createLoadingDialog();
            setting_dialog.show();
        }
    }

    public void closeDialog() {
        if (setting_dialog != null) {
            setting_dialog.dismiss();
            setting_dialog = null;
        }
    }


    class MyclickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.setting_exit://弹出对话框提示用户是否退出
                    showBoolExitDialog();
                    break;
                case R.id.setting_back:
                    closeDialog();//关闭设置的弹出的dialog
                    break;
//                case R.id.setting_about:
//                    aboutPospiDialog();
//                    break;
                case R.id.receipt_setting:
                    setReceiptDialog();
                    break;
                case R.id.setting_account:
                    accountInfoDialog();
                    break;
//                case R.id.setting_check:
//                    pingDialog();
//                    break;
                case R.id.huanban:
                    killAllActivity();
                    startActivity(MainPospiActivity.class);
                    break;
                case R.id.erp:
                    erpDialog();
                    break;
                case R.id.printer:
//                    Log.d("printer", "openPortConfigurationDialog ");
                    if (Build.MODEL.equalsIgnoreCase(URL.MODEL_DT92)) {
                        Intent intent = new Intent(Main_Login.this,
                                PrinterConnectDialog.class);
                        try {
                            boolean[] state = getConnectState();
                            intent.putExtra(CONNECT_STATUS, state);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(Main_Login.this,
                                PrintActivity.class);
                        startActivity(intent);
                    }
                    break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy: ");
//        Intent erp = new Intent(getApplicationContext(), ERPService.class);
//        stopService(erp);

        Intent i = new Intent(getApplicationContext(), UpLoadService.class);
        stopService(i);
        if (conn != null) {
            unbindService(conn); // unBindService
        }
        String path = Environment.getExternalStorageDirectory()
                + "/ipos";
        LogCatHelper logCatHelper = LogCatHelper.getInstance(this, path);
        logCatHelper.stop();
    }

    private void connection() {
        conn = new PrinterServiceConnection();
        Intent intent = new Intent(Main_Login.this, GpPrintService.class);
        bindService(intent, conn, Context.BIND_AUTO_CREATE);// bindService
    }

    /**
     * 得到打印机状态
     *
     * @return
     */
    public boolean[] getConnectState() {
        boolean[] state = new boolean[GpPrintService.MAX_PRINTER_CNT];
        for (int i = 0; i < GpPrintService.MAX_PRINTER_CNT; i++) {
            state[i] = false;
        }
        for (int i = 0; i < GpPrintService.MAX_PRINTER_CNT; i++) {
            try {
                if (mGpService.getPrinterConnectStatus(i) == GpDevice.STATE_CONNECTED) {
                    state[i] = true;
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return state;
    }

    private AlertDialog exit_dialog;

    public void showBoolExitDialog() {
        exit_dialog = new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("确定退出登录？")
                .setPositiveButton("确定退出", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        exit_dialog.dismiss();
                        closeDialog();
                        killAllActivity();//关闭所有的activity
                        SharedPreferences settings = getSharedPreferences("login_saveInfo", MODE_PRIVATE);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.remove("login");
                        editor.apply();//删除已经存储的登录的信息
                        startActivity(UserLoginActivity.class);//打开用户的登录的界面

                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        exit_dialog.dismiss();
                    }
                })
                .create();
        exit_dialog.show();
    }

    /**
     * 点击收银之后收银变色
     */
    public void clickCash() {
        llCashier.setBackgroundColor(getResources().getColor(R.color.blue_sale));
        ivCashier.setImageResource(R.drawable.icon_shouying2);
        tvCashier.setTextColor(getResources().getColor(R.color.white));
    }

    public void unclickCash() {
        llCashier.setBackgroundColor(getResources().getColor(R.color.white));
        ivCashier.setImageResource(R.drawable.icon_shouying);
        tvCashier.setTextColor(getResources().getColor(R.color.blue_sale));
    }


    public void clickStatis() {
        llStatistics.setBackgroundColor(getResources().getColor(R.color.blue_sale));
        ivStatistics.setImageResource(R.drawable.icon_tongji2);
        tvStatistics.setTextColor(getResources().getColor(R.color.white));
    }


    public void unclickStatis() {
        llStatistics.setBackgroundColor(getResources().getColor(R.color.white));
        ivStatistics.setImageResource(R.drawable.icon_tongji);
        tvStatistics.setTextColor(getResources().getColor(R.color.blue_sale));
    }


    public void clickmem() {
        llMember.setBackgroundColor(getResources().getColor(R.color.blue_sale));
        ivMember.setImageResource(R.drawable.icon_huiyuan2);
        tvMember.setTextColor(getResources().getColor(R.color.white));
    }

    public void unclickmen() {
        llMember.setBackgroundColor(getResources().getColor(R.color.white));
        ivMember.setImageResource(R.drawable.icon_huiyuan);
        tvMember.setTextColor(getResources().getColor(R.color.blue_sale));
    }


    public void clickshift() {
        llShift.setBackgroundColor(getResources().getColor(R.color.blue_sale));
        ivShift.setImageResource(R.drawable.icon_gdd);
        tvShift.setTextColor(getResources().getColor(R.color.white));
    }


    public void unclickshift() {
        llShift.setBackgroundColor(getResources().getColor(R.color.white));
        ivShift.setImageResource(R.drawable.icon_gd);
        tvShift.setTextColor(getResources().getColor(R.color.blue_sale));
    }

    private void recover() {
        llCashier.setBackgroundColor(getResources().getColor(R.color.white));
        ivCashier.setImageResource(R.drawable.icon_shouying);
        tvCashier.setTextColor(getResources().getColor(R.color.blue_sale));
        llMember.setBackgroundColor(getResources().getColor(R.color.white));
        ivMember.setImageResource(R.drawable.icon_huiyuan);
        tvMember.setTextColor(getResources().getColor(R.color.blue_sale));
        llStatistics.setBackgroundColor(getResources().getColor(R.color.white));
        ivStatistics.setImageResource(R.drawable.icon_tongji);
        tvStatistics.setTextColor(getResources().getColor(R.color.blue_sale));
        llShift.setBackgroundColor(getResources().getColor(R.color.white));
        ivShift.setImageResource(R.drawable.icon_gd);
        tvShift.setTextColor(getResources().getColor(R.color.blue_sale));
    }

}
