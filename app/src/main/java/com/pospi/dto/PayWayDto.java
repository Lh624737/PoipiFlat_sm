package com.pospi.dto;

/**
 * Created by Qiyan on 2016/7/22.
 */
public class PayWayDto {
    private String colorCodeShow;
    private String Sid;
    private String UId;
    private String Name;
    private String colorCode;
    private String orderBy;
    private int payType1;
    private int status;
    private String AliPayUrl;
    private String payWayId;


    public String getPayWayId() {
        return payWayId;
    }

    public void setPayWayId(String payWayId) {
        this.payWayId = payWayId;
    }


    public String getColorCodeShow() {
        return colorCodeShow;
    }

    public void setColorCodeShow(String colorCodeShow) {
        this.colorCodeShow = colorCodeShow;
    }

    public String getSid() {
        return Sid;
    }

    public void setSid(String sid) {
        Sid = sid;
    }

    public String getUId() {
        return UId;
    }

    public void setUId(String UId) {
        this.UId = UId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getColorCode() {
        return colorCode;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public int getPayType1() {
        return payType1;
    }

    public void setPayType1(int payType1) {
        this.payType1 = payType1;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getAliPayUrl() {
        return AliPayUrl;
    }

    public void setAliPayUrl(String aliPayUrl) {
        AliPayUrl = aliPayUrl;
    }
}
