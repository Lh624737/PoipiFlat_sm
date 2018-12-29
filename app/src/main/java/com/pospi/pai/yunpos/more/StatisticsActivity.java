package com.pospi.pai.yunpos.more;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pospi.fragment.CashierTodayFragment;
import com.pospi.fragment.TodaySaleAllFragment;
import com.pospi.fragment.TodaySaleFragment;
import com.pospi.fragment.TodayTimeStaticFragment;
import com.pospi.fragment.TradeListFragment;
import com.pospi.fragment.UpLoadFragment;
import com.pospi.pai.yunpos.R;
import com.pospi.pai.yunpos.base.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class StatisticsActivity extends BaseActivity {

    @Bind(R.id.today_sale_statis)
    TextView todaySaleStatis;
    @Bind(R.id.cashier_today)
    TextView cashierToday;
    @Bind(R.id.today_sale_all)
    TextView todaySaleAll;
    @Bind(R.id.today_time_statis)
    TextView todayTimeStatis;
    @Bind(R.id.today_sale_layout)
    LinearLayout Layout;
    @Bind(R.id.trade_list)
    TextView tradeList;
    @Bind(R.id.manual_upload)
    TextView manualUpload;
    @Bind(R.id.static_name)
    TextView static_name;

    public static final int REFUNDS_IN = 55;
    public static final int MAIN_IN = 56;
    public static final String REFUNDS_NAME = "refunds";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        ButterKnife.bind(this);

//        int inWay = getIntent().getIntExtra(REFUNDS_NAME, MAIN_IN);
//        if (inWay == MAIN_IN) {
//            todaySaleStatis.setBackgroundColor(getResources().getColor(R.color.white));
//            cashierToday.setBackgroundColor(getResources().getColor(R.color.white));
//            todaySaleAll.setBackgroundColor(getResources().getColor(R.color.white));
//            todayTimeStatis.setBackgroundColor(getResources().getColor(R.color.white));
//            manualUpload.setBackgroundColor(getResources().getColor(R.color.white));
//            tradeList.setBackgroundColor(getResources().getColor(R.color.white));
//        } else if (inWay == REFUNDS_IN) {


//        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        static_name.setText("交易清单");
        todaySaleStatis.setBackgroundColor(getResources().getColor(R.color.white));
        cashierToday.setBackgroundColor(getResources().getColor(R.color.white));
        todaySaleAll.setBackgroundColor(getResources().getColor(R.color.white));
        todayTimeStatis.setBackgroundColor(getResources().getColor(R.color.white));
        manualUpload.setBackgroundColor(getResources().getColor(R.color.white));
        tradeList.setBackgroundColor(getResources().getColor(R.color.grey_light_bg));

        FragmentManager fragmentManager4 = getFragmentManager();
        FragmentTransaction fragmentTransaction4 = fragmentManager4.beginTransaction();
        TradeListFragment fragment4 = new TradeListFragment();
        fragmentTransaction4.replace(R.id.today_sale_layout, fragment4);
        fragmentTransaction4.commit();
    }

    //点击事件
    @OnClick({R.id.today_sale_statis, R.id.cashier_today, R.id.today_sale_all,
            R.id.today_time_statis, R.id.trade_list, R.id.manual_upload,
            R.id.back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.today_sale_statis:
                static_name.setText("本日商品销售统计");
                todaySaleStatis.setBackgroundColor(getResources().getColor(R.color.grey_light_bg));
                cashierToday.setBackgroundColor(getResources().getColor(R.color.white));
                todaySaleAll.setBackgroundColor(getResources().getColor(R.color.white));
                todayTimeStatis.setBackgroundColor(getResources().getColor(R.color.white));
                manualUpload.setBackgroundColor(getResources().getColor(R.color.white));
                tradeList.setBackgroundColor(getResources().getColor(R.color.white));

//                然后，你能够使用add()方法把Fragment添加到指定的视图中，如：
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                TodaySaleFragment fragment = new TodaySaleFragment();
                fragmentTransaction.replace(R.id.today_sale_layout, fragment);
                fragmentTransaction.commit();

                break;
            case R.id.cashier_today:
                static_name.setText("收银员本日收款报表");
                todaySaleStatis.setBackgroundColor(getResources().getColor(R.color.white));
                cashierToday.setBackgroundColor(getResources().getColor(R.color.grey_light_bg));
                todaySaleAll.setBackgroundColor(getResources().getColor(R.color.white));
                todayTimeStatis.setBackgroundColor(getResources().getColor(R.color.white));
                tradeList.setBackgroundColor(getResources().getColor(R.color.white));
                manualUpload.setBackgroundColor(getResources().getColor(R.color.white));

                FragmentManager fragmentManager1 = getFragmentManager();
                FragmentTransaction fragmentTransaction1 = fragmentManager1.beginTransaction();
                CashierTodayFragment fragment1 = new CashierTodayFragment();
                fragmentTransaction1.replace(R.id.today_sale_layout, fragment1);
                fragmentTransaction1.commit();
                break;
            case R.id.today_sale_all:
                static_name.setText("本日收款汇总表");
                todaySaleStatis.setBackgroundColor(getResources().getColor(R.color.white));
                cashierToday.setBackgroundColor(getResources().getColor(R.color.white));
                todaySaleAll.setBackgroundColor(getResources().getColor(R.color.grey_light_bg));
                todayTimeStatis.setBackgroundColor(getResources().getColor(R.color.white));
                manualUpload.setBackgroundColor(getResources().getColor(R.color.white));
                tradeList.setBackgroundColor(getResources().getColor(R.color.white));

                FragmentManager fragmentManager2 = getFragmentManager();
                FragmentTransaction fragmentTransaction2 = fragmentManager2.beginTransaction();
                TodaySaleAllFragment fragment2 = new TodaySaleAllFragment();
                fragmentTransaction2.replace(R.id.today_sale_layout, fragment2);
                fragmentTransaction2.commit();

                break;
            case R.id.today_time_statis:
                static_name.setText("本日时段销售统计");
                todaySaleStatis.setBackgroundColor(getResources().getColor(R.color.white));
                cashierToday.setBackgroundColor(getResources().getColor(R.color.white));
                todaySaleAll.setBackgroundColor(getResources().getColor(R.color.white));
                todayTimeStatis.setBackgroundColor(getResources().getColor(R.color.grey_light_bg));
                manualUpload.setBackgroundColor(getResources().getColor(R.color.white));
                tradeList.setBackgroundColor(getResources().getColor(R.color.white));

                FragmentManager fragmentManager3 = getFragmentManager();
                FragmentTransaction fragmentTransaction3 = fragmentManager3.beginTransaction();
                TodayTimeStaticFragment fragment3 = new TodayTimeStaticFragment();
                fragmentTransaction3.replace(R.id.today_sale_layout, fragment3);
                fragmentTransaction3.commit();

                break;
            case R.id.trade_list:
                static_name.setText("交易清单");
                todaySaleStatis.setBackgroundColor(getResources().getColor(R.color.white));
                cashierToday.setBackgroundColor(getResources().getColor(R.color.white));
                todaySaleAll.setBackgroundColor(getResources().getColor(R.color.white));
                todayTimeStatis.setBackgroundColor(getResources().getColor(R.color.white));
                manualUpload.setBackgroundColor(getResources().getColor(R.color.white));
                tradeList.setBackgroundColor(getResources().getColor(R.color.grey_light_bg));

                FragmentManager fragmentManager4 = getFragmentManager();
                FragmentTransaction fragmentTransaction4 = fragmentManager4.beginTransaction();
                TradeListFragment fragment4 = new TradeListFragment();
                fragmentTransaction4.replace(R.id.today_sale_layout, fragment4);
                fragmentTransaction4.commit();
                break;
            case R.id.manual_upload:
                static_name.setText("手动上传");
                todaySaleStatis.setBackgroundColor(getResources().getColor(R.color.white));
                cashierToday.setBackgroundColor(getResources().getColor(R.color.white));
                todaySaleAll.setBackgroundColor(getResources().getColor(R.color.white));
                todayTimeStatis.setBackgroundColor(getResources().getColor(R.color.white));
                tradeList.setBackgroundColor(getResources().getColor(R.color.white));
                manualUpload.setBackgroundColor(getResources().getColor(R.color.grey_light_bg));

                FragmentManager fragmentManager5 = getFragmentManager();
                FragmentTransaction fragmentTransaction5 = fragmentManager5.beginTransaction();
                UpLoadFragment fragment5 = new UpLoadFragment();
                fragmentTransaction5.replace(R.id.today_sale_layout, fragment5);
                fragmentTransaction5.commit();
                break;
        }
    }

}