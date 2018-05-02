package com.pospi.dto;

/**
 * Created by Qiyan on 2016/6/29.
 */
public class Tabledto {
    private String sid;
    private String uid;
    private double width;
    private double x;
    private double y;
    private double height;
    private String name;
    private String sectionId;
    private int tableType;
    private boolean IsDel;
    private String ShopId;
    private int status;


    public boolean isDel() {
        return IsDel;
    }

    public void setDel(boolean del) {
        IsDel = del;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSectionId() {
        return sectionId;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }

    public int getTableType() {
        return tableType;
    }

    public void setTableType(int tableType) {
        this.tableType = tableType;
    }

    public String getShopId() {
        return ShopId;
    }

    public void setShopId(String shopId) {
        this.ShopId = shopId;
    }
}
