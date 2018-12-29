package com.pospi.pai.yunpos.cash;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.digi.module.Printer.IPrinter;
import com.digi.port.Printer;
import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
//import com.lany.sp.SPHelper;
import com.google.zxing.BarcodeFormat;
import com.king.zxing.util.CodeUtils;
import com.lany.sp.SPHelper;
import com.lijunhuayc.upgrade.helper.UpgradeHelper;
import com.maning.library.MClearEditText;
import com.maning.updatelibrary.InstallUtils;
import com.mylhyl.circledialog.CircleDialog;
import com.mylhyl.circledialog.params.ProgressParams;
import com.mylhyl.circledialog.view.listener.OnCreateBodyViewListener;
import com.pax.dal.IDAL;
import com.pospi.adapter.MsgAdapter;
import com.pospi.adapter.RestDialogAdapter;
import com.pospi.adapter.SearchGoodAdapter;
import com.pospi.adapter.ShoppingAdapter;
import com.pospi.adapter.VipSaleAdapter;
import com.pospi.callbacklistener.HttpCallBackListener;
import com.pospi.dao.GoodsDao;
import com.pospi.dao.ModifiedGroupDao;
import com.pospi.dao.OrderDao;
import com.pospi.dto.GoodsDto;
import com.pospi.dto.LoginReturnDto;
import com.pospi.dto.MenuDto;
import com.pospi.dto.OrderDto;
import com.pospi.dto.VipBeen;
import com.pospi.fragment.BlankFragment;
import com.pospi.greendao.TablebeenDao;
import com.pospi.http.HttpConnection;
import com.pospi.http.MaxNO;
import com.pospi.pai.yunpos.R;
import com.pospi.pai.yunpos.been.CustomerBeen;
import com.pospi.pai.yunpos.been.MsgBeen;
import com.pospi.pai.yunpos.been.PermitonBeen;
import com.pospi.pai.yunpos.been.SaveOrderInfo;
import com.pospi.pai.yunpos.been.UpDateBeen;
import com.pospi.pai.yunpos.bh.SupGoodsListActivity;
import com.pospi.pai.yunpos.caigou.CgshListActivity;
import com.pospi.pai.yunpos.caigou.CountsListActivity;
import com.pospi.pai.yunpos.caigou.OverFlowListActivity;
import com.pospi.pai.yunpos.caigou.ReceiveBillListActivity;
import com.pospi.pai.yunpos.caigou.RefundBillListActivity;
import com.pospi.pai.yunpos.diaob.DboOutListActivity;
import com.pospi.pai.yunpos.diaob.DboReceiveListActivity;
import com.pospi.pai.yunpos.login.Constant;
import com.pospi.pai.yunpos.login.UserLoginActivity;
import com.pospi.pai.yunpos.more.RestOrderActivity;
import com.pospi.pai.yunpos.more.ScaleGoodsActivity;
import com.pospi.pai.yunpos.more.StatisticsActivity;
import com.pospi.pai.yunpos.pay.PayActivity;
import com.pospi.pai.yunpos.report.GoodsReportActivity;
import com.pospi.pai.yunpos.report.InventorySearchActivity;
import com.pospi.pai.yunpos.report.KhReportActivity;
import com.pospi.pai.yunpos.report.SaleCateReportActivity;
import com.pospi.pai.yunpos.report.StoreReportActivity;
import com.pospi.pai.yunpos.report.YWReportActivity;
import com.pospi.pai.yunpos.table.SetTableActivity;
import com.pospi.pai.yunpos.util.DiscountDialogActivity;
import com.pospi.pai.yunpos.util.DzcUtil;
import com.pospi.pai.yunpos.util.GoodsUtil;
import com.pospi.pai.yunpos.util.LogUtil;
import com.pospi.pai.yunpos.util.ModifiedDialogActivity;
//import com.pospi.pai.pospiflat.util.UnionConfig;
import com.pospi.pai.yunpos.util.PermitionUtil;
import com.pospi.pai.yunpos.util.ScaleUItil;
import com.pospi.pai.yunpos.util.TwiceConfirmExitUtil;
import com.pospi.pai.yunpos.util.UnionConfig;
import com.pospi.pai.yunpos.vip.VIPRegisterActivity;
import com.pospi.pai.yunpos.vip.VipActivity;
import com.pospi.pai.yunpos.vip.VipGoodsBeen;
import com.pospi.pai.yunpos.xs.OutBillListActivity;
import com.pospi.pai.yunpos.xs.SaleListActivity;
import com.pospi.update.UpdateHelper;
import com.pospi.util.App;
import com.pospi.util.DoubleSave;
import com.pospi.util.DownUtil;
import com.pospi.util.GetData;
import com.pospi.util.NetUtil;
import com.pospi.util.OderUtil;
import com.pospi.util.PromotionUtil;
import com.pospi.util.Sava_list_To_Json;
import com.pospi.util.SaveMenuInfo;
import com.pospi.util.StarPrintHelper;
import com.pospi.util.SumiUtil;
import com.pospi.util.TabPrintHelper;
import com.pospi.util.ViewFindUtils;
import com.pospi.util.constant.URL;
import com.pospi.view.swipemenulistview.SwipeMenu;
import com.pospi.view.swipemenulistview.SwipeMenuCreator;
import com.pospi.view.swipemenulistview.SwipeMenuItem;
import com.pospi.view.swipemenulistview.SwipeMenuListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ezy.ui.view.BadgeButton;

import static com.pospi.util.App.getContext;

/**
 * 点选收款
 */

public class PointActivity extends FragmentActivity implements BlankFragment.OnGridViewClick {

    @Bind(R.id.vip_name)
    TextView vipName;
    @Bind(R.id.menu)
    ImageView menu;
    @Bind(R.id.goods_lv)
    SwipeMenuListView goodsLv;
    @Bind(R.id.pay)
    Button pay;
    @Bind(R.id.tv_num)
    TextView tvNum;
    @Bind(R.id.tv_discount2)
    TextView tvDiscount;
    @Bind(R.id.tv_money)
    TextView tvMoney;
    @Bind(R.id.parent)
    LinearLayout parent;
    @Bind(R.id.vip_bt)
    TextView vip_bt;
    @Bind(R.id.et_goods_code)
    MClearEditText et_goods_code;
    @Bind(R.id.tv_rest_order)
    TextView tv_rest_order;
    @Bind(R.id.order_no)
    TextView order_no;
    @Bind(R.id.yj_price)
    TextView yj_price;
    @Bind(R.id.tv_md_name)
    TextView tv_md_name;
    @Bind(R.id.tv_syy)
    TextView tv_syy;
    @Bind(R.id.ll_yyy)
    LinearLayout ll_yyy;
    @Bind(R.id.tv_daogou)
    TextView tv_daogou;
    @Bind(R.id.et_search_good)
    EditText et_search_good;
    @Bind(R.id.gv_search_goods)
    GridView gv_search_goods;
    @Bind(R.id.tv_type)
    TextView tv_type;
    @Bind(R.id.tv_syjh)
    TextView tv_syjh;
    @Bind(R.id.bt_msg)
    BadgeButton bt_msg;

    private ArrayList<Fragment> mFragments = new ArrayList<>();


    private List<String> titles = null;
    private List<String> code = null;
    private List<MenuDto> menuDtos = null;
    private List<GoodsDto> lv_goodsBeens = new ArrayList<>();

    private PopupWindow mPopWindow, morePopWindow, mPricePopupWindow;
    private AlertDialog dialog, hydialog;
    private GoodsDao dao;
    private OrderDto orderDto;
    //    private ScannerManager sm;
    private ShoppingAdapter adapter;
    private MyClickListener listener;


    private EditText etHy;

    private double total_money;
    private boolean more_popup_state = false;
    private boolean popup_state, repeat;
    private boolean sale_state = true;
    private String maxNo;
    private int posi;
    public static final int REQUESTCODE = 101;
    public static final int TableRequest = 22;
    private int eatingNumber = 0;
    private String tableNumber = "";
    private String tableNamel;
    private int good_num = 1;
    private GoodsDto goodsdto;
    private ModifiedGroupDao modifiedGroupDao;
    private TablebeenDao tablebeenDao;
    private String tableId = "";
    private String vip_number = "";
    private GoodsDao goodsDao;
    private PromotionUtil util;
    private ViewPager vp;
    private SlidingTabLayout tabLayout_8;
    private TextView tv_jp_sl;
    private TextView tv_jp_zk;
    private TextView tv_jp_zr;
    private RelativeLayout rl_hyxf;
    private LinearLayout ll_hyxx;
    private LinearLayout ll_hycx;
    private LinearLayout ll_hymsg;
    private TextView hyxx_name;
    private TextView hyxx_no;
    private TextView hyxx_phone;
    private TextView hyxx_type;
    private TextView hyxx_mzye;
    private TextView hyxx_jf;
    private TextView tv_hyxx_no;
    private LinearLayout ll_hycz;
    private LinearLayout hycz_menu;
    private SearchGoodAdapter searchGoodAdapter;
    private TextView tv_jp_dj;
    private String SaleType = "";
    private ScaleUItil scaleUItil;
    private SumiUtil sumiUtil;
    private PermitonBeen permiton;
    private String mode = "";
    private VipBeen lsVipBeen;
    private DownUtil downUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            setContentView(R.layout.point_select);
            ButterKnife.bind(this);
            downUtil = new DownUtil(this);
            downUtil.initMsg(handler);//初始化消息
            downUtil.checkVertion(getSupportFragmentManager());//检查版本更新
            permiton = new PermitionUtil().getPermiton();//获取操作权限
//            rePermition();//获取动态权限
            scaleUItil = new ScaleUItil(this);//电子秤
            scaleUItil.openScale();
            sumiUtil = new SumiUtil(this);//副屏
            sumiUtil.clear();
            dao = new GoodsDao(this);
            init();
            downUtil.downCus();
            tv_syy.setText(SPHelper.getInstance().getString(Constant.CUSTOMER_name));
            tv_md_name.setText(SPHelper.getInstance().getString(Constant.STORE_NAME));
            tv_syjh.setText(SPHelper.getInstance().getString(Constant.SYJH));

            util = new PromotionUtil(this);
            et_goods_code.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    String code = et_goods_code.getText().toString().trim();
                    if (!code.isEmpty()) {
                        if (DzcUtil.isPlu(code)) {
                            searchDzcGood(DzcUtil.getRule(code));
                        } else {
                            searchGood(code);
                        }

                    }

                    return true;
                }
            });
            tablebeenDao = App.getInstance().getDaoSession().getTablebeenDao();
            goodsDao = new GoodsDao(this);
//
//            getData();
            sale_state = true;
            listener = new MyClickListener();
            orderDto = (OrderDto) getIntent().getSerializableExtra(RestOrderActivity.REST_ORDER);

            goodsLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    posi = position;
                    showSetPricePopupWindow(lv_goodsBeens.get(position));
