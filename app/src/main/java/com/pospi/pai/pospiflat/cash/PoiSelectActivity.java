package com.pospi.pai.pospiflat.cash;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.flyco.tablayout.SlidingTabLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.pax.api.scanner.ScanResult;
import com.pax.api.scanner.ScannerListener;
import com.pax.api.scanner.ScannerManager;
import com.pospi.dao.GoodsDao;
import com.pospi.dao.MemberDao;
import com.pospi.dao.OrderDao;
import com.pospi.dto.GoodsDto;
import com.pospi.dto.MemberDto;
import com.pospi.dto.MenuDto;
import com.pospi.dto.OrderDto;
import com.pospi.fragment.BlankFragment;
import com.pospi.http.MaxNO;
import com.pospi.http.Server;
import com.pospi.pai.pospiflat.more.LockActivity;
import com.pospi.pai.pospiflat.R;
import com.pospi.pai.pospiflat.more.RestOrderActivity;
import com.pospi.pai.pospiflat.more.StatisticsActivity;
import com.pospi.pai.pospiflat.pay.PayActivity;
import com.pospi.util.GetData;
import com.pospi.util.Sava_list_To_Json;
import com.pospi.util.constant.URL;
import com.pospi.util.ViewFindUtils;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PoiSelectActivity extends FragmentActivity implements BlankFragment.OnGridViewClick {
    @Bind(R.id.tv_No)
    TextView tvNo;
    @Bind(R.id.tv_num)
    TextView tvNum;
    @Bind(R.id.tv_money)
    TextView tvMoney;
    @Bind(R.id.btn_cancle)
    Button btnCancle;
    @Bind(R.id.btn_pay)
    Button btnPay;
    @Bind(R.id.lv_sale)
    ListView lvSale;
    @Bind(R.id.menu)
    ImageView menu;
    @Bind(R.id.tv_sale)
    TextView tvSale;
    @Bind(R.id.iv_sale)
    ImageView ivSale;
    @Bind(R.id.layout1)
    LinearLayout layout1;
    @Bind(R.id.layout2)
    LinearLayout layout2;
    @Bind(R.id.point_cashier_name)
    TextView cashierName;
    @Bind(R.id.iv_huiyuan)
    ImageView ivHuiyuan;
    @Bind(R.id.point_hy_name)
    TextView hyName;
    @Bind(R.id.btn_restOrder)
    Button btnRestOrder;

    private ArrayList<Fragment> mFragments = new ArrayList<>();
    /**
     * Tablayout的数据
     */
    private List<String> titles = null;
    private List<String> code = null;
    private MyLvAdapter adapter;
    private List<MenuDto> menuDtos = null;
    private int nums;
    private double total_money;
    private GoodsDao dao;
    private List<GoodsDto> lv_goodsBeens;
    private PopupWindow mPopWindow;
    private boolean popup_state;
    private boolean sale_state;
    private AlertDialog dialog;

    private String maxNo;
    private OrderDto orderDto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poi_select);
        ButterKnife.bind(this);

        request();
        sale_state = true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        orderDto = (OrderDto) getIntent().getSerializableExtra(RestOrderActivity.REST_ORDER);
        if (orderDto != null) {
            maxNo = orderDto.getMaxNo();
            Log.i("跳转之后", "NO" + orderDto.getMaxNo() + "");
            lv_goodsBeens = Sava_list_To_Json.changeToList(orderDto.getOrder_info());
            total_money = Double.parseDouble(orderDto.getYs_money());
            nums = lv_goodsBeens.size();
            tvNum.setText(String.valueOf(nums));
            tvMoney.setText(orderDto.getYs_money());
        } else {
            maxNo = MaxNO.getMaxNo(getApplicationContext());
            lv_goodsBeens = new ArrayList<>();
        }
        adapter = new MyLvAdapter(this, lv_goodsBeens);
        lvSale.setAdapter(adapter);
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }
    }

    /**
     * 请求服务器得到商品数据
     */
    private void request() {
        SharedPreferences preferences = getSharedPreferences("StoreMessage", MODE_PRIVATE);
        String shopid = preferences.getString("Id", "");
        RequestParams params = new RequestParams();//实例化后存入键值对
        params.put("value", shopid);
        new Server().getConnect(getApplicationContext(), new URL().SYNCCATEGORY, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int position, Header[] headers, byte[] bytes) {
                String response = new String(bytes);
                try {
                    JSONObject jsonObject = new JSONObject(response);
//                    Log.i("Value", jsonObject.getString("Value"));

                    menuDtos = new Gson().fromJson(jsonObject.getString("Value"),
                            new TypeToken<List<MenuDto>>() {
                            }.getType());

                    titles = new ArrayList<>();
                    code = new ArrayList<>();
                    for (int i = 0; i < menuDtos.size(); i++) {
                        titles.add(menuDtos.get(i).getName());
                        code.add(menuDtos.get(i).getSid());
                    }
                    Log.i("code.size", "" + code.size());
                    init();
                } catch (Exception e) {
                    Toast.makeText(PoiSelectActivity.this, "获取商品信息失败！！", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                Toast.makeText(PoiSelectActivity.this, "网络开小差了！请检查网络设置！", Toast.LENGTH_SHORT).show();
                Log.i("onFailure", new String(bytes));
            }
        });
    }

    /**
     * 绑定Tab和ViewPager，并将获取到的商品数据传进去
     */
    private void init() {

        tvNo.setText(String.valueOf(MaxNO.getMaxNo(getApplicationContext())));

        dao = new GoodsDao(this);
        /**
         * GridView的数据
         */
        String[] mTitles = new String[titles.size()];
        List<GoodsDto> goodsBeens = new ArrayList<>();
        goodsBeens.clear();
        for (int i = 0; i < titles.size(); i++) {
            mTitles[i] = titles.get(i);
//            Log.i("goodsBeens.size", dao.findSelectPointGoods(code.get(i)).size() + "");
            goodsBeens = dao.findSelectPointGoods(code.get(i));
            mFragments.add(new BlankFragment(this, goodsBeens));
        }
        View decorView = getWindow().getDecorView();
        ViewPager vp = ViewFindUtils.find(decorView, R.id.vp);

        MyPagerAdapter mAdapter = new MyPagerAdapter(getSupportFragmentManager());
        vp.setAdapter(mAdapter);

        SlidingTabLayout tabLayout_8 = ViewFindUtils.find(decorView, R.id.tl_8);
        if (mTitles.length < 6) {
            tabLayout_8.setTabSpaceEqual(true);
        }
        tabLayout_8.setViewPager(vp, mTitles, this, mFragments);
    }

    EditText etHy;
    AlertDialog hydialog;

    @OnClick({R.id.menu, R.id.btn_cancle, R.id.btn_pay, R.id.iv_huiyuan, R.id.btn_restOrder})
    public void onClick(View view) {
        MyHyClickListener listener = new MyHyClickListener();
        switch (view.getId()) {
            case R.id.iv_huiyuan:
                View scHy = getLayoutInflater().inflate(R.layout.huiyuan_dialog, null);
                hydialog = new AlertDialog.Builder(this)
                        .setView(scHy)
                        .create();
                hydialog.show();
                etHy = (EditText) scHy.findViewById(R.id.hy_et);
                Button scan = (Button) scHy.findViewById(R.id.hy_btn_scan);
                Button search = (Button) scHy.findViewById(R.id.hy_btn_search);
                scan.setOnClickListener(listener);
                search.setOnClickListener(listener);

                break;
            case R.id.btn_cancle:
                delete();
                break;
            case R.id.btn_restOrder:
                restOrder();
                break;
            case R.id.btn_pay:
                if (sale_state) {//销售模式
                    /**
                     * 当点击了收款之后，会把金额给传送到下一个界面
                     */
                    if (!(tvNum.getText().toString().equals("0件"))) {
                        Intent intent = new Intent(PoiSelectActivity.this, PayActivity.class);
                        intent.putExtra("money", String.valueOf(total_money));
                        intent.putExtra("orderType", URL.ORDERTYPE_SALE);
                        intent.putExtra("maxNo", maxNo);
                        startActivity(intent);

                        Sava_list_To_Json.changeToJaon(getApplicationContext(), lv_goodsBeens);
                        textviewClear();
                        maxNo+=1;
                    } else {
                        Toast.makeText(PoiSelectActivity.this, "未点选任何商品！", Toast.LENGTH_SHORT).show();
                    }
                } else {//退款模式
                    //当点击了退款之后，会把金额给传送到下一个界面
                    if (!(tvNum.getText().toString().equals("0件"))) {
                        Intent intent = new Intent(PoiSelectActivity.this, PayActivity.class);
                        intent.putExtra("money", String.valueOf(total_money));
                        intent.putExtra("orderType", URL.ORDERTYPE_REFUND);
                        intent.putExtra("maxNo", MaxNO.getMaxNo(getApplicationContext()));
                        startActivity(intent);

                        Sava_list_To_Json.changeToJaon(getApplicationContext(), lv_goodsBeens);
                        textviewClear();
                        maxNo+=1;
                    } else {
                        Toast.makeText(PoiSelectActivity.this, "未点选任何商品！", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.menu:
                if (!popup_state) {
                    showPopupWindow();
                } else {
                    dismissPopupWindow();
                }
                break;
        }
    }

    private ScannerManager sm;

    class MyHyClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.hy_btn_scan:
                    if (Build.MODEL.equalsIgnoreCase(URL.MODEL_D800)) {
                        sm = ScannerManager.getInstance(getApplication());
                        sm.scanOpen();
                        sm.scanStart(new ScannerListener() {
                            @Override
                            public void scanOnComplete() {
                                sm.scanClose();
                            }

                            @Override
                            public void scanOnCancel() {

                            }

                            @Override
                            public void scanOnRead(ScanResult arg0) {
                                if (arg0 != null) {
                                    MemberDto memberDto;
                                    if (arg0.getContent().trim().length() == 11) {
                                        memberDto = new MemberDao(getApplicationContext()).findMemberByTel(arg0.getContent().trim());
                                    } else {
                                        memberDto = new MemberDao(getApplicationContext()).findMemberByNumber(arg0.getContent().trim());
                                    }
                                    if (memberDto == null) {
                                        Toast.makeText(getApplicationContext(), "未添加该会员", Toast.LENGTH_SHORT).show();
                                        hydialog.dismiss();
                                        hyName.setText("");
                                    } else {
                                        hyName.setText(memberDto.getName());
                                        hydialog.dismiss();
                                    }
                                    sm.scanClose();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(getApplicationContext(), "请用扫码枪扫入", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.hy_btn_search:
                    if (!(etHy.getText().toString().isEmpty())) {
                        MemberDto memberDto;
                        if (etHy.getText().toString().trim().length() == 11) {
                            memberDto = new MemberDao(getApplicationContext()).findMemberByTel(etHy.getText().toString().trim());
                        } else {
                            memberDto = new MemberDao(getApplicationContext()).findMemberByNumber(etHy.getText().toString().trim());
                        }
                        if (memberDto == null) {
                            Toast.makeText(getApplicationContext(), "未添加该会员", Toast.LENGTH_SHORT).show();
                            hyName.setText("");
                            hydialog.dismiss();
                        } else {
                            hyName.setText(memberDto.getName());
                            hydialog.dismiss();
                        }
                    }
                    break;
            }
        }
    }

    /**
     * 弹出菜单
     */
    private void showPopupWindow() {
        popup_state = true;
        View contentView = LayoutInflater.from(PoiSelectActivity.this).inflate(R.layout.menu_fragment, null);
        mPopWindow = new PopupWindow(contentView);
        mPopWindow.setWidth(240);
        mPopWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

        LinearLayout l1 = (LinearLayout) contentView.findViewById(R.id.return_good);
        LinearLayout l3 = (LinearLayout) contentView.findViewById(R.id.money_box);
        LinearLayout l5 = (LinearLayout) contentView.findViewById(R.id.screen_lock);
        LinearLayout l7 = (LinearLayout) contentView.findViewById(R.id.back_menu);

        MyClickListener listener = new MyClickListener();
        l1.setOnClickListener(listener);
        l3.setOnClickListener(listener);
        l5.setOnClickListener(listener);
        l7.setOnClickListener(listener);

        mPopWindow.showAsDropDown(menu);
    }

    private class MyClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.return_good:
                    layout1.setBackgroundColor(getResources().getColor(R.color.blue_sale));
                    layout2.setBackgroundColor(getResources().getColor(R.color.blue_sale));
                    tvSale.setText("销售");
                    ivSale.setImageResource(R.drawable.xiaoshou);
                    btnPay.setText("付款");
                    dismissPopupWindow();
                    showReturnDialog();
                    break;
                case R.id.money_box:
                    dismissPopupWindow();
                    break;
                case R.id.screen_lock:
                    dismissPopupWindow();
                    startActivity(new Intent(PoiSelectActivity.this, LockActivity.class));
                    break;
                case R.id.back_menu:
                    dismissPopupWindow();
                    finish();
                    break;
                case R.id.have_note:
                    dialog.dismiss();
                    Intent intent = new Intent(PoiSelectActivity.this, StatisticsActivity.class);
                    intent.putExtra(StatisticsActivity.REFUNDS_NAME, StatisticsActivity.REFUNDS_IN);
                    startActivity(intent);
                    break;
                case R.id.not_note:
                    if (lv_goodsBeens.size() > 0) {
                        new AlertDialog.Builder(PoiSelectActivity.this)
                                .setMessage("此操作会销毁正在销售的订单！是否要销毁该订单？")
                                .setTitle("提示")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        textviewClear();
                                        changeState();
                                    }
                                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).create().show();
                    } else {
                        changeState();
                    }
                    break;
            }
        }
    }

    /**
     * 挂单
     */
    private void restOrder() {
        if (lv_goodsBeens.size() > 0) {
            final AlertDialog deleteDialog = new AlertDialog.Builder(this)
                    .setTitle("提示")
                    .setMessage("确定挂单？")
                    .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            addRestOrderinfo();
                        }
                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .create();
            deleteDialog.show();
        } else {
            Toast.makeText(PoiSelectActivity.this, " 没有可挂单的订单！", Toast.LENGTH_SHORT).show();
        }
    }


    public void addRestOrderinfo() {
        String shopId = getSharedPreferences("StoreMessage", MODE_PRIVATE).getString("Id", "");
        OrderDto orderDto = new OrderDto();
        orderDto.setOrder_info(Sava_list_To_Json.changeGoodDtoToJaon(lv_goodsBeens));//存储的该订单的商品的信息
        orderDto.setPayway(null);
        orderDto.setTime(GetData.getYYMMDDTime());
        orderDto.setSs_money("0.00");
        orderDto.setYs_money(String.valueOf(total_money));
        orderDto.setZl_money("0.00");
        orderDto.setShop_id(shopId);
        orderDto.setOrderType(URL.ORDERTYPE_SALE);
        orderDto.setMaxNo(maxNo);
        orderDto.setCheckoutTime(GetData.getDataTime());
        orderDto.setDetailTime(GetData.getHHmmssTimet());
        orderDto.setHasReturnGoods(URL.hasReturnGoods_No);
        orderDto.setOut_trade_no(null);
        //当挂单的时候该数据用于保存是哪个界面进行的挂单操作
        orderDto.setPayReturn(RestOrderActivity.REST_Point);
        orderDto.setIfFinishOrder(URL.BAOLIU);
        /**
         * 给数据库里面添加一条记录
         */
        new OrderDao(getApplicationContext()).addOrder(orderDto);
        textviewClear();
        maxNo += 1;
        tvNo.setText(String.valueOf(maxNo));
        Toast.makeText(PoiSelectActivity.this, " 挂单成功！", Toast.LENGTH_SHORT).show();

    }

    /**
     * 改变状态为退款模式
     */
    public void changeState() {
        layout1.setBackgroundColor(getResources().getColor(R.color.red));
        layout2.setBackgroundColor(getResources().getColor(R.color.red));
        tvSale.setText("退款");
        ivSale.setImageResource(R.drawable.down);
        btnPay.setText("退款");
        sale_state = false;
        dialog.dismiss();
    }

    public void dismissPopupWindow() {
        if (mPopWindow != null) {
            mPopWindow.dismiss();
        }
        popup_state = false;
    }

    public void showReturnDialog() {
        View dialogView = getLayoutInflater().inflate(R.layout.return_dialog, null);
        dialog = new AlertDialog.Builder(PoiSelectActivity.this).setView(dialogView).create();

        Button have_note = (Button) dialogView.findViewById(R.id.have_note);
        Button not_note = (Button) dialogView.findViewById(R.id.not_note);
        MyClickListener listener = new MyClickListener();
        have_note.setOnClickListener(listener);
        not_note.setOnClickListener(listener);

        int height = URL.getScreemHeight();
        int width = URL.getScreemWidth();
        //设置对话框的高度和宽度
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = width / 5;
        lp.height = height / 5;
        dialog.show();
    }

    /**
     * 清除订单数据
     */
    private void textviewClear() {
        adapter.delete();
        nums = 0;
        total_money = 0.00;
        tvNum.setText("0件");
        tvMoney.setText(R.string._zero);
    }

    /**
     * 删除该订单时候弹出的对话框
     */
    public void delete() {
        if (lv_goodsBeens.size() > 0) {
            new AlertDialog.Builder(this)
                    .setMessage("是否要销毁该订单？")
                    .setTitle("提示")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            textviewClear();
                        }
                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            }).create().show();
        } else {
            Toast.makeText(PoiSelectActivity.this, "没有可取消的订单！", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Fragment里的回掉
     */
    @SuppressLint("DefaultLocale")
    @Override
    public void ongridviewclick(GoodsDto value) {
        int n = 1;
        if (sale_state) {
            value.setNum(1);
        } else {
            value.setNum(-1);
            n = -1;
        }
        lv_goodsBeens.add(value);
        adapter.notifyDataSetChanged();
        nums += 1;
        tvNum.setText(String.format("%d件", nums));
        total_money += value.getPrice();
        tvMoney.setText(String.format("￥%s", total_money * n));
    }

    class MyLvAdapter extends BaseAdapter {

        private Context context;
        private List<GoodsDto> goodsBeens;

        public MyLvAdapter(Context context, List<GoodsDto> goodsBeens) {
            this.context = context;
            this.goodsBeens = goodsBeens;
        }

        //清除数据
        public void delete() {
            goodsBeens.clear();
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return goodsBeens.size();
        }

        @Override
        public Object getItem(int position) {
            return goodsBeens.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(context).inflate(R.layout.lvsale_item, null);
//                holder.num = (TextView) convertView.findViewById(R.id.tv_No);
                holder.name = (TextView) convertView.findViewById(R.id.tv_name);
                holder.momey = (TextView) convertView.findViewById(R.id.tv_money);
                holder.price = (TextView) convertView.findViewById(R.id.tv_num_price);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            GoodsDto goodsBean = goodsBeens.get(position);
//            holder.num.setText(String.valueOf(position + 1));
            holder.name.setText(goodsBean.getName().trim());
            holder.price.setText(goodsBean.getNum() + "*" + goodsBean.getPrice());
            holder.momey.setText(String.valueOf(goodsBean.getNum() * goodsBean.getPrice()));

            return convertView;
        }

        class ViewHolder {
//            TextView num;
            TextView name;
            TextView price;
            TextView momey;
        }
    }

}
