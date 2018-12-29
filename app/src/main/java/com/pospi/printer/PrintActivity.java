package com.pospi.printer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.hardware.usb.UsbManager;
import android.net.DhcpInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.print.sdk.PrinterConstants;
import com.android.print.sdk.PrinterInstance;
import com.android.print.sdk.bluetooth.BluetoothPort;
import com.android.print.sdk.wifi.WifiAdmin;
import com.pospi.pai.yunpos.R;
import com.pospi.pai.yunpos.base.BaseActivity;
import com.pospi.util.App;

/**
 * Created by Qiyan on 2016/5/16.
 */
public class PrintActivity extends BaseActivity implements View.OnClickListener {

    private Context mContext;
    private ImageView imageView;
    private Button btnBluetooth, btnWifi, btnUsb;
    private int offset = 0;
    private int currIndex = 0;// 0--bluetooth,1--wifi,2--usb
    private int bmpW;
    private boolean showUSB; // before android3.0 don't show usb
    private static boolean isConnected;
    protected static IPrinterOpertion myOpertion;
    private PrinterInstance mPrinter;

    public static final int CONNECT_DEVICE = 1;
    public static final int ENABLE_BT = 2;
    private TextView conn_state;
    private TextView conn_address;
    private TextView conn_name;
    private RadioButton paperWidth_58;
    private RadioButton paperWidth_80;
    private RadioButton printer_type_remin;
    private RadioButton printer_type_styuls;
    private boolean is58mm = true;
    private boolean isStylus = false;
    private ProgressDialog dialog;
    private View v1;
    private String bt_mac;
    private String bt_name;
    private String wifi_mac;
    private String wifi_name;

    private Button btn_note;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        v1 = getLayoutInflater().inflate(R.layout.set_print, null);
        setContentView(v1);
        showUSB = Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD_MR1;//对于SDK小于2.3的版本不显示USB界面
        mContext = this;
        InitView();//初始化界面


