package com.pospi.pai.yunpos.cash;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.pax.api.scanner.ScanResult;
import com.pax.api.scanner.ScannerListener;
import com.pax.api.scanner.ScannerManager;
import com.pospi.adapter.AddGoodsAdapter;
import com.pospi.dao.GoodsDao;
import com.pospi.dao.MemberDao;
import com.pospi.dao.OrderDao;
import com.pospi.dto.GoodsDto;
import com.pospi.dto.MemberDto;
import com.pospi.dto.OrderDto;
import com.pospi.http.MaxNO;
import com.pospi.http.Server;
import com.pospi.pai.yunpos.R;
import com.pospi.pai.yunpos.base.BaseActivity;
import com.pospi.pai.yunpos.more.LockActivity;
import com.pospi.pai.yunpos.more.RestOrderActivity;
import com.pospi.pai.yunpos.more.StatisticsActivity;
import com.pospi.pai.yunpos.pay.PayActivity;
import com.pospi.util.CashierLogin_pareseJson;
import com.pospi.util.CustomDialog;
import com.pospi.util.GetData;
import com.pospi.util.Sava_list_To_Json;
import com.pospi.util.constant.URL;

import org.apache.http.Header;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SaleActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.sale_tv_No)
    TextView No;
    @Bind(R.id.sale_tv_num)
    TextView num;
    @Bind(R.id.sale_tv_money)
    TextView money;
    @Bind(R.id.sale_lv)
    ListView lv;
    @Bind(R.id.btn_cancle)
    Button btn_cancle;
    @Bind(R.id.btn_pay)
    Button btn_pay;
    @Bind(R.id.sale_et)
    EditText et;
    @Bind(R.id.scan_code_iv)
    ImageView scan_code;
    @Bind(R.id.seve)
    TextView seven;
    @Bind(R.id.eigh)
    TextView eight;
    @Bind(R.id.nin)
    TextView nine;
    @Bind(R.id.fou)
    TextView four;
    @Bind(R.id.fiv)
    TextView five;
    @Bind(R.id.si)
    TextView six;
    @Bind(R.id.on)
    TextView one;
    @Bind(R.id.tw)
    TextView two;
    @Bind(R.id.thre)
    TextView three;
    @Bind(R.id.qc)
    TextView qingc;
    @Bind(R.id.zer)
    TextView zero;
    @Bind(R.id.sure)
    TextView queren;
    @Bind(R.id.menu)
    ImageView menu;

    public static String number = "";//做加法器的
    @Bind(R.id.layout1)
    LinearLayout layout1;
    @Bind(R.id.layout2)
    LinearLayout layout2;
    @Bind(R.id.tv_sale)
    TextView tvSale;
    @Bind(R.id.iv_sale)
    ImageView ivSale;
    @Bind(R.id.sale_cashier_name)
    TextView cashiername;
    @Bind(R.id.iv_huiyuan)
    ImageView ivhy;
    @Bind(R.id.sale_hy_name)
    TextView hyName;
    @Bind(R.id.btn_rest)
    Button btnRest;
    private GoodsDao goodsDao;
    private List<GoodsDto> goodsDtos;//new ArrayList<>();
    private AddGoodsAdapter adapter;
    private ScannerManager sm;
    private PopupWindow mPopWindow;
    private boolean popup_state;
    private boolean sale_state;
    private AlertDialog dialog;
    private CustomDialog.Builder ibuilder;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private OrderDto orderDto;

    private String maxNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sale);
        ButterKnife.bind(this);
        sale_state = true;

        orderDto = (OrderDto) getIntent().getSerializableExtra(RestOrderActivity.REST_ORDER);
        if (orderDto != null) {
            maxNo = orderDto.getMaxNo();
            Log.i("跳转之后", "NO" + orderDto.getMaxNo() + "");
            goodsDtos = Sava_list_To_Json.changeToList(orderDto.getOrder_info());
            num.setText(String.valueOf(goodsDtos.size()));
            money.setText(orderDto.getYs_money());

        } else {
            maxNo = MaxNO.getMaxNo(getApplicationContext());
            goodsDtos = new ArrayList<>();
        }

        initWidgets();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    /**
     * 弹出菜单
     */
    private void showPopupWindow() {
        popup_state = true;
        View contentView = LayoutInflater.from(SaleActivity.this).inflate(R.layout.menu_fragment, null);
        mPopWindow = new PopupWindow(contentView);
        mPopWindow.setWidth(220);
        mPopWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);



        mPopWindow.showAsDropDown(menu);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();

        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                judgeCode(et.getText().toString().trim());
            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // choose an action type.
                "Sale Page", // Define a title for the content shown.
                // If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),//Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.pospi.pai.pospiflat/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }


    public void initWidgets() {
        //得到最大的小票号 //得到最大的小票号
        No.setText(String.valueOf(maxNo));

        et.setInputType(InputType.TYPE_NULL);

        goodsDao = new GoodsDao(getApplicationContext());
        adapter = new AddGoodsAdapter(getApplicationContext(), goodsDtos);
        int whichOne = getSharedPreferences("islogin", MODE_PRIVATE).getInt("which", 0);
        String cashier_name = new CashierLogin_pareseJson().parese(
                getSharedPreferences("cashierMsgDtos", MODE_PRIVATE)
                        .getString("cashierMsgDtos", ""))
                .get(whichOne).getName();
        cashiername.setText(cashier_name);

        lv.setAdapter(adapter);
    }

    /**
     * 判断数据库里面是否有该条形码的商品
     *
     * @param code 商品的条形码
     */
    @SuppressLint("SetTextI18n")
    public void judgeCode(String code) {
        Log.i("code", code);
        GoodsDto goodsDto = goodsDao.selectGoods(code);
        if (goodsDto != null) {
            Log.i("code_name", goodsDto.getName());
            if (sale_state) {
                goodsDto.setNum(1);
            } else {
                goodsDto.setNum(-1);
            }
            Log.i("code_size", goodsDtos.size() + "");
            goodsDtos.add(goodsDto);
            Log.i("code_size", goodsDtos.size() + "");
            num.setText(goodsDtos.size() + "件");
            double dou = 0;
            for (int i = 0; i < goodsDtos.size(); i++) {
                dou = dou + goodsDto.getNum() * goodsDtos.get(i).getPrice();
            }
            BigDecimal b = new BigDecimal(Double.parseDouble(String.valueOf(dou)));
            double f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            money.setText(String.valueOf(f1));

            adapter.notifyDataSetChanged();
            lv.setFocusable(false);
            lv.setClickable(false);

            et.setText("");
            number = "";
            et.setFocusable(true);
            et.setFocusableInTouchMode(true);
            et.requestFocus();
        }
    }

    /**
     * 删除该订单时候弹出的对话框
     */
    public void delete() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setMessage("是否要销毁该订单？")
                .setTitle("提示")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        money.setText(R.string.zero);
                        num.setText(R.string.jian_0);
                        goodsDtos.clear();
                        adapter.notifyDataSetChanged();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create();
        dialog.show();
    }

    EditText etHy;
    AlertDialog hydialog;

    @OnClick({R.id.menu, R.id.btn_cancle, R.id.btn_pay, R.id.scan_code_iv, R.id.seve, R.id.eigh,
            R.id.nin, R.id.fou, R.id.fiv, R.id.si, R.id.on, R.id.tw, R.id.thre, R.id.qc, R.id.zer, R.id.sure, R.id.iv_huiyuan, R.id.btn_rest})
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
            case R.id.btn_rest:
                if (!goodsDtos.isEmpty()) {
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
                    showToast("请选择商品");
                }
                break;
            case R.id.btn_pay:
                if (sale_state) {//销售模式
                    /**
                     * 当点击了收款之后，会把金额给传送到下一个界面
                     */
                    if (!goodsDtos.isEmpty()) {
                        Intent intent = new Intent(SaleActivity.this, PayActivity.class);
                        intent.putExtra("money", money.getText().toString());
                        intent.putExtra("orderType", URL.ORDERTYPE_SALE);
                       // intent.putExtra("maxNo", Integer.parseInt(No.getText().toString()));
                        intent.putExtra("maxNo", Long.parseLong(No.getText().toString()));
                        startActivity(intent);

                        Sava_list_To_Json.changeToJaon(getApplicationContext(), goodsDtos);
                        goodsDtos.clear();
                        adapter.notifyDataSetChanged();
                        num.setText(R.string.jian_0);
                        money.setText(R.string.zero);
                    } else {
                        showToast("商品为空");
                    }
                } else {//退款模式
                    //当点击了退款之后，会把金额给传送到下一个界面
                    if (!(goodsDtos.size() == 0)) {
                        Intent intent = new Intent(SaleActivity.this, PayActivity.class);
                        intent.putExtra("money", money.getText().toString());
                        intent.putExtra("orderType", URL.ORDERTYPE_REFUND);
                        intent.putExtra("maxNo", MaxNO.getMaxNo(getApplicationContext()));
                        startActivity(intent);

                        Sava_list_To_Json.changeToJaon(getApplicationContext(), goodsDtos);
                        goodsDtos.clear();
                        adapter.notifyDataSetChanged();
                        num.setText(R.string.jian_0);
                        money.setText(R.string.zero);
                    } else {
                        showToast("未选取任何商品！");
                    }
                }
                break;
            case R.id.scan_code_iv:
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
                                et.setText(arg0.getContent());
                                sm.scanClose();
                            }
                        }
                    });
                } else {
                    showToast("暂不支持此功能");
                }
                break;
            case R.id.seve:
                number = String.format("%s%s", number, seven.getText().toString());
                break;
            case R.id.eigh:
                number = String.format("%s%s", number, eight.getText().toString());
                break;
            case R.id.nin:
                number = String.format("%s%s", number, nine.getText().toString());
                break;
            case R.id.fou:
                number = String.format("%s%s", number, four.getText().toString());
                break;
            case R.id.fiv:
                number = String.format("%s%s", number, five.getText().toString());
                break;
            case R.id.si:
                number = String.format("%s%s", number, six.getText().toString());
                break;
            case R.id.on:
                number = String.format("%s%s", number, one.getText().toString());
                break;
            case R.id.tw:
                number = String.format("%s%s", number, two.getText().toString());
                break;
            case R.id.thre:
                number = String.format("%s%s", number, three.getText().toString());
                break;
            case R.id.zer:
                number = String.format("%s%s", number, zero.getText().toString());
                break;
            case R.id.qc:
                number = "";
                break;
            case R.id.sure:
                if (!et.getText().toString().isEmpty()) {
                    GoodsDto goodsDto = goodsDao.selectGoods(et.getText().toString());
                    if (goodsDto == null) {
                        Toast.makeText(getApplicationContext(), "商品不存在", Toast.LENGTH_SHORT).show();
                    } else {
                        judgeCode(et.getText().toString().trim());
                    }
                }
                break;
            case R.id.menu:
                if (!popup_state) {
                    showPopupWindow();
                } else {
                    sale_state = true;
                    dismissPopupWindow();
                }
                break;
            case R.id.return_good:
                sale_state = true;
                layout1.setBackgroundColor(getResources().getColor(R.color.blue_sale));
                layout2.setBackgroundColor(getResources().getColor(R.color.blue_sale));
                tvSale.setText("销售");
                ivSale.setImageResource(R.drawable.xiaoshou);
                btn_pay.setText("付款");
                dismissPopupWindow();
                showReturnDialog();
                break;
