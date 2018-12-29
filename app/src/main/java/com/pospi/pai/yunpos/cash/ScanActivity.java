package com.pospi.pai.yunpos.cash;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.gprinter.command.EscCommand;
import com.gprinter.command.LabelCommand;
import com.pax.api.scanner.ScanResult;
import com.pax.api.scanner.ScannerListener;
import com.pax.api.scanner.ScannerManager;
import com.pospi.adapter.AddGoodsAdapter;
import com.pospi.dao.GoodsDao;
import com.pospi.dao.MemberDao;
import com.pospi.dao.OrderDao;
import com.pospi.dao.TableDao;
import com.pospi.dto.GoodsDto;
import com.pospi.dto.MemberDto;
import com.pospi.dto.OrderDto;
import com.pospi.dto.Tabledto;
import com.pospi.http.MaxNO;
import com.pospi.pai.yunpos.R;
import com.pospi.pai.yunpos.base.BaseActivity;
import com.pospi.pai.yunpos.more.LockActivity;
import com.pospi.pai.yunpos.more.RestOrderActivity;
import com.pospi.pai.yunpos.more.StatisticsActivity;
import com.pospi.pai.yunpos.pay.PayActivity;
import com.pospi.pai.yunpos.table.SetTableActivity;
import com.pospi.pai.yunpos.util.DiscountDialogActivity;
import com.pospi.util.App;
import com.pospi.util.CustomDialog;
import com.pospi.util.DoubleSave;
import com.pospi.util.GetData;
import com.pospi.util.Sava_list_To_Json;
import com.pospi.util.TurnSize;
import com.pospi.util.constant.URL;
import com.pospi.view.swipemenulistview.SwipeMenu;
import com.pospi.view.swipemenulistview.SwipeMenuCreator;
import com.pospi.view.swipemenulistview.SwipeMenuItem;
import com.pospi.view.swipemenulistview.SwipeMenuListView;

