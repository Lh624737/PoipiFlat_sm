package com.pospi.pai.pospiflat.more;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.pospi.adapter.RestOrderAdapter;
import com.pospi.dao.OrderDao;
import com.pospi.dto.OrderDto;
import com.pospi.dto.Tablebeen;
import com.pospi.greendao.TablebeenDao;
import com.pospi.pai.pospiflat.R;
import com.pospi.pai.pospiflat.base.BaseActivity;
import com.pospi.pai.pospiflat.cash.PointActivity;
import com.pospi.pai.pospiflat.cash.ScanActivity;
import com.pospi.util.App;
import com.pospi.util.Sava_list_To_Json;
import com.pospi.util.constant.tableinfo.TableStatusConstance;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RestOrderActivity extends BaseActivity {

    @Bind(R.id.lv_order)
    ListView lvOrder;

    private List<OrderDto> orderDtos;
    public static final String REST_ORDER = "restOrder";
    public static final String REST_SCAN = "scan";
    public static final String REST_Point = "point_select";

    private OrderDao orderDao;

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
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent;
                if (orderDtos.get(position).getPayReturn().equals(REST_SCAN)) {
                    intent = new Intent(RestOrderActivity.this, ScanActivity.class);
                } else {
                    intent = new Intent(RestOrderActivity.this, PointActivity.class);
                }
                Bundle bundle = new Bundle();
                bundle.putSerializable(REST_ORDER, orderDtos.get(position));
                intent.putExtras(bundle);
                startActivity(intent);
                Sava_list_To_Json.addShoppings(RestOrderActivity.this,orderDtos.get(position).getOrder_info());
                Log.i("跳转之前", "NO" + orderDtos.get(position).getMaxNo() + "");

                boolean delete = orderDao.deleteRestOrderInfo(orderDtos.get(position).getMaxNo());
                Log.i("delete", delete + "");
                TablebeenDao dao = App.getInstance().getDaoSession().getTablebeenDao();
                Tablebeen tablebeen = dao.queryBuilder().where(TablebeenDao.Properties.TId.eq(orderDtos.get(position).getTableId())).unique();
                if (tablebeen != null) {
                    tablebeen.setStatus(TableStatusConstance.Status_Free);
                    dao.update(tablebeen);
                }
                finish();
            }
        });

    }
}
