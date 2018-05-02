package com.pospi.printer;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.android.print.sdk.wifi.WifiAdmin;
import com.pospi.pai.pospiflat.R;

import java.util.List;

public class WifiDeviceList extends Activity
{
	private static final String TAG = "DeviceListActivity";
	private Button scanButton;
	private Button backButton;
	private WifiAdmin mWifiAdmin;


	private ArrayAdapter<String> deviceArrayAdapter;
	private ListView mFoundDevicesListView;

	 private WifiManager mWifiManager;

     // 扫描结果列表
     private List<ScanResult> list;
     private ScanResult mScanResult;

     private String ssid;
     private String pswd;
     private int mkey;
     private String address;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.device_list);
		setTitle(R.string.select_device);

		setResult(Activity.RESULT_CANCELED);

		initView();

	}

	private void initView()
	{
		scanButton = (Button) findViewById(R.id.button_scan);
		scanButton.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				getAllNetWorkList();
			}
		});

		backButton= (Button) findViewById(R.id.button_bace);
		backButton.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
                 finish();
			}

		});

		deviceArrayAdapter = new ArrayAdapter<String>(this,
				R.layout.device_item);

		mFoundDevicesListView = (ListView) findViewById(R.id.paired_devices);
		mFoundDevicesListView.setAdapter(deviceArrayAdapter);
		mFoundDevicesListView.setOnItemClickListener(mDeviceClickListener);

		  mWifiAdmin=new WifiAdmin(WifiDeviceList.this);

		/*Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();

		if (pairedDevices.size() > 0) {
			for (BluetoothDevice device : pairedDevices) {
				mPairedDevicesArrayAdapter.add(device.getName() + " ( "
						+ getResources().getText(R.string.has_paired) + " )"
						+ "\n" + device.getAddress());
			}
		}*/
	}


	 private void registerWIFI() {
	        IntentFilter mWifiFilter = new IntentFilter();
	        mWifiFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
	        registerReceiver(mWifiConnectReceiver, mWifiFilter);
	    }

	    private BroadcastReceiver mWifiConnectReceiver = new BroadcastReceiver() {

	    @Override
	    public void onReceive(Context context, Intent intent) {
//	            Log.d(TAG, "Wifi onReceive action = " + intent.getAction());
	            if (intent.getAction().equals(WifiManager.WIFI_STATE_CHANGED_ACTION)) {
	                int message = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, -1);
//	                Log.d(TAG, "liusl wifi onReceive msg=" + message);
	        switch (message) {
	        case WifiManager.WIFI_STATE_DISABLED:
//	            Log.d(TAG, "WIFI_STATE_DISABLED");
	        break;
	        case WifiManager.WIFI_STATE_DISABLING:
//	            Log.d(TAG, "WIFI_STATE_DISABLING");
	        break;
	        case WifiManager.WIFI_STATE_ENABLED:
//	            Log.d(TAG, "WIFI_STATE_ENABLED");

/*	             mWifiManager = (WifiManager)getSystemService(WIFI_SERVICE);
	            DhcpInfo dhcpinfo = mWifiManager.getDhcpInfo();
	            address= mWifiAdmin.intToIp(dhcpinfo.serverAddress);
	            returnToPreviousActivity(address);
	            unregisterReceiver(mWifiConnectReceiver);*/
	        break;
	        case WifiManager.WIFI_STATE_ENABLING:
//	            Log.d(TAG, "WIFI_STATE_ENABLING");
	        break;
	        case WifiManager.WIFI_STATE_UNKNOWN:
//	            Log.d(TAG, "WIFI_STATE_UNKNOWN");
	        break;
	        default:
	                break;
	                }
	            }
	        }
	    };

	private void returnToPreviousActivity(String address) {

		Intent intent = new Intent();
		intent.putExtra("ip_address", address);
//		Log.v("ipaddress",address);
		intent.putExtra("device_name", ssid);
		setResult(Activity.RESULT_OK, intent);


		finish();
	}

	private OnItemClickListener mDeviceClickListener = new OnItemClickListener()
	{
		public void onItemClick(AdapterView<?> av, View v, int position, long arg3)
		{
            ScanResult mScanResult1=(ScanResult) list.get(position);
            ssid=mScanResult1.SSID;


            String mkey1=mScanResult1.capabilities;
            if(mkey1.indexOf("NOPASS")>0)
            {
            	mkey=1;
            }else if(mkey1.indexOf("WEP")>0)
            {
            	mkey=2;
            }else if(mkey1.indexOf("WPA")>0)
            {
            	mkey=3;
            }
//            showDialog_Layout(WifiDeviceList.this);
		}
	};


//	private void showDialog_Layout(Context context)
//	{
//        LayoutInflater inflater = LayoutInflater.from(this);
//         View textEntryView = inflater.inflate(
//                R.layout.ip_address_edit, null);
//         final EditText edtInput=(EditText) textEntryView.findViewById(R.id.edtInput);
//         AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        builder.setCancelable(false);
//        builder.setTitle(R.string.password);
//        builder.setView(textEntryView);
//        builder.setPositiveButton(R.string.yesconn, new DialogInterface.OnClickListener()
//        {
//
//
//			@Override
//			public void onClick(DialogInterface dialog, int which)
//			{
//				pswd=edtInput.getText().toString();
//	           	 if(pswd!=null && !pswd.equals(""))
//	                {
//	           		  registerWIFI();
//	                  WifiAdmin wifiAdmin = new WifiAdmin(WifiDeviceList.this);
//	                  boolean is=wifiAdmin.addNetwork(wifiAdmin.CreateWifiInfo(ssid, pswd, mkey));
//                      if(is)
//                      {
//                    	  mWifiManager = (WifiManager)getSystemService(WIFI_SERVICE);
//                    	  DhcpInfo dhcpinfo = mWifiManager.getDhcpInfo();
//
//       	                  address= mWifiAdmin.intToIp(dhcpinfo.serverAddress);
//
//       	                  returnToPreviousActivity(address);
//       	                  unregisterReceiver(mWifiConnectReceiver);
//                      }else
//                      {
//                    	  returnToPreviousActivity("");
//       	                  unregisterReceiver(mWifiConnectReceiver);
//                      }
//
//	                }
//			}});
//
//        builder.setNegativeButton(R.string.cancel,new DialogInterface.OnClickListener()
//        {
//
//
//			@Override
//			public void onClick(DialogInterface dialog, int which)
//			{
//
//			}
//		});
//
//        builder.create();
//        final AlertDialog dialog=builder.show();
//
//
//    }

	public void getAllNetWorkList(){

	  deviceArrayAdapter.clear();
      //开始扫描网络

      mWifiAdmin.startScan();
      list=mWifiAdmin.getWifiList();
      if(list!=null){
          for(int i=0;i<list.size();i++)
          {
              //得到扫描结果
              mScanResult=list.get(i);
              deviceArrayAdapter.add(mScanResult.SSID+"\n "+mScanResult.capabilities+"\n");

          }
      }
  }











}
