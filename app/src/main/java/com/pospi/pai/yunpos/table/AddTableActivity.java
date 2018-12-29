package com.pospi.pai.yunpos.table;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.flyco.tablayout.SegmentTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.pospi.dao.AreaInfoDao;
import com.pospi.dao.TableDao;
import com.pospi.dto.AreaInfoDto;
import com.pospi.dto.Tabledto;
import com.pospi.pai.yunpos.R;
import com.pospi.util.ViewFindUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddTableActivity extends FragmentActivity {
    @Bind(R.id.iv_back)
    ImageView ivBack;
    private PopupWindow addTablePop;
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private static List<Tabledto> tables = new ArrayList<>();//获取本地桌台的集合
    private static List<AreaInfoDto> areas = new ArrayList<>();//获取本地区域的集合
    private TableDao tableDao;
    private AreaInfoDao areaInfoDao;
    private Tabledto table;
    private AreaInfoDto area;
    private View mDecorView;
    private SegmentTabLayout tabLayout;
    private LayoutInflater mLayoutInflater;
    private String[] titles;//区域的名称
    private int current_area = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_add_table);
//        setTitle("餐桌");
        ButterKnife.bind(this);

        try {
            init();
            setTabLayout();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 实例化
     */
    private void init() {
        mLayoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        tableDao = new TableDao(this);
        area = new AreaInfoDto();
        areaInfoDao = new AreaInfoDao(this);

        mDecorView = getWindow().getDecorView();
        tabLayout = ViewFindUtils.find(mDecorView, R.id.add_table_tl);

//        setTitles();
    }

    /**
     * 设置区域标题数组
     */
//    private void setTitles() {
//        tables = tableDao.findTableInfo();
//        for (int i = 0; i < tables.size(); i++) {
//            tables.get(i).setId(i + 1);
//        }
//        areas = areaInfoDao.findTableInfo();
//        titles = new String[areas.size()];
//        List<Tabledto> tableDtos = new ArrayList<>();
//        for (int i = 0; i < areas.size(); i++) {
//            for (AreaInfoDto areaInfoDto : areas) {
//                if (areaInfoDto.getAreaNum() == i) {
//                    titles[i] = areaInfoDto.getAreaName();
//                }
//            }
//            for (Tabledto tabledto : tables) {
//                Log.i("tablesArea", tabledto.getArea() + "");
//                if (tabledto.getArea() == i) {
//                    tableDtos.add(tabledto);
//                }
//            }
//            Log.i("tableDtos", tableDtos.size() + "");
//            mFragments.add(new ViewPagerFragment(this, tableDtos));
//            tableDtos.clear();
//        }
//    }

    /**
     * 设置setTabLayout与viewPager关联
     */
    private void setTabLayout() {
        try {
            final ViewPager vp = ViewFindUtils.find(mDecorView, R.id.add_table_vp);
            vp.setAdapter(new FragmentAdapter(getSupportFragmentManager()));

            if (titles != null) {
                tabLayout.setTabData(titles);
            }

            tabLayout.setOnTabSelectListener(new OnTabSelectListener() {
                @Override
                public void onTabSelect(int position) {
                    vp.setCurrentItem(position);
                    current_area = position;
                }

                @Override
                public void onTabReselect(int position) {
                }
            });

            vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                }

                @Override
                public void onPageSelected(int position) {
                    tabLayout.setCurrentTab(position);
                    current_area = position;
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                }
            });
            vp.setCurrentItem(current_area);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.iv_back)
    public void onClick() {
        Intent data = new Intent();
        Bundle bundle = new Bundle();
//        bundle.p
        data.putExtras(bundle);
        setResult(RESULT_OK,data);
        finish();
    }

    class FragmentAdapter extends FragmentPagerAdapter {
        public FragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }
    }
}
