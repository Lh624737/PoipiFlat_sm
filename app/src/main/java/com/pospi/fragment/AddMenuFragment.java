package com.pospi.fragment;

import android.app.Fragment;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.pospi.dto.GoodsDto;
import com.pospi.dto.MenuDto;
import com.pospi.http.Server;
import com.pospi.pai.yunpos.R;
import com.pospi.pai.yunpos.wifi_printer.PrintActivity;
import com.pospi.util.GetData;
import com.pospi.util.SaveMenuInfo;
import com.pospi.util.TurnSize;
import com.pospi.util.constant.URL;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class AddMenuFragment extends Fragment {


    @Bind(R.id.iv_menu)
    ImageView ivMenu;
    @Bind(R.id.et_category_code)
    EditText etCategoryCode;
    @Bind(R.id.et_category_name)
    EditText etCategoryName;
    @Bind(R.id.et_category_price)
    EditText etCategoryPrice;
    @Bind(R.id.tv_category)
    TextView tvCategory;
    @Bind(R.id.btn_delete)
    Button btnDelete;

    private GoodsDto goodsDto;
    private Context context;
    public static final int REQUEST_CODE_PICK_IMAGE = 44;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.addmenu_right, container, false);
        ButterKnife.bind(this, view);

        context = getActivity().getApplicationContext();
        goodsDto = ((PrintActivity) getActivity()).getMenuDto();
        if (goodsDto != null) {
            Glide.with(context).load(new URL().host() + goodsDto
                    .getImage()).placeholder(R.drawable.noimageshow).dontAnimate().into(ivMenu);
            etCategoryCode.setText(goodsDto.getCode());
            etCategoryName.setText(goodsDto.getName());
            etCategoryPrice.setText(String.valueOf(goodsDto.getPrice()));
            tvCategory.setText(((PrintActivity) getActivity()).getCategory_name());
        }

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.tv_category, R.id.btn_delete, R.id.iv_menu})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_category:
                showPopupWindow();
                break;
            case R.id.btn_delete:
                if (goodsDto != null) {

                    //请求接口
                    SharedPreferences preferences = getActivity().getSharedPreferences("Token", Context.MODE_PRIVATE);
                    String value = preferences.getString("value", "");
                    String[] names = value.split("\\,");
                    final RequestParams params = new RequestParams();//实例化后存入键值对
                    params.put("Sid", goodsDto.getSid());
                    params.put("Uid", names[1]);
                    params.put("category_sid", goodsDto.getCategory_sid());
                    params.put("price", goodsDto.getPrice());
                    params.put("CostPrice", goodsDto.getPrice());//
                    params.put("Name", goodsDto.getName());
                    params.put("Code", goodsDto.getCode());
                    params.put("CreateTime", GetData.getDataTime());
                    params.put("IsDel", true);
                    params.put("Code_bm", goodsDto.getCode());
                    params.put("valuationType", 0);
                    params.put("genre", 1);

                    try {
                        new Server().getConnect(getActivity().getApplicationContext(), new URL().AddUpMenus, params, new AsyncHttpResponseHandler() {
                            @Override
                            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                                try {
//                                    Log.i("AddUponSuccess", new String(bytes));
                                    JSONObject object = new JSONObject(new String(bytes));
                                    if (object.getInt("Result") == 1) {
                                        ((PrintActivity) getActivity()).showSuccessDialog();
                                    } else {
                                        Toast.makeText(context, "请求服务器失败，错误信息：" + object.getString("Message"), Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
//                                Log.i("AddUponFailure", new String(bytes));
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case R.id.iv_menu:
                getImageFromAlbum();
                break;
        }
    }


    protected void getImageFromAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                "image/*");
        try {
            startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(context, "无相册", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_PICK_IMAGE:
                if (data != null) {
                    ivMenu.setImageURI(data.getData());
                }
                break;
        }

    }

    /**
     * 清除文本框
     */
    public void textClear() {
        ivMenu.setImageResource(R.drawable.noimageshow);
        etCategoryCode.setText("");
        etCategoryName.setText("");
        etCategoryPrice.setText("");
        tvCategory.setText("");
    }

    /**
     * 得到用户输入的商品信息
     *
     * @return
     */
    public GoodsDto getGoodsDto() {
        GoodsDto goodsDto = new GoodsDto();
        try {
            goodsDto.setPrice(Double.parseDouble(etCategoryPrice.getText().toString().trim()));
        } catch (NumberFormatException e) {
            Toast.makeText(context, "请输入正确的价格！", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        goodsDto.setName(etCategoryName.getText().toString().trim());
        goodsDto.setCode(etCategoryCode.getText().toString().trim());
        String category = tvCategory.getText().toString();
        String menuInfo = context.getSharedPreferences("MenuDto_json", Context.MODE_PRIVATE).getString("json", "");
        List<MenuDto> menuDtos = SaveMenuInfo.changeToList(menuInfo);
        for (int i = 0; i < menuDtos.size(); i++) {
            if (menuDtos.get(i).getName().equals(category)) {
                goodsDto.setCategory_sid(menuDtos.get(i).getSid());
            }
        }
        return goodsDto;
    }

    public void showPopupWindow() {
        String menuInfo = getActivity().getSharedPreferences("MenuDto_json", Context.MODE_PRIVATE).getString("json", "");
        List<MenuDto> menuDtos = SaveMenuInfo.changeToList(menuInfo);
        final List<String> name = new ArrayList<>();
        for (int i = 0; i < menuDtos.size(); i++) {
            name.add(menuDtos.get(i).getName());
        }

        LayoutInflater mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = mLayoutInflater.inflate(R.layout.category_pop_left, null);
        // R.layout.pop为PopupWindow 的布局文件
        final PopupWindow pop = new PopupWindow(contentView, TurnSize.dip2px(context, 200), TurnSize.dip2px(context, 300));
        pop.setBackgroundDrawable(new BitmapDrawable());
        // 指定 PopupWindow 的背景
        pop.setFocusable(true);
        pop.showAsDropDown(tvCategory, -200, -170);
        ListView lv_category = (ListView) contentView.findViewById(R.id.lv_category_pop);
        CategoryAdapter categoryAdapter = new CategoryAdapter(context, name);
        lv_category.setAdapter(categoryAdapter);
        lv_category.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tvCategory.setText(name.get(position));
                pop.dismiss();
            }
        });
    }

    /**
     * 得到文本框的状态，如果全部不为空则返回true
     *
     * @return
     */
    public Boolean getEditState() {
        if (TextUtils.isEmpty(etCategoryName.getText().toString().trim())) {
            return false;
        }
        if (TextUtils.isEmpty(etCategoryCode.getText().toString().trim())) {
            return false;
        }
        if (TextUtils.isEmpty(etCategoryPrice.getText().toString().trim())) {
            return false;
        }
        if (TextUtils.isEmpty(tvCategory.getText().toString().trim())) {
            return false;
        }
        return true;
    }

    private class CategoryAdapter extends BaseAdapter {
        private Context context;
        private List<String> category;

        public CategoryAdapter(Context context, List<String> category) {
            this.context = context;
            this.category = category;
        }

        @Override
        public int getCount() {
            return category.size();
        }

        @Override
        public Object getItem(int position) {
            return category.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.category_item, null);
                holder = new ViewHolder();
                holder.category = (TextView) convertView.findViewById(R.id.category);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.category.setText(category.get(position));
            return convertView;
        }


        class ViewHolder {
            TextView category;
        }

    }
}
