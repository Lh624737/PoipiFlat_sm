package com.pospi.service;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import com.pospi.dao.OrderDao;
import com.pospi.dao.PayWayDao;
import com.pospi.dto.OrderDto;
import com.pospi.dto.PayWayDto;
import com.pospi.http.UpLoadToServel;
import com.pospi.pai.yunpos.been.LogBeen;
import com.pospi.pai.yunpos.util.LogUtil;
import com.pospi.util.App;
import com.pospi.util.UpdateOrder;
import com.pospi.util.constant.URL;

import java.util.List;

/**
 * Created by Qiyan on 2016/8/11.
 */

//开启服务遍历数据库是否有没有上传的订单，如果有就去上传订单
public class UpLoadService extends IntentService {

    private final static String TAG = "UpLoadService";

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public UpLoadService(String name) {
        super(name);
    }

    public UpLoadService() {
        super("hello");
//        Log.i(TAG, "无参构造()");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
//        Log.i(TAG, "onHandleIntent()");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        Log.i(TAG, "onStartCommand()");
        PayWayDto payWayDto = null;
        List<OrderDto> orderDtos = new OrderDao(App.getContext()).findNOUpLoad();
        List<PayWayDto> payWayDtos = new PayWayDao(App.getContext()).findAllPayWay();
        List<LogBeen> logBeens = new LogUtil().searchNoUp();
        if (logBeens != null && logBeens.size() > 0) {
            new LogUtil().upData(App.getContext(), logBeens);
        }

        Log.i("order", "UpLoadService,大小为：" + orderDtos.size());
        for (OrderDto orderDto : orderDtos) {
//            Log.i(TAG, "order_time：" + orderDto.getCheckoutTime());
            for (int i = 0; i < payWayDtos.size(); i++) {
                if (orderDto.getStatus() == payWayDtos.get(i).getPayType1()) {
                    payWayDto = payWayDtos.get(i);
                }
            }
            try {
                if (orderDto.getOrderType() == URL.ORDERTYPE_REFUND) {
                    new UpLoadToServel(App.getContext()).uploadOrderToServer(orderDto, payWayDto, "2", App.getContext(), UpdateOrder.findInfo);
                } else {
                    new UpLoadToServel(App.getContext()).uploadOrderToServer(orderDto, payWayDto, "1", App.getContext(), UpdateOrder.findInfo);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        AlarmManager manager = (AlarmManager) App.getContext().getSystemService(ALARM_SERVICE);
        int anHour = 300 * 1000; // 这是5min的毫秒数
        long triggerAtTime = SystemClock.elapsedRealtime() + anHour;
        Intent i = new Intent(this, UpLoadReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);

        if (new OrderDao(App.getContext()).findOrderNOUpLoad().size() > 0) {
            App.setHasNoUpLoad(true);
        } else {
            App.setHasNoUpLoad(false);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        Log.i(TAG, "onCreate()");
    }

    @Override
    public void onDestroy() {
//        Log.i(TAG, "onDestroy");
        super.onDestroy();
    }
}