//                    Intent intent = new Intent(PointActivity.this, DiscountDialogActivity.class);
//                    Bundle bundle = new Bundle();
//                    bundle.putSerializable(DiscountDialogActivity.DISCOUNT, lv_goodsBeens.get(position));
//                    intent.putExtras(bundle);
//                    startActivityForResult(intent, DiscountDialogActivity.DISCOUNTDIALOGACTIVITY_REQUEST);
                }
            });

            et_search_good.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    et_search_good.setFocusableInTouchMode(true);
                    return false;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //消息提醒
    private void setMsg(int number) {
        if (number > 0) {
            bt_msg.setBadgeVisible(true);
            bt_msg.setBadgeText(String.valueOf(number));
        } else {
            bt_msg.setBadgeVisible(false);
        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();

    }


    @Override
    protected void onStart() {
        super.onStart();
        reStartUI();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        //支付成功
        isDicount = false;
        vipBeen = null;
        vipName.setText("");
        String model = intent.getStringExtra("data");
        if (model != null && model.equals(Constant.Refund)) {
            SaleType = model;
            tv_type.setText("退货");
            sale_state = false;

        } else if (model != null && model.equals(Constant.LX)) {
            tv_type.setText("练习模式");
            mode = Constant.LX;
        } else if (model != null && model.equals("xs")) {
            mode = "";
            tv_type.setText("收银");
            sale_state = true;
        }
        if (Constant.LX.equals(mode)) {
            tv_type.setText("练习模式");
        } else {
            tv_type.setText("收银");
            sale_state = true;
        }
        Sava_list_To_Json.clearGoodsMsg(PointActivity.this);
    }

    public void reStartUI() {
        if (orderDto != null) {
            maxNo = orderDto.getMaxNo();
//            Log.i("跳转之后", "NO" + orderDto.getMaxNo() + "");
            lv_goodsBeens = Sava_list_To_Json.changeToList(orderDto.getOrder_info());
            tableId = orderDto.getTableId();
            setScreenData();
            orderDto = null;
        } else {
            String goodsMsg = getSharedPreferences("goodsdto_json1", MODE_PRIVATE).getString("goodsMsg", "");
            if (goodsMsg.equals("")) {
                lv_goodsBeens = new ArrayList<>();
            } else {
                lv_goodsBeens = Sava_list_To_Json.changeToList(goodsMsg);
            }
            maxNo = MaxNO.getMaxNo(getApplicationContext());
//            lv_goodsBeens = new ArrayList<>();
            setScreenData();
        }
        adapter = new ShoppingAdapter(this, lv_goodsBeens);
        goodsLv.setAdapter(adapter);
        order_no.setText("订单号:" + maxNo);
        setSwipeListView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.postDelayed(runnable, 0);
        gv_search_goods.setVisibility(View.GONE);
        goodsBeenList.clear();


    }

    @OnClick({R.id.menu, R.id.pay, R.id.tv_delete_all, R.id.tv_discount_all, R.id.tv_rest_order, R.id.ll_yyy, R.id.bt_search_goods,
            R.id.vip_bt, R.id.iv_search_xz, R.id.ll_count,R.id.bt_msg})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_msg://消息提醒
                getMsgDialog();
                setMsg(0);

                break;
            case R.id.ll_count:
                new AlertDialog.Builder(this)
                        .setTitle("提示")
                        .setMessage("是否退出登录")
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SPHelper.getInstance().putBoolean("login", false);
                                startActivity(new Intent(PointActivity.this, UserLoginActivity.class));
                                Sava_list_To_Json.clearGoodsMsg(PointActivity.this);
                                new LogUtil().save(Constant.LOG_EXSIT);
                                finish();
                            }
                        })
                        .setNegativeButton("取消", null).show();
                break;
            case R.id.bt_search_goods:
                boolean mode_img = SPHelper.getInstance().getBoolean(Constant.MODE_IMG, false);
                //联网查商品
                vp.setVisibility(View.GONE);
                gv_search_goods.setVisibility(View.VISIBLE);
                searchGoodAdapter = new SearchGoodAdapter(this, goodsBeenList, mode_img);
                gv_search_goods.setAdapter(searchGoodAdapter);
                gv_search_goods.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //商品点击动画visToInvis.start()
                        Interpolator accelerator = new AccelerateInterpolator();
                        Interpolator decelerator = new DecelerateInterpolator();
                        final View visibleList, invisibleList;
                        final ObjectAnimator visToInvis, invisToVis;
                        if (view.getVisibility() == View.GONE) {
                            visibleList = view;
                            invisibleList = view;
                            visToInvis = ObjectAnimator.ofFloat(visibleList, "rotationY", 0f, 90f);
                            invisToVis = ObjectAnimator.ofFloat(invisibleList, "rotationY", -90f, 0f);
                        } else {
                            invisibleList = view;
                            visibleList = view;
                            visToInvis = ObjectAnimator.ofFloat(visibleList, "rotationY", 0f, -90f);
                            invisToVis = ObjectAnimator.ofFloat(invisibleList, "rotationY", 90f, 0f);
                        }
                        visToInvis.setDuration(200);
                        invisToVis.setDuration(200);
                        visToInvis.setInterpolator(accelerator);
                        invisToVis.setInterpolator(decelerator);
                        visToInvis.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator anim) {
                                visibleList.setVisibility(View.GONE);
                                invisToVis.start();
                                invisibleList.setVisibility(View.VISIBLE);
                            }
                        });
                        visToInvis.start();
                        GoodsDto dto = new GoodsUtil().getNewGoods(goodsBeenList.get(position));
                        if (dto.equals("0")) {
                            goodsdto = dto;
                            showSetWeightPopupWindow();
                        } else {
                            if (dto.getDzc().equals("1")&&SPHelper.getInstance().getBoolean(Constant.DZC_TAB)) {//计分，打印标签
                                new TabPrintHelper(PointActivity.this, dto).printTabNum();
                            }
                            addGoods(dto);
                        }

                    }
                });
                List<GoodsDto> list = goodsDao.findGoodsByFilter(et_search_good.getText().toString().trim());
                goodsBeenList.clear();
                goodsBeenList.addAll(list);
                searchGoodAdapter.notifyDataSetChanged();
                break;
            case R.id.ll_yyy:
                showYYY(ll_yyy);
                break;
            case R.id.iv_search_xz:
                String code = et_goods_code.getText().toString().trim();
                if (!code.isEmpty()) {
                    if (DzcUtil.isPlu(code)) {
                        searchDzcGood(DzcUtil.getRule(code));
                    } else {
                        searchGood(code);
                    }
                }
                break;
            case R.id.tv_delete_all:
                delete();
                break;
            case R.id.vip_bt:
                showhyDialog();
//                vipDialog();

                break;
            case R.id.menu:
                startActivity(new Intent(this, MenuActivity.class));
//                if (!popup_state) {
//                    showPopupWindow();
//                } else {
//                    dismissPopupWindow();
//                }
                break;
            case R.id.tv_discount_all:
                if (lv_goodsBeens.size() > 0) {
                    showBillDiscout();
//                    Intent intent1 = new Intent(PointActivity.this, OrderDiscountActivity.class);
//                    intent1.putExtra("money", Double.parseDouble(tvMoney.getText().toString()));
//                    startActivityForResult(intent1, 123);
                }
                break;
            case R.id.tv_rest_order:
                if (!permiton.isCanGd()) {
                    Toast.makeText(this, Constant.PERMITION_TOAST, Toast.LENGTH_SHORT).show();
                    return;
                }

                if (tv_rest_order.getText().toString().equals("挂单")) {
                    restOrder();

                } else {
                    getRestOrderDialog();
                }

                break;
            case R.id.pay:
                //
                UnionConfig.accessToken();

