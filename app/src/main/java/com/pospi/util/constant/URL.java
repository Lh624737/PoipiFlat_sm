package com.pospi.util.constant;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.pospi.pai.yunpos.R;
import com.pospi.util.App;
import com.pospi.util.SystemBarTintManager;

/**
 * 向服务器请求数据的域名   把域名存储起来
 * Created by Qiyan on 2016/5/11.
 */
public class URL {

    private static Context context = App.getContext();

    /**
     * 服务器地址
     */
//    public String HOST= "http://192.7.7.172";http://192.7.7.172:8889
    public static String HOST = "http://106.15.72.240:36666/sysapi/";
    private String PayHost;

    public String host() {
        try {
            HOST = context.getSharedPreferences("url", Context.MODE_PRIVATE).getString("url", "http://106.15.72.240:36666/sysapi/");
        } catch (NullPointerException e) {
            HOST = "http://106.15.72.240:36666/sysapi/";//http://192.7.7.172:9999
        }
        return HOST;
    }

    public String payhost() {
        try {
            PayHost = context.getSharedPreferences("url", Context.MODE_PRIVATE).getString("pay_url", "http://pay.pospi.com");
        } catch (NullPointerException e) {
            PayHost = "http://pay.pospi.com";//http://pay.pospi.comhttp://www.101base.cn/index.php
        }
        return PayHost;
    }

    public final static String MODEL_T8 = "T8";
    public final static String MODEL_D800 = "d800";
    public final static String MODEL_E500 = "E500";
    public final static String MODEL_DT92 = "DT92II_printer";
    public final static String MODEL_IPOS100 = "IPOS100";
    public final static String MODEL_T1 = "t1host";
    public final static String MODEL_T2 = "T2";
    public final static String MODEL_D1 = "D1";
    public final static int ORDERTYPE_SALE = 1111;//正常销售模式
    public final static int ORDERTYPE_REFUND = 2222;//退货的模式
    public final static int hasReturnGoods_Yes = 112;//退过货
    public final static int hasReturnGoods_No = 113;//没有过退货

    public final static int YES = 250;//已经完成的订单
    public final static int NO = 251;//未完成的订单
    public final static int ZHUOTai = 252;//桌台的订单
    public final static int BAOLIU = 253;//保留的订单
    private String key = "ce3e5f7eef24f699fae4902db18276c1";

    /**
     * 登陆
     */
    public String LOGIN = host() + "/api.php?m=opendkq&openkey="+key+"&a=login";
    /**
     * 商品
     */
    public String SYNC_MENUS = host() + "/api.php?m=opendkq&openkey="+key+"&a=goods";
    /**
     * 收银员
     */
    public String SYNCCASHIER = host() + "/api.php?m=opendkq&openkey="+key+"&a=cashier";
    /**
     * 分类
     */
    public String SYNCCATEGORY = host() + "/index.php/Api/Root/SyncCategory";
    /**
     * 桌子
     */
    public String TABLE = host() + "/api.php?m=opendkq&openkey="+key+"&a=desk";
    /**
     * 上传订单
     */
    public String SYNCORDER = host() + "/api.php?m=opendkq&openkey="+key+"&a=sell";
    /**
     * 店铺信息
     */
    public String SYNCSHOP = host() + "/index.php/Api/Root/SyncShop";
    /**
     * 支付方式
     */
    public String PAYMENT = "http://pay.pospi.com" + "/Api/Pay/Payment";
    /**
     * 退款
     */
    public String REFUND = "http://pay.pospi.com"  + "/Api/Pay/Refund";
    /**
     * 会员查询
     */
    public String FINDMEMBER = host() + "/index.php/Api/Root/FindMember";
    /**
     * 桌台查询/SyncPayType
     */
    public String SYNCTable = host() + "/index.php/Api/Root/SyncTable";
    /**
     * 下载支付方式
     */
    public String SyncPayType = host() + "/index.php/Api/Root/SyncPayType";
    /**
     * 上传储值卡交易流水
     */
    public String AddCardRecord = host() + "/index.php/Api/Root/AddCardRecord";
    /**
     * 下载储值卡信息
     */
    public String QueryCard = host() + "/index.php/Api/OnLineCard/QueryCard";
    public String SyncTime = host() + "/index.php/Api/OnLineCard/SyncTime";


    /**
     * 下载做法
     */
    public String SyncModified = host() + "/index.php/Api/Root/SyncModified";
    /**
     * 下载做法组
     */
    public String SyncModifiedGroup = host() + "/api.php?m=opendkq&openkey="+key+"&a=ys";
    /**
     * 添加或修改类别
     */
    public String AddUpCategory = host() + "/index.php/Api/Root/AddUpCategory";
    /**
     * 删除类别
     */
    public String DelCategory = host() + "/index.php/Api/Root/DelCategory";
    /**
     * 添加或修改商品
     */
    public String AddUpMenus = host() + "/index.php/Api/Root/AddUpMenus";
    /**
     * 删除商品
     */
    public String DelMenus = host() + "/index.php/Api/Root/DelMenus";

