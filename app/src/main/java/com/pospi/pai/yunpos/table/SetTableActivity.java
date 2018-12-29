package com.pospi.pai.yunpos.table;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.pospi.adapter.TableAdapter;
import com.pospi.adapter.TableGridAdapter;
import com.pospi.dao.OrderDao;
import com.pospi.dao.TableDao;
import com.pospi.dto.OrderDto;
import com.pospi.dto.Tablebeen;
import com.pospi.dto.Tabledto;
import com.pospi.greendao.TablebeenDao;
import com.pospi.pai.yunpos.R;
import com.pospi.pai.yunpos.base.BaseActivity;
import com.pospi.util.App;
import com.pospi.util.constant.tableinfo.TableStatusConstance;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SetTableActivity extends BaseActivity {
    @Bind(R.id.gridView)
    GridView gridView;
    @Bind(R.id.set_table_number)
    TextView setTableNumber;
    @Bind(R.id.et_person_name)
    EditText et_person_name;
    @Bind(R.id.et_person_number)
    EditText etPersonNumber;
//    @Bind(R.id.iv_add)
//    ImageView ivAdd;
    @Bind(R.id.table_more)
    Button tableMore;
    @Bind(R.id.table_sure)
    Button tableSure;
    @Bind(R.id.num_unused)
    TextView numUnused;
    @Bind(R.id.num_used)
    TextView numUsed;
    @Bind(R.id.num_reserved)
    TextView numReserved;
    @Bind(R.id.num_cleaning)
    TextView numCleaning;

    private TableDao tableDao;
    private OrderDao orderDao;
    private TableGridAdapter adapter;
    private TableAdapter tableAdapter;

    private List<Tabledto> tabledtos;
    private List<OrderDto> orderDtos;

    private int eatingNumber;

    public static final String TableName = "tableName";
    public static final String EatingNumber = "eatingNumber";
    public static final String TableID = "tableId";
    public static final String TableOrder = "hasorder";

    private int posion = -2;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
//                    gridView.setAdapter(adapter);

                    gridView.setAdapter(tableAdapter);
                    break;

            }
        }
    };
    private List<Tablebeen> tablebeens;
    private int chooseNumber;
    private boolean isChoose = false;
    private TablebeenDao dao;
    private String tableId;
    private Tablebeen tablebeen;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_table);
        dao = App.getInstance().getDaoSession().getTablebeenDao();
        ButterKnife.bind(this);
        init();

        new Thread(new Runnable() {
            @Override
            public void run() {
//                selectDataFromDb();
                Message msg = Message.obtain();
                msg.what = 1;
                handler.sendMessage(msg);
            }
        }).start();

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (tablebeens.get(position).getStatus() == 0) {
                    setTableNumber.setText(tablebeens.get(position).getNumber() + "号桌");
                    chooseNumber = position;
                    isChoose = true;
                } else {
                    Toast.makeText(SetTableActivity.this, "该桌已被选，请选择其他餐桌", Toast.LENGTH_SHORT).show();
                }