        InitImageView();
        ExitApplication.getInstance().addActivity(this);//退出功能


    }

    private void InitView() {

        paperWidth_58 = (RadioButton) findViewById(R.id.width_58mm);
        paperWidth_58.setOnClickListener(this);
        paperWidth_80 = (RadioButton) findViewById(R.id.width_80mm);
        paperWidth_80.setOnClickListener(this);

        printer_type_remin = (RadioButton) findViewById(R.id.type_remin);
        printer_type_remin.setOnClickListener(this);
        printer_type_styuls = (RadioButton) findViewById(R.id.type_styuls);
        printer_type_styuls.setOnClickListener(this);


        conn_state = (TextView) findViewById(R.id.connect_state);
        conn_state.setOnClickListener(this);
        conn_name = (TextView) findViewById(R.id.connect_name);
        conn_name.setOnClickListener(this);
        conn_address = (TextView) findViewById(R.id.connect_address);
        conn_address.setOnClickListener(this);


        btnBluetooth = (Button) findViewById(R.id.btnBluetooth);
        btnBluetooth.setOnClickListener(this);

        btnWifi = (Button) findViewById(R.id.btnWifi);
        btnWifi.setOnClickListener(this);

        btnUsb = (Button) findViewById(R.id.btnUsb);
        if (showUSB) {
            btnUsb.setOnClickListener(this);
        } else {
            btnUsb.setVisibility(View.GONE);
        }

        btn_note = (Button) findViewById(R.id.btn_note);
        btn_note.setOnClickListener(this);

        dialog = new ProgressDialog(mContext);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setTitle("Connecting...");
        dialog.setMessage("Please Wait...");
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);

        setTitleTextColor(0);
    }

    private void updateButtonState() {
        /**
         * 如果没有进行连接则就显示未连接
         */
        if (!isConnected) {
            conn_address.setText(R.string.no_conn_address);
            conn_state.setText(R.string.connect);
            conn_name.setText(R.string.no_conn_name);

        } else {

            if (currIndex == 0) {
                if (bt_mac != null && !bt_mac.equals("")) {
                    conn_address.setText(mContext.getResources().getString(R.string.str_address) + bt_mac);
                    conn_state.setText(R.string.disconnect);
                    conn_name.setText(mContext.getResources().getString(R.string.str_name) + bt_name);
                } else if (bt_mac == null) {
                    bt_mac = BluetoothPort.getmDeviceAddress();
                    bt_name = BluetoothPort.getmDeviceName();
                    conn_address.setText(mContext.getResources().getString(R.string.str_address) + bt_mac);
                    conn_state.setText(R.string.disconnect);
                    conn_name.setText(mContext.getResources().getString(R.string.str_name) + bt_name);
                }

            } else if (currIndex == 1) {
                conn_address.setText(mContext.getResources().getString(R.string.str_address) + wifi_mac);
                conn_state.setText(R.string.disconnect);
                conn_name.setText(mContext.getResources().getString(R.string.str_name) + wifi_name);
            } else if (currIndex == 2) {
                conn_address.setText(mContext.getResources().getString(R.string.disconnect));
                conn_state.setText(R.string.disconnect);
                conn_name.setText(mContext.getResources().getString(R.string.disconnect));
            }
        }

        btnBluetooth.setEnabled(!isConnected);
        btnWifi.setEnabled(!isConnected);
        btnUsb.setEnabled(!isConnected);


    }

    @Override
    protected void onActivityResult(final int requestCode, int resultCode,
                                    final Intent data) {
        switch (requestCode) {
            case CONNECT_DEVICE:
                if (resultCode == Activity.RESULT_OK) {
                    if (currIndex == 0) {
                        bt_mac = data.getExtras().getString(BluetoothDeviceList.EXTRA_DEVICE_ADDRESS);
                        bt_name = data.getExtras().getString(BluetoothDeviceList.EXTRA_DEVICE_NAME);
                        dialog.show();
                        new Thread(new Runnable() {
                            public void run() {
                                myOpertion.open(data);
                            }
                        }).start();
                    } else if (currIndex == 2) {
                        myOpertion.open(data);
                    } else if (currIndex == 1) {
                        wifi_mac = data.getStringExtra("ip_address");
                        wifi_name = data.getExtras().getString("device_name");
                        if (!wifi_mac.equals("") && wifi_mac != null) {

                            myOpertion.open(data);
                            dialog.show();
                        } else {
                            mHandler.obtainMessage(PrinterConstants.Connect.FAILED).sendToTarget();
                        }
                    }
                }
                break;
            case ENABLE_BT:
                if (resultCode == Activity.RESULT_OK) {
                    myOpertion.chooseDevice();
                } else {
                    Toast.makeText(this, R.string.bt_not_enabled,
                            Toast.LENGTH_SHORT).show();
                }
        }
    }

    /**
     * ImageView有一个旋转的动画
     */
    private void InitImageView() {
        imageView = (ImageView) findViewById(R.id.cursor);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenW = dm.widthPixels;
        bmpW = BitmapFactory.decodeResource(getResources(), R.drawable.slide1)
                .getWidth();

        offset = (screenW / (btnUsb.getVisibility() == View.VISIBLE ? 3 : 2)
                - bmpW - 4) / 2;//
        Matrix matrix = new Matrix();
        matrix.postTranslate(offset, 0);
        imageView.setImageMatrix(matrix);
    }

    /**
     * 点击之后设置动画
     *
     * @param view
     */
    public void onPageSelected(View view) {
        int index;
        if (view == btnBluetooth) {
            index = 0;
        } else if (view == btnWifi) {
            index = 1;
        } else {
            index = 2;
        }

        int one = offset * 2 + bmpW;

        Animation animation = new TranslateAnimation(one * currIndex, one
                * index, 0, 0);
        currIndex = index;
        animation.setFillAfter(true);
        animation.setDuration(300);
        imageView.startAnimation(animation);
        setTitleTextColor(index);
    }


    private void setTitleTextColor(int index) {
        switch (index) {
            case 0:
                btnBluetooth.setTextColor(Color.BLUE);
                btnWifi.setTextColor(Color.BLACK);
                btnUsb.setTextColor(Color.BLACK);
                break;
            case 1:
                btnBluetooth.setTextColor(Color.BLACK);
                btnWifi.setTextColor(Color.BLUE);
                btnUsb.setTextColor(Color.BLACK);
                break;
            case 2:
                btnBluetooth.setTextColor(Color.BLACK);
                btnWifi.setTextColor(Color.BLACK);
                btnUsb.setTextColor(Color.BLUE);
                break;

            default:
                break;
        }
        updateButtonState();
    }


    /**
     * 判断当前是那种连接方式进行的连接
     */
    private void openConn() {
        if (!isConnected) {
            switch (currIndex) {
                /**
                 * 当前连接的是蓝牙
                 */
                case 0: // bluetooth
                    new AlertDialog.Builder(this).setTitle(R.string.str_message)
                            .setMessage(R.string.str_connlast)
                            .setPositiveButton(R.string.yesconn, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    /**
                                     * 确认连接到上次连接到上次的连接的设备
                                     */

                                    myOpertion = new BluetoothOperation(PrintActivity.this, mHandler);
                                    Context context = mContext;
                                    //开启自动连接
                                    myOpertion.btAutoConn(context, mHandler);

                                }
                            })
                            .setNegativeButton(R.string.str_resel, new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    myOpertion = new BluetoothOperation(PrintActivity.this, mHandler);
                                    myOpertion.chooseDevice();
                                }

                            })
                            .show();

                    break;
                /**
                 * 当前连接的是WIFI
                 */
                case 1: // wifi


                    WifiManager mWifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
                    if (!mWifi.isWifiEnabled()) {
                        mWifi.setWifiEnabled(true);
                    }
                    WifiInfo wifiInfo = mWifi.getConnectionInfo();
                    wifi_name = wifiInfo.getSSID();
                    String content;

                    if (wifi_name != null && !wifi_name.equals("")) {
                        content = mContext.getResources().getString(R.string.str_wifi_now) + "" + wifi_name + "。";
                        DhcpInfo dhcpinfo = mWifi.getDhcpInfo();

                        WifiAdmin mWifiAdmin = new WifiAdmin(PrintActivity.this);

                        wifi_mac = mWifiAdmin.intToIp(dhcpinfo.serverAddress);
                    } else {
                        content = mContext.getResources().getString(R.string.str_wifi_no);
                    }


                    new AlertDialog.Builder(this).setTitle(R.string.str_message)
                            .setMessage(content)
                            .setPositiveButton(R.string.yesconn, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    if (wifi_name != null && !wifi_name.equals("")) {

                                        myOpertion = new WifiOperation(PrintActivity.this, mHandler);
                                        Intent intent = new Intent();
                                        intent.putExtra("ip_address", wifi_mac);
                                        myOpertion.open(intent);
                                    } else {
                                        Toast.makeText(mContext, R.string.str_wifi_no, Toast.LENGTH_SHORT).show();
                                    }

                                }
                            })
                            .setNegativeButton(R.string.str_resel, new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    myOpertion = new WifiOperation(PrintActivity.this, mHandler);
                                    myOpertion.chooseDevice();
                                }

                            })
                            .show();

                    break;
                /**
                 * 当前连接的是usb
                 */
                case 2: // usb


                    new AlertDialog.Builder(this).setTitle(R.string.str_message)
                            .setMessage(R.string.str_connlast)
                            .setPositiveButton(R.string.yesconn, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    myOpertion = new UsbOperation(PrintActivity.this, mHandler);
                                    UsbManager manager = (UsbManager) getSystemService(Context.USB_SERVICE);
                                    myOpertion.usbAutoConn(manager);

                                }
                            })
                            .setNegativeButton(R.string.str_resel, new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    myOpertion = new UsbOperation(PrintActivity.this, mHandler);
                                    myOpertion.chooseDevice();
                                }

                            })
                            .show();

                    break;
                default:
                    break;
            }
        } else {
            myOpertion.close();
            myOpertion = null;
            mPrinter = null;
            App.mPrinter=null;
        }
    }

    //用于接受连接状态消息的 Handler
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case PrinterConstants.Connect.SUCCESS:
                    isConnected = true;
                    App.mPrinter= myOpertion.getPrinter();
                    mPrinter = myOpertion.getPrinter();
                    break;
                case PrinterConstants.Connect.FAILED:
                    isConnected = false;
                    Toast.makeText(mContext, R.string.conn_failed,
                            Toast.LENGTH_SHORT).show();
                    break;
                case PrinterConstants.Connect.CLOSED:
                    isConnected = false;
                    Toast.makeText(mContext, R.string.conn_closed, Toast.LENGTH_SHORT)
                            .show();
                    break;
                case PrinterConstants.Connect.NODEVICE:
                    isConnected = false;
                    Toast.makeText(mContext, R.string.conn_no, Toast.LENGTH_SHORT)
                            .show();
                    break;

                default:
                    break;
            }

            updateButtonState();

            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
        }

    };


    @Override
    public void onClick(View view) {
        if (view == conn_state || view == conn_address || view == conn_name) {
            openConn();
        } else if (view == btnBluetooth || view == btnWifi || view == btnUsb) {
            onPageSelected(view);
        } else if (view == btn_note) {
//            PrintUtils.printNote(getApplication(), mContext.getResources(), mPrinter, is58mm);
//            try {
//                new PrintUtils().cutPaper();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }

        } else if (view == paperWidth_58 || view == paperWidth_80) {
            is58mm = view == paperWidth_58;
            paperWidth_58.setChecked(is58mm);
            paperWidth_80.setChecked(!is58mm);

        } else if (view == printer_type_remin || view == printer_type_styuls) {
            isStylus = view == printer_type_remin;
            printer_type_remin.setChecked(isStylus);
            printer_type_styuls.setChecked(!isStylus);

        }

    }


    @Override
    protected void onStop() {
        super.onStop();
//        if (myOpertion != null) {
//            myOpertion.close();
//        }

    }


}