import org.apache.commons.lang.ArrayUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ScanActivity extends BaseActivity {

    @Bind(R.id.menu)
    ImageView menu;
    @Bind(R.id.lv_scan)
    SwipeMenuListView lv_scan;
    @Bind(R.id.cashier_name)
    TextView cashierName;
    @Bind(R.id.vip_icon)
    TextView vipIcon;
    @Bind(R.id.vip_name)
    TextView vipName;
    @Bind(R.id.tv_num)
    TextView tvNum;
    @Bind(R.id.tv_discount)
    TextView tvDiscount;
    @Bind(R.id.tv_money)
    TextView tvMoney;
    @Bind(R.id.et_led)
    EditText etLed;
    @Bind(R.id.scan_iv)
    ImageView scanIv;
    @Bind(R.id.scan_qc)
    TextView scanQc;
    @Bind(R.id.scan_sure)
    TextView scanSure;
//    @Bind(R.id.more_payway)
//    Button morePayway;
    @Bind(R.id.pay)
    Button pay;
//    @Bind(R.id.table_name)
//    TextView tableName;
//    @Bind(R.id.ll_table)
//    LinearLayout llTable;

    private String CodeNumberShow = "";
    private boolean more_popup_state;
    private GoodsDao goodsDao;

    private boolean sale_state;
    private List<GoodsDto> goodsDtos = new ArrayList<>();//new ArrayList<>();
    private AddGoodsAdapter adapter;

    private AlertDialog dialog;
    private PopupWindow mPopWindow;
    private boolean popup_state;
    private CustomDialog.Builder ibuilder;
    private OrderDto orderDto;
    private ScannerManager sm;

    private int posi;

    private String maxNo;
    private int eatingNumber = 0;
    private String tableNumber = "";
    private String tableNamel = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        ButterKnife.bind(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        orderDto = (OrderDto) getIntent().getSerializableExtra(RestOrderActivity.REST_ORDER);
        ReStartUI();
        init();

        lv_scan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                posi = position;
                Intent intent = new Intent(ScanActivity.this, DiscountDialogActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(DiscountDialogActivity.DISCOUNT, goodsDtos.get(position));
                intent.putExtras(bundle);
                startActivityForResult(intent, 100);
            }
        });
        ETRequestFocus();
    }

    @Override
    protected void onResume() {
        ETRequestFocus();
        super.onResume();
    }

    /**
     * 设置EditText获取焦点
     */
    public void ETRequestFocus() {
        etLed.setFocusable(true);
        etLed.setFocusableInTouchMode(true);
        etLed.requestFocus();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        ETRequestFocus();
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 100:
                if (resultCode == 1111) {
                    int num = data.getIntExtra("num", 1);
                    double discount = data.getDoubleExtra("discount", 0.0);
                    goodsDtos.get(posi).setNum(num);
                    Log.i("num", "" + num);
                    Log.i("discount", "" + discount);

                    goodsDtos.get(posi).setDiscount(discount);
                    setScreenData();
                    adapter.notifyDataSetChanged();
                } else if (resultCode == 2222) {
                    goodsDtos.remove(posi);
                    adapter.notifyDataSetChanged();
                    setScreenData();
                }
                ETRequestFocus();
                break;
            case PointActivity.TableRequest:
                switch (resultCode) {
                    case 1:
                        eatingNumber = data.getIntExtra(SetTableActivity.EatingNumber, 1);
                        tableNumber = data.getStringExtra(SetTableActivity.TableID);
                        tableNamel = data.getStringExtra(SetTableActivity.TableName);
                        Log.i("tableNamel", tableNamel);
//                        tableName.setText(eatingNumber + "人" + tableNamel);
                        break;
                    case 2:
                        orderDto = (OrderDto) data.getSerializableExtra(SetTableActivity.TableOrder);
                        tableNumber = orderDto.getTableNumber();
                        tableNamel = data.getStringExtra(SetTableActivity.TableName);
                        Log.i("tableNamel", tableNamel);
//                        tableName.setText(eatingNumber + "人" + tableNamel);
                        ReStartUI();
                        break;
                }
            case 123:
                switch (resultCode) {
                    case 0:
                        break;
                    case 1:
                        double discount = 0;
                        double dou = 0;//订单总额
                        String discount_type = data.getStringExtra("discount_type");
                        if (discount_type.equals("折扣")) {
                            double discount_num = data.getDoubleExtra("discount_num", 0) / 100;
                            for (int i = 0; i < goodsDtos.size(); i++) {
                                GoodsDto goodsDto = goodsDtos.get(i);
                                dou = dou + goodsDtos.get(i).getNum() * goodsDtos.get(i).getPrice();
                                if (i == goodsDtos.size() - 1) {
                                    Log.i("discounall", "onActivityResult: " + new BigDecimal(dou * discount_num).setScale(2, RoundingMode.DOWN).doubleValue());
                                    goodsDto.setDiscount(new BigDecimal(dou * discount_num).setScale(2, RoundingMode.DOWN).doubleValue() - discount);
                                } else {
                                    discount += new BigDecimal(goodsDto.getPrice() * goodsDto.getNum() * discount_num).setScale(2, RoundingMode.DOWN).doubleValue();
                                    Log.i("discoun+", "onActivityResult: " + discount);
                                    goodsDto.setDiscount(new BigDecimal(goodsDto.getPrice() * goodsDto.getNum() * discount_num).setScale(2, RoundingMode.DOWN).doubleValue());
                                }
                            }
                        } else {
                            for (int i = 0; i < goodsDtos.size(); i++) {
                                GoodsDto goodsDto = goodsDtos.get(i);
                                dou = dou + goodsDtos.get(i).getNum() * goodsDtos.get(i).getPrice();
                            }
                            double discount_money = data.getDoubleExtra("discount_num", 0);
                            double discount_num = new BigDecimal(discount / dou).setScale(2, RoundingMode.DOWN).doubleValue();
                            for (int i = 0; i < goodsDtos.size(); i++) {
                                GoodsDto goodsDto = goodsDtos.get(i);
                                if (i == goodsDtos.size() - 1) {
                                    Log.i("discounall", "onActivityResult: " + new BigDecimal(dou * discount_num).setScale(2, RoundingMode.DOWN).doubleValue());
                                    goodsDto.setDiscount(DoubleSave.doubleSaveTwo(discount_money - discount));
                                } else {
                                    discount += new BigDecimal(goodsDto.getPrice() * goodsDto.getNum() * discount_num).setScale(2, RoundingMode.DOWN).doubleValue();
                                    Log.i("discoun+", "onActivityResult: " + discount);
                                    goodsDto.setDiscount(new BigDecimal(goodsDto.getPrice() * goodsDto.getNum() * discount_num).setScale(2, RoundingMode.DOWN).doubleValue());
                                }
                            }
                        }
                        setScreenData();
                        adapter.notifyDataSetChanged();
                        break;
                }
                break;
        }

    }

    public void ReStartUI() {
        if (orderDto != null) {
            Log.i("跳转之后", "NO" + orderDto.getMaxNo() + "");
            goodsDtos = Sava_list_To_Json.changeToList(orderDto.getOrder_info());
            setScreenData();
            maxNo = orderDto.getMaxNo();
        } else {
            goodsDtos = new ArrayList<>();
            maxNo = MaxNO.getMaxNo(getApplicationContext());
        }
        adapter = new AddGoodsAdapter(ScanActivity.this, goodsDtos);
        lv_scan.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        etLed.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (etLed.getText().toString().trim().length() > 4) {
                    judgeCode(etLed.getText().toString().trim());
                }
//                judgeCode(etLed.getText().toString().trim());
            }
        });
    }


    public void init() {
        goodsDao = new GoodsDao(getApplicationContext());
        sale_state = true;
        etLed.setInputType(InputType.TYPE_NULL);

        int whichOne = getSharedPreferences("islogin", MODE_PRIVATE).getInt("which", 0);
//        String cashier_name = new CashierLogin_pareseJson().parese(
//                getSharedPreferences("cashierMsgDtos", MODE_PRIVATE)
//                        .getString("cashierMsgDtos", ""))
//                .get(whichOne).getName();
//        cashierName.setText(cashier_name);


        setSwipeListView();
    }

    private void setSwipeListView() {
        // step 1. create a MenuCreator
        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem openItem = new SwipeMenuItem();
                // set item background
                openItem.setBackground(new ColorDrawable(Color.rgb(0xD9, 0x47,
                        0x47)));
                // set item width
                openItem.setWidth(dp2px(100));
                // set item title
                openItem.setTitle("删除");
                // set item title fontsize
                openItem.setTitleSize(18);
                // set item title font color
                openItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(openItem);
            }
        };
        // set creator
        lv_scan.setMenuCreator(creator);

        // step 2. listener item click event
        lv_scan.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public void onMenuItemClick(int position, SwipeMenu menu, int index) {
                GoodsDto item = goodsDtos.get(position);
//					delete(item);
                goodsDtos.remove(position);
                adapter.notifyDataSetChanged();
                setScreenData();
            }
        });
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    public void numberClick(View view) {
        TextView tv = (TextView) view;
        CodeNumberShow += tv.getText().toString();
        etLed.setText(CodeNumberShow);
    }

    AlertDialog hydialog;
    EditText etHy;

    @OnClick({R.id.menu, R.id.vip_icon, R.id.scan_iv, R.id.scan_qc, R.id.scan_sure, R.id.pay})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.menu:
                if (!popup_state) {
                    showPopupWindow();
                } else {
                    dismissPopupWindow();
                }
                break;
            case R.id.vip_icon:
                View scHy = getLayoutInflater().inflate(R.layout.huiyuan_dialog, null);
                hydialog = new AlertDialog.Builder(this)
                        .setView(scHy)
                        .create();
                hydialog.show();
                etHy = (EditText) scHy.findViewById(R.id.hy_et);
                Button scan = (Button) scHy.findViewById(R.id.hy_btn_scan);
                Button search = (Button) scHy.findViewById(R.id.hy_btn_search);
                MyClickListener listener = new MyClickListener();
                scan.setOnClickListener(listener);
                search.setOnClickListener(listener);
                break;
            case R.id.scan_iv:
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
                                etLed.setText(arg0.getContent());
                                sm.scanClose();
                            }
                        }
                    });
                } else {
                    showToast("暂不支持此功能");
                }
                break;
            case R.id.scan_qc:
                CodeNumberShow = "";
                etLed.setText(CodeNumberShow);
                break;
            case R.id.scan_sure:
                if (!etLed.getText().toString().isEmpty()) {
                    GoodsDto goodsDto = goodsDao.selectGoods(etLed.getText().toString());
                    if (goodsDto == null) {
                        Toast.makeText(getApplicationContext(), "商品不存在", Toast.LENGTH_SHORT).show();
                    } else {
                        judgeCode(etLed.getText().toString().trim());
                    }
                }
                break;
