package com.pospi.fragment;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.pospi.adapter.TradeListAdapter;
import com.pospi.dao.OrderDao;
import com.pospi.dto.OrderDto;
import com.pospi.pai.yunpos.R;
import com.pospi.pai.yunpos.more.RefundsActivity;
import com.pospi.util.GetData;

import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class TradeListFragment extends Fragment {

    @Bind(R.id.tradlist_data)
    TextView data;
    @Bind(R.id.sale_et)
    EditText saleEt;
    @Bind(R.id.tradlistview)
    ListView tradlistview;
    @Bind(R.id.btn_search)
    Button btnSearch;
    private Context context;
    private TradeListAdapter adapter;
    private List<OrderDto> dtos;

    public static final String REFUNDS = "refunds";

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trade_list, container, false);
        ButterKnife.bind(this, view);
        context = getActivity().getApplicationContext();
        data.setText(GetData.getYYMMDDTime());//进入界面的时候就会把日期定位在当天
        AdapteAdapter(data.getText().toString().trim());

        tradlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), RefundsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(REFUNDS, dtos.get(position));
                intent.putExtras(bundle);
                startActivityForResult(intent, 1);

            }
        });

        saleEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (saleEt.getText().toString().length() == 10) {
                    searchOrder();
                }
            }
        });


        return view;
    }


    public void searchOrder() {
        String maxNo;
        try {
            maxNo = saleEt.getText().toString().trim();
            OrderDto selectDto = new OrderDao(getActivity()).selectOrderByNo(maxNo);
            Log.i("searchOrder", "getMiYaNumber: "+selectDto.getMiYaNumber());
            if (selectDto == null) {
                Toast.makeText(getActivity().getApplicationContext(), "未查询到该订单！", Toast.LENGTH_SHORT).show();
            } else {
                dtos.clear();
                dtos.add(selectDto);
                adapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            Toast.makeText(getActivity().getApplicationContext(), "请输入合适的订单号！", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    /**
     * 从数据库里面查询数据
     *
     * @param date 传入需要查询的日期
     */
    public void AdapteAdapter(String date) {
        dtos = new OrderDao(getActivity().getApplicationContext()).selectGoods(date);
        adapter = new TradeListAdapter(context, dtos);
        tradlistview.setAdapter(adapter);
    }


    @OnClick({R.id.tradlist_data, R.id.btn_search})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tradlist_data:
                final Calendar c = Calendar.getInstance();
                DatePickerDialog dialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        c.set(year, monthOfYear, dayOfMonth);
                        data.setText(DateFormat.format("yyyy-MM-dd", c));
                        AdapteAdapter(data.getText().toString().trim());
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                dialog.show();
                break;
            case R.id.btn_search:
                searchOrder();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        getActivity().finish();
    }
}
