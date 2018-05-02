package com.pospi.fragment;

import android.app.Fragment;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.pospi.pai.pospiflat.R;
import com.pospi.pai.pospiflat.wifi_printer.PrintTest;
import com.pospi.util.TurnSize;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class AddPrinterFragment extends Fragment {


    @Bind(R.id.btn_model)
    Button btnModel;
    @Bind(R.id.et_name)
    EditText etName;
    @Bind(R.id.et_ip)
    EditText etIp;
    @Bind(R.id.et_dk)
    EditText etDk;
    @Bind(R.id.btn_use_way)
    Button btnUseWay;
    @Bind(R.id.btn_test)
    Button btnTest;
    @Bind(R.id.btn_delete)
    Button btnDelete;
    private Context context;
    private ListView lvPrintModel;
    private String[] printModels = {"厨房打印机", "小票打印机"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.printer_right, container, false);
        ButterKnife.bind(this, view);
        btnTest.setVisibility(View.INVISIBLE);
        btnDelete.setVisibility(View.INVISIBLE);
        context = getActivity();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.btn_model, R.id.btn_use_way, R.id.btn_test, R.id.btn_delete})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_model:
                break;
            case R.id.btn_use_way:
                showPrinterModel();
                break;
            case R.id.btn_test:
                if (!etDk.getText().toString().trim().isEmpty() && !etIp.getText().toString().trim().isEmpty()&&!etName.getText().toString().isEmpty()) {
                    PrintTest.test(etIp.getText().toString().trim(), Integer.parseInt(etDk.getText().toString().trim()));
                }else {
                    Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_delete:
                break;
        }
    }

    /**
     * show出打印机的类型
     */
    public void showPrinterModel() {
        LayoutInflater mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = mLayoutInflater.inflate(R.layout.main_pop_left, null);
        // R.layout.pop为PopupWindow 的布局文件
        final PopupWindow pop = new PopupWindow(contentView, TurnSize.dip2px(context, 200), TurnSize.dip2px(context, 150));
        pop.setBackgroundDrawable(new BitmapDrawable());
        // 指定 PopupWindow 的背景
        pop.setFocusable(true);
        pop.showAsDropDown(btnUseWay);
        lvPrintModel = (ListView) contentView.findViewById(R.id.lv_printer_model);
        lvPrintModel.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_expandable_list_item_1, printModels));
        lvPrintModel.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                btnUseWay.setText(printModels[position]);
                pop.dismiss();
                btnTest.setVisibility(View.VISIBLE);
            }
        });

    }

    public void textClear() {
        etIp.setText("");
        etDk.setText("");
        etName.setText("");
        btnDelete.setVisibility(View.INVISIBLE);
        btnTest.setVisibility(View.INVISIBLE);
        btnUseWay.setText("请选择");
    }
}