//            case R.id.more_payway:
//                if (!more_popup_state) {
//                    showMorePopupWindow();
//                } else {
//                    dismissMorePopupWindow();
//                }
//                break;
            case R.id.pay:
                if (sale_state) {//销售模式
                    /**
                     * 当点击了收款之后，会把金额给传送到下一个界面
                     */
                    if (!goodsDtos.isEmpty()) {
                        Intent intent = new Intent(ScanActivity.this, PayActivity.class);
                        intent.putExtra("money", tvMoney.getText().toString());
                        Log.i("money", tvMoney.getText().toString());
                        intent.putExtra("orderType", URL.ORDERTYPE_SALE);
                        intent.putExtra("maxNo", maxNo);
                        startActivity(intent);

                        Sava_list_To_Json.changeToJaon(getApplicationContext(), goodsDtos);

                        goodsDtos.clear();
                        setScreenData();
//                        maxNo = MaxNO.getPhoneMac()+(Integer.parseInt(maxNo.substring(12)) + 1);
                        maxNo = MaxNO.getPhoneMac()+(Integer.parseInt(maxNo.substring(maxNo.length()-4,maxNo.length())) + 1);
                    } else {
                        showToast("商品为空");
                    }
                } else {//退款模式
                    //当点击了退款之后，会把金额给传送到下一个界面
                    if (!(goodsDtos.size() == 0)) {
                        Intent intent = new Intent(ScanActivity.this, PayActivity.class);
                        intent.putExtra("money", tvMoney.getText().toString());
                        intent.putExtra("orderType", URL.ORDERTYPE_REFUND);
                        intent.putExtra("maxNo", maxNo);
                        startActivity(intent);

                        Sava_list_To_Json.changeToJaon(getApplicationContext(), goodsDtos);
//                        maxNo = MaxNO.getPhoneMac()+(Integer.parseInt(maxNo.substring(12)) + 1);
                        maxNo = MaxNO.getPhoneMac()+(Integer.parseInt(maxNo.substring(maxNo.length()-4,maxNo.length())) + 1);
                    } else {
                        showToast("未选取任何商品！");
                    }
                }
                break;
