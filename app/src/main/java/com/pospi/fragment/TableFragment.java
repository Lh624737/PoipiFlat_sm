package com.pospi.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.pospi.dto.Tabledto;
import com.pospi.pai.pospiflat.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

@SuppressLint("ValidFragment")
public class TableFragment extends Fragment {
    public interface OnTableClick {
        void onTableClick(Tabledto value);//value为传入的值
    }

    @Bind(R.id.gv_table)
    GridView gridView;
    private Context context;
    private List<Tabledto> tabledtos = new ArrayList<>();
    private Tabledto tabledto;
    private OnTableClick listener;

    public TableFragment(Context context, List<Tabledto> tabledtos) {
        this.context = context;
        this.tabledtos = tabledtos;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_table, null);
        ButterKnife.bind(this, v);
//
//        gridView.setAdapter(new MyAdapter(context, tabledtos));
//        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                //商品点击动画visToInvis.start()
//                Interpolator accelerator = new AccelerateInterpolator();
//                Interpolator decelerator = new DecelerateInterpolator();
//                final View visibleList, invisibleList;
//                final ObjectAnimator visToInvis, invisToVis;
//                if (view.getVisibility() == View.GONE) {
//                    visibleList = view;
//                    invisibleList = view;
//                    visToInvis = ObjectAnimator.ofFloat(visibleList, "rotationY", 0f, 90f);
//                    invisToVis = ObjectAnimator.ofFloat(invisibleList, "rotationY", -90f, 0f);
//                } else {
//                    invisibleList = view;
//                    visibleList = view;
//                    visToInvis = ObjectAnimator.ofFloat(visibleList, "rotationY", 0f, -90f);
//                    invisToVis = ObjectAnimator.ofFloat(invisibleList, "rotationY", 90f, 0f);
//                }
//                visToInvis.setDuration(200);
//                invisToVis.setDuration(200);
//                visToInvis.setInterpolator(accelerator);
//                invisToVis.setInterpolator(decelerator);
//                visToInvis.addListener(new AnimatorListenerAdapter() {
//                    @Override
//                    public void onAnimationEnd(Animator anim) {
//                        visibleList.setVisibility(View.GONE);
//                        invisToVis.start();
//                        invisibleList.setVisibility(View.VISIBLE);
//                    }
//                });
//                visToInvis.start();
//
//                tabledto = tabledtos.get(position);
//                if (listener != null) {
//                    listener.onTableClick(tabledto);
//                }
//            }
//        });
        return v;
    }

    @Override
    public void onAttach(Activity activity) {
        listener = (OnTableClick) activity;
        super.onAttach(activity);
    }

//    class MyAdapter extends BaseAdapter {
//        //上下文对象
//        private Context context;
//        private List<Tabledto> tabledtos;
//
//        MyAdapter(Context context, List<Tabledto> tabledtos) {
//            this.context = context;
//            this.tabledtos = tabledtos;
//        }
//
//        @Override
//        public int getCount() {
//            return tabledtos.size();
//        }
//
//        @Override
//        public Object getItem(int item) {
//            return item;
//        }
//
//        @Override
//        public long getItemId(int id) {
//            return id;
//        }
//
////        //创建View方法
////        @Override
////        public View getView(int position, View convertView, ViewGroup parent) {
////            ViewHolder holder;
////            if (convertView == null) {
////                holder = new ViewHolder();
////                convertView = LayoutInflater.from(context).inflate(R.layout.gv_table_item, null);
////                holder.layout_null = (LinearLayout) convertView.findViewById(R.id.ll_null);
////                holder.layout_have = (LinearLayout) convertView.findViewById(R.id.ll_have);
////
////                holder.layout_null_name = (TextView) convertView.findViewById(R.id.ll_null_table_name);
////                holder.layout_null_num = (TextView) convertView.findViewById(R.id.ll_null_person_num);
////                holder.layout_have_name = (TextView) convertView.findViewById(R.id.ll_have_table_name);
////                holder.layout_have_num = (TextView) convertView.findViewById(R.id.ll_have_person_num);
////                holder.layout_have_time = (TextView) convertView.findViewById(R.id.ll_have_time);
////                holder.layout_have_no = (TextView) convertView.findViewById(R.id.ll_have_no);
////                holder.layout_have_money = (TextView) convertView.findViewById(R.id.ll_have_money);
////                convertView.setTag(holder);
////            } else {
////                holder = (ViewHolder) convertView.getTag();
////            }
////            if (tabledtos.get(position).getTableState() == 0) {
////                holder.layout_null_name.setText(tabledtos.get(position).getTableName());
////                holder.layout_null_num.setText(String.valueOf(tabledtos.get(position).getScheduledNum()));
////            } else if (tabledtos.get(position).getTableState() == 1) {
////                holder.layout_have_name.setText(tabledtos.get(position).getTableName());
////                holder.layout_have_num.setText(tabledtos.get(position).getScheduledNum());
////                holder.layout_have_time.setText(tabledtos.get(position).getTime());
////                holder.layout_have_no.setText(tabledtos.get(position).getMaxNo());
////                holder.layout_have_money.setText(String.valueOf(tabledtos.get(position).getMoney()));
////            }
////            return convertView;
////        }
//
//        class ViewHolder {
//            TextView layout_null_name;
//            TextView layout_null_num;
//            TextView layout_have_name;
//            TextView layout_have_num;
//            TextView layout_have_time;
//            TextView layout_have_no;
//            TextView layout_have_money;
//            LinearLayout layout_null;
//            LinearLayout layout_have;
//        }
//    }
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        ButterKnife.unbind(this);
//    }
}