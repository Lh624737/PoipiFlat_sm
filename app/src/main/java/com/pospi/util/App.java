package com.pospi.util;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;

import com.android.print.sdk.PrinterInstance;
import com.gprinter.aidl.GpService;
import com.lany.sp.BuildConfig;
import com.lany.sp.SPHelper;
import com.newland.jsums.paylib.model.OrderInfo;
import com.pax.dal.IDAL;
import com.pax.neptunelite.api.NeptuneLiteUser;
import com.pospi.dao.OrderDao;
import com.pospi.greendao.DaoMaster;
import com.pospi.greendao.DaoSession;
import com.pospi.pai.yunpos.util.DeletFile;
import com.pospi.service.ERPService;
import com.pospi.service.UpLoadService;
import com.pospi.util.constant.URL;
import com.tsy.sdk.myokhttp.MyOkHttp;

import java.io.File;
import java.util.concurrent.TimeUnit;

import me.xiaopan.android.preference.PreferencesUtils;
import okhttp3.OkHttpClient;

/**
 * Created by huangqi on 2016/7/24.
 */
public class App extends Application {
    public static boolean isAidl;
    public static GpService mGpService = null;
    private static boolean state = false;
    private static Context context;
    private static App app;
    private static boolean isUpLoad = true;
    private Typeface typeface;
    private Typeface numTypeface;

    public static boolean isHasNoUpLoad() {
        return hasNoUpLoad;
    }

    public static void setHasNoUpLoad(boolean hasNoUpLoad) {
        App.hasNoUpLoad = hasNoUpLoad;
    }

    private static boolean hasNoUpLoad = false;

    public static PrinterInstance mPrinter;
    private MyOkHttp myOkHttp;
    private IDAL idal;
//    public static boolean openLocalPrinter = true;
//    public static boolean openWifiPrinter = false;
//    public static Socket wifiPrinterSocket;
    private SQLiteDatabase db;
    private DaoMaster daoMaster;
    private DaoSession daoSession;

    public Typeface getNumTypeface() {
        return numTypeface;
    }

    public Typeface getTypeface() {
        return typeface;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        app = this;
        initDataBase();
        //初始化网络请求
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(15000L, TimeUnit.MILLISECONDS)
                .readTimeout(15000L, TimeUnit.MILLISECONDS).build();
        myOkHttp = new MyOkHttp(okHttpClient);
        //初始化sharedprefece
        SPHelper.getInstance().init(this, BuildConfig.DEBUG);
        initDataBase();
        //初始化字体库
        typeface = Typeface.createFromAsset(getAssets(), "fonts/PingFang.ttf");
        numTypeface = Typeface.createFromAsset(getAssets(), "fonts/Dosis-Regular.ttf");

        if (!(new OrderDao(App.getContext()).findOrderERPNOUpLoad().size() > 0) || !(new OrderDao(App.getContext()).findOrderNOUpLoad().size() > 0)) {
            App.hasNoUpLoad = false;
        } else {
            App.hasNoUpLoad = true;
        }

        isUpLoad = PreferencesUtils.getBoolean(App.getContext(), "IsUpLoad", true);
        if (isUpLoad) {//如果打开上传订单开关。则开启上传服务
//            Intent i = new Intent();
//            i.setClass(this, UpLoadService.class);
//            startService(i);
            SharedPreferences preferences = this.getSharedPreferences("ERP", Context.MODE_PRIVATE);
            String url = preferences.getString("url", "");//地址
            //检测机器是否为佳博，若是就开启erp服务
//            if (!TextUtils.isEmpty(url) && Build.MODEL.equals(URL.MODEL_DT92)) {
//                Intent erp = new Intent(context, ERPService.class);
//                startService(erp);
//            }

        }
        //检测机器是否为d800，若是则初始化米雅支付配置文件
        if (Build.MODEL.equals(URL.MODEL_D800)) {
            initMiYa();
        }
        if (Build.MODEL.equals(URL.MODEL_E500)) {
            try {
                idal = NeptuneLiteUser.getInstance().getDal(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        isAidl = true;
        AidlUtil.getInstance().connectPrinterService(this);


    }
    public void setAidl(boolean aidl) {
        isAidl = aidl;
    }
    //初始化greendao
    private void initDataBase() {
        DaoMaster.DevOpenHelper openHelper = new DaoMaster.DevOpenHelper(this, "pospi_db", null);
        db = openHelper.getWritableDatabase();
        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
    }
    public MyOkHttp getMyOkHttp() {
        return myOkHttp;
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }

    public static App getInstance(){
        return app;
    }
    public IDAL getIdal(){
        return idal;
    }

    private void initMiYa() {
        /**
         * 初始化米雅配置文件
         */
        String filePath = android.os.Environment.getExternalStorageDirectory().getPath() + "/miyajpos/miyaapay/config";
        File f = new File(filePath );
        if (f.exists()){
            DeletFile.deleteAllFiles(f);
        }
        WriteToSDcard wtd = new WriteToSDcard(this);
        wtd.write("config", "miyaConfig.txt");
        wtd.write("config" ,"payConfig.txt");
        wtd.write("config", "printConfig.txt");
        wtd.write("miyalog" , "log.log");
        wtd.write("miyareceitp" , "receitp.txt");
    }

    private static OrderInfo lastOrderInfo = null;

    public static OrderInfo getLastOrderInfo() {
        return lastOrderInfo;
    }

    public static void setLastOrderInfo(OrderInfo lastOrderInfo) {
        App.lastOrderInfo = lastOrderInfo;
    }

    public static boolean isUpLoad() {
        return isUpLoad;
    }

    public static void setIsUpLoad(boolean isUpLoad) {
        App.isUpLoad = isUpLoad;
    }

    public static Context getContext() {
        return context;
    }

    public static boolean isState() {
        return state;
    }

    public static void setState(boolean state) {
        App.state = state;
    }
    //    private PrinterServiceConnection conn = null;

    public static GpService getmGpService() {
        return mGpService;
    }

    public static void setmGpService(GpService mGpService) {
        App.mGpService = mGpService;
    }

    private static final int REQUEST_EXTERNAL_STORAGE = 1;  
               private static String[] PERMISSIONS_STORAGE = {  
                   Manifest.permission.READ_EXTERNAL_STORAGE,
                   Manifest.permission.WRITE_EXTERNAL_STORAGE
               };
    public static void verifyStoragePermissions(Activity activity) {
                // Check if we have write permission  
                int permission = ActivityCompat.checkSelfPermission(activity,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE);  
          
                if (permission != PackageManager.PERMISSION_GRANTED) {
                        // We don't have permission so prompt the user  
                        ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,  
                                        REQUEST_EXTERNAL_STORAGE);  
                    }  
            }  
}