//            case R.id.ll_table:
//                Intent intent = new Intent(ScanActivity.this, SetTableActivity.class);
//                startActivityForResult(intent, PointActivity.TableRequest);
//                break;

        }
    }

    private void dismissMorePopupWindow() {
        if (morePopWindow != null) {
            morePopWindow.dismiss();
        }
        more_popup_state = false;
    }

    /**
     * 显示更多PopupWindow
     */
    private PopupWindow morePopWindow;

//    private void showMorePopupWindow() {
//        more_popup_state = true;
//        View contentView = LayoutInflater.from(ScanActivity.this).inflate(R.layout.menu_more, null);
//        morePopWindow = new PopupWindow(contentView);
//        morePopWindow.setWidth(morePayway.getWidth());
//        morePopWindow.setHeight(TurnSize.dip2px(getApplicationContext(), 200));
//
//        TextView cancel_order = (TextView) contentView.findViewById(R.id.cancel_order);
//        TextView guaDan = (TextView) contentView.findViewById(R.id.guaDan);
//        TextView luoDan = (TextView) contentView.findViewById(R.id.luodan);
//        TextView discount = (TextView) contentView.findViewById(R.id.discount);
//
//        MyClickListener listener = new MyClickListener();
//        cancel_order.setOnClickListener(listener);
//        guaDan.setOnClickListener(listener);
//        luoDan.setOnClickListener(listener);
//        discount.setOnClickListener(listener);
//
//        //指定PopupWindow显示在你指定的view下
//        int[] location = new int[2];
//        morePayway.getLocationOnScreen(location);
//        morePopWindow.showAtLocation(morePayway, Gravity.NO_GRAVITY, location[0], location[1] - morePopWindow.getHeight());
//    }

    public void judgeCode(String code) {
        GoodsDto goodsDto = new GoodsDto();

        goodsDto = goodsDao.selectGoods(code);
        if (goodsDto == null) {
//            Toast.makeText(this, "无商品信息", Toast.LENGTH_SHORT).show();
            return;
        }
        if (goodsDto != null) {
            if (goodsDtos.size() != 0) {
                for (int i =0 ;i<goodsDtos.size();i++) {
                    if (goodsDtos.get(i).getCode().equals(code)) {
                        goodsDtos.get(i).setNum(goodsDtos.get(i).getNum()+1);
                        break;
                    }
                    if (i == goodsDtos.size()-1) {
                        Log.i("gds", goodsDto.getName()+"------"+goodsDto.getNum() );
                        goodsDtos.add(goodsDto);
                        break;
                    }
                }

            } else {
                if (sale_state) {
                    goodsDto.setNum(1);
                } else {
                    goodsDto.setNum(-1);
                }
                goodsDtos.add(goodsDto);
            }
//            Log.i("code_name", goodsDto.getName());
            for (GoodsDto gd : goodsDtos) {
                Log.i("gd", gd.getName() + "----" + gd.getNum());
            }

//            adapter.add(goodsDto);
            adapter.notifyDataSetChanged();
            setScreenData();

//            vp.setAdapter(adapter);
            lv_scan.setFocusable(false);

            etLed.setText("");
            CodeNumberShow = "";

            ETRequestFocus();
        } else if (code.length() == 13){

        }
    }

    public void setScreenData() {
        double dou = 0;
        double totalNum = 0;
        double discount = 0.0;
        for (int i = 0; i < goodsDtos.size(); i++) {
            dou = dou + goodsDtos.get(i).getNum() * goodsDtos.get(i).getPrice();
            totalNum = totalNum + goodsDtos.get(i).getNum();
            discount += goodsDtos.get(i).getDiscount();
//            Log.i("tvNum1", String.valueOf(totalNum));

        }
        Log.i("tvNum2", String.valueOf(totalNum));
        tvDiscount.setText(String.valueOf(DoubleSave.doubleSaveTwo(discount)));
        tvNum.setText(String.valueOf(totalNum));
        tvMoney.setText(String.valueOf(DoubleSave.doubleSaveTwo(dou - discount)));
//        tableName.setText(eatingNumber + "人" + tableNamel);
    }

    public void delete() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setMessage("是否要销毁该订单？")
                .setTitle("提示")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        goodsDtos.clear();
                        adapter.notifyDataSetChanged();
                        setScreenData();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create();
        dialog.show();
    }

    private void showPopupWindow() {
        popup_state = true;
        View contentView = LayoutInflater.from(ScanActivity.this).inflate(R.layout.menu_fragment, null);
        mPopWindow = new PopupWindow(contentView);
        mPopWindow.setWidth(240);
        mPopWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
//
//        LinearLayout l1 = (LinearLayout) contentView.findViewById(R.id.return_good);
//        LinearLayout l3 = (LinearLayout) contentView.findViewById(R.id.money_box);
//        LinearLayout l5 = (LinearLayout) contentView.findViewById(R.id.screen_lock);
//        LinearLayout l7 = (LinearLayout) contentView.findViewById(R.id.back_menu);
//
//        MyClickListener listener = new MyClickListener();
//        l1.setOnClickListener(listener);
//        l3.setOnClickListener(listener);
//        l5.setOnClickListener(listener);
//        l7.setOnClickListener(listener);

        mPopWindow.showAsDropDown(menu);
    }

    public void changeState() {
        pay.setText("退款");
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
        dialog = new AlertDialog.Builder(ScanActivity.this).setView(dialogView).create();

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

    private class MyClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.return_good:
                    pay.setText("付款");
                    dismissPopupWindow();
                    showReturnDialog();
                    break;
//                case R.id.money_box:
//                    try {
//                        EscCommand esc = new EscCommand();
//                        esc.addGeneratePluseAtRealtime(LabelCommand.FOOT.F2, (byte) 2);//开钱箱
//                        Vector<Byte> datas = esc.getCommand(); // 发送数据
//                        Byte[] Bytes = datas.toArray(new Byte[datas.size()]);
//                        byte[] bytes = ArrayUtils.toPrimitive(Bytes);
//                        String sss = Base64.encodeToString(bytes, Base64.DEFAULT);// 最终转换好的数据
//                        App.getmGpService().sendEscCommand(0, sss);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                    dismissPopupWindow();
//                    break;
//                case R.id.screen_lock:
//                    dismissPopupWindow();
//                    startActivity(new Intent(ScanActivity.this, LockActivity.class));
//                    break;
//                case R.id.back_menu:
//                    dismissPopupWindow();
//                    finish();
//                    break;
                case R.id.have_note:
                    dialog.dismiss();
                    Intent intent = new Intent(ScanActivity.this, StatisticsActivity.class);
                    intent.putExtra(StatisticsActivity.REFUNDS_NAME, StatisticsActivity.REFUNDS_IN);
                    startActivity(intent);
                    break;
                case R.id.not_note:
                    if (goodsDtos.size() > 0) {
                        delete();
                        changeState();
                    } else {
                        changeState();
                    }
                    break;
                case R.id.cancel_order:
                    delete();
                    dismissMorePopupWindow();
                    break;
                case R.id.guaDan:
                    restOrder();
                    dismissMorePopupWindow();
                    break;
                case R.id.discount:
                    if (goodsDtos.size() > 0) {
                        Intent intent1 = new Intent(ScanActivity.this, OrderDiscountActivity.class);
                        intent1.putExtra("money", Double.parseDouble(tvMoney.getText().toString()));
                        startActivityForResult(intent1, 123);
                    }
                    morePopWindow.dismiss();
                    more_popup_state = false;
                    break;
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
                                        showToast("未添加该会员");
                                        hydialog.dismiss();
                                        vipName.setText("");
                                    } else {
                                        vipName.setText(memberDto.getName());
                                        hydialog.dismiss();
                                    }
                                    sm.scanClose();
                                }
                            }
                        });
                    } else {
                        showToast("暂不支持此功能");
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
                            showToast("未添加该会员");
                            vipName.setText("");
                            hydialog.dismiss();
                        } else {
                            vipName.setText(memberDto.getName());
                            hydialog.dismiss();
                        }
                    }
                    break;
                case R.id.luodan:
                    LuoDanOrder();
                    break;
            }
        }
    }

    private void LuoDanOrder() {
        if (goodsDtos.size() > 0) {
            String shopId = getSharedPreferences("StoreMessage", MODE_PRIVATE).getString("Id", "");
            OrderDto orderDto = new OrderDto();
            orderDto.setOrder_info(Sava_list_To_Json.changeGoodDtoToJaon(goodsDtos));//存储的该订单的商品的信息
            orderDto.setPayway(null);
            orderDto.setTime(GetData.getYYMMDDTime());
            orderDto.setSs_money("0.00");
            orderDto.setYs_money(tvMoney.getText().toString());
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
            orderDto.setIfFinishOrder(URL.ZHUOTai);

            if (tableNumber.trim().isEmpty()) {

                List<Tabledto> FreeTable = new TableDao(getApplicationContext()).findFreeTableInfo();
                if (FreeTable.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "没有空闲餐桌", Toast.LENGTH_SHORT).show();
                } else {
                    orderDto.setTableNumber(FreeTable.get(0).getSid());
                    //给数据库里面添加一条记录
                    new OrderDao(getApplicationContext()).addOrder(orderDto);
                    goodsDtos.clear();
                    adapter.notifyDataSetChanged();
                    setScreenData();
                    Toast.makeText(ScanActivity.this, " 落单成功！", Toast.LENGTH_SHORT).show();
//                    maxNo = MaxNO.getPhoneMac()+(Integer.parseInt(maxNo.substring(12)) + 1);
                    maxNo = MaxNO.getPhoneMac()+(Integer.parseInt(maxNo.substring(maxNo.length()-4,maxNo.length())) + 1);
                }
            } else {
                orderDto.setTableNumber(tableNumber);
                //给数据库里面添加一条记录
                new OrderDao(getApplicationContext()).addOrder(orderDto);
                goodsDtos.clear();
                eatingNumber = 0;
                tableNamel = "";
                adapter.notifyDataSetChanged();
                setScreenData();
                Toast.makeText(ScanActivity.this, " 落单成功！", Toast.LENGTH_SHORT).show();
//                maxNo = MaxNO.getPhoneMac()+(Integer.parseInt(maxNo.substring(12)) + 1);
                maxNo = MaxNO.getPhoneMac()+(Integer.parseInt(maxNo.substring(maxNo.length()-4,maxNo.length())) + 1);
            }
        } else {
            Toast.makeText(ScanActivity.this, " 没有可落单的订单！", Toast.LENGTH_SHORT).show();
        }
    }

    private void restOrder() {
        if (goodsDtos.size() > 0) {
            final AlertDialog deleteDialog = new AlertDialog.Builder(this)
                    .setTitle("提示")
                    .setMessage("确定挂单？")
                    .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            addRestOrderinfo();
//                            maxNo = MaxNO.getPhoneMac()+(Integer.parseInt(maxNo.substring(12)) + 1);
//                            maxNo = MaxNO.getPhoneMac()+(Integer.parseInt(maxNo.substring(maxNo.length()-4,maxNo.length())) + 1);
                        }
                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .create();
            deleteDialog.show();
        } else {
            Toast.makeText(ScanActivity.this, " 没有可挂单的订单！", Toast.LENGTH_SHORT).show();
        }
    }

    public void addRestOrderinfo() {
        String shopId = getSharedPreferences("StoreMessage", MODE_PRIVATE).getString("Id", "");
        OrderDto orderDto = new OrderDto();
        orderDto.setOrder_info(Sava_list_To_Json.changeGoodDtoToJaon(goodsDtos));//存储的该订单的商品的信息
        orderDto.setPayway(null);
        orderDto.setTime(GetData.getYYMMDDTime());
        orderDto.setSs_money("0.00");
        orderDto.setYs_money(tvMoney.getText().toString());
        orderDto.setZl_money("0.00");
        orderDto.setShop_id(shopId);
        orderDto.setOrderType(URL.ORDERTYPE_SALE);
        orderDto.setMaxNo(maxNo);
        orderDto.setCheckoutTime(GetData.getDataTime());
        orderDto.setDetailTime(GetData.getHHmmssTimet());
        orderDto.setHasReturnGoods(URL.hasReturnGoods_No);
        orderDto.setOut_trade_no(null);
        //当挂单的时候该数据用于保存是哪个界面进行的挂单操作
        orderDto.setPayReturn(RestOrderActivity.REST_SCAN);
        orderDto.setIfFinishOrder(URL.BAOLIU);
        /**
         * 给数据库里面添加一条记录
         */
        new OrderDao(getApplicationContext()).addOrder(orderDto);
        goodsDtos.clear();
        adapter.notifyDataSetChanged();
        setScreenData();
        Toast.makeText(ScanActivity.this, " 挂单成功！", Toast.LENGTH_SHORT).show();
    }
}