    /**
     * 下載儲值卡狀態
     */
    public String getCardStatus = host() + "/index.php/Api/Root/getCardStatus";
//
//    /**
//     * 登陆
//     */
//    public String LOGIN = host() + "/api/root/login";
//    /**
//     * 商品
//     */
//    public String SYNC_MENUS = host() + "/api/root/syncmenus";
//    /**
//     * 收银员
//     */
//    public String SYNCCASHIER = host() + "/api/root/synccashier";
//    /**
//     * 分类
//     */
//    public String SYNCCATEGORY = host() + "/api/root/synccategory";
//    /**
//     * 单据号
//     */
//    public String GETNO = host() + "/api/root/getno";
//    /**
//     * 上传订单
//     */
//    public String SYNCORDER = host() + "/api/root/syncorder";
//    /**
//     * 店铺信息
//     */
//    public String SYNCSHOP = host() + "/api/root/syncshop";
//    /**
//     * 支付方式
//     */
//    public String PAYMENT = "http://pay.pospi.com" + "/Api/Pay/Payment";
//    /**
//     * 退款
//     */
//    public String REFUND = "http://pay.pospi.com"  + "/Api/Pay/Refund";
//    /**
//     * 会员查询
//     */
//    public String FINDMEMBER = host() + "/api/root/findmember";
//    /**
//     * 桌台查询/SyncPayType
//     */
//    public String SYNCTable = host() + "/api/root/synctable";
//    /**
//     * 下载支付方式
//     */
//    public String SyncPayType = host() + "/api/root/syncpaytype";
//    /**
//     * 上传储值卡交易流水
//     */
//    public String AddCardRecord = host() + "/api/root/addcardrecord";
//    /**
//     * 下载储值卡信息
//     */
//    public String QueryCard = host() + "/api/onlinecard/querycard";
//    public String SyncTime = host() + "/api/onlinecard/synctime";
//
//
//    /**
//     * 下载做法
//     */
//    public String SyncModified = host() + "/api/root/syncmodified";
//    /**
//     * 下载做法组
//     */
//    public String SyncModifiedGroup = host() + "/api/root/syncmodifiedgroup";
//    /**
//     * 添加或修改类别
//     */
//    public String AddUpCategory = host() + "/api/root/addupcategory";
//    /**
//     * 删除类别
//     */
//    public String DelCategory = host() + "/api/root/delcategory";
//    /**
//     * 添加或修改商品
//     */
//    public String AddUpMenus = host() + "/api/root/addupmenus";
//    /**
//     * 删除商品
//     */
//    public String DelMenus = host() + "/api/root/delmenus";
//
//    /**
//     * 下載儲值卡狀態
//     */
//    public String getCardStatus = host() + "/api/root/getcardstatus";
    /**
     * ERP商品
     */
    public String postsalescreate = "http://pos.pospi.com:9002/Api/Service/postsalescreate";
    public String postsalescreate2 = "http://pos.pospi.com:9002/Api/Service/postsalescreate2";

    /**
     * 得到屏幕高度
     */
    public static int getScreemHeight() {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getHeight();
    }

    /**
     * 得到屏幕宽度
     */
    public static int getScreemWidth() {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getWidth();
    }

    /**
     * 改变通知栏颜色
     */
    public static void setSystemBar(Activity activity) {
        if (android.os.Build.VERSION.SDK_INT > 18) {
            Window window = activity.getWindow();

            //取消设置透明状态栏,使 ContentView 内容不再覆盖状态栏
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

            // 创建状态栏的管理实例
            SystemBarTintManager tintManager = new SystemBarTintManager(activity);
            // 激活状态栏设置
            tintManager.setStatusBarTintEnabled(true);
            // 激活导航栏设置
            tintManager.setNavigationBarTintEnabled(true);
            // 设置一个颜色给系统栏
            tintManager.setTintColor(activity.getResources().getColor(R.color.blue_sale));

            ViewGroup mContentView = (ViewGroup) activity.findViewById(Window.ID_ANDROID_CONTENT);
            View mChildView = mContentView.getChildAt(0);
            if (mChildView != null) {
                //注意不是设置 ContentView 的 FitsSystemWindows, 而是设置 ContentView 的第一个子 View . 预留出系统 View 的空间.
                ViewCompat.setFitsSystemWindows(mChildView, true);
            }
        }
    }

}
