package com.pospi.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Qiyan on 2016/5/25.
 */
public class OrderDto implements Serializable {
    private static final long serialVersionUID = -7060210544600464481L;
    private String payway;
    private String time;
    private String ss_money;
    private String ys_money;
    private String zl_money;
    private String order_info;
    private String cashiername;//收银员的信息
    private String shop_id;//店铺的id
    private String detailTime;
    private int orderType;//订单的类型，销售还是退货
    private String maxNo;//小票号
    private String checkoutTime;
    private int hasReturnGoods;
    private String out_trade_no;
    private String payReturn;
    private int ifFinishOrder;//是否是已经完成的订单
    private int serialNumber;//序列号，即日流水号
    private String tableNumber;//桌台号
    private int status;//就餐人数//改为payWayDto的status
    private String orderId;
    private int upLoadServer;
    private int upLoadERP;
    private String orderNo;
    //桌号id
    private String tableId;
    //订单sid
    private String orderSid;

    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    private List<PayDetails> payList = new ArrayList<>();

    public void add(PayDetails payDetails) {
        payList.add(payDetails);
    }
    public String getOrderSid() {
        return orderSid;
    }

    public void setOrderSid(String orderSid) {
        this.orderSid = orderSid;
    }

    public String getMiYaNumber() {
        return miYaNumber;
    }

    public void setMiYaNumber(String miYaNumber) {
        this.miYaNumber = miYaNumber;
    }

    private String miYaNumber;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public int getUpLoadERP() {
        return upLoadERP;
    }

    public void setUpLoadERP(int upLoadERP) {
        this.upLoadERP = upLoadERP;
    }

    public int getUpLoadServer() {
        return upLoadServer;
    }

    public void setUpLoadServer(int upLoadServer) {
        this.upLoadServer = upLoadServer;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public int getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(int serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(String tableNumber) {
        this.tableNumber = tableNumber;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getIfFinishOrder() {
        return ifFinishOrder;
    }

    public void setIfFinishOrder(int ifFinishOrder) {
        this.ifFinishOrder = ifFinishOrder;
    }

    public String getPayReturn() {
        return payReturn;
    }

    public void setPayReturn(String payReturn) {
        this.payReturn = payReturn;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public int getHasReturnGoods() {
        return hasReturnGoods;
    }

    public void setHasReturnGoods(int hasReturnGoods) {
        this.hasReturnGoods = hasReturnGoods;
    }

    public String getCheckoutTime() {
        return checkoutTime;
    }

    public void setCheckoutTime(String checkoutTime) {
        this.checkoutTime = checkoutTime;
    }

    public String getMaxNo() {
        return maxNo;
    }

    public void setMaxNo(String maxNo) {
        this.maxNo = maxNo;
    }

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }

    public String getDetailTime() {
        return detailTime;
    }

    public void setDetailTime(String detailTime) {
        this.detailTime = detailTime;
    }

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }

    public String getCashiername() {
        return cashiername;
    }

    public void setCashiername(String cashiername) {
        this.cashiername = cashiername;
    }

    public String getPayway() {
        return payway;
    }

    public void setPayway(String payway) {
        this.payway = payway;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSs_money() {
        return ss_money;
    }

    public void setSs_money(String ss_money) {
        this.ss_money = ss_money;
    }

    public String getYs_money() {
        return ys_money;
    }

    public void setYs_money(String ys_money) {
        this.ys_money = ys_money;
    }

    public String getZl_money() {
        return zl_money;
    }

    public void setZl_money(String zl_money) {
        this.zl_money = zl_money;
    }

    public String getOrder_info() {
        return order_info;
    }

    public void setOrder_info(String order_info) {
        this.order_info = order_info;
    }
}
