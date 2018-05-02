package com.pospi.pai.pospiflat.util;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.pospi.dao.ModifiedGroupDao;
import com.pospi.dto.ModifiedDto;
import com.pospi.dto.Remark;
import com.pospi.fragment.ModifiedFragment;
import com.pospi.pai.pospiflat.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ModifiedDialogActivity extends FragmentActivity implements ModifiedFragment.OnFragmentClick {

    @Bind(R.id.name)
    TextView name;
    @Bind(R.id.tablayout)
    TabLayout tablayout;
    @Bind(R.id.cancle)
    Button cancle;
    @Bind(R.id.sure)
    Button sure;
    @Bind(R.id.vp_mofdified)
    ViewPager vpMofdified;

    private final static String TAG = "ModifiedDialogActivity";
    private List<ModifiedDto> modifiedDtos;
    private List<String> modifiedName = new ArrayList<>();
    public static final int REQUESTCODE = 199;
    public static final int RESULTCODE_CANCLE = 200;
    public static final int RESULTCODE_SURE = 201;
    private String[] groupsids;
    private String good_name;
    private ModifiedGroupDao modifiedGroupDao;
    private List<Fragment> mFragments;
    private Set<String> mSet = new HashSet<>();
    private List<ModifiedFragment> modifiedFragments = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modified);
        ButterKnife.bind(this);

        groupsids = getIntent().getStringArrayExtra("groupsids");
        good_name = getIntent().getStringExtra("good_name");
        Log.i(TAG, "groupsids: " + groupsids);
        initWedgit();
    }

    private void initWedgit() {
        modifiedDtos = new ArrayList<>();
        modifiedDtos.clear();

        name.setText(good_name);
        mFragments = new ArrayList<>();

        modifiedGroupDao = new ModifiedGroupDao(this);
        //设置TabLayout的模式
//        tablayout.setTabMode(TabLayout.MODE_FIXED);
        //为TabLayout添加tab名称
        List<String> ModifiedGroup_names = new ArrayList<>();
        for (String groupsid : groupsids) {
            String ModifiedGroup_name = modifiedGroupDao.findModifiedGroupByGroupSid(groupsid);//有数据
            ModifiedGroup_names.add(ModifiedGroup_name);
            Log.i(TAG, "ModifiedGroup_name: " + ModifiedGroup_name);
            ModifiedFragment fragment = new ModifiedFragment(this, groupsid);
            mFragments.add(fragment);
            modifiedFragments.add(fragment);
        }
        //Fragment+ViewPager+FragmentViewPager组合的使用
        ModifiedPagerAdapter adapter = new ModifiedPagerAdapter(getSupportFragmentManager(), ModifiedGroup_names);
        vpMofdified.setAdapter(adapter);
        tablayout.setupWithViewPager(vpMofdified);
    }

    @OnClick({R.id.cancle, R.id.sure})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cancle:
                ModifiedDialogActivity.this.setResult(RESULTCODE_CANCLE);
                finish();
                break;
            case R.id.sure:
                for (ModifiedFragment fragment : modifiedFragments) {
                    List<Remark> remarks = fragment.getmRemarks();
                    for (Remark remark : remarks) {
                        if (remark.isChoose()) {
                            mSet.add(remark.getName());
                        }
                    }
                }

                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("modifieds", (Serializable) mSet);//
                intent.putExtras(bundle);
                ModifiedDialogActivity.this.setResult(RESULTCODE_SURE, intent);
                break;
        }
        finish();
    }

    @Override
    public void onFragmentClick(String modifiedDto) {
        Log.i("remark", "备注----------------->"+modifiedDto);
//        if (!modifiedDto.equals("")) {
//            mSet.add(modifiedDto);
//        }
//        if (modifiedDtos.size() == 0) {
//            modifiedDtos.add(modifiedDto);
//        } else {
//            try {
//                for (ModifiedDto modifiedDto1 : modifiedDtos) {
//                    if (modifiedDto1.getGroupSid().equals(modifiedDto.getGroupSid())) {
//                        modifiedDtos.remove(modifiedDto1);
//                        if(!modifiedDto1.getName().equals(modifiedDto.getName())){
//                            modifiedDtos.add(modifiedDto);
//                        }
//                    }else{
//                        modifiedDtos.add(modifiedDto);
//                    }
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
    }

    public List<ModifiedDto> getModifiedDtos() {
        return modifiedDtos;
    }

    class ModifiedPagerAdapter extends FragmentPagerAdapter {
        private List<String> ModifiedGroup_names;

        public ModifiedPagerAdapter(FragmentManager fm, List<String> ModifiedGroup_names) {
            super(fm);
            this.ModifiedGroup_names = ModifiedGroup_names;
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return ModifiedGroup_names.get(position);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }
    }
//    //屏蔽Home键
//    @Override
//    public void onAttachedToWindow() {
//        System.out.println("Page01 -->onAttachedToWindow");
//        this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG);
//        super.onAttachedToWindow();
//    }
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if(keyCode == KeyEvent.KEYCODE_MENU) {//MENU键
//            //监控/拦截菜单键
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }
//    //屏蔽菜单键
//    @Override
//    protected void onPause() {
//        super.onPause();
//
//        ActivityManager activityManager = (ActivityManager) getApplicationContext()
//                .getSystemService(Context.ACTIVITY_SERVICE);
//
//        activityManager.moveTaskToFront(getTaskId(), 0);
//    }

}
