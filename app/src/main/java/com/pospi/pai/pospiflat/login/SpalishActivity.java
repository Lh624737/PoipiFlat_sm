package com.pospi.pai.pospiflat.login;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.pospi.dto.UpdateInfo;
import com.pospi.pai.pospiflat.R;
import com.pospi.pai.pospiflat.base.BaseActivity;
import com.pospi.util.DownLoadUtil;
import com.pospi.util.LogCatHelper;
import com.pospi.util.UpdateInfoParser;

import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class SpalishActivity extends BaseActivity {

    private static final int GET_INFO_SUCCESS = 10;
    private static final int SERVER_ERROR = 11;
    private static final int SERVER_URL_ERROR = 12;
    private static final int PROTOCOL_ERROR = 13;
    private static final int IO_ERROR = 14;
    private static final int XML_PARSE_ERROR = 15;
    private static final int DOWNLOAD_SUCCESS = 16;
    private static final int DOWNLOAD_ERROR = 17;
    protected static final String TAG = "SplashActivity";
    private UpdateInfo info;
    private ProgressDialog pd;

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case XML_PARSE_ERROR:
                    showToast("xml解析错误");
                    loadMainUI();
                    break;
                case IO_ERROR:
//                    showToast("I/O异常");
                    loadMainUI();
                    break;
                case PROTOCOL_ERROR:
                    showToast("PROTOCOL_ERROR");
                    loadMainUI();
                    break;
                case SERVER_URL_ERROR:
                    showToast("SERVER_URL_ERROR");
                    loadMainUI();
                    break;
                case SERVER_ERROR:
                    showToast("SERVER_ERROR");
                    loadMainUI();
                    break;
                case GET_INFO_SUCCESS:
                    int server_version = Integer.parseInt(info.getVersion().trim());
                    int current_version = getVersion();
                    if (server_version == current_version) {
                        loadMainUI();
                    } else {
                        if (server_version > current_version) {
                            showUpdateDialog();
                        } else {
                            loadMainUI();
                        }
                    }
                    break;
                case DOWNLOAD_SUCCESS:
                    File file = (File) msg.obj;
                    installApk(file);
                    break;
                case DOWNLOAD_ERROR:
                    showToast("下载apk文件失败");
                    loadMainUI();
                    break;
            }
        }
    };

    /**
     * 启动登录界面
     */
    private void loadMainUI() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences ppp = getSharedPreferences("login_saveInfo", MODE_PRIVATE);
                boolean isIn = ppp.getBoolean("login", false);
                if (!isIn) {
                    startActivity(UserLoginActivity.class);
                } else {
                    startActivity(MainPospiActivity.class);
                }
                SpalishActivity.this.finish(); // 结束启动动画界面
            }
        }, 500); //启动动画持续1.5秒钟
    }
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
        setContentView(R.layout.spalish);

//        Log.i(TAG, "Build.MODEL: "+ Build.MODEL);
//        DisplayMetrics metric = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(metric);
//        int width = metric.widthPixels;  // 宽度（PX）
//        Log.i("DisplayMetrics", "宽width: " + width);
//        int height = metric.heightPixels;  // 高度（PX）
//        float density = metric.density;  // 密度（0.75 / 1.0 / 1.5）
//        int densityDpi = metric.densityDpi;  // 密度DPI（120 / 160 / 240）
//        Log.i("DisplayMetrics", "高height: " + height);
//        Log.i("DisplayMetrics", "密度density: " + density);
//        Log.i("DisplayMetrics", "DPI: " + densityDpi);

        //检查版本更新的线程
        new Thread(new CheckVersionTask()) {
        }.start();
        String path = Environment.getExternalStorageDirectory()
                + "/ipos/log";
        LogCatHelper logCatHelper = LogCatHelper.getInstance(this, path);
        logCatHelper.start();

