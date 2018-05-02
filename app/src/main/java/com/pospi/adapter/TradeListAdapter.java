package com.pospi.adapter;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pospi.dto.GoodsDto;
import com.pospi.dto.OrderDto;
import com.pospi.pai.pospiflat.R;
import com.pospi.util.DoubleSave;
import com.pospi.util.Sava_list_To_Json;
import com.pospi.util.UpLoadServer;
import com.pospi.util.UploadERP;
import com.pospi.util.constant.PayWay;
import com.pospi.util.constant.URL;

import java.util.List;

/**
 * Created by Qiyan on 2016/6/3.
 */
public class TradeListAdapter extends BaseAdapter {
    private Context context;
    private List<OrderDto> dtos;
    private double card_discount;

    public TradeListAdapter(Context context, List<OrderDto> dtos) {
        this.context = context;
        this.dtos = dtos;
        card_discount = context.getSharedPreferences("StoreMessage", Context.MODE_PRIVATE).getFloat("Discount", 1);
    }

    @Override
    public int getCount() {
        return dtos.size();
    }

    @Override
    public Object getItem(int position) {
        return dtos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.tradelistitem, null);
            holder = new ViewHolder();
            holder.no = (TextView) convertView.findViewById(R.id.maxno);
            holder.orderType = (TextView) convertView.findViewById(R.id.ordertype);
//            holder.table=(TextView)convertView.findViewById(R.id.table);
            holder.num = (TextView) convertView.findViewById(R.id.goodsnum);
            holder.ys = (TextView) convertView.findViewById(R.id.ys);
            holder.ss = (TextView) convertView.findViewById(R.id.ss);
            holder.zl = (TextView) convertView.findViewById(R.id.zl);
            holder.time = (TextView) convertView.findViewById(R.id.paytime);
            holder.upload = (TextView) convertView.findViewById(R.id.upload);
            holder.erp = (TextView) convertView.findViewById(R.id.erp);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        OrderDto dto = dtos.get(position);
        holder.no.setText(String.valueOf(dto.getMaxNo()));
        if (dto.getOrderType() == URL.ORDERTYPE_SALE) {
            holder.orderType.setText("销售");
            holder.orderType.setTextColor(context.getResources().getColor(R.color.black));
        } else if (dto.getOrderType() == URL.ORDERTYPE_REFUND) {
            holder.orderType.setText("退货");
            holder.orderType.setTextColor(context.getResources().getColor(R.color.red));
        }
        if(dto.getPayway().equals(PayWay.CZK)){
            holder.ss.setText(String.valueOf(DoubleSave.doubleSaveTwo(Double.parseDouble(dto.getSs_money())*card_discount)));
            holder.ys.setText(String.valueOf(DoubleSave.doubleSaveTwo(Double.parseDouble(dto.getYs_money())*card_discount)));
        }else {
            holder.ys.setText(dto.getYs_money());
            holder.ss.setText(dto.getSs_money());
        }
//        holder.table.setText("无");
        List<GoodsDto> goodsDtos = Sava_list_To_Json.changeToList(dto.getOrder_info());
        int num = 0;
        for (int i = 0; i < goodsDtos.size(); i++) {
            num += goodsDtos.get(i).getNum();
        }
        holder.num.setText(String.valueOf(num));
        holder.zl.setText(dto.getZl_money());
        holder.time.setText(dto.getCheckoutTime());
        if (dto.getUpLoadServer() == UpLoadServer.hasUpLoad) {
            holder.upload.setText("已上传");
        } else if (dto.getUpLoadServer() == UpLoadServer.noUpload) {
            holder.upload.setText("未上传");
        }
//        else {
//            holder.erp.setText("--");
//        }
        if (!Build.MODEL.equals(URL.MODEL_D800)) {
            if (dto.getUpLoadERP() == UploadERP.hasUpLoad) {
                holder.erp.setText("已上传");
            } else if (dto.getUpLoadERP() == UploadERP.noUpload) {
                holder.erp.setText("未上传");
            } else {
                holder.erp.setText("--");
            }
        }

        return convertView;
    }

    class ViewHolder {
        TextView no;
        TextView orderType;
        //       TextView table;
        TextView num;
        TextView ys;
        TextView ss;
        TextView zl;
        TextView time;
        TextView upload;
        TextView erp;
    }
}
