package com.pospi.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.pospi.dto.MenuDto;
import com.pospi.http.Server;
import com.pospi.pai.pospiflat.R;
import com.pospi.pai.pospiflat.wifi_printer.PrintActivity;
import com.pospi.util.SaveMenuInfo;
import com.pospi.util.constant.URL;

import org.apache.http.Header;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class AddCategoryFragment extends Fragment {


    @Bind(R.id.et_category)
    EditText etCategory;
    @Bind(R.id.btn_del)
    Button btnDel;

    private String category_name;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.addcategory_right, container, false);
        ButterKnife.bind(this, view);

        category_name = ((PrintActivity) getActivity()).getCategory_name();
        if (category_name != null) {
            etCategory.setText(category_name);
        }
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.btn_del)
    public void onClick() {
        String category = getCategory();
        String menuInfo = getActivity().getSharedPreferences("MenuDto_json", Context.MODE_PRIVATE).getString("json", "");
        List<MenuDto> menuDtos = SaveMenuInfo.changeToList(menuInfo);
        for (int i = 0; i < menuDtos.size(); i++) {
            if (menuDtos.get(i).getName().equals(category)) {
                //请求接口
                SharedPreferences preferences = getActivity().getSharedPreferences("Token", Context.MODE_PRIVATE);
                String value = preferences.getString("value", "");
                String[] names = value.split("\\,");
                final RequestParams params = new RequestParams();//实例化后存入键值对
                params.put("Sid", menuDtos.get(i).getSid());
                params.put("Uid", names[1]);
                params.put("Name", category);
//                            params.put("orderBy", );
//                            params.put("CreateTime", );
                params.put("IsDel", true);

                try {
                    new Server().getConnect(getActivity().getApplicationContext(), new URL().DelCategory, params, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int i, Header[] headers, byte[] bytes) {
//                            Log.i("DelCategory", new String(bytes));
                            ((PrintActivity)getActivity()).showSuccessDialog(getCategory());
                        }

                        @Override
                        public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
//                            Log.i("DelCategory", new String(bytes));
                            ((PrintActivity)getActivity()).showToast("删除失败！");
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 清空文本框
     */
    public void textClear() {
        etCategory.setText("");
    }

    /**
     * 得到输入框的文本
     */
    public String getCategory() {
        return etCategory.getText().toString().trim();
    }
}