//            case R.id.money_box:
////                try {
////                    EscCommand esc = new EscCommand();
////                    esc.addGeneratePluseAtRealtime(LabelCommand.FOOT.F2, (byte) 2);//开钱箱
////                    Vector<Byte> datas = esc.getCommand(); // 发送数据
////                    Byte[] Bytes = datas.toArray(new Byte[datas.size()]);
////                    byte[] bytes = ArrayUtils.toPrimitive(Bytes);
////                    String sss = Base64.encodeToString(bytes, Base64.DEFAULT);// 最终转换好的数据
////                    App.getmGpService().sendEscCommand(0, sss);
////                } catch (Exception e) {
////                    e.printStackTrace();
////                }
//                showToast("暂不支持...");
//                dismissPopupWindow();
//                break;
//            case R.id.screen_lock:
//                dismissPopupWindow();
//                startActivity(LockActivity.class);
//                break;
//            case R.id.back_menu:
//                dismissPopupWindow();
//                finish();
//                break;
            case R.id.have_note:
                dialog.dismiss();
                Intent intent = new Intent(SaleActivity.this, StatisticsActivity.class);
                intent.putExtra(StatisticsActivity.REFUNDS_NAME, StatisticsActivity.REFUNDS_IN);
                startActivity(intent);
                break;
            case R.id.not_note:
                if (goodsDtos.size() > 0) {

                    ibuilder = new CustomDialog.Builder(SaleActivity.this);
                    ibuilder.setTitle("询问");
                    ibuilder.setMessage("确定要销毁此单？");
                    ibuilder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            goodsDtos.clear();
                            adapter.notifyDataSetChanged();
                            num.setText(R.string.jian_0);
                            money.setText(R.string.zero);
                            changeState();
                        }
                    });
                    ibuilder.setNegativeButton("取消", null);
                    ibuilder.create().show();

                } else {
                    changeState();
                }
                break;
        }
        et.setText(number);
    }

    public void addRestOrderinfo() {
        String shopId = getSharedPreferences("StoreMessage", MODE_PRIVATE).getString("Id", "");
        OrderDto orderDto = new OrderDto();
        orderDto.setOrder_info(Sava_list_To_Json.changeGoodDtoToJaon(goodsDtos));//存储的该订单的商品的信息
        orderDto.setPayway(null);
        orderDto.setTime(GetData.getYYMMDDTime());
        orderDto.setSs_money("0.00");
        orderDto.setYs_money(money.getText().toString());
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
        money.setText(R.string.zero);
        num.setText(R.string.jian_0);
        goodsDtos.clear();
        adapter.notifyDataSetChanged();
        Toast.makeText(SaleActivity.this, " 挂单成功！", Toast.LENGTH_SHORT).show();
        maxNo = maxNo + 1;
        No.setText(String.valueOf(maxNo));
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, //  choose an action type.
                "Sale Page", // Define a title for the content shown.
                // If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),// Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.pospi.pai.pospiflat/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

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
                                        showToast("未添加该会员");
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

    public void findVip(String card) {
        RequestParams params = new RequestParams();
        params.put("value", card);

        new Server().getConnect(getApplicationContext(), new URL().FINDMEMBER, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                Log.d("findMember", new String(bytes));
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {

            }
        });
    }

    /**
     * 改变状态为退款模式
     */
    public void changeState() {
        layout1.setBackgroundColor(getResources().getColor(R.color.red));
        layout2.setBackgroundColor(getResources().getColor(R.color.red));
        tvSale.setText("退款");
        ivSale.setImageResource(R.drawable.down);
        btn_pay.setText("退款");
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
        dialog = new AlertDialog.Builder(SaleActivity.this).setView(dialogView).create();

        Button have_note = (Button) dialogView.findViewById(R.id.have_note);
        Button not_note = (Button) dialogView.findViewById(R.id.not_note);
        have_note.setOnClickListener(this);
        not_note.setOnClickListener(this);

        int height = URL.getScreemHeight();
        int width = URL.getScreemWidth();
        //设置对话框的高度和宽度
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = width / 5;
        lp.height = height / 5;
        dialog.show();
    }
}