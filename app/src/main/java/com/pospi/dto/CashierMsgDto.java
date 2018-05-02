package com.pospi.dto;

/**
 * Created by Qiyan on 2016/4/13.
 */
public class CashierMsgDto {


    private String name;//收银员的名字
    private String number;//收银员的编号

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getIsAdmin() {
        return IsAdmin;
    }

    public void setIsAdmin(int isAdmin) {
        IsAdmin = isAdmin;
    }

    public boolean isdel() {
        return Isdel;
    }

    public void setIsdel(boolean isdel) {
        Isdel = isdel;
    }

    public String getShopId() {
        return ShopId;
    }

    public void setShopId(String shopId) {
        ShopId = shopId;
    }

    public boolean isAdmin_bool() {
        return IsAdmin_bool;
    }

    public void setIsAdmin_bool(boolean isAdmin_bool) {
        IsAdmin_bool = isAdmin_bool;
    }

    public String getConfirm() {
        return Confirm;
    }

    public void setConfirm(String confirm) {
        Confirm = confirm;
    }

    private String sid;
    private String pwd;
    private String phone;
    private int IsAdmin;
    private boolean Isdel;
    private String ShopId;
    private boolean IsAdmin_bool;
    private String Confirm;

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    private String Uid;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
