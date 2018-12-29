package com.pospi.fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pospi.dao.OrderDao;
import com.pospi.dao.PayWayDao;
import com.pospi.dto.OrderDto;
import com.pospi.dto.PayWayDto;
import com.pospi.http.UpLoadToServel;
import com.pospi.pai.yunpos.R;
import com.pospi.pai.yunpos.util.CreateFiles;
import com.pospi.util.App;
import com.pospi.util.CommonUtil;
import com.pospi.util.GetData;
import com.pospi.util.UpdateOrder;
import com.pospi.util.constant.Config;
import com.pospi.util.constant.URL;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UpLoadFragment extends Fragment {

    @Bind(R.id.btn_manual_upload)
    Button btnManualUpload;
    //    @Bind(R.id.btn_manual_erp)
//    Button btnManualErp;
//    @Bind(R.id.btn_ftp_upload)
//    Button ftpUpload;
    @Bind(R.id.pb_upload)
    ProgressBar pbUpload;
    @Bind(R.id.layout_upload)
    RelativeLayout layoutUpload;
    @Bind(R.id.text_upload)
    TextView textUpload;
    private Button ftpUpload;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manual_upload, container, false);
        ftpUpload = (Button) view.findViewById(R.id.btn_ftp_upload);
        ftpUpload.setVisibility(View.GONE);
        ftpUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("ftp", "开始上传");
                File file = new File(CreateFiles.path + "S10122" + GetData.getDate() + ".txt");
                if (file.exists()) {
                    if (!isNetworkAvailable()) {
                        Toast.makeText(App.getContext(), "网络不可用", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    uploadFTP();
                } else {
                    if (new File(CreateFiles.path + "C10122" + GetData.getDate() + ".txt").exists()) {
                        Toast.makeText(App.getContext(), "今日销售报表已上传", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(App.getContext(), "文件不存在", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.btn_manual_upload})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_manual_upload:
                if (!isNetworkAvailable()) {
                    Toast.makeText(App.getContext(), "网络不可用", Toast.LENGTH_SHORT).show();
                    return;
                }
                PayWayDto payWayDto = null;
                List<OrderDto> orderDtos = new OrderDao(App.getContext()).findNOUpLoad();
                List<PayWayDto> payWayDtos = new PayWayDao(App.getContext()).findAllPayWay();
//        Log.i(TAG, "UpLoadService,大小为：" + orderDtos.size());
                layoutUpload.setVisibility(View.VISIBLE);
                int size = orderDtos.size();
                for (int j = 0; j < size; j++) {
//            Log.i(TAG, "order_time：" + orderDto.getCheckoutTime());
                    for (int i = 0; i < payWayDtos.size(); i++) {
                        Log.i("payType1", "订单的付款方式" + orderDtos.get(j).getStatus());
                        Log.i("payType1", "本地存储的付款方式" + payWayDtos.get(i).getPayType1());
                        if (orderDtos.get(j).getStatus() == payWayDtos.get(i).getPayType1()) {
                            payWayDto = payWayDtos.get(i);
                        }
                    }
                    try {
                        if (orderDtos.get(j).getOrderType() == URL.ORDERTYPE_REFUND) {
                            new UpLoadToServel(App.getContext()).uploadOrderToServer(orderDtos.get(j), payWayDto, "2", App.getContext(), UpdateOrder.findInfo);
                        } else {
                            new UpLoadToServel(App.getContext()).uploadOrderToServer(orderDtos.get(j), payWayDto, "1", App.getContext(), UpdateOrder.findInfo);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
//                int num = size - new OrderDao(App.getContext()).findNOUpLoad().size();
//                String str = "上传完成！一共上传" + size + "笔订单，上传成功：" + num + "笔，上传失败：" + (size - num) + "笔";
//                show(str);
                Toast.makeText(getActivity(), "正在上传。。。", Toast.LENGTH_SHORT).show();
                textUpload.setText("上传完成！");
                layoutUpload.setVisibility(View.INVISIBLE);
                break;
//            case R.id.btn_manual_erp://        Log.i(TAG, "onStartCommand()");
//                List<OrderDto> erp_orderDtos = new OrderDao(App.getContext()).findOrderERPNOUpLoad();
//                List<PayWayDto> erp_payWayDtos = new PayWayDao(App.getContext()).findAllPayWay();
//                PayWayDto erp_payWayDto = new PayWayDto();
////        Log.i(TAG, "ERPService,大小为：" + orderDtos.size());
//
//                layoutUpload.setVisibility(View.VISIBLE);
//                int erp_size = erp_orderDtos.size();
//                for (int j = 0; j < erp_size; j++) {
//                    for (int i = 0; i < erp_payWayDtos.size(); i++) {
//                        if (erp_orderDtos.get(j).getStatus() == erp_payWayDtos.get(i).getPayType1()) {
//                            erp_payWayDto = erp_payWayDtos.get(i);
//                        }
//                    }
//
//                }
////                int num1 = erp_size - new OrderDao(App.getContext()).findERPNOUpLoad().size();
////                String str1 = "上传完成！一共上传" + erp_size + "笔订单，上传成功：" + num1 + "笔，上传失败：" + (erp_size - num1) + "笔.";
////                show(str1);
//                Toast.makeText(getActivity(), "上传完成！", Toast.LENGTH_SHORT).show();
//                textUpload.setText("上传完成！");
//                layoutUpload.setVisibility(View.INVISIBLE);
//                break;
        }
    }

    private void uploadFTP() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                ArrayList<String> filelist = new ArrayList<String>();
                filelist.add(CreateFiles.path+"S10122"+ GetData.getDate()+".txt");
                Config config = new Config();
                config.setHostname("180.168.67.66");
                config.setPasswd("S10122");
                config.setUsername("S10122");
                config.setPort(21);

                CommonUtil com = new CommonUtil();
                String msg = com.ftpUpload(config, filelist);
                switch (msg) {
                    case "1":
                        ftpUpload.post(new Runnable() {
                            @Override
                            public void run() {
                                ftpUpload.setText("销售报表已上传");
                                ftpUpload.setClickable(false);
                            }
                        });
                        break;
                    case "2":
                        break;
                    case "3":
                        break;
                    default:
                        break;
                }
                Log.i("ftp", msg);
            }
        }).start();
    }

    public void show(String srt) {
        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setMessage(srt)
                .setTitle("提示")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();
        dialog.show();
    }
    /**
     * 检查当前网络是否可用
     *
     *
     */

    public boolean isNetworkAvailable()
    {
        Context context =App.getContext();
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null)
        {
            return false;
        }
        else
        {
            // 获取NetworkInfo对象
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

            if (networkInfo != null && networkInfo.length > 0)
            {
                for (int i = 0; i < networkInfo.length; i++)
                {
//                    System.out.println(i + "===状态===" + networkInfo[i].getState());
//                    System.out.println(i + "===类型===" + networkInfo[i].getTypeName());
                    // 判断当前网络状态是否为连接状态
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED)
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }

}
