package com.pospi.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.pospi.dto.MenuDto;
import com.pospi.pai.pospiflat.R;
import com.pospi.pai.pospiflat.wifi_printer.PrintActivity;
import com.pospi.util.SaveMenuInfo;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class CategoryFragment extends Fragment {

    @Bind(R.id.lv_category)
    ListView lvCategory;
    private List<MenuDto> menuDtos;
    private List<String> name = new ArrayList<>();
    private CategoryAdapter categoryAdapter;
    private Context context;

    public CategoryFragment(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_addcategory, container, false);
        ButterKnife.bind(this, view);
        context = getActivity().getApplicationContext();
        String menuInfo = getActivity().getSharedPreferences("MenuDto_json", Context.MODE_PRIVATE).getString("json", "");
        menuDtos = SaveMenuInfo.changeToList(menuInfo);
        for (int i = 0; i < menuDtos.size(); i++) {
            name.add(menuDtos.get(i).getName());
        }
        categoryAdapter = new CategoryAdapter(getActivity().getApplicationContext(), name);
        lvCategory.setAdapter(categoryAdapter);
        lvCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((PrintActivity) getActivity()).setCategory_name(name.get(position));
                ((PrintActivity) getActivity()).setRightFragment(1);
            }
        });
        return view;
    }

    public void delete(String str) {
        if (categoryAdapter == null) {
            categoryAdapter = new CategoryAdapter(context, name);
            categoryAdapter.delete(str);
        }
    }

    public void add(String str) {
        if (categoryAdapter == null) {
            categoryAdapter = new CategoryAdapter(context, name);
            categoryAdapter.add(str);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    public class CategoryAdapter extends BaseAdapter {
        private Context context;
        private List<String> leftName;

        public CategoryAdapter(Context context, List<String> leftName) {
            this.context = context;
            this.leftName = leftName;
        }

        public void delete(String name) {
            leftName.remove(name);
            notifyDataSetChanged();
        }

        public void add(String name) {
            leftName.add(name);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return leftName.size();
        }

        @Override
        public Object getItem(int position) {
            return leftName.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(context).inflate(R.layout.left_list_item, null);
                holder.left = (TextView) convertView.findViewById(R.id.left_list_item);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.left.setText(leftName.get(position));
            return convertView;
        }

        class ViewHolder {
            TextView left;
        }
    }
}
