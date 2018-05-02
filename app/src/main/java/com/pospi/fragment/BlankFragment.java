package com.pospi.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pospi.dto.GoodsDto;
import com.pospi.pai.pospiflat.R;
import com.pospi.util.constant.URL;
import com.pospi.view.swipemenulistview.AutoAdjustSizeTextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

@SuppressLint("ValidFragment")
public class BlankFragment extends Fragment {
    public interface OnGridViewClick {
        void ongridviewclick(GoodsDto value);//value为传入的值
    }

    @Bind(R.id.gridView)
    GridView gridView;
    private Context context;
    private List<GoodsDto> goodsBeens;
    private GoodsDto goodsBean;
    private OnGridViewClick listener;

    public BlankFragment(Context context, List<GoodsDto> goodsBeens) {
        this.context = context;
        this.goodsBeens = goodsBeens;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_1, null);
        ButterKnife.bind(this, v);

        gridView.setAdapter(new MyAdapter(context, goodsBeens));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //商品点击动画visToInvis.start()
                Interpolator accelerator = new AccelerateInterpolator();
                Interpolator decelerator = new DecelerateInterpolator();
                final View visibleList, invisibleList;
                final ObjectAnimator visToInvis, invisToVis;
                if (view.getVisibility() == View.GONE) {
                    visibleList = view;
                    invisibleList = view;
                    visToInvis = ObjectAnimator.ofFloat(visibleList, "rotationY", 0f, 90f);
                    invisToVis = ObjectAnimator.ofFloat(invisibleList, "rotationY", -90f, 0f);
                } else {
                    invisibleList = view;
                    visibleList = view;
                    visToInvis = ObjectAnimator.ofFloat(visibleList, "rotationY", 0f, -90f);
                    invisToVis = ObjectAnimator.ofFloat(invisibleList, "rotationY", 90f, 0f);
                }
                visToInvis.setDuration(200);
                invisToVis.setDuration(200);
                visToInvis.setInterpolator(accelerator);
                invisToVis.setInterpolator(decelerator);
                visToInvis.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator anim) {
                        visibleList.setVisibility(View.GONE);
                        invisToVis.start();
                        invisibleList.setVisibility(View.VISIBLE);
                    }
                });
                visToInvis.start();

                goodsBean = goodsBeens.get(position);
                if (listener != null) {
                    listener.ongridviewclick(goodsBean);
                }
            }
        });
        return v;
    }

    @Override
    public void onAttach(Activity activity) {
        listener = (OnGridViewClick) activity;
        super.onAttach(activity);
    }

    class MyAdapter extends BaseAdapter {
        //上下文对象
        private Context context;
        private List<GoodsDto> goodsBeens;
        //无图设置
        private boolean wutu;
        MyAdapter(Context context, List<GoodsDto> goodsBeens) {
            this.context = context;
            this.goodsBeens = goodsBeens;
            wutu = getWuTu();
        }

        @Override
        public int getCount() {
            return goodsBeens.size();
        }

        @Override
        public Object getItem(int item) {
            return item;
        }

        @Override
        public long getItemId(int id) {
            return id;
        }

        //创建View方法
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                if (wutu) {
                    convertView = LayoutInflater.from(context).inflate(R.layout.goods_item, null);
                    holder.tv_good_name_wutu = (TextView) convertView.findViewById(R.id.tv_goodName);
                } else {
                    convertView = LayoutInflater.from(context).inflate(R.layout.gv_goods_item, null);
                    holder.good_image = (ImageView) convertView.findViewById(R.id.img_good);
                    holder.tv_good_name = (AutoAdjustSizeTextView) convertView.findViewById(R.id.tv_goodName);
                    holder.layout_bottom = (LinearLayout) convertView.findViewById(R.id.layout_bottom);
                }
                holder.tv_good_price = (TextView) convertView.findViewById(R.id.tv_goodPrice);
//                holder.good_name = (TextView) convertView.findViewById(R.id.goodName);
//                holder.good_price = (TextView) convertView.findViewById(R.id.goodPrice);
//                holder.layout_center = (LinearLayout) convertView.findViewById(R.id.layout_center);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            //判断图片url是否为空，为空就设置iv背景色
//            Log.i("image_url", "image_url: "+new URL().host() + goodsBeens.get(position)
//                    .getImage());
//            if (goodsBeens.get(position).getImage() != null) {

            if (!wutu) {
                Glide.with(context).load(new URL().host() + goodsBeens.get(position)
                        .getImage()).placeholder(R.drawable.noimageshow).dontAnimate().into(holder.good_image);
                holder.tv_good_name.setText(goodsBeens.get(position).getName());
            } else {
                holder.tv_good_name_wutu.setText(goodsBeens.get(position).getName());
            }
           // holder.tv_good_name.setText("面包店分设定夫人地方");
           // holder.tv_good_price.setText("￥100.0");
            holder.tv_good_price.setText(String.format("￥%s", String.valueOf(goodsBeens.get(position).getPrice())));
//            } else {
//                holder.layout_center.setVisibility(View.VISIBLE);
//                holder.layout_bottom.setVisibility(View.GONE);
//                holder.good_name.setText(goodsBeens.get(position).getName());
//                holder.good_price.setText(String.format("￥%s", String.valueOf(goodsBeens.get(position).getPrice())));
//                holder.good_image.setBackgroundColor(Color.parseColor(goodsBeens.get(position).getColorCodeShow()));
//            }
            return convertView;
        }

        class ViewHolder {
            ImageView good_image;
            AutoAdjustSizeTextView tv_good_name;
            TextView tv_good_name_wutu;
            TextView tv_good_price;
            TextView good_name;
            TextView good_price;
            LinearLayout layout_center;
            LinearLayout layout_bottom;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
    //获取无图模式设置
    private boolean getWuTu() {
        SharedPreferences sp = context.getSharedPreferences("wutu", Context.MODE_PRIVATE);
        return sp.getBoolean("wutuModle", false);
    }
}