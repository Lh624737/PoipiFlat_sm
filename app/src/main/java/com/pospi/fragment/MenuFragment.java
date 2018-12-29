package com.pospi.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pospi.dao.GoodsDao;
import com.pospi.dto.GoodsDto;
import com.pospi.dto.MenuDto;
import com.pospi.pai.yunpos.R;
import com.pospi.pai.yunpos.wifi_printer.PrintActivity;
import com.pospi.util.SaveMenuInfo;
import com.pospi.view.swipemenulistview.PinnedHeaderListView;
import com.pospi.view.swipemenulistview.SectionedBaseAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class MenuFragment extends Fragment {

    @Bind(R.id.pinnedListView)
    com.pospi.view.swipemenulistview.PinnedHeaderListView pinnedListView;

    private TestSectionedAdapter sectionedAdapter;
    private List<List<String>> lists = new ArrayList<>();
    private List<List<GoodsDto>> list_goodsDtos = new ArrayList<>();
    private List<String> category = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_addmenu, container, false);
        ButterKnife.bind(this, view);

        String menuInfo = getActivity().getSharedPreferences("MenuDto_json", Context.MODE_PRIVATE).getString("json", "");
        List<MenuDto> menuDtos = SaveMenuInfo.changeToList(menuInfo);
        for (int i = 0; i < menuDtos.size(); i++) {
            category.add(menuDtos.get(i).getName());
        }


        GoodsDao dao = new GoodsDao(getActivity().getApplicationContext());
        for (int i = 0; i < category.size(); i++) {
            List<GoodsDto> goodsBeens = dao.findSelectPointGoods(menuDtos.get(i).getSid());
            List<String> list = new ArrayList<>();
            for (int j = 0; j < goodsBeens.size(); j++) {
                list.add(goodsBeens.get(j).getName());
            }
            list_goodsDtos.add(goodsBeens);
            lists.add(list);
        }

        sectionedAdapter = new TestSectionedAdapter(getActivity().getApplicationContext(), category, lists);
        pinnedListView.setAdapter(sectionedAdapter);
        pinnedListView.setOnItemClickListener(new PinnedHeaderListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int section, int position, long id) {
                ((PrintActivity) getActivity()).setCategory_name(category.get(section));
                ((PrintActivity) getActivity()).setMenuDto(list_goodsDtos.get(section).get(position));
                ((PrintActivity) getActivity()).setRightFragment(2);
            }

            @Override
            public void onSectionClick(AdapterView<?> adapterView, View view, int section, long id) {

            }
        });
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    public class TestSectionedAdapter extends SectionedBaseAdapter {

        private Context mContext;
        private List<String> leftStr;
        private List<List<String>> lists;

        public TestSectionedAdapter(Context context, List<String> leftStr, List<List<String>> lists) {
            this.mContext = context;
            this.leftStr = leftStr;
            this.lists = lists;
        }

        @Override
        public Object getItem(int section, int position) {
            return lists.get(section).get(position);
        }

        @Override
        public long getItemId(int section, int position) {
            return position;
        }

        @Override
        public int getSectionCount() {
            return leftStr.size();
        }

        @Override
        public int getCountForSection(int section) {
            return lists.get(section).size();
        }

        @Override
        public View getItemView(final int section, final int position, View convertView, ViewGroup parent) {
            RelativeLayout layout = null;
            if (convertView == null) {
                LayoutInflater inflator = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                layout = (RelativeLayout) inflator.inflate(R.layout.left_list_item, null);
            } else {
                layout = (RelativeLayout) convertView;
            }
            ((TextView) layout.findViewById(R.id.left_list_item)).setText(lists.get(section).get(position));

//        layout.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View arg0) {
//                Toast.makeText(mContext, lists.get(section).get(position).getName(), Toast.LENGTH_SHORT).show();
//            }
//        });
            return layout;
        }

        @Override
        public View getSectionHeaderView(int section, View convertView, ViewGroup parent) {
            LinearLayout layout = null;
            if (convertView == null) {
                LayoutInflater inflator = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                layout = (LinearLayout) inflator.inflate(R.layout.header_item, null);
            } else {
                layout = (LinearLayout) convertView;
            }
            layout.setClickable(false);
            ((TextView) layout.findViewById(R.id.textItem)).setText(leftStr.get(section));
            return layout;
        }

    }
}