//                UnionConfig.accessToken();
                if (sale_state) {//销售模式
                    /**
                     * 当点击了收款之后，会把金额给传送到下一个界面
                     */
                    if (!lv_goodsBeens.isEmpty()) {
                        setbillDiscount();
//                        setML();
                        Intent intent = new Intent(PointActivity.this, PayActivity.class);
                        intent.putExtra("money", tvMoney.getText().toString());
                        intent.putExtra("orderType", URL.ORDERTYPE_SALE);
                        intent.putExtra("maxNo", maxNo);
                        intent.putExtra("vip", vipBeen);
                        intent.putExtra("vip_number", vip_number);
                        intent.putExtra("cus", customerBeen);
                        intent.putExtra("mode", mode);
                        startActivity(intent);
                        Sava_list_To_Json.changeToJaon(getApplicationContext(), lv_goodsBeens);
//                        textviewClear();
//                        maxNo = ""+(Integer.parseInt(maxNo) + 1);
                    } else {
                        Toast.makeText(PointActivity.this, "未点选任何商品！", Toast.LENGTH_SHORT).show();
                    }
                } else {//退款模式
                    //当点击了退款之后，会把金额给传送到下一个界面
                    if (!lv_goodsBeens.isEmpty()) {
                        setbillDiscount();
                        setML();
                        Intent intent = new Intent(PointActivity.this, PayActivity.class);
                        intent.putExtra("money", tvMoney.getText().toString());
                        intent.putExtra("orderType", URL.ORDERTYPE_REFUND);
                        intent.putExtra("maxNo", maxNo);
                        intent.putExtra("vip", vipBeen);
                        intent.putExtra("vip_number", vip_number);
                        intent.putExtra("cus", customerBeen);
                        startActivity(intent);
                        Sava_list_To_Json.changeToJaon(getApplicationContext(), lv_goodsBeens);
//                        textviewClear();
//                        maxNo = ""+(Integer.parseInt(maxNo) + 1);
                    } else {
                        Toast.makeText(PointActivity.this, "未点选任何商品！", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    private void setMsgStatus(String id) {
        Map<String, String> params = new HashMap<>();
        params.put("logid", SPHelper.getInstance().getString(Constant.USER_ID));
        params.put("sysid", SPHelper.getInstance().getString(Constant.SYS_ID));
        params.put("model", "syspara.msg");
        params.put("fun", "isread");
        params.put("table", "xb_sys_message_user");
        params.put("id", id);
        new HttpConnection().postNet(params, new HttpCallBackListener() {
            @Override
            public void CallBack(String Response) {

            }
        });
    }

    //抹零
    private void setML() {
        float ml = SPHelper.getInstance().getFloat(Constant.POS_MLSX);
        double temp = Double.parseDouble(tvMoney.getText().toString())-(int)Double.parseDouble(tvMoney.getText().toString());
        if (ml > 0 && temp > 0) {
            if (ml > temp) {
                double ls = lv_goodsBeens.get(0).getLszk();
                lv_goodsBeens.get(0).setLszk(DoubleSave.doubleSaveTwo(ls + temp));
                adapter.notifyDataSetChanged();
                setScreenData();
            }
        }
    }

    private boolean isDicount = false;

    private void setbillDiscount() {
        String prodicountAll = util.checkbill(Double.parseDouble(tvMoney.getText().toString()));
        String prodiscount = util.chekDetail(lv_goodsBeens);
        String dismoney = "0";
        if (prodicountAll.contains("%")) {
            double ss = Double.parseDouble(tvMoney.getText().toString());
            double re = ss * (1 - Double.parseDouble(prodicountAll.substring(0, prodicountAll.length() - 1)) / 100);
            if (re > Double.parseDouble(prodiscount)) {
                dismoney = prodicountAll;
            } else {
                dismoney = prodiscount;
            }
        } else {
            dismoney = Double.parseDouble(prodicountAll) > Double.parseDouble(prodiscount) ? prodicountAll : prodiscount;
        }
        double proReduce = 0;
        if (dismoney.contains("%")) {
            proReduce = DoubleSave.doubleSaveTwo((1 - Double.parseDouble(dismoney.substring(0, dismoney.length() - 1)) / 100) * Double.parseDouble(tvMoney.getText().toString()));
        } else {
            proReduce = DoubleSave.doubleSaveTwo(Double.parseDouble(dismoney));
        }
        if (proReduce != 0) {
            double oldMoeny = 0;
            for (GoodsDto dto : lv_goodsBeens) {
                oldMoeny += dto.getOldPrice() * dto.getNum();
            }

            Toast.makeText(this, "促销整单优惠金额：" + proReduce, Toast.LENGTH_SHORT).show();
            proReduce = proReduce / oldMoeny;
            for (GoodsDto dto : lv_goodsBeens) {
                dto.setCxzk(DoubleSave.doubleSaveTwo(proReduce * dto.getNum() * dto.getPrice()));
            }
            isDicount = true;
        } else {
            if (isDicount) {
                for (GoodsDto dto : lv_goodsBeens) {
                    dto.setCxzk(0);
                }
                isDicount = false;
            }

        }
        adapter.notifyDataSetChanged();
        setScreenData();

    }

    private void setlsbillDiscount(double proReduce, String type) {

        switch (type) {
            case "zk":
                proReduce = (1 - proReduce / 10);
                for (GoodsDto dto : lv_goodsBeens) {
                    dto.setLszk(DoubleSave.doubleSaveTwo(proReduce * dto.getNum() * dto.getPrice()));
                }
                break;
            case "zr":
                if (proReduce < Double.parseDouble(tvMoney.getText().toString())) {
                    for (GoodsDto dto : lv_goodsBeens) {
                        dto.setLszk(DoubleSave.doubleSaveTwo(proReduce));
                    }
                } else {
                    Toast.makeText(this, "折让金额不能大于总金额", Toast.LENGTH_SHORT).show();
                }

                break;
            case "zs":

                for (GoodsDto dto : lv_goodsBeens) {
                    dto.setLszk(DoubleSave.doubleSaveTwo(dto.getNum() * dto.getPrice()));
                }
                break;
        }

        adapter.notifyDataSetChanged();
        setScreenData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) { //resultCode为回传的标记，我在B中回传的是RESULT_OK
            case RESULT_OK:
                Bundle b = data.getExtras(); //data为B中回传的Intent
//                String str = b.getString("str1");//str即为回传的值
                break;
            case ModifiedDialogActivity.REQUESTCODE:
                switch (resultCode) {
                    case ModifiedDialogActivity.RESULTCODE_CANCLE:
                        break;
                    case ModifiedDialogActivity.RESULTCODE_SURE:
                        Bundle bundle = data.getExtras(); //data为B中回传的Intent
                        Set<String> set = (Set<String>) bundle.getSerializable("modifieds");
//                        List<ModifiedDto> modifiedDtos = (List<ModifiedDto>) bundle.getSerializable("modifieds");

                        String remark = "";
                        for (String s : set) {
                            remark += (s + " ");
                        }
                        Log.i("remark", remark);
                        goodsdto.setModified(remark);
                        if (goodsdto.getPrice() > 0) {
                            if (sale_state) {
                                for (int i = 0; i < lv_goodsBeens.size(); i++) {
                                    if (lv_goodsBeens.get(i).getName().equals(goodsdto.getName())) {
                                        lv_goodsBeens.get(i).setNum(lv_goodsBeens.get(i).getNum() + 1);
                                        repeat = false;
                                    }
                                }
                                if (repeat) {
                                    goodsdto.setNum(1);
                                    lv_goodsBeens.add(goodsdto);
                                }
                            } else {
                                for (int i = 0; i < lv_goodsBeens.size(); i++) {
                                    if (lv_goodsBeens.get(i).getName().equals(goodsdto.getName())) {
                                        lv_goodsBeens.get(i).setNum(lv_goodsBeens.get(i).getNum() - 1);
                                        repeat = false;
                                    }
                                }
                                if (repeat) {
                                    goodsdto.setNum(-1);
                                    lv_goodsBeens.add(goodsdto);
                                }
                            }
                            adapter.notifyDataSetChanged();
                            setScreenData();
                            break;
                        }
                        break;

                }
                break;
            case DiscountDialogActivity.DISCOUNTDIALOGACTIVITY_REQUEST://点击进行折扣的返回值
                Log.i("onActivityResult", "onActivityResult: " + "收到返回");
                switch (resultCode) {
                    case DiscountDialogActivity.DISCOUNTDIALOGACTIVITY_RESULT_OK:
                        double num = data.getDoubleExtra("num", 1);
                        double discount = data.getDoubleExtra("discount", 0.0);
                        Log.i("result", "数量: " + num + "折让：" + discount);
                        lv_goodsBeens.get(posi).setNum(num);
                        lv_goodsBeens.get(posi).setLszk(DoubleSave.doubleSaveTwo(discount));
                        setScreenData();
                        adapter.notifyDataSetChanged();
                        break;

                    case DiscountDialogActivity.DISCOUNTDIALOGACTIVITY_RESULT_CANCEL:

                        lv_goodsBeens.remove(posi);
                        adapter.notifyDataSetChanged();
                        setScreenData();
                        break;
                }
                break;
            case TableRequest:
                switch (resultCode) {
                    case 1:
//                        eatingNumber = data.getIntExtra(SetTableActivity.EatingNumber, 1);
                        tableNumber = data.getStringExtra(SetTableActivity.TableName);
                        tableId = data.getStringExtra(SetTableActivity.TableID);
//                        Log.i("tableNamel", tableNamel);
//                        tableName.setText(tableNumber);
                        break;
                    case 2:
                        orderDto = (OrderDto) data.getSerializableExtra(SetTableActivity.TableOrder);
                        tableNumber = orderDto.getTableNumber();
                        tableNamel = data.getStringExtra(SetTableActivity.TableName);
//                        Log.i("tableNamel", tableNamel);
//                        tableName.setText(eatingNumber + "人" + tableNamel);
                        reStartUI();
                        break;
                }
                break;
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
                            for (int i = 0; i < lv_goodsBeens.size(); i++) {
                                GoodsDto goodsDto = lv_goodsBeens.get(i);
                                dou = dou + lv_goodsBeens.get(i).getNum() * lv_goodsBeens.get(i).getPrice();
                                if (i == lv_goodsBeens.size() - 1) {
//                                    Log.i("discounall", "onActivityResult: " + new BigDecimal(dou * discount_num).setScale(2, RoundingMode.DOWN).doubleValue());
                                    goodsDto.setLszk(new BigDecimal(dou * discount_num).setScale(2, RoundingMode.DOWN).doubleValue() - discount);
                                } else {
                                    discount += new BigDecimal(goodsDto.getPrice() * goodsDto.getNum() * discount_num).setScale(2, RoundingMode.DOWN).doubleValue();
//                                    Log.i("discoun+", "onActivityResult: " + discount);
                                    goodsDto.setLszk(new BigDecimal(goodsDto.getPrice() * goodsDto.getNum() * discount_num).setScale(2, RoundingMode.DOWN).doubleValue());
                                }
                            }
                        } else {
                            for (int i = 0; i < lv_goodsBeens.size(); i++) {
                                dou = dou + lv_goodsBeens.get(i).getNum() * lv_goodsBeens.get(i).getPrice();
                            }
                            double discount_money = data.getDoubleExtra("discount_num", 0);
                            double discount_num = new BigDecimal(discount_money / dou).setScale(2, RoundingMode.DOWN).doubleValue();
                            for (int i = 0; i < lv_goodsBeens.size(); i++) {
                                GoodsDto goodsDto = lv_goodsBeens.get(i);
                                if (i == lv_goodsBeens.size() - 1) {
//                                    Log.i("discounall", "onActivityResult: " + new BigDecimal(dou * discount_num).setScale(2, RoundingMode.DOWN).doubleValue());
                                    goodsDto.setLszk(DoubleSave.doubleSaveTwo(discount_money - discount));
                                } else {
                                    discount += new BigDecimal(goodsDto.getPrice() * goodsDto.getNum() * discount_num).setScale(2, RoundingMode.DOWN).doubleValue();
//                                    Log.i("discoun+", "onActivityResult: " + discount);
                                    goodsDto.setLszk(new BigDecimal(goodsDto.getPrice() * goodsDto.getNum() * discount_num).setScale(2, RoundingMode.DOWN).doubleValue());
                                }
                            }
                        }
                        setScreenData();
                        adapter.notifyDataSetChanged();
                        break;
                }
                break;
            default:
                break;
        }
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

    public void getData() {


        String menuInfo = getSharedPreferences("MenuDto_json", MODE_PRIVATE).getString("json", "");
        menuDtos = SaveMenuInfo.changeToList(menuInfo);
        titles = new ArrayList<>();
        code = new ArrayList<>();
        mFragments.clear();
        titles.clear();
        code.clear();
        for (int i = 0; i < menuDtos.size(); i++) {
            Log.i("menu", menuDtos.get(i).getSid() + "---" + menuDtos.get(i).getName());
            if (dao.findSelectPointGoods(menuDtos.get(i).getSid()).size() > 0) {
                titles.add(menuDtos.get(i).getName());
                code.add(menuDtos.get(i).getSid());
            }

        }

    }

    /**
     * 绑定Tab和ViewPager，并将获取到的商品数据传进去
     */
    private void init() {
        getData();
        try {
            modifiedGroupDao = new ModifiedGroupDao(this);

            /**
             * GridView的数据
             */
            String[] mTitles = new String[titles.size()];

            for (int i = 0; i < titles.size(); i++) {
                mTitles[i] = titles.get(i);
                List<GoodsDto> goodsBeens = dao.findSelectPointGoods(code.get(i));
                Log.i("goods", "商品数量：-------" + goodsBeens.size());
                if (goodsBeens.size() > 0) {
                    mFragments.add(new BlankFragment(this, goodsBeens));
                }

            }
            View decorView = getWindow().getDecorView();
            vp = ViewFindUtils.find(decorView, R.id.vp);
            vp.setVisibility(View.VISIBLE);

            MyPagerAdapter mAdapter = new MyPagerAdapter(getSupportFragmentManager());
            vp.setAdapter(mAdapter);

            tabLayout_8 = ViewFindUtils.find(decorView, R.id.tl_8);
            tabLayout_8.setOnTabSelectListener(new OnTabSelectListener() {
                @Override
                public void onTabSelect(int position) {
                    vp.setVisibility(View.VISIBLE);
                    gv_search_goods.setVisibility(View.GONE);
                    goodsBeenList.clear();
                }

                @Override
                public void onTabReselect(int position) {

                }
            });
            if (mTitles.length < 6) {
                tabLayout_8.setTabSpaceEqual(true);
            }
//            tabLayout_8.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    vp.setVisibility(View.VISIBLE);
//                    gv_search_goods.setVisibility(View.GONE);
//
//                }
//            });
            try {
                tabLayout_8.setViewPager(vp, mTitles, this, mFragments);
            } catch (Exception e) {
//                finish();
                Toast.makeText(PointActivity.this, "商品资料为空", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置可左滑的ListView的参数属性
     */
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
        goodsLv.setMenuCreator(creator);

        // step 2. listener item click event
        goodsLv.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public void onMenuItemClick(int position, SwipeMenu menu, int index) {
//                GoodsDto item = lv_goodsBeens.get(position);
//					delete(item);
                if (!permiton.isCanDelete()) {
                    Toast.makeText(PointActivity.this, Constant.PERMITION_TOAST, Toast.LENGTH_SHORT).show();
                    return;
                }
                lv_goodsBeens.remove(position);
                adapter.notifyDataSetChanged();
                if (lv_goodsBeens.size() == 0) {
                    tv_rest_order.setText("取单");
                }
                setScreenData();
                new LogUtil().save(Constant.LOG_DELET);
            }
        });
    }

    /**
     * dp转换
     */
    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    private CustomerBeen customerBeen;

    private void showYYY(View v) {
        final List<CustomerBeen> beens = SaveOrderInfo.changeCusToList(SPHelper.getInstance().getString(Constant.CUS_LIST));
        List<String> strings = new ArrayList<>();
        strings.clear();
        for (CustomerBeen been : beens) {
            strings.add(been.getName());
        }
        View contentView = LayoutInflater.from(PointActivity.this).inflate(R.layout.dialog_yyy, null);
        final PopupWindow yyyPopupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        yyyPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        yyyPopupWindow.setFocusable(true);
        contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int height = contentView.getMeasuredHeight();
        int width = contentView.getMeasuredWidth();
        int[] location = new int[2];
        v.getLocationOnScreen(location);
        yyyPopupWindow.showAtLocation(v, Gravity.NO_GRAVITY, (location[0] + v.getWidth() / 2) - width / 2, location[1] - height);

        ListView listView = contentView.findViewById(R.id.yyy_list);
        listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, strings));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                customerBeen = beens.get(position);
                tv_daogou.setText(customerBeen.getName());
                yyyPopupWindow.dismiss();
            }
        });
    }




    private class MyClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.cd_bb:
                    dismissPopupWindow();
                    break;
                case R.id.cd_kqx://
                    dismissPopupWindow();
                    startActivity(new Intent(PointActivity.this, UserLoginActivity.class));
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
                    break;
                case R.id.cd_th://退款
                    pay.setText("付款");
                    dismissPopupWindow();
                    startActivity(new Intent(PointActivity.this, StatisticsActivity.class));
//                    showReturnDialog();
                    break;
                case R.id.cd_hygl://会员管理

                    dismissPopupWindow();
                    startActivity(new Intent(PointActivity.this, VipActivity.class));
                    break;
                case R.id.cd_yw: //店铺业务
                    dismissPopupWindow();
                    break;
                case R.id.cd_sz://
                    dismissPopupWindow();

                    break;
                case R.id.have_note:
                    dialog.dismiss();
                    Intent intent = new Intent(PointActivity.this, StatisticsActivity.class);
                    intent.putExtra(StatisticsActivity.REFUNDS_NAME, StatisticsActivity.REFUNDS_IN);
                    startActivity(intent);
                    break;
                case R.id.not_note:
                    if (lv_goodsBeens.size() > 0) {
                        new AlertDialog.Builder(PointActivity.this)
                                .setMessage("此操作会销毁正在销售的订单！是否要销毁该订单？")
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
                    }
                    changeState();
                    break;
                case R.id.cancel_order:
                    try {
                        more_popup_state = false;
                        morePopWindow.dismiss();
                        delete();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case R.id.guaDan:
                    restOrder();
                    more_popup_state = false;
                    morePopWindow.dismiss();
                    break;
                case R.id.discount:
                    if (lv_goodsBeens.size() > 0) {
                        Intent intent1 = new Intent(PointActivity.this, OrderDiscountActivity.class);
                        intent1.putExtra("money", Double.parseDouble(tvMoney.getText().toString()));
                        startActivityForResult(intent1, 123);
                    }
                    more_popup_state = false;
                    morePopWindow.dismiss();
                    break;
                case R.id.luodan:
                    getRestOrderDialog();
                    more_popup_state = false;
                    morePopWindow.dismiss();
                    break;
                case R.id.hy_btn_scan:
                    hydialog.dismiss();
                    break;
                case R.id.hy_btn_search:
                    if (!(etHy.getText().toString().isEmpty())) {
                        vip_number = etHy.getText().toString().trim();
                        hydialog.dismiss();
                        search(vip_number);
                    } else {
                        Toast.makeText(PointActivity.this, "请输入会员号", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.clear:
                    numberAdd = "";
                    good_price.setText("");
                    break;
                case R.id.sure1:
                    popup_state = false;
                    try {
                        if (mPricePopupWindow != null) {
                            mPricePopupWindow.dismiss();
                        }
                        if (!numberAdd.equals("")) {
                            GoodsDto goodsDto = new GoodsDto();
                            goodsDto.setName(goodsdto.getName());
                            goodsDto.setCategory_sid(goodsdto.getCategory_sid());
                            goodsDto.setSid(goodsdto.getSid());
                            //设置订单详情的sid
                            goodsDto.setGoodsId(GetData.getStringRandom(32));
                            goodsDto.setModified("");
                            if (sale_state) {
                                for (int i = 0; i < lv_goodsBeens.size(); i++) {
                                    if (lv_goodsBeens.get(i).getSid().equals(goodsDto.getSid())) {
                                        lv_goodsBeens.get(i).setNum(lv_goodsBeens.get(i).getNum() + 1);
                                        goodsDto.setPrice(Double.parseDouble(numberAdd));
                                        repeat = false;
                                    }
                                }
                                if (repeat) {
                                    goodsDto.setNum(good_num);
                                    goodsDto.setPrice(Double.parseDouble(numberAdd));
                                    lv_goodsBeens.add(goodsDto);
                                }
                            } else {
                                for (int i = 0; i < lv_goodsBeens.size(); i++) {
                                    if (lv_goodsBeens.get(i).getSid().equals(goodsDto.getSid())) {
                                        lv_goodsBeens.get(i).setNum(lv_goodsBeens.get(i).getNum() + 1);
                                        repeat = false;
                                    }
                                }
                                if (repeat) {
                                    goodsDto.setNum(-good_num);
                                    goodsDto.setPrice(-Double.parseDouble(numberAdd));
                                    lv_goodsBeens.add(goodsDto);
                                }
                            }
                            adapter.notifyDataSetChanged();
                            setScreenData();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;
            }
        }
    }

    private DialogFragment restFragment;

    private void getRestOrderDialog() {
        restFragment = new CircleDialog.Builder(this)
                .setBodyView(R.layout.dialog_restorder, new OnCreateBodyViewListener() {
                    @Override
                    public void onCreateBodyView(View view) {
                        ListView restListView = view.findViewById(R.id.list_left);
                        final List<OrderDto> restOrderDtos = getrestOrder();
                        restListView.setAdapter(new RestDialogAdapter(PointActivity.this, restOrderDtos));
                        restListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                closeRest();
                                new LogUtil().save(Constant.LOG_QD);
                                tv_rest_order.setText("挂单");
                                setRestData(restOrderDtos.get(position));
                                new OrderDao(PointActivity.this).deleteRestOrderInfo(restOrderDtos.get(position).getMaxNo());

                            }
                        });
                    }
                })
                .setWidth(0.5f)
                .show();
    }

    private List<MsgBeen> msgBeens = new ArrayList<>();
    private void getMsgDialog() {
        restFragment = new CircleDialog.Builder(this)
                .setBodyView(R.layout.dialog_msg, new OnCreateBodyViewListener() {
                    @Override
                    public void onCreateBodyView(View view) {
                        ListView restListView = view.findViewById(R.id.list_left);
                        MsgAdapter msgAdapter = new MsgAdapter(PointActivity.this, msgBeens);
                        restListView.setAdapter(msgAdapter);
                        getMsg(msgAdapter);
                    }
                })
                .setWidth(0.5f)
                .show();
    }
    Runnable runnable=new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            //要做的事情
            downUtil.initMsg(handler);
            handler.postDelayed(this, 60000*5);
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
    }

    private void getMsg(final MsgAdapter msgAdapter) {

        Map<String, String> params = new HashMap<>();
        params.put("logid", SPHelper.getInstance().getString(Constant.USER_ID));
        params.put("sysid", SPHelper.getInstance().getString(Constant.SYS_ID));
        params.put("model", "syspara.msg");
        params.put("fun", "message_list");
        new HttpConnection().postNet(params, new HttpCallBackListener() {
            @Override
            public void CallBack(String Response) {
                try {
                    JSONObject object = new JSONObject(Response);
                    final List<MsgBeen> beens = new Gson().fromJson(object.getJSONArray("result").toString(), new TypeToken<List<MsgBeen>>() {
                    }.getType());
                    if (beens != null) {
                        msgBeens.clear();
                        msgBeens.addAll(beens);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                msgAdapter.notifyDataSetChanged();
                                for (MsgBeen been : beens) {
                                    setMsgStatus(been.getId());
                                }

                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private void setRestData(OrderDto orderDto) {
        maxNo = orderDto.getMaxNo();
        List<GoodsDto> list = Sava_list_To_Json.changeToList(orderDto.getOrder_info());
        vipBeen = new Gson().fromJson(orderDto.getTableId(), VipBeen.class);
        if (vipBeen != null) {
            vipName.setText("会员号:" + vipBeen.getNum());
        }

        lv_goodsBeens.clear();
        lv_goodsBeens.addAll(list);
        adapter.notifyDataSetChanged();
        setScreenData();
    }

    private void closeRest() {
        restFragment.dismiss();
    }

    private List<OrderDto> getrestOrder() {
        OrderDao orderDao = new OrderDao(this);

        return orderDao.selectRestOrder();
    }

    private boolean isHide = false;

    private void hintKbOne() {
        if (isHide) {
            InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            // 得到InputMethodManager的实例
            if (imm.isActive()) {
                // 如果开启
                imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,
                        InputMethodManager.HIDE_NOT_ALWAYS);

            }
            isHide = false;
        }

    }

    VipBeen vipBeen;

    private void search(String num) {
        showvipDialog("请求中...");
        NetUtil.searchVip(num,handler);
    }

    private void hyCz(String num) {
        showvipDialog("正在充值...");
        NetUtil.hyCz(num, handler, lsVipBeen);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    lsVipBeen = (VipBeen) msg.obj;
                    close();
                    ll_hycx.setVisibility(View.GONE);
                    ll_hycz.setVisibility(View.VISIBLE);
                    tv_hyxx_no.setText(lsVipBeen.getNum());
                    hyxx_name.setText(lsVipBeen.getName());
                    hyxx_no.setText(lsVipBeen.getNum());
                    hyxx_phone.setText(lsVipBeen.getTel());
                    hyxx_jf.setText(lsVipBeen.getJfye());
                    hyxx_mzye.setText(lsVipBeen.getMzye());
                    hyxx_type.setText(lsVipBeen.getTypename());
                    ll_hymsg.setVisibility(View.VISIBLE);
                    searchBillfast();

//                    vipName.setText("会员号:"+vipBeen.getNum());
//                    Toast.makeText(PointActivity.this, "查询成功", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    close();
                    Toast.makeText(PointActivity.this, "查询失败：" + (String) msg.obj, Toast.LENGTH_SHORT).show();
                    break;
                case 4:
                    close();
                    hycz_menu.setVisibility(View.GONE);
                    search(lsVipBeen.getNum());
                    Toast.makeText(PointActivity.this, "充值成功", Toast.LENGTH_SHORT).show();
                    break;
                case 5:
                    close();
                    Toast.makeText(PointActivity.this, "充值失败，请重试", Toast.LENGTH_SHORT).show();
                    break;
                case 9:
                    setMsg(msg.arg1);
                    break;
            }

        }
    };
    private DialogFragment vipDialogFragment;

    private void showvipDialog(String msg) {
        vipDialogFragment = new CircleDialog.Builder()
                .setProgressText(msg)
                .setProgressStyle(ProgressParams.STYLE_SPINNER)
                .setWidth(0.5f)
                .show(getSupportFragmentManager());
    }

    private void close() {
        vipDialogFragment.dismiss();
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
            Toast.makeText(PointActivity.this, " 没有可挂单的订单！", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 添加一条挂单
     */
    private String vipinfo = "";

    public void addRestOrderinfo() {
        if (lv_goodsBeens.size() == 0) {
            Toast.makeText(this, "未选商品", Toast.LENGTH_SHORT).show();
            return;
        }
        if (vipBeen != null) {
            vipinfo = new Gson().toJson(vipBeen, VipBeen.class);
        }
        OderUtil.saveOrder(this,Sava_list_To_Json.changeGoodDtoToJaon(lv_goodsBeens),
                tvMoney.getText().toString(),
                vipinfo,maxNo,vip_number);
        vipBeen = null;
        vipName.setText("");
        textviewClear();
        maxNo = MaxNO.getMaxNo(this);
        Sava_list_To_Json.clearGoodsMsg(this);
        Toast.makeText(PointActivity.this, " 挂单成功！", Toast.LENGTH_SHORT).show();
        tv_rest_order.setText("取单");
        new LogUtil().save(Constant.LOG_GD);
    }

    /**
     * 改变状态为退款模式
     */
    public void changeState() {
//        llGoodTitle.setBackgroundColor(getResources().getColor(R.color.pay_bg));
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


    /**
     * 清除订单数据
     */
    private void textviewClear() {
        adapter.delete();
        total_money = 0.00;
        tvNum.setText("0");
        tvDiscount.setText("0.0");
        tvMoney.setText(String.valueOf(total_money));
        eatingNumber = 0;
        tableNumber = "";
//        tableName.setText("");
        lv_goodsBeens.clear();
        vip_number = "";
        vipName.setText("");
        vipBeen = null;
        tv_daogou.setText("导购");
        tv_rest_order.setText("取单");
        Sava_list_To_Json.clearGoodsMsg(this);
        setScreenData();
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
            Toast.makeText(PointActivity.this, "没有可取消的订单！", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Fragment里的回掉
     */
    @Override
    public void ongridviewclick(GoodsDto value) {
        GoodsDto dto = new GoodsUtil().getNewGoods(value);
        if (dto.getDzc().equals("0")) {
            goodsdto = dto;
            showSetWeightPopupWindow();
        } else {
            if (dto.getDzc().equals("1")&&SPHelper.getInstance().getBoolean(Constant.DZC_TAB)) {//计分，打印标签
                new TabPrintHelper(PointActivity.this, dto).printTabNum();
            }
            addGoods(dto);

        }

    }

    private void addGoods(GoodsDto dto) {
        if (vipBeen != null) {
            if (dto.getUsezk().equals("1")) {
                dto.setProType(Constant.PRO_HYJ);
                switch (vipBeen.getHyj()) {
                    case "HYJ3":
                        dto.setHyzk(dto.getPrice() - dto.getHyj3());
                        break;
                    case "HYJ2":
                        dto.setHyzk(dto.getPrice() - dto.getHyj2());
                        break;
                    case "HYJ1":
                        dto.setHyzk(dto.getPrice() - dto.getHyj1());
                        break;
                    case "HYJ":
                        dto.setHyzk(dto.getPrice() - dto.getHyj());
                        break;
                }


            }

        }

        tv_rest_order.setText("挂单");
        util.getUsePro();
        util.checkSinglePro(dto);
        List<GoodsDto> zpList = util.checkGivePro(dto);//获取赠品

        check(dto);
        if (zpList.size() != 0) {
            if (zpList.get(0).getLogic().equals("2")) {//选一个商品
                showSelect(zpList);
            } else {
                lv_goodsBeens.addAll(zpList);
            }
        } else {
            List<GoodsDto> zpslist = util.check(lv_goodsBeens);
            lv_goodsBeens.addAll(zpslist);
        }


        et_goods_code.setText("");
        adapter.notifyDataSetChanged();

        setScreenData();


    }

    //单选赠品
    private DialogFragment dialogFragment;

    private void showSelect(final List<GoodsDto> zpList) {
        dialogFragment = new CircleDialog.Builder(this)
                .setBodyView(R.layout.select_zp, new OnCreateBodyViewListener() {
                    @Override
                    public void onCreateBodyView(View view) {
                        ListView listView = view.findViewById(R.id.zp_list);
                        listView.setAdapter(new ZpAdapter(zpList));
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                lv_goodsBeens.add(zpList.get(i));
                                adapter.notifyDataSetChanged();
                                dialogFragment.dismiss();
                            }
                        });
                    }
                })
                .setWidth(0.5f)
                .setCancelable(false)
                .setTitle("请选择一个赠品")
                .setNegative("取消", null)
                .show();
    }

    class ZpAdapter extends BaseAdapter {
        private List<GoodsDto> zpList;

        public ZpAdapter(List<GoodsDto> zpList) {
            this.zpList = zpList;
        }

        @Override
        public int getCount() {
            return zpList.size();
        }

        @Override
        public Object getItem(int i) {
            return zpList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            Zpholder zpholder;
            if (view == null) {
                zpholder = new Zpholder();
                view = LayoutInflater.from(PointActivity.this).inflate(R.layout.zp_list_item, null);
                zpholder.name = view.findViewById(R.id.tv_name);
                view.setTag(zpholder);

            } else {
                zpholder = (Zpholder) view.getTag();
            }
            zpholder.name.setText(zpList.get(i).getName());

            return view;
        }

        class Zpholder {
            TextView name;
        }
    }

    private void check(GoodsDto gd) {
        if (!gd.getDzc().equals("0")) {
            boolean change = false;
            for (GoodsDto dto : lv_goodsBeens) {
                if (dto.getSid().equals(gd.getSid())) {
                    change = true;
                    dto.setNum(DoubleSave.doubleSaveTwo(dto.getNum() + gd.getNum()));

                    if (dto.getProType().equals(Constant.PRO_N_REDUCE)) {
                        if (dto.getNum() == dto.getMuchnum()) {
                            dto.setCxzk(DoubleSave.doubleSaveTwo(dto.getPrice() - dto.getProPrice()));
                        }
                    } else if (dto.getProType().equals(Constant.PRO_HYJ)) {
                        dto.setHyzk(DoubleSave.doubleSaveThree(dto.getHyzk() * dto.getNum()));
                    } else {
                        if (dto.getNum() <= dto.getSingnum() && dto.getNum() >= dto.getMuchnum()) {
                            dto.setCxzk(DoubleSave.doubleSaveTwo(dto.getNum() * dto.getPrice() - dto.getNum() * dto.getProPrice() + dto.getPrice() * (1 - dto.getProDiscout()) * dto.getNum()));
                        }
                    }
                    break;
                }
            }
            if (!change) {
                lv_goodsBeens.add(gd);
            }
        } else {
            lv_goodsBeens.add(gd);
        }


    }


    private void showhyDialog() {
        View contentView = LayoutInflater.from(PointActivity.this).inflate(R.layout.dialog_hy_login, null);
        mPricePopupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mPricePopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPricePopupWindow.setFocusable(true);
        backgroundAlpaha(PointActivity.this, 0.5f);
        mPricePopupWindow.showAtLocation(parent, Gravity.RIGHT, 0, 0);
        mPricePopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpaha(PointActivity.this, 1.0f);
                number_zr = "";
                number_zk = "";
                number_sl = "";
                goods_zk = 0;
                yh_type = "zk";

            }
        });
        final LinearLayout jp = contentView.findViewById(R.id.jp);
        final Button bt_search = contentView.findViewById(R.id.bt_search);
        final TextView tv_hy_no = contentView.findViewById(R.id.tv_hy_no);
        ImageView iv_close = contentView.findViewById(R.id.iv_close);

        Button jp_zl_sl = contentView.findViewById(R.id.jp_zl_sl);
        TextView jp_sure_sl = contentView.findViewById(R.id.jp_sure_sl);
        tv_jp_sl = contentView.findViewById(R.id.tv_jp_sl);

        ll_hycz = contentView.findViewById(R.id.ll_hycz);
        LinearLayout menu_hycz = contentView.findViewById(R.id.menu_hycz);
        menu_hycz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PointActivity.this, VIPRegisterActivity.class);
                startActivity(intent);
            }
        });

        //会员查询
        ll_hycx = contentView.findViewById(R.id.ll_hycx);
        //会员充值界面
        hycz_menu = contentView.findViewById(R.id.hycz_menu);
        ImageView iv_return = contentView.findViewById(R.id.iv_return);
        Button bt_cz = contentView.findViewById(R.id.bt_cz);
        final EditText hycz_je = contentView.findViewById(R.id.hycz_je);
        final TextView hycz_type = contentView.findViewById(R.id.hycz_type);
        final TextView hycz_no = contentView.findViewById(R.id.hycz_no);


        iv_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hycz_menu.setVisibility(View.GONE);
            }
        });
        bt_cz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hycz_je.getText().toString().trim().isEmpty()) {
                    Toast.makeText(PointActivity.this, "请输入正确的充值金额", Toast.LENGTH_SHORT).show();
                } else {
                    hyCz(hycz_je.getText().toString().trim());
                }

            }
        });


        //查询成功页面
        ll_hymsg = contentView.findViewById(R.id.ll_hymsg);

        tv_hyxx_no = contentView.findViewById(R.id.tv_hyxx_no);
        TextView tv_hyxx_sy = contentView.findViewById(R.id.tv_hyxx_sy);

        TextView choose_hyxx = contentView.findViewById(R.id.choose_hyxx);
        TextView choose_jyjl = contentView.findViewById(R.id.choose_jyjl);

        //会员信息页面
        ll_hyxx = contentView.findViewById(R.id.ll_hyxx);
        hyxx_name = contentView.findViewById(R.id.hyxx_name);
        hyxx_no = contentView.findViewById(R.id.hyxx_no);
        hyxx_phone = contentView.findViewById(R.id.hyxx_phone);
        hyxx_type = contentView.findViewById(R.id.hyxx_type);
        hyxx_mzye = contentView.findViewById(R.id.hyxx_mzye);
        hyxx_jf = contentView.findViewById(R.id.hyxx_jf);
        final TextView hyxx_choose_status = contentView.findViewById(R.id.hyxx_choose_status);
        final TextView xfjl_choose_status = contentView.findViewById(R.id.xfjl_choose_status);
        final ListView xfjl_list = contentView.findViewById(R.id.xfjl_list);

        //会员消费记录页面
        rl_hyxf = contentView.findViewById(R.id.rl_hyxf);

        tv_hyxx_sy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //开始使用会员
                mPricePopupWindow.dismiss();
                vipBeen = lsVipBeen;
                vipName.setText("会员号:" + vipBeen.getNum());
            }
        });

        choose_hyxx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rl_hyxf.setVisibility(View.GONE);
                ll_hyxx.setVisibility(View.VISIBLE);
                hyxx_choose_status.setVisibility(View.VISIBLE);
                xfjl_choose_status.setVisibility(View.INVISIBLE);
            }
        });

        choose_jyjl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VipSaleAdapter vipSaleAdapter = new VipSaleAdapter(PointActivity.this, topBeens);
                xfjl_list.setAdapter(vipSaleAdapter);
                rl_hyxf.setVisibility(View.VISIBLE);
                ll_hyxx.setVisibility(View.GONE);
                hyxx_choose_status.setVisibility(View.INVISIBLE);
                xfjl_choose_status.setVisibility(View.VISIBLE);
            }
        });

        bt_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tv_hy_no.getText().toString().isEmpty()) {
                    Toast.makeText(PointActivity.this, "请输入信息", Toast.LENGTH_SHORT).show();
                } else {
                    search(tv_hy_no.getText().toString());
                }
            }
        });

        ll_hycz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//充值
                hycz_menu.setVisibility(View.VISIBLE);
                hycz_type.setText(lsVipBeen.getTypename());
                hycz_no.setText(lsVipBeen.getNum());
            }
        });
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPricePopupWindow.dismiss();

            }
        });

        tv_hy_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jp.setVisibility(View.VISIBLE);
                bt_search.setVisibility(View.GONE);
            }
        });
        jp_sure_sl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tv_hy_no.setText(tv_jp_sl.getText().toString());
                jp.setVisibility(View.GONE);
                bt_search.setVisibility(View.VISIBLE);
                number_sl = "";
                tv_jp_sl.setText("");
            }
        });
        jp_zl_sl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                number_sl = "";
                tv_jp_sl.setText("");
            }
        });

    }

    private void searchBillfast() {
        Map<String, String> params = new HashMap<>();
        params.put("logid", SPHelper.getInstance().getString(Constant.USER_ID));
        params.put("sysid", SPHelper.getInstance().getString(Constant.SYS_ID));
        params.put("model", "pos.mreport");
        params.put("fun", "findvipxfjl");

        params.put("vipid", lsVipBeen.getId());
        params.put("startrq", GetData.getAnyTime(30));
        params.put("endrq", GetData.getAnyTime(0));
        params.put("market", "");

        Log.i("vipsf", params.toString());
        new HttpConnection().postNet(params, new HttpCallBackListener() {
            @Override
            public void CallBack(String Response) {
                Log.i("vipsf", Response);
                try {
                    JSONObject jsonObject = new JSONObject(Response);
                    Log.i("login", jsonObject.getString("errMsg"));
                    JSONArray result = jsonObject.getJSONArray("result");
                    getResult(result);
//                    count = jsonObject.getJSONObject("sumrow").getString("sum_sl");
//                    je = jsonObject.getJSONObject("sumrow").getString("sum_xsjje");
//                    Message message = Message.obtain();
//                    message.what = 1;
//                    handler.sendMessage(message);
                } catch (JSONException e) {

                    e.printStackTrace();
                }

            }
        });

    }

    List<VipGoodsBeen> topBeens = new ArrayList<>();

    private void getResult(JSONArray result) {
        topBeens.clear();
        List<VipGoodsBeen> mBeens = new Gson().fromJson(result.toString(), new TypeToken<List<VipGoodsBeen>>() {
        }.getType());
        topBeens.addAll(mBeens);
    }


    private void showBillDiscout() {
        View contentView = LayoutInflater.from(PointActivity.this).inflate(R.layout.dialog_bill_discount, null);
        mPricePopupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mPricePopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPricePopupWindow.setFocusable(true);
        backgroundAlpaha(PointActivity.this, 0.5f);
        mPricePopupWindow.showAtLocation(parent, Gravity.RIGHT, 0, 0);
        RadioGroup radioGroup = contentView.findViewById(R.id.rg_bt);
        RadioButton rb_zk = contentView.findViewById(R.id.rb_zk);
        RadioButton rb_zr = contentView.findViewById(R.id.rb_zr);
        RadioButton rb_zs = contentView.findViewById(R.id.rb_zs);

        ImageView iv_close = contentView.findViewById(R.id.iv_close);

        final LinearLayout ll_zk = contentView.findViewById(R.id.ll_zk);
        final LinearLayout ll_zr = contentView.findViewById(R.id.ll_zr);

        Button jp_zl = contentView.findViewById(R.id.jp_zl);
        TextView jp_sure = contentView.findViewById(R.id.jp_sure);
        Button jp_zl_zr = contentView.findViewById(R.id.jp_zl_zr);
        TextView jp_sure_zr = contentView.findViewById(R.id.jp_sure_zr);

        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPricePopupWindow.dismiss();
            }
        });

        tv_jp_zk = contentView.findViewById(R.id.tv_jp_zk);
        tv_jp_zr = contentView.findViewById(R.id.tv_jp_zr);

        jp_zl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                number_zk = "";
                tv_jp_zk.setText("");

            }
        });
        jp_zl_zr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                number_zr = "";
                tv_jp_zr.setText("");
            }
        });
        jp_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (yh_type) {
                    case "zk":
                        if (!tv_jp_zk.getText().toString().trim().isEmpty()) {
                            setlsbillDiscount(Double.parseDouble(tv_jp_zk.getText().toString().trim()), "zk");
                        }

                        break;
                    case "zr":
                        if (!tv_jp_zr.getText().toString().trim().isEmpty()) {
                            setlsbillDiscount(Double.parseDouble(tv_jp_zr.getText().toString().trim()), "zr");
                        }
                        break;
                    case "zs":
                        setlsbillDiscount(0, "zs");
                        break;
                }
                mPricePopupWindow.dismiss();
            }
        });
        jp_sure_zr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (yh_type) {
                    case "zk":
                        if (!tv_jp_zk.getText().toString().trim().isEmpty()) {
                            setlsbillDiscount(Double.parseDouble(tv_jp_zk.getText().toString().trim()), "zk");
                        }

                        break;
                    case "zr":
                        if (!tv_jp_zr.getText().toString().trim().isEmpty()) {
                            setlsbillDiscount(Double.parseDouble(tv_jp_zr.getText().toString().trim()), "zr");
                        }
                        break;
                    case "zs":
                        setlsbillDiscount(0, "zs");
                        break;
                }
                mPricePopupWindow.dismiss();
            }
        });

        rb_zk.setChecked(true);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_zk:
                        yh_type = "zk";
                        ll_zk.setVisibility(View.VISIBLE);
                        ll_zr.setVisibility(View.GONE);
                        break;
                    case R.id.rb_zr:
                        ll_zk.setVisibility(View.GONE);
                        ll_zr.setVisibility(View.VISIBLE);
                        yh_type = "zr";
                        break;
                    case R.id.rb_zs:
                        yh_type = "zs";
                        break;
                }
            }
        });


        mPricePopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpaha(PointActivity.this, 1.0f);
                number_zr = "";
                number_zk = "";
                number_sl = "";
                goods_zk = 0;
                yh_type = "zk";

            }
        });
    }

    /**
     * 弹出设置价格窗口
     */
    private EditText good_price;
    private TextView text_num;
    double goods_sl = 0;
    double goods_zk = 0;
    private String yh_type = "zk";
    double lsDis = 0;

    private void showSetPricePopupWindow(final GoodsDto dto) {
        lsDis = 0;
        goods_zk = 0;
        goods_sl = dto.getNum();
        if (dto.getDiscount() != 0) {
            lsDis = dto.getDiscount() / dto.getNum();
        }
        View contentView = LayoutInflater.from(PointActivity.this).inflate(R.layout.dialog_zk_single, null);
        mPricePopupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mPricePopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPricePopupWindow.setFocusable(true);
        backgroundAlpaha(PointActivity.this, 0.5f);

        final TextView tv_zk = contentView.findViewById(R.id.tv_zk);
        final TextView tv_sl = contentView.findViewById(R.id.tv_sl);
        final LinearLayout ll_zk = contentView.findViewById(R.id.ll_zk);
        final LinearLayout ll_sl = contentView.findViewById(R.id.ll_sl);
        final LinearLayout ll_nor = contentView.findViewById(R.id.ll_nor);
        final LinearLayout ll_yhfs = contentView.findViewById(R.id.ll_yhfs);
        final LinearLayout ll_zr = contentView.findViewById(R.id.ll_zr);
        final LinearLayout ll_dj = contentView.findViewById(R.id.ll_dj);

        LinearLayout ll_main = contentView.findViewById(R.id.ll_main);
        TextView tv_name = contentView.findViewById(R.id.tv_name);
        TextView tv_no = contentView.findViewById(R.id.tv_no);
        ImageView iv_close = contentView.findViewById(R.id.iv_close);
        RelativeLayout re_jian = contentView.findViewById(R.id.re_jian);
        RelativeLayout re_jia = contentView.findViewById(R.id.re_jia);

        Button jp_zl_sl = contentView.findViewById(R.id.jp_zl_sl);
        Button jp_zl = contentView.findViewById(R.id.jp_zl);
        TextView jp_sure = contentView.findViewById(R.id.jp_sure);
        TextView jp_sure_sl = contentView.findViewById(R.id.jp_sure_sl);
        tv_jp_zk = contentView.findViewById(R.id.tv_jp_zk);
        tv_jp_zr = contentView.findViewById(R.id.tv_jp_zr);
        tv_jp_sl = contentView.findViewById(R.id.tv_jp_sl);
        tv_jp_dj = contentView.findViewById(R.id.tv_jp_dj);


        Button bt_sure = contentView.findViewById(R.id.bt_sure);

        final LinearLayout ll_zk_tv = contentView.findViewById(R.id.ll_zk_tv);
        final LinearLayout ll_zr_tv = contentView.findViewById(R.id.ll_zr_tv);

        final TextView tv_zr = contentView.findViewById(R.id.tv_zr);

        RadioGroup radioGroup = contentView.findViewById(R.id.rg_bt);
        final RadioButton rb_zk = contentView.findViewById(R.id.rb_zk);
        final RadioButton rb_zr = contentView.findViewById(R.id.rb_zr);
        final RadioButton rb_zs = contentView.findViewById(R.id.rb_zs);
        RadioButton rb_dj = contentView.findViewById(R.id.rb_dj);


        Button jp_zl_zr = contentView.findViewById(R.id.jp_zl_zr);
        Button jp_zl_dj = contentView.findViewById(R.id.jp_zl_dj);
        TextView jp_sure_zr = contentView.findViewById(R.id.jp_sure_zr);
        TextView jp_sure_dj = contentView.findViewById(R.id.jp_sure_dj);
        tv_zr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_zk.setVisibility(View.GONE);
                ll_sl.setVisibility(View.GONE);
                ll_nor.setVisibility(View.GONE);
                ll_yhfs.setVisibility(View.VISIBLE);
                ll_zr.setVisibility(View.VISIBLE);
            }
        });
        jp_sure_dj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//该单价
                dto.setNum(Double.parseDouble(tv_sl.getText().toString()));
                if (!tv_jp_dj.getText().toString().isEmpty()) {
                    goods_zk = DoubleSave.doubleSaveTwo((dto.getPrice() - Double.parseDouble(tv_jp_dj.getText().toString())) * dto.getNum());
                }
                setSingleDiscount(dto, goods_zk);
            }
        });

        jp_zl_dj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                number_dj = "";
                tv_jp_dj.setText("");
            }
        });
        jp_zl_zr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                number_zr = "";
                tv_jp_zr.setText("");
            }
        });
        jp_sure_zr.setOnClickListener(new View.OnClickListener() {//折让确认
            @Override
            public void onClick(View v) {
                dto.setNum(Double.parseDouble(tv_sl.getText().toString()));
                if (!tv_jp_zr.getText().toString().isEmpty()) {
                    dto.setLszk(0);
                    if (Double.parseDouble(tv_jp_zr.getText().toString().trim()) > (dto.getPrice() * dto.getNum() - dto.getDiscount())) {
                        Toast.makeText(PointActivity.this, "折让金额不能大于商品总额", Toast.LENGTH_SHORT).show();
                    } else {
                        goods_zk = Double.parseDouble(tv_jp_zr.getText().toString().trim());

                    }
                } else {
                    goods_zk = lsDis * dto.getNum();
                }
                setSingleDiscount(dto, goods_zk);
                number_zr = "";

            }
        });

        rb_zk.setChecked(true);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_zk:
//                        ll_zr_tv.setVisibility(View.GONE);
//                        ll_zk_tv.setVisibility(View.VISIBLE);
                        ll_zk.setVisibility(View.VISIBLE);
                        ll_nor.setVisibility(View.GONE);
                        ll_zr.setVisibility(View.GONE);
                        ll_sl.setVisibility(View.GONE);
                        ll_dj.setVisibility(View.GONE);
                        yh_type = "zk";
                        break;
                    case R.id.rb_zr:
                        ll_zk.setVisibility(View.GONE);
                        ll_nor.setVisibility(View.GONE);
                        ll_zr.setVisibility(View.VISIBLE);
                        ll_sl.setVisibility(View.GONE);
                        ll_dj.setVisibility(View.GONE);
                        yh_type = "zr";
                        break;
                    case R.id.rb_zs:
                        ll_zk.setVisibility(View.GONE);
                        ll_nor.setVisibility(View.VISIBLE);
                        ll_zr.setVisibility(View.GONE);
                        ll_sl.setVisibility(View.GONE);
                        ll_zr_tv.setVisibility(View.GONE);
                        ll_zk_tv.setVisibility(View.GONE);
                        ll_dj.setVisibility(View.GONE);
                        yh_type = "zs";
                        break;
                    case R.id.rb_dj:
                        ll_zk.setVisibility(View.GONE);
                        ll_nor.setVisibility(View.GONE);
                        ll_zr.setVisibility(View.GONE);
                        ll_sl.setVisibility(View.GONE);
                        ll_zr_tv.setVisibility(View.GONE);
                        ll_zk_tv.setVisibility(View.GONE);
                        ll_dj.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });

        tv_name.setText(dto.getName());
        tv_sl.setText(goods_sl + "");
        jp_zl_sl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                number_sl = "";
                tv_jp_sl.setText("");
            }
        });
        jp_zl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                number_zk = "";
                tv_jp_zk.setText("");
            }
        });
        jp_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//折扣键盘
                dto.setNum(Double.parseDouble(tv_sl.getText().toString()));
                String zk = tv_jp_zk.getText().toString();
                if (!zk.isEmpty()) {
                    dto.setLszk(0);
                    goods_zk = DoubleSave.doubleSaveTwo((1 - Double.parseDouble(zk) / 10) * dto.getPrice() * dto.getNum());
                    double zkl = Double.parseDouble(zk) / 10;
                    double lsper = permiton.getZkl() / 100;
                    double gper = dto.getMinzkl();
                    if (zkl < lsper || zkl < gper) {
                        Toast.makeText(PointActivity.this, "超出折扣，无权限操作", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } else {
                    goods_zk = lsDis * dto.getNum();
                }
                setSingleDiscount(dto, goods_zk);
                number_zk = "";
                if (dto.getDzc().equals("1")&&SPHelper.getInstance().getBoolean(Constant.DZC_TAB)) {//计分，打印标签
                    new TabPrintHelper(PointActivity.this, dto).printTabNum();
                }

            }
        });
        jp_sure_sl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//数量键盘
                if (!tv_jp_sl.getText().toString().isEmpty()) {
                    dto.setNum(Double.parseDouble(tv_jp_sl.getText().toString()));
                }
                setSingleDiscount(dto, lsDis * dto.getNum());
                number_sl = "";
                if (dto.getDzc().equals("1")&&SPHelper.getInstance().getBoolean(Constant.DZC_TAB)) {//计分，打印标签
                    new TabPrintHelper(PointActivity.this, dto).printTabNum();
                }
            }
        });

        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPricePopupWindow.dismiss();
            }
        });
        re_jia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goods_sl++;
                tv_sl.setText(goods_sl + "");
            }
        });
        bt_sure.setOnClickListener(new View.OnClickListener() {//赠送
            @Override
            public void onClick(View v) {
                dto.setNum(Double.parseDouble(tv_sl.getText().toString()));
                setSingleDiscount(dto, DoubleSave.doubleSaveTwo(dto.getPrice() * dto.getNum()));
            }
        });

        re_jian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (goods_sl > 1) {
                    goods_sl--;
                    tv_sl.setText(goods_sl + "");
                } else {
                    Toast.makeText(PointActivity.this, "数量不能再减啦", Toast.LENGTH_SHORT).show();
                }
            }
        });

//        ll_main.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ll_zk.setVisibility(View.VISIBLE);
//                ll_sl.setVisibility(View.GONE);
//                ll_zr.setVisibility(View.GONE);
//                ll_nor.setVisibility(View.GONE);
//                ll_yhfs.setVisibility(View.VISIBLE);
//                number_sl = "";
//                number_zk = "";
//            }
//        });
        tv_zk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_zk.setVisibility(View.VISIBLE);
                ll_sl.setVisibility(View.GONE);
                ll_nor.setVisibility(View.GONE);
                ll_yhfs.setVisibility(View.VISIBLE);
                ll_zr.setVisibility(View.GONE);
            }
        });
        tv_sl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_zk.setVisibility(View.GONE);
                ll_sl.setVisibility(View.VISIBLE);
                tv_jp_sl.setHint("请输入数量");
                tv_jp_sl.setText("");
                number_sl = "";
//                ll_nor.setVisibility(View.GONE);
//                ll_yhfs.setVisibility(View.INVISIBLE);
                ll_zr.setVisibility(View.GONE);

//                rb_zk.setChecked(false);
//                rb_zr.setChecked(false);
//                rb_zs.setChecked(false);

            }
        });

        mPricePopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpaha(PointActivity.this, 1.0f);
                number_zr = "";
                number_zk = "";
                number_sl = "";
                goods_zk = 0;
                yh_type = "zk";

            }
        });
        mPricePopupWindow.showAtLocation(parent, Gravity.RIGHT, 0, 0);
    }

    private void setSingleDiscount(GoodsDto dto, double lszk) {
        if (lszk != 0) {
            dto.setLszk(lszk);
        }
        number_dj = "";
        number_zr = "";
        number_sl = "";
        number_zk = "";
        mPricePopupWindow.dismiss();
        adapter.notifyDataSetChanged();
        setScreenData();

    }


    /**
     * 设置价格窗口的数字键点击事件
     */
    private String numberAdd = "";

    public void popupWindowNumberClick(View view) {
        TextView tv = (TextView) view;
        if (numberAdd.isEmpty()) {
            if (view.getId() != R.id.dian) {
                numberAdd += tv.getText().toString();
            }
        } else {
            if (numberAdd.equals("0") && view.getId() == R.id.dian) {
                numberAdd = tv.getText().toString();
            } else {
                numberAdd += tv.getText().toString();
            }
        }
        good_price.setText(numberAdd);
    }

    public void numberClick(View view) {
        TextView tv = (TextView) view;
        if (numberAdd.isEmpty()) {
            if (view.getId() != R.id.dian) {
                numberAdd += tv.getText().toString();
            }
        } else {
            if (numberAdd.equals("0") && view.getId() == R.id.dian) {
                numberAdd = tv.getText().toString();
            } else {
                numberAdd += tv.getText().toString();
            }
        }
        good_weight.setText(numberAdd);
    }

    private String number_sl = "";

    public void numberClick_sl(View view) {
        TextView tv = (TextView) view;
        if (!tv.getText().toString().equals(".")) {
            number_sl += tv.getText().toString();
        }
        tv_jp_sl.setText(number_sl);
    }

    private String number_zk = "";

    public void numberClick_zk(View view) {
        TextView tv = (TextView) view;
        String ss = tv.getText().toString();
        if (number_zk.isEmpty()) {
            if (!ss.equals(".") && !ss.equals("0")) {
                number_zk += ss;
            }
        } else if (number_zk.length() == 1) {
            if (ss.equals(".") || ss.equals("0")) {
                number_zk += ss;
            }

        } else {
            if (number_zk.contains(".")) {
                if (!ss.equals(".") && ss.length() <= 3) {
                    number_zk += ss;
                }
            }
        }
        tv_jp_zk.setText(number_zk);
    }

    private String number_dj = "";

    public void numberClick_dj(View view) {
        TextView tv = (TextView) view;

        if (number_dj.isEmpty()) {
            if (!tv.getText().toString().equals(".")) {
                number_dj += tv.getText().toString();
            }
        } else {
            if (!number_dj.contains(".")) {
                number_dj += tv.getText().toString();
            } else {
                if (!tv.getText().toString().equals(".")) {
                    number_dj += tv.getText().toString();
                }
            }
        }

        tv_jp_dj.setText(number_dj);
    }

    private String number_zr = "";

    public void numberClick_zr(View view) {
        TextView tv = (TextView) view;

        if (number_zr.isEmpty()) {
            if (!tv.getText().toString().equals(".") && !tv.getText().toString().equals("0")) {
                number_zr += tv.getText().toString();
            }
        } else {
            if (!number_zr.contains(".")) {
                number_zr += tv.getText().toString();
            } else {
                if (!tv.getText().toString().equals(".")) {
                    number_zr += tv.getText().toString();
                }
            }
        }

        tv_jp_zr.setText(number_zr);
    }

    public void setScreenData() {
        good_num = 0;
        numberAdd = "";
        double dou = 0;
        double totalNum = 0;
        double discount = 0.0;
        for (int i = 0; i < lv_goodsBeens.size(); i++) {
            dou = dou + lv_goodsBeens.get(i).getNum() * lv_goodsBeens.get(i).getPrice();
//            if (sale_state) {
            totalNum = DoubleSave.doubleSaveTwo(totalNum + lv_goodsBeens.get(i).getNum());
//            } else {
//                totalNum = DoubleSave.doubleSaveTwo(totalNum - lv_goodsBeens.get(i).getNum());
//            }
            discount += lv_goodsBeens.get(i).getDiscount();
        }
        tvDiscount.setText(String.valueOf(DoubleSave.doubleSaveTwo(discount)));
        tvNum.setText(String.valueOf(totalNum));
        tvMoney.setText(String.valueOf(DoubleSave.doubleSaveTwo(dou - discount)));
        yj_price.setText(String.valueOf(DoubleSave.doubleSaveTwo(dou)));

        sumiUtil.initImgesMenus(lv_goodsBeens);
        sumiUtil.checkImgsMenuExists(SPHelper.getInstance().getLong(Constant.IMG_KEY, 0L));
    }


    @Override
    protected void onDestroy() {
        if (mPopWindow != null) {
            mPopWindow = null;
        }
        if (morePopWindow != null) {
            morePopWindow = null;
        }
        if (mPricePopupWindow != null) {
            mPricePopupWindow = null;
        }
        if (dialog != null) {
            dialog = null;
        }
        super.onDestroy();
    }

    //设置称重窗口
    EditText good_weight;
    TextView tv_singlePrice;

    /**
     * 设置添加屏幕的背景透明度
     **/
    public void backgroundAlpaha(Activity context, float bgAlpha) {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        context.getWindow()
                .addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        context.getWindow().setAttributes(lp);
    }

    private void showSetWeightPopupWindow() {
//        final SerialPortManager portManager = new SerialPortManager();
//        portManager.openSerialPort(new File("/dev/ttyHSL1"), 9600);
//        byte[] b = new byte[]{0x04,0x02,0x31,0x30,0x03};
//        portManager.sendBytes(b);
//        BluetoothHelper.connect(this);
        View contentView = LayoutInflater.from(PointActivity.this).inflate(R.layout.dialog_dzc, null);
        mPricePopupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mPricePopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPricePopupWindow.setFocusable(true);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        backgroundAlpaha(PointActivity.this, 0.5f);

        TextView sure = (TextView) contentView.findViewById(R.id.btn_sure);
//        text_num = (TextView) contentView.findViewById(R.id.text_num);
        CheckBox tab_box = contentView.findViewById(R.id.tab_box);
        tab_box.setChecked(SPHelper.getInstance().getBoolean(Constant.DZC_TAB,false));
        tab_box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SPHelper.getInstance().putBoolean(Constant.DZC_TAB,isChecked);
            }
        });

        good_weight = (EditText) contentView.findViewById(R.id.et_sl);
        tv_singlePrice = contentView.findViewById(R.id.et_money);
        ImageView iv_dismiss = contentView.findViewById(R.id.iv_dismiss);
        TextView tvName = (TextView) contentView.findViewById(R.id.tv_name);
        TextView tvPrice = (TextView) contentView.findViewById(R.id.tv_price);
        TextView tv_no = contentView.findViewById(R.id.tv_no);

        TextView clear_weight = (TextView) contentView.findViewById(R.id.tv_cz);
        TextView tv_qp = contentView.findViewById(R.id.tv_qp);
        tv_qp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scaleUItil.touchTare();
            }
        });

        iv_dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPricePopupWindow.dismiss();
            }
        });
//        TextView choose_type = contentView.findViewById(R.id.choose_type);
//        choose_type.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //手动输入
////                portManager.closeSerialPort();
//                numberAdd = "";
//                good_weight.setText("");
//            }
//        });
        if (Build.MODEL.equals(URL.MODEL_T1) || Build.MODEL.equals(URL.MODEL_T2)||Build.MODEL.equals(URL.MODEL_D1)) {
        } else {
            scaleUItil.showWight(good_weight,tv_singlePrice,goodsdto.getPrice());
        }
        tv_no.setText(goodsdto.getCode());
        tvName.setText(goodsdto.getName());
        tvPrice.setText("单价："+goodsdto.getPrice() + "元/kg");
//        tv_singlePrice.setText(goodsdto.getPrice()+"元/500g");
//        jian.setOnClickListener(listener);
//        jia.setOnClickListener(listener);
//        clear.setOnClickListener(listener);
        clear_weight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numberAdd = "";
                good_weight.setText("");
                scaleUItil.zeroScale();
            }
        });
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                portManager.closeSerialPort();
//                App.mClient.disconnect(mac);
//                App.mClient.unnotify(mac, App.serviceUuid, App.characterUuid, new BleUnnotifyResponse() {
//                    @Override
//                    public void onResponse(int code) {
//
//                    }
//                });
                if (mPricePopupWindow != null) {
                    mPricePopupWindow.dismiss();
                }
//                mPricePopupWindow.dismiss();
                String weight = good_weight.getText().toString().trim();
                if (weight.equals("") || weight.equals("UF") || weight.equals("OF")) {
                    numberAdd = "";
                    good_weight.setText("");
                    return;
                }



//                double gd_price =0;
//                if ( Pattern.matches("^\\d+(\\.\\d+)?",weight)) {
//                    gd_price = DoubleSave.doubleSaveTwo(Double.parseDouble(weight)*goodsDto.getPrice());
//                }  else {
//                    Toast.makeText(ScanActivity.this, "不合法重量", Toast.LENGTH_SHORT).show();
//                    numberAdd = "";
//                    good_weight.setText("");
//                    return;
//                }

//                Toast.makeText(ScanActivity.this, "称重总价为："+DoubleSave.doubleSaveTwo(gd_price)+"元", Toast.LENGTH_SHORT).show();
                if (sale_state) {
                    goodsdto.setNum(DoubleSave.doubleSaveThree(Double.parseDouble(weight)));
                } else {
                    goodsdto.setNum(-DoubleSave.doubleSaveThree(Double.parseDouble(weight)));
                }

                if (!checkGoods(goodsdto)) {
                    lv_goodsBeens.add(goodsdto);
                }
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Log.i("tag", goodsdto.getCode());
//                        Bitmap b = CodeUtils.createBarCode(goodsdto.getCode(), BarcodeFormat.CODE_128, 200, 60,null,true,13,Color.BLACK);
//                        Bitmap bitmap = StarPrintHelper.creatBq(goodsdto, PointActivity.this, b);
//                        Printer.printer2PrintBitmap(bitmap);
//                        Printer.printer2FeedPaper(3);
//                        Printer.printer2CutPaper();
//                    }
//                }).start();

                if (SPHelper.getInstance().getBoolean(Constant.DZC_TAB, false)) {
                    new TabPrintHelper(PointActivity.this, goodsdto).printTab();
                }
                adapter.notifyDataSetChanged();
                setScreenData();
                numberAdd = "";
                good_weight.setText("");

            }
        });

        mPricePopupWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);
        mPricePopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                scaleUItil.stopShow();
                backgroundAlpaha(PointActivity.this, 1.0f);
                numberAdd = "";
//                App.mClient.unnotify(mac, App.serviceUuid, App.characterUuid, new BleUnnotifyResponse() {
//                    @Override
//                    public void onResponse(int code) {
//
//                    }
//                });
            }
        });
//        portManager.setOnSerialPortDataListener(new OnSerialPortDataListener() {
//            @Override
//            public void onDataReceived(byte[] bytes) {
//                try {
//                    Thread.sleep(500);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                String data = new String(bytes).trim();
//                final String[] d;
//                if (data.length() > 15) {
//                    d = data.split("\u001B");
////                    String weight = Double.parseDouble(d[2])/500;
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            try {
//                                Log.i("data", d[2] + "<<<<<<<<<<<<<");
//                                double wg = DoubleSave.doubleSaveThree(Double.parseDouble(Integer.parseInt(d[2]) + "") / 500);
//                                good_weight.setText(String.valueOf(wg));
//                                tv_singlePrice.setText(DoubleSave.doubleSaveTwo(wg * goodsDto.getPrice()) + "元");
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//
//
//                        }
//                    });
//
//
//                }
//
//
//            }
//
//            @Override
//            public void onDataSent(byte[] bytes) {
//
//            }
//        });

//        App.mClient.notify(mac, App.serviceUuid, App.characterUuid, new BleNotifyResponse() {
//            @Override
//            public void onNotify(UUID service, UUID character, byte[] value) {
//                try {
//                    Log.i("onNotify", new String(value));
//                    String[] ss = new String(value).split(" ");
//                    String data = "";
//                    if (ss.length == 4) {
//                        data = Integer.parseInt(ss[2]) + "g";
//                    } else if (ss.length == 3) {
//                        data = ss[2];
//                    }
//                    Log.i("onNotify", data);
//                    String weight = data;
//                    int len = data.length();
//                    double zl = 0;
//                    double price =0;
////                    if (Pattern.matches("^\\d+(\\.\\d+)?g", data)) {
////                        weight = data.substring(0, len - 1);
////                        zl = DoubleSave.doubleSaveThree(Double.parseDouble(weight) / 500);
////                        price = DoubleSave.doubleSaveThree(Double.parseDouble(weight) / 500 * goodsDto.getPrice());
////                        Log.i("weight", weight + "-----------" + price);
////                    } else {
////                        String s = data.substring(0, len - 4);//注意kg为单位时，后有两个空格字符
////                        Log.i("weight", s+"-----------"+len);
////                        price = DoubleSave.doubleSaveThree(Double.parseDouble(s)*2*goodsDto.getPrice());
////                        zl = DoubleSave.doubleSaveThree(Double.parseDouble(s)*2);
////                    }
//                    if (Pattern.matches("^\\d+(\\.\\d+)?g", data)) {
//                        weight = data.substring(0, len - 1);
//                        zl = DoubleSave.doubleSaveThree(Double.parseDouble(weight));
//                        price = DoubleSave.doubleSaveThree(Double.parseDouble(weight) / 500 * goodsDto.getPrice());
//                        Log.i("weight", weight + "-----------" + price);
//                    } else {
//                        String s = data.substring(0, len - 4);//注意kg为单位时，后有两个空格字符
//                        Log.i("weight", s+"-----------"+len);
//                        price = DoubleSave.doubleSaveThree(Double.parseDouble(s)*2*goodsDto.getPrice());
//                        zl = DoubleSave.doubleSaveThree(Double.parseDouble(s)*500);
//                    }
//                    good_weight.setText(String.valueOf(zl));
//                    tv_singlePrice.setText(price+"元");
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//
//            }
//
//            @Override
//            public void onResponse(int code) {
//                Log.i("blue", code+"----------");
//            }
//        });
    }

    //检测当前购物车是否存在该商品
    public boolean checkGoods(GoodsDto gd) {
        for (GoodsDto dto : lv_goodsBeens) {
            if (gd.getCode().equals(dto.getCode())) {
                dto.setNum(DoubleSave.doubleSaveThree(gd.getNum() + dto.getNum()));
                return true;
            }
        }
        return false;
    }

    private void searchGood(String code) {
        GoodsDto dto = goodsDao.selectGoods(code);
        if (dto != null) {
            addGoods(dto);
        } else {
            et_goods_code.setText("");
            Toast.makeText(this, "商品不存在", Toast.LENGTH_SHORT).show();
        }
        goodsLv.setFocusable(false);
        ETRequestFocus();
    }

    private void searchDzcGood(String[] rules) {
        int len = SPHelper.getInstance().getInt(Constant.POS_DZC_NUM);
        switch (len) {
            case 0:
                len = 1;
                break;
            case 1:
                len = 10;
                break;
            case 2:
                len = 100;
                break;
            default:
                len = 1;
                break;
        }
        GoodsDto dto = goodsDao.selectDzcGoods(rules[2]);
        if (dto != null) {
            switch (rules[0]) {
                case "je":
                    double je = Double.parseDouble(rules[3]) / len;
                    dto.setNum(DoubleSave.doubleSaveThree(je / dto.getPrice()));
                    break;
                case "num":
                    dto.setNum(DoubleSave.doubleSaveThree(Double.parseDouble(rules[3]) / len));
                    break;
            }
            addGoods(dto);
        } else {
            et_goods_code.setText("");
            Toast.makeText(this, "商品不存在", Toast.LENGTH_SHORT).show();
        }
        goodsLv.setFocusable(false);
        ETRequestFocus();
    }

    public void ETRequestFocus() {
        vp.setFocusable(false);
        tabLayout_8.setFocusable(false);
        et_goods_code.setFocusable(true);
        et_goods_code.setFocusableInTouchMode(true);
        et_goods_code.requestFocus();
    }


    private List<GoodsDto> goodsBeenList = new ArrayList<>();



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN
                && event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            List<Activity> activities = new ArrayList<Activity>();
            activities.add(PointActivity.this);
            TwiceConfirmExitUtil.getInstance().showToast(
                    getApplicationContext(), activities);
            return true;// 消费掉后退键
        }
        return super.onKeyDown(keyCode, event);

    }


}
