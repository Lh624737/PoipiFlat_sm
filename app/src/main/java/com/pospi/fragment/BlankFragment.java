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
import com.lany.sp.SPHelper;
import com.pospi.dto.GoodsDto;
import com.pospi.pai.yunpos.R;
import com.pospi.pai.yunpos.login.Constant;
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
            wutu = getWuTu();
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(context).inflate(R.layout.gv_goods_item, null);
                holder.good_image = (ImageView) convertView.findViewById(R.id.img_good);
                holder.tv_good_name = (TextView) convertView.findViewById(R.id.tv_goodName);
                holder.tv_code = (TextView) convertView.findViewById(R.id.tv_code);
                holder.tv_name = convertView.findViewById(R.id.tv_name);
                holder.tv_goodPrice = convertView.findViewById(R.id.tv_goodPrice);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            if (!wutu) {
                Glide.with(context).load(new URL().host() + goodsBeens.get(position)
                        .getImage()).placeholder(R.drawable.image_none).dontAnimate().into(holder.good_image);
                holder.good_image.setVisibility(View.VISIBLE);
                holder.tv_name.setVisibility(View.GONE);
            } else {
                holder.good_image.setVisibility(View.GONE);
                holder.tv_name.setVisibility(View.VISIBLE);
                holder.tv_name.setText(goodsBeens.get(position).getName());
            }
            holder.tv_code.setText(goodsBeens.get(position).getCode());
            holder.tv_good_name.setText(goodsBeens.get(position).getName());
            holder.tv_goodPrice.setText(String.format("￥%s",
                    String.valueOf(goodsBeens.get(position).getPrice())) + "/" + goodsBeens.get(position).getUnit());
            return convertView;
        }

        class ViewHolder {
            ImageView good_image;
            TextView tv_good_name;
            TextView tv_goodPrice;
            TextView tv_name;
            TextView tv_code;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    //获取无图模式设置
    private boolean getWuTu() {
        return SPHelper.getInstance().getBoolean(Constant.MODE_IMG);
    }
}