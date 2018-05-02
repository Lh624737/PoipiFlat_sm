package com.pospi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pospi.dto.PayWayDto;
import com.pospi.pai.pospiflat.R;
import com.pospi.util.constant.PayWay;

import java.util.List;

public class PaymentAdapter extends BaseAdapter {
    private Context context;
    private List<PayWayDto> payWayDtos;

    public PaymentAdapter(Context context, List<PayWayDto> payWayDtos) {
        this.context = context;
        this.payWayDtos = payWayDtos;
    }

    @Override
    public int getCount() {
        return payWayDtos.size();
    }

    @Override
    public Object getItem(int position) {
        return payWayDtos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_payment_item, null);
            holder = new ViewHolder();
            holder.iv_payment = (ImageView) convertView.findViewById(R.id.iv_payment);
            holder.tv_payment = (TextView) convertView.findViewById(R.id.tv_payment);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        PayWayDto payWayDto = payWayDtos.get(position);
        int payType = payWayDto.getPayType1();
        if (payType==(PayWay.CASH)) {//现金
            holder.iv_payment.setImageResource(R.drawable.icon_xianjin);
            holder.tv_payment.setText(payWayDto.getName());
        } else if (payType==(PayWay.WXPAY)) {//微信支付
            holder.iv_payment.setImageResource(R.drawable.icon_weixin);
            holder.tv_payment.setText(payWayDto.getName());
        } else if (payType==(PayWay.ALIPAY)) {//支付宝支付
            holder.iv_payment.setImageResource(R.drawable.icon_zhifubao);
            holder.tv_payment.setText(payWayDto.getName());
        } else if (payType==(PayWay.BANK_CARD)) {//银行卡
            holder.iv_payment.setImageResource(R.drawable.icon_payment_yhk);
            holder.tv_payment.setText(payWayDto.getName());
        } else if (payType==(PayWay.MIANZHI_CARD)) {//储值卡
            holder.iv_payment.setImageResource(R.drawable.icon_chuzhika1);
            holder.tv_payment.setText(payWayDto.getName());
        } else if (payType==(PayWay.OTHER)) {//其他
            holder.iv_payment.setImageResource(R.drawable.icon_other);
            holder.tv_payment.setText(payWayDto.getName());
        } else if (payType==(PayWay.CREDIT_CARD)) {//信用卡
            holder.iv_payment.setImageResource(R.drawable.icon_payment_yhk);
            holder.tv_payment.setText(payWayDto.getName());
        } else if (payType==(PayWay.CASH_GIFT)) {//礼券
            holder.iv_payment.setImageResource(R.drawable.icon_other);
            holder.tv_payment.setText(payWayDto.getName());
        } else if (payType==(PayWay.E_COUPON)) {//电子券
            holder.iv_payment.setImageResource(R.drawable.icon_chuzhika);
            holder.tv_payment.setText(payWayDto.getName());
        } else if (payType==(PayWay.ZYMPOSPAY)) {//中银mPOS
            holder.iv_payment.setImageResource(R.drawable.icon_mpos);
            holder.tv_payment.setText(payWayDto.getName());
        } else if (payType==(PayWay.VERIFONE_E355)) {//verifone E315
            holder.iv_payment.setImageResource(R.drawable.icon_e315);
            holder.tv_payment.setText(payWayDto.getName());
        }else {
            holder.iv_payment.setImageResource(R.drawable.icon_other);
            holder.tv_payment.setText(payWayDto.getName());
        }
        return convertView;
    }


    class ViewHolder {
        ImageView iv_payment;
        TextView tv_payment;
    }

}