//        Log.d(URL.getScreemHeight(getApplicationContext()) + "", "" + URL.getScreemWidth(getApplicationContext()));
//        Log.i("当前的年月日", GetData.getYYMMDDNoSpellingTime());
    }

    /**
     * 检查版本更新线程
     */
    private class CheckVersionTask implements Runnable {
        public void run() {
            SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
            boolean autoupdate = sp.getBoolean("autoupdate", true);
            if (!autoupdate) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                loadMainUI();
            }
            long startTime = System.currentTimeMillis();
            Message msg = Message.obtain();
            try {
                String serverurl = getResources().getString(R.string.serverurl);
                URL url = new URL(serverurl);
                HttpURLConnection conn = (HttpURLConnection) url
                        .openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(5000);
                int code = conn.getResponseCode();
                long endTime;
                if (code == 200) {
                    InputStream is = conn.getInputStream();
                    info = UpdateInfoParser.getUpdateInfo(is);
                    endTime = System.currentTimeMillis();
                    long resulttime = endTime - startTime;
                    if (resulttime < 1000) {
                        try {
                            Thread.sleep(1000 - resulttime);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    msg.what = GET_INFO_SUCCESS;
                    handler.sendMessage(msg);
                } else {
                    msg.what = SERVER_ERROR;
                    handler.sendMessage(msg);
                    endTime = System.currentTimeMillis();
                    long resulttime = endTime - startTime;
                    if (resulttime < 1000) {
                        try {
                            Thread.sleep(1000 - resulttime);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
                msg.what = SERVER_URL_ERROR;
                handler.sendMessage(msg);
            } catch (ProtocolException e) {
                msg.what = PROTOCOL_ERROR;
                handler.sendMessage(msg);
                e.printStackTrace();
            } catch (IOException e) {
                msg.what = IO_ERROR;
                handler.sendMessage(msg);
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                msg.what = XML_PARSE_ERROR;
                handler.sendMessage(msg);
                e.printStackTrace();
            }
        }
    }

    /**
     * 下载apk文件
     */
    protected void installApk(File file) {
        // ��ʽ��ͼ
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");// ������ͼ�Ķ���
        intent.addCategory("android.intent.category.DEFAULT");// Ϊ��ͼ��Ӷ�������
        // intent.setType("application/vnd.android.package-archive");
        // intent.setData(Uri.fromFile(file));
        intent.setDataAndType(Uri.fromFile(file),
                "application/vnd.android.package-archive");// ������ͼ�����������
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_CANCELED) {
                loadMainUI();
            }
        }
    }

    /**
     * 提示更新dialog
     */
    protected void showUpdateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(getResources().getDrawable(R.drawable.notification));
        builder.setTitle("版本更新");
        builder.setMessage("检测到新版本，是否需要安装更新？");
        pd = new ProgressDialog(SpalishActivity.this);
        pd.setMessage("正在开始下载...");
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                Log.i(TAG, "开始下载..." + info.getApkurl());
                // 检查Sdcard
                if (Environment.MEDIA_MOUNTED.equals(Environment
                        .getExternalStorageState())) {
                    pd.show();
                    pd.setCancelable(false);
                    new Thread() {
                        public void run() {
                            String path = info.getApkurl();
                            String filename = DownLoadUtil.getFilename(path);
                            File file = new File(Environment
                                    .getExternalStorageDirectory(), filename);
                            file = DownLoadUtil.getFile(path,
                                    file.getAbsolutePath(), pd);
                            if (file != null) {
                                Message msg = Message.obtain();
                                msg.what = DOWNLOAD_SUCCESS;
                                msg.obj = file;
                                handler.sendMessage(msg);
                            } else {
                                Message msg = Message.obtain();
                                msg.what = DOWNLOAD_ERROR;
                                handler.sendMessage(msg);
                            }
                            pd.dismiss();
                        }
                    }.start();
                } else {
                    Toast.makeText(getApplicationContext(), "sd卡不存在！", Toast.LENGTH_SHORT).show();
                    loadMainUI();
                }
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                loadMainUI();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.setCancelable(false);
    }

    /**
     * 得到本地版本号
     *
     * @return
     */
    private int getVersion() {
        PackageManager pm = this.getPackageManager();
        try {
            PackageInfo info = pm.getPackageInfo(getPackageName(), 0);
            return info.versionCode;
        } catch (Exception e) {
            return 1000;
        }
    }
}