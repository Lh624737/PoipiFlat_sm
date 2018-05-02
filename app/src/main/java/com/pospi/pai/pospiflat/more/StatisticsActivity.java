package com.pospi.pai.pospiflat.more;

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
import com.pospi.pai.pospiflat.R;
import com.pospi.pai.pospiflat.base.BaseActivity;

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
            todaySaleStatis.setBackgroundColor(getResources().getColor(R.color.white));
            cashierToday.setBackgroundColor(getResources().getColor(R.color.white));
            todaySaleAll.setBackgroundColor(getResources().getColor(R.color.white));
            todayTimeStatis.setBackgroundColor(getResources().getColor(R.color.white));
            manualUpload.setBackgroundColor(getResources().getColor(R.color.white));
            tradeList.setBackgroundColor(getResources().getColor(R.color.gray));

            FragmentManager fragmentManager4 = getFragmentManager();
            FragmentTransaction fragmentTransaction4 = fragmentManager4.beginTransaction();
            TradeListFragment fragment4 = new TradeListFragment();
            fragmentTransaction4.replace(R.id.today_sale_layout, fragment4);
            fragmentTransaction4.commit();

//        }
    }

    //点击事件
    @OnClick({R.id.today_sale_statis, R.id.cashier_today, R.id.today_sale_all, R.id.today_time_statis, R.id.trade_list, R.id.manual_upload})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.today_sale_statis:
                todaySaleStatis.setBackgroundColor(getResources().getColor(R.color.gray));
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
                todaySaleStatis.setBackgroundColor(getResources().getColor(R.color.white));
                cashierToday.setBackgroundColor(getResources().getColor(R.color.gray));
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
                todaySaleStatis.setBackgroundColor(getResources().getColor(R.color.white));
                cashierToday.setBackgroundColor(getResources().getColor(R.color.white));
                todaySaleAll.setBackgroundColor(getResources().getColor(R.color.gray));
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
                todaySaleStatis.setBackgroundColor(getResources().getColor(R.color.white));
                cashierToday.setBackgroundColor(getResources().getColor(R.color.white));
                todaySaleAll.setBackgroundColor(getResources().getColor(R.color.white));
                todayTimeStatis.setBackgroundColor(getResources().getColor(R.color.gray));
                manualUpload.setBackgroundColor(getResources().getColor(R.color.white));
                tradeList.setBackgroundColor(getResources().getColor(R.color.white));

                FragmentManager fragmentManager3 = getFragmentManager();
                FragmentTransaction fragmentTransaction3 = fragmentManager3.beginTransaction();
                TodayTimeStaticFragment fragment3 = new TodayTimeStaticFragment();
                fragmentTransaction3.replace(R.id.today_sale_layout, fragment3);
                fragmentTransaction3.commit();

                break;
            case R.id.trade_list:
                todaySaleStatis.setBackgroundColor(getResources().getColor(R.color.white));
                cashierToday.setBackgroundColor(getResources().getColor(R.color.white));
                todaySaleAll.setBackgroundColor(getResources().getColor(R.color.white));
                todayTimeStatis.setBackgroundColor(getResources().getColor(R.color.white));
                manualUpload.setBackgroundColor(getResources().getColor(R.color.white));
                tradeList.setBackgroundColor(getResources().getColor(R.color.gray));

                FragmentManager fragmentManager4 = getFragmentManager();
                FragmentTransaction fragmentTransaction4 = fragmentManager4.beginTransaction();
                TradeListFragment fragment4 = new TradeListFragment();
                fragmentTransaction4.replace(R.id.today_sale_layout, fragment4);
                fragmentTransaction4.commit();
                break;
            case R.id.manual_upload:
                todaySaleStatis.setBackgroundColor(getResources().getColor(R.color.white));
                cashierToday.setBackgroundColor(getResources().getColor(R.color.white));
                todaySaleAll.setBackgroundColor(getResources().getColor(R.color.white));
                todayTimeStatis.setBackgroundColor(getResources().getColor(R.color.white));
                tradeList.setBackgroundColor(getResources().getColor(R.color.white));
                manualUpload.setBackgroundColor(getResources().getColor(R.color.gray));

                FragmentManager fragmentManager5 = getFragmentManager();
                FragmentTransaction fragmentTransaction5 = fragmentManager5.beginTransaction();
                UpLoadFragment fragment5 = new UpLoadFragment();
                fragmentTransaction5.replace(R.id.today_sale_layout, fragment5);
                fragmentTransaction5.commit();
                break;
        }
    }

}