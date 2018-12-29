package com.pospi.pai.yunpos.wifi_printer;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.pospi.dao.PrintDao;
import com.pospi.dto.GoodsDto;
import com.pospi.dto.MenuDto;
import com.pospi.dto.PrintDto;
import com.pospi.fragment.AddCategoryFragment;
import com.pospi.fragment.AddMenuFragment;
import com.pospi.fragment.AddPrinterFragment;
import com.pospi.fragment.CategoryFragment;
import com.pospi.fragment.MenuFragment;
import com.pospi.fragment.PrinterFragment;
import com.pospi.http.Server;
import com.pospi.pai.yunpos.R;
import com.pospi.pai.yunpos.base.BaseActivity;
import com.pospi.util.GetData;
import com.pospi.util.SaveMenuInfo;
import com.pospi.util.constant.URL;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PrintActivity extends BaseActivity {

    @Bind(R.id.title_middle)
    TextView titleMiddle;
    @Bind(R.id.title_right)
    TextView titleRight;
    @Bind(R.id.area_middle)
    FrameLayout areaMiddle;
    @Bind(R.id.area_right)
    FrameLayout areaRight;
    @Bind(R.id.tv_add_category)
    TextView tvAddCategory;
    @Bind(R.id.tv_add_menu)
    TextView tvAddMenu;
    @Bind(R.id.tv_add_printer)
    TextView tvAddPrinter;
    @Bind(R.id.tv_add)
    TextView tvAdd;
    @Bind(R.id.tv_save_add)
    TextView tvSaveAdd;
    private String[] types = {"小票打印机", "厨房打印机"};
    private int port;

    private PrintDao printDao;
    private PrintDto printDto;
    private List<PrintDto> printDtos;
    private ArrayAdapter adapter;

    private List<String> printerName;

    private String random;
    private boolean isUpdata = false;//是更新数据还是增加数据

    private AddCategoryFragment addCategoryFragment;
    private AddMenuFragment addMenuFragment;
    private AddPrinterFragment addPrinterFragment;
    private CategoryFragment categoryFragment;
    private MenuFragment menuFragment;
    private PrinterFragment printerFragment;

    private MenuDto menuDto;
    private String category_name = "";
    private GoodsDto goodsDto = null;
    private int fragment_position = 1;
    private String uid;

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public GoodsDto getMenuDto() {
        return goodsDto;
    }

    public void setMenuDto(GoodsDto goodsDto) {
        this.goodsDto = goodsDto;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print);
        ButterKnife.bind(this);

        setDefaultFragment();
//        initData();
    }

    /**
     * 设置默认Fragment
     */
    private void setDefaultFragment() {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        categoryFragment = new CategoryFragment(getApplicationContext());
        addCategoryFragment = new AddCategoryFragment();
        transaction.replace(R.id.area_right, addCategoryFragment);
        transaction.replace(R.id.area_middle, categoryFragment);
        transaction.commit();

//        SharedPreferences preferences = getSharedPreferences("Token", MODE_PRIVATE);
//        String value = preferences.getString("value", "");
//        String[] names = value.split("\\,");
//        uid = names[1];
    }

    @OnClick({R.id.tv_add_category, R.id.tv_add_menu, R.id.tv_add_printer, R.id.tv_add, R.id.tv_save_add})
    public void onClick(View view) {
        FragmentManager fm = getFragmentManager();
        // 开启Fragment事务
        FragmentTransaction transaction = fm.beginTransaction();
        switch (view.getId()) {
            case R.id.tv_add_category:
                categoryFragment = new CategoryFragment(getApplicationContext());
                addCategoryFragment = new AddCategoryFragment();
                transaction.replace(R.id.area_right, addCategoryFragment);
                transaction.replace(R.id.area_middle, categoryFragment);
                fragment_position = 1;
                titleRight.setText("新增分类");
                titleMiddle.setText("设置分类");
                category_name = "";
                break;
            case R.id.tv_add_menu:
                menuFragment = new MenuFragment();
                tvSaveAdd.setText("添加");
                addMenuFragment = new AddMenuFragment();
                transaction.replace(R.id.area_right, addMenuFragment);
                transaction.replace(R.id.area_middle, menuFragment);
                fragment_position = 2;
                titleRight.setText("新增商品");
                titleMiddle.setText("设置商品");
                goodsDto = null;
                tvSaveAdd.setText("添加");
                break;
            case R.id.tv_add_printer:
                printerFragment = new PrinterFragment();
                addPrinterFragment = new AddPrinterFragment();
                transaction.replace(R.id.area_right, addPrinterFragment);
                transaction.replace(R.id.area_middle, printerFragment);
                fragment_position = 3;
                titleRight.setText("打印机详情");
                titleMiddle.setText("打印机列表");
                tvSaveAdd.setText("添加");
                break;
            case R.id.tv_add:
                tvSaveAdd.setText("添加");
                category_name = "";
                switch (fragment_position) {
                    case 1:
                        addCategoryFragment.textClear();
                        break;
                    case 2:
                        addMenuFragment.textClear();
                        break;
                    case 3:
                        addPrinterFragment.textClear();
                        break;
                }
                break;
            case R.id.tv_save_add:
                SaveAddClick();
                break;
        }
        // transaction.addToBackStack();
        // 事务提交
        transaction.commit();
    }

    private void SaveAddClick() {
        switch (fragment_position) {
            case 1:
                final String category = addCategoryFragment.getCategory();
                if (TextUtils.isEmpty(category)) {
                    showTipDialog();
                } else {
                    //当输入框不为空，判断是修改还是添加
                    if (category_name.equals("")) {
                        //category_name为空，则为添加状态
                        //请求接口
                        final RequestParams params = new RequestParams();//实例化后存入键值对
                        params.put("Sid", GetData.getStringRandom(32));
                        params.put("Uid", uid);
                        params.put("Name", category);
//                        params.put("orderBy", );
//                        params.put("CreateTime", );
//                        params.put("IsDel", );
                        try {
                            new Server().getConnect(getApplicationContext(), new URL().AddUpCategory, params, new AsyncHttpResponseHandler() {
                                @Override
                                public void onSuccess(int i, Header[] headers, byte[] bytes) {
//                                    Log.i("AddUponSuccess", new String(bytes));
                                    showSuccessDialog(category);
                                }

                                @Override
                                public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
//                                    Log.i("AddUponFailure", new String(bytes));
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        //category_name不为空，则为修改状态
                        if (category.equals(category_name)) {
                            showSuccessDialog();
                        } else {
                            //请求接口
                            String menuInfo = getSharedPreferences("MenuDto_json", MODE_PRIVATE).getString("json", "");
                            List<MenuDto> menuDtos = SaveMenuInfo.changeToList(menuInfo);
                            for (int i = 0; i < menuDtos.size(); i++) {
                                if (menuDtos.get(i).getName().equals(category_name)) {
                                    menuDto = menuDtos.get(i);
                                }
                            }

                            final RequestParams params = new RequestParams();//实例化后存入键值对
                            params.put("Sid", menuDto.getSid());
                            params.put("Uid", uid);
                            params.put("Name", category);
//                            params.put("orderBy", );
//                            params.put("CreateTime", );
//                            params.put("IsDel", );

                            try {
                                new Server().getConnect(getApplicationContext(), new URL().AddUpCategory, params, new AsyncHttpResponseHandler() {
                                    @Override
                                    public void onSuccess(int i, Header[] headers, byte[] bytes) {
//                                        Log.i("AddUponSuccess", new String(bytes));
                                        showSuccessDialog();
                                        categoryFragment.add(category_name);
                                    }

                                    @Override
                                    public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
//                                        Log.i("AddUponFailure", new String(bytes));
                                    }
                                });
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                break;
            case 2:
                if (tvSaveAdd.getText().equals("添加")) {
                    //添加商品信息状态
                    if (addMenuFragment.getEditState()) {
                        //请求接口
                        GoodsDto goodsDto = addMenuFragment.getGoodsDto();
                        final RequestParams params = new RequestParams();//实例化后存入键值对
                        params.put("Sid", GetData.getStringRandom(32));
                        params.put("Uid", uid);
                        params.put("category_sid", goodsDto.getCategory_sid());
                        params.put("price", goodsDto.getPrice());
                        params.put("CostPrice", goodsDto.getPrice());//
                        params.put("Name", goodsDto.getName());
//                        params.put("unit", );//不用
                        params.put("Code", goodsDto.getCode());
//                        params.put("orderBy", );
                        params.put("CreateTime", GetData.getDataTime());
//                        params.put("isHide", );
//                        params.put("IsDel", false);
                        params.put("Code_bm", goodsDto.getCode());
//                        params.put("specification", );
                        params.put("valuationType", 0);
                        params.put("genre", 1);
//                        params.put("Code_bm", );

                        try {
                            new Server().getConnect(getApplicationContext(), new URL().AddUpMenus, params, new AsyncHttpResponseHandler() {
                                @Override
                                public void onSuccess(int i, Header[] headers, byte[] bytes) {
                                    try {
//                                        Log.i("AddUpMenus", new String(bytes));
                                        JSONObject object = new JSONObject(new String(bytes));
                                        if (object.getInt("Result") == 1) {
                                            showSuccessDialog();
                                        } else {
                                            showToast("请求服务器失败，错误信息：" + object.getString("Message"));
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
//                                    Log.i("AddUpMenusFailure", new String(bytes));
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        showToast("请完善商品信息！");
                    }
                } else {
                    //修改商品信息状态
                    if (addMenuFragment.getEditState()) {
                        //请求接口
                        GoodsDto goodsDto = addMenuFragment.getGoodsDto();
                        final RequestParams params = new RequestParams();//实例化后存入键值对
                        params.put("Sid", GetData.getStringRandom(32));
                        params.put("Uid", uid);
                        params.put("category_sid", goodsDto.getCategory_sid());
                        params.put("price", goodsDto.getPrice());
//                        params.put("CostPrice", );
                        params.put("Name", goodsDto.getName());
//                        params.put("unit", );
//                        params.put("Code", );
//                        params.put("orderBy", );
//                        params.put("CreateTime", );
//                        params.put("isHide", );
//                        params.put("IsDel", );
//                        params.put("Code_bm", );
//                        params.put("specification", );
//                        params.put("valuationType", );
//                        params.put("genre", );
//                        params.put("Code_bm", );

                        try {
                            new Server().getConnect(getApplicationContext(), new URL().AddUpMenus, params, new AsyncHttpResponseHandler() {
                                @Override
                                public void onSuccess(int i, Header[] headers, byte[] bytes) {
//                                    Log.i("AddUponSuccess", new String(bytes));
                                    showSuccessDialog();
                                }

                                @Override
                                public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
//                                    Log.i("AddUponFailure", new String(bytes));
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        showToast("请完善商品信息！");
                    }
                }
                break;
            case 3:
                break;
        }
    }

    /**
     * 显示提示Dialog
     */
    private void showTipDialog() {
        final AlertDialog dialog = new AlertDialog.Builder(PrintActivity.this).create();
        dialog.show();
        View view = getLayoutInflater().inflate(R.layout.success_dialog, null);
        dialog.setContentView(view);
        dialog.getWindow().setLayout(300, 200);

        TextView sure = (TextView) view.findViewById(R.id.success_sure);
        TextView content = (TextView) view.findViewById(R.id.content);
        content.setText("请输入类别名称!");
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setCanceledOnTouchOutside(false);
    }

    /**
     * 显示操作成功Dialog
     */
    public void showSuccessDialog() {
        final AlertDialog dialog = new AlertDialog.Builder(PrintActivity.this).create();
        dialog.show();
        View view = getLayoutInflater().inflate(R.layout.success_dialog, null);
        dialog.setContentView(view);
        dialog.getWindow().setLayout(300, 200);
        TextView sure = (TextView) view.findViewById(R.id.success_sure);
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setCanceledOnTouchOutside(false);
    }

    /**
     * 显示操作成功Dialog,并更新ListView
     */
    public void showSuccessDialog(String str) {
        final AlertDialog dialog = new AlertDialog.Builder(PrintActivity.this).create();
        dialog.show();
        View view = getLayoutInflater().inflate(R.layout.success_dialog, null);
        dialog.setContentView(view);
        dialog.getWindow().setLayout(300, 200);
        TextView sure = (TextView) view.findViewById(R.id.success_sure);
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setCanceledOnTouchOutside(false);

        RequestParams params = new RequestParams();//实例化后存入键值对
        SharedPreferences preferences = getSharedPreferences("StoreMessage", Context.MODE_PRIVATE);
        String shopid = preferences.getString("Id", "");
        params.put("value", shopid);
        new Server().getConnect(this, new URL().SYNCCATEGORY, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int code, Header[] headers, byte[] bytes) {
                if (code == 200) {
                    try {
                        JSONObject object = new JSONObject(new String(bytes));
                        Gson gson = new Gson();
                        List<MenuDto> dtos = gson.fromJson(object.getString("Value"), new TypeToken<List<MenuDto>>() {
                        }.getType());
                        SaveMenuInfo.saveAsJson(getApplication(), dtos);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                try {
//                    Log.i("onFailure", new String(bytes) + "");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 设置右边Fragment
     */
    public void setRightFragment(int position) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        switch (position) {
            case 1:
                addCategoryFragment = new AddCategoryFragment();
                transaction.replace(R.id.area_right, addCategoryFragment);
                break;
            case 2:
                addMenuFragment = new AddMenuFragment();
                transaction.replace(R.id.area_right, addMenuFragment);
                break;
            case 3:
                addPrinterFragment = new AddPrinterFragment();
                transaction.replace(R.id.area_right, addPrinterFragment);
                break;
        }
        transaction.commit();
        tvSaveAdd.setText("保存");
    }


//    public void initData() {
//        printDao = new PrintDao(getApplicationContext());
//        printType.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, types));
//        printType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                clearData();
//                delete.setVisibility(View.GONE);
//                switch (position) {
//                    case 0:
//                        printDetail.setVisibility(View.VISIBLE);
//                        wifiPrinter.setVisibility(View.GONE);
//                        break;
//                    case 1:
//                        printDetail.setVisibility(View.GONE);
//                        wifiPrinter.setVisibility(View.VISIBLE);
//                        break;
//                }
//            }
//        });
//
//        openPrinter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                AppLication.openLocalPrinter = isChecked;
//            }
//        });
//
//        giveName();
//        printList.setAdapter(adapter);
//
//        printList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                printDetail.setVisibility(View.GONE);
//                wifiPrinter.setVisibility(View.VISIBLE);
//                delete.setVisibility(View.VISIBLE);
//                PrintDto printDto = printDtos.get(position);
//                etName.setText(printDto.getName());
//                etDk.setText(String.valueOf(printDto.getPort()));
//                etIp.setText(String.valueOf(printDto.getIp()));
//                delete.setVisibility(View.VISIBLE);
//                isUpdata = true;
//                random = printDto.getPrintId();
//            }
//        });
//    }
//
//    public void giveName() {
//        printDtos = printDao.findAllPrinter();
//        printerName = new ArrayList<>();
//        for (int i = 0; i < printDtos.size(); i++) {
//            printerName.add(i, printDtos.get(i).getName());
//        }
//        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, printerName);
//
//    }
//
//    @OnClick({R.id.connect, R.id.printer_test, R.id.delete})
//    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.connect:
//                if (etDk.getText().toString().trim().isEmpty() || etIp.getText().toString().trim().isEmpty() || etName.getText().toString().isEmpty()) {
//                    showToast("请补全打印机信息后保存");
//                } else {
//                    if (isUpdata) {
//                        try {
//                            port = Integer.parseInt(etDk.getText().toString());
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                            showToast("请输入合适的端口号");
//                        }
//                        printDto = new PrintDto();
//                        printDto.setPrintId(random);
//                        printDto.setName(etName.getText().toString());
//                        printDto.setIp(etIp.getText().toString());
//                        printDto.setPort(port);
//                        Log.i("name", printDto.getName() + "之前");
//                        if (printDao.updataPrintInfo(printDto)) {
//                            showToast("保存成功");
//                            adapter.clear();
//                            giveName();
//                            clearData();
//                        }
//                    } else {
//                        try {
//                            port = Integer.parseInt(etDk.getText().toString());
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                            showToast("请输入合适的端口号");
//                        }
//                        printDto = new PrintDto();
//                        printDto.setName(etName.getText().toString());
//                        printDto.setIp(etIp.getText().toString());
//                        printDto.setPort(port);
//                        if (printDao.addPrintInfo(printDto)) {
//                            showToast("保存成功");
//                            adapter.clear();
//                            giveName();
//                            clearData();
//                        }
//                    }
//                }
//                break;
//            case R.id.printer_test:
//                if (!etDk.getText().toString().trim().isEmpty() && !etIp.getText().toString().trim().isEmpty()) {
//                    PrintTest.test(etIp.getText().toString().trim(), Integer.parseInt(etDk.getText().toString().trim()));
//                }
//                break;
//            case R.id.delete:
//                if (printDao.deletePrintDto(random)) {
//                    adapter.clear();
//                    giveName();
//                    clearData();
//                }
//                break;
//        }
//    }
//
//    public void clearData() {
//        etIp.setText("");
//        etDk.setText("");
//        etName.setText("");
//        isUpdata = false;
//    }
}
