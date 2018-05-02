package com.pospi.service;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;

import com.pospi.dao.OrderDao;
import com.pospi.dao.PayWayDao;
import com.pospi.dto.OrderDto;
import com.pospi.dto.PayWayDto;
import com.pospi.http.UpLoadToServel;
import com.pospi.util.App;
import com.pospi.util.constant.URL;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Qiyan on 2016/9/18.
 */
public class ERPService extends IntentService {
    private List<OrderDto> orderDtos = new ArrayList<>();
    private List<PayWayDto> payWayDtos = new ArrayList<>();
    private PayWayDto payWayDto;
    public String type;
    private final static String TAG = "ERPService";

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public ERPService(String name) {
        super(name);
    }

    public ERPService() {
        super("hello");
    }


    @Override
    public void onCreate() {
        super.onCreate();
//        Log.i(TAG, "onCreate()");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        Log.i(TAG, "onStartCommand()");
        orderDtos = new OrderDao(App.getContext()).findOrderERPNOUpLoad();
        payWayDtos = new PayWayDao(App.getContext()).findAllPayWay();
//        Log.i(TAG, "ERPService,大小为：" + orderDtos.size());
        for (OrderDto orderDto : orderDtos) {
            for (int i = 0; i < payWayDtos.size(); i++) {
                if (orderDto.getStatus() == payWayDtos.get(i).getPayType1()) {
                    payWayDto = payWayDtos.get(i);
                }
            }
            try {
                if (orderDto.getOrderType() == URL.ORDERTYPE_REFUND) {
                    new UpLoadToServel(App.getContext()).postWebServer(orderDto, payWayDto.getName(), false);
                } else {
                    new UpLoadToServel(App.getContext()).postWebServer(orderDto, payWayDto.getName(), true);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        AlarmManager manager = (AlarmManager) App.getContext().getSystemService(ALARM_SERVICE);
        int anHour = 300 * 1000; // 这是5min的毫秒数
        long triggerAtTime = SystemClock.elapsedRealtime() + anHour;
        Intent i = new Intent(this, ErpReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
        if (new OrderDao(App.getContext()).findOrderERPNOUpLoad().size() > 0) {
            App.setHasNoUpLoad(true);
        } else {
            App.setHasNoUpLoad(false);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
//        Log.i(TAG, "onHandleIntent()");

    }


    @Override
    public void onDestroy() {
//        Log.i(TAG, "onDestroy" );
        super.onDestroy();
    }
}
