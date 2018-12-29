package com.pospi.pai.yunpos.more;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.pospi.adapter.RestOrderAdapter;
import com.pospi.dao.OrderDao;
import com.pospi.dto.OrderDto;
import com.pospi.dto.Tablebeen;
import com.pospi.greendao.TablebeenDao;
import com.pospi.pai.yunpos.R;
import com.pospi.pai.yunpos.base.BaseActivity;
import com.pospi.pai.yunpos.cash.PointActivity;
import com.pospi.pai.yunpos.cash.ScanActivity;
import com.pospi.pai.yunpos.pay.PayActivity;
import com.pospi.util.App;
import com.pospi.util.Sava_list_To_Json;
import com.pospi.util.constant.URL;
import com.pospi.util.constant.tableinfo.TableStatusConstance;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RestOrderActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.lv_order)
    ListView lvOrder;
    @Bind(R.id.guadan_layout)
    LinearLayout parent;

    private List<OrderDto> orderDtos;

    public static final String REST_ORDER = "restOrder";
    public static final String REST_SCAN = "scan";
    public static final String REST_Point = "point_select";
    int mPosition;
    private OrderDao orderDao;
    private PopupWindow popupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rest_order);
        ButterKnife.bind(this);
        orderDao = new OrderDao(getApplicationContext());

        orderDtos = orderDao.selectRestOrder();
        RestOrderAdapter adapter = new RestOrderAdapter(getApplicationContext(), orderDtos);
        lvOrder.setAdapter(adapter);
        lvOrder.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                mPosition = position;


                showSetPricePopupWindow();
//                AlertDialog.Builder builder = new AlertDialog.Builder(RestOrderActivity.this);
//                builder.setMessage("提示");
//                builder.setPositiveButton("结账", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//
//
//                    }
//                });
//                builder.setNeutralButton("修改", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//
//
//                    }
//                });
//                builder.setNegativeButton("取消", null);
//                builder.show();
//
//
//
            }
        });

    }

    private void showSetPricePopupWindow() {
        View contentView = LayoutInflater.from(this).inflate(R.layout.dialog_table, null);
        popupWindow = new PopupWindow(contentView, 500, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setFocusable(true);
        TextView table_name = contentView.findViewById(R.id.table_name);
        Button pay = contentView.findViewById(R.id.guadan_toPay);
        Button detail = contentView.findViewById(R.id.guadan_toDetail);
        Button cancle = contentView.findViewById(R.id.guadan_toCancle);
        TextView rest_order_detail_id = contentView.findViewById(R.id.rest_order_detail_id);
        TextView rest_order_detail_time = contentView.findViewById(R.id.rest_order_detail_time);
        rest_order_detail_id.setText("订单号:  "+orderDtos.get(mPosition).getMaxNo());
        rest_order_detail_time.setText("时间:     "+orderDtos.get(mPosition).getCheckoutTime());
        table_name.setText(orderDtos.get(mPosition).getTableNumber() + "号桌");
        pay.setOnClickListener(this);
        detail.setOnClickListener(this);
        cancle.setOnClickListener(this);

        popupWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.guadan_toPay:
                popupWindow.dismiss();
                Intent intent = new Intent(RestOrderActivity.this, PayActivity.class);
                intent.putExtra("money", orderDtos.get(mPosition).getYs_money());
                intent.putExtra("orderType", URL.ORDERTYPE_SALE);
                intent.putExtra("maxNo", orderDtos.get(mPosition).getMaxNo());
                intent.putExtra("tableId", orderDtos.get(mPosition).getTableId());
                Log.i("table", orderDtos.get(mPosition).getTableNumber());

                intent.putExtra("tableNumber", orderDtos.get(mPosition).getTableNumber());
                startActivity(intent);
                Sava_list_To_Json.changeToJaon(getApplication(), Sava_list_To_Json.changeToList(orderDtos.get(mPosition).getOrder_info()));
                finish();
                break;
            case R.id.guadan_toDetail:
                popupWindow.dismiss();
                TableStatusConstance.TABLE_MAXNO = orderDtos.get(mPosition).getMaxNo();
                Intent intent1;
                if (orderDtos.get(mPosition).getPayReturn().equals(REST_SCAN)) {
                    intent1 = new Intent(RestOrderActivity.this, ScanActivity.class);
                } else {
                    intent1 = new Intent(RestOrderActivity.this, PointActivity.class);
                }
                Bundle bundle = new Bundle();
                bundle.putSerializable(REST_ORDER, orderDtos.get(mPosition));
                intent1.putExtras(bundle);
                startActivity(intent1);
                Sava_list_To_Json.addShoppings(RestOrderActivity.this, orderDtos.get(mPosition).getOrder_info());
                Log.i("跳转之前", "NO" + orderDtos.get(mPosition).getMaxNo() + "");

//                        boolean delete = orderDao.deleteRestOrderInfo(orderDtos.get(position).getMaxNo());
//                        Log.i("delete", delete + "");
//                TablebeenDao dao = App.getInstance().getDaoSession().getTablebeenDao();
//                Tablebeen tablebeen = dao.queryBuilder().where(TablebeenDao.Properties.TId.eq(orderDtos.get(mPosition).getTableId())).unique();
//                if (tablebeen != null) {
//                    tablebeen.setStatus(TableStatusConstance.Status_Free);
//                    dao.update(tablebeen);
//                }
                finish();
                break;
            case R.id.guadan_toCancle:
                new AlertDialog.Builder(this).setTitle("提示")
                        .setMessage("是否删除？")
                        .setPositiveButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                popupWindow.dismiss();
                                boolean delete = orderDao.deleteRestOrderInfo(orderDtos.get(mPosition).getMaxNo());
                                Log.i("delete", delete + "");
                                TablebeenDao dao = App.getInstance().getDaoSession().getTablebeenDao();
                                Tablebeen tablebeen = dao.queryBuilder().where(TablebeenDao.Properties.TId.eq(orderDtos.get(mPosition).getTableId())).unique();
                                if (tablebeen != null) {
                                    tablebeen.setStatus(TableStatusConstance.Status_Free);
                                    dao.update(tablebeen);
                                }

                            }
                        })
                        .setNegativeButton("否", null).show();





                break;
        }
    }
}