//                setTableNumber.setText(tabledtos.get(position).getName());
//                etPersonNumber.setText(String.valueOf(1));
//                posion = position;
//                eatingNumber = 1;
//                if (tabledtos.get(position).getStatus() == TableStatusConstance.Status_Dining) {
//                    OrderDto orderDto = orderDao.selectTableHasOrder(tabledtos.get(position).getSid());
//                    if (orderDto != null) {
//                        Intent intent = new Intent();
//                        Bundle bundle = new Bundle();
//                        bundle.putSerializable(TableOrder, orderDto);
//                        intent.putExtras(bundle);
//                        intent.putExtra(TableName, tabledtos.get(position).getName());
//                        Log.i("TableName", TableName);
//                        Log.i("orderInfo", orderDto.getOrder_info());
//                        SetTableActivity.this.setResult(2, intent);
//                        finish();
//                    }
//
//                }
            }
        });

    }

    //没有餐桌名字的时候就会默认添加一条数据
    public void giveDefaultName() {
        for (int i = 0; i < tabledtos.size(); i++) {
            if (tabledtos.get(i).getName().trim().isEmpty()) {
                tabledtos.get(i).setName("餐桌" + i);
            }
        }

    }

    public void init() {
        tablebeens = dao.loadAll();
//        for (int i=1;i<10;i++) {
//            Tablebeen tablebeen = new Tablebeen();
//            tablebeen.setNumber(i + "");
//            tablebeen.setStatus(0);
//            tablebeens.add(tablebeen);
//        }
        tableAdapter = new TableAdapter(this, tablebeens);

//        tableDao = new TableDao(this);
//        orderDao = new OrderDao(this);
        eatingNumber = 1;
    }

    //从数据库中查询数据
    public void selectDataFromDb() {
        tabledtos = tableDao.findTableInfo();
        orderDtos = orderDao.selectTableOrder();
        for (int i = 0; i < tabledtos.size(); i++) {
            Tabledto good1 = tabledtos.get(i);
            String good_name1 = good1.getSid();
            for (int j = tabledtos.size() - 1; j > i; j--) {
                Tabledto good2 = tabledtos.get(j);
                String good_name2 = good2.getSid();
                if (good_name1.equals(good_name2)) {
                    tabledtos.remove(good2);
                }
            }
        }

        for (int i = 0; i < tabledtos.size(); i++) {
            for (int j = 0; j < orderDtos.size(); j++) {
                if (tabledtos.get(i).getSid().equals(orderDtos.get(j).getTableNumber())) {
                    tabledtos.get(i).setStatus(TableStatusConstance.Status_Dining);
                }
            }
        }
        giveDefaultName();
        adapter = new TableGridAdapter(getApplicationContext(), orderDtos, tabledtos);
    }

    @OnClick({R.id.table_more,R.id.table_sure})
    public void onClick(View view) {
        switch (view.getId()) {
//            case R.id.iv_subtract:
//                if (eatingNumber > 0) {
//                    eatingNumber -= 1;
//                }
//                etPersonNumber.setText(String.valueOf(eatingNumber));
//                break;
//            case R.id.iv_add:
//                eatingNumber += 1;
//                etPersonNumber.setText(String.valueOf(eatingNumber));
//                break;
            case R.id.table_more:
                break;
            case R.id.table_sure:
                if (isChoose) {
                    updateTable();
                    Intent intent = new Intent();
                    intent.putExtra(TableName, tablebeen.getNumber());
                    intent.putExtra(TableID, tablebeen.getTId());
                    this.setResult(1, intent);
                    finish();
                } else {
                    Toast.makeText(this, "请选餐桌", Toast.LENGTH_SHORT).show();
                }
//                if (eatingNumber > 0) {
//                    if (posion >= 0) {

//                        Log.i("TableName", tabledtos.get(posion).getName());
//                        intent.putExtra(TableName, tabledtos.get(posion).getName());
//                        intent.putExtra(EatingNumber, eatingNumber);

//                    }
//                }
                break;
        }
    }

    //更新餐桌状态
    private void updateTable() {
        setTableNumber.setText("未选餐桌");
        et_person_name.setText("");
        etPersonNumber.setText("");
        isChoose = false;
//        tablebeens.get(chooseNumber).setStatus(1);
//        tableAdapter.notifyDataSetChanged();
        tablebeen = tablebeens.get(chooseNumber);
        //更新数据库数据
//        tableId = tablebeens.get(chooseNumber).getTId();
//        tablebeen = dao.queryBuilder().where(TablebeenDao.Properties.TId.eq(tableId)).unique();
//        if (tablebeen != null) {
//            tablebeen.setStatus(1);
//            dao.update(tablebeen);
//        }

    }
    private void updateTableMsg(String sid){
        Tablebeen t = dao.queryBuilder().where(TablebeenDao.Properties.TId.eq(sid)).unique();
        if (t != null) {
            t.setStatus(1);
            dao.update(t);
        }
    }
}