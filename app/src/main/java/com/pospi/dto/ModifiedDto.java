package com.pospi.dto;

import java.io.Serializable;

/**
 * Created by Qiyan on 2016/7/26.
 */
public class ModifiedDto implements Serializable{

    /**
     * Sid : 4e6f36d35d9d45c104c7658ca01c29e8
     * Uid : 9eaf92ae8ac07a50ca9d46d30318cb66
     * Name : 多辣
     * GroupSid : 3a551ef3987b971850837590f54a6bda
     * Price : 0
     * CostPrice : 0
     * unit :
     * image : /upload/image/2017-03-27/58d879779c531.gif
     * colorCode : 0xFF8000FF
     * CreateTime : 2017-03-27 10:31:19
     * orderBy : 0
     * colorCodeShow : #8000FF
     */

    private String Sid;
    private String Uid;
    private String Name;
    private String GroupSid;
    private String Price;
    private String CostPrice;
    private String unit;
    private String image;
    private String colorCode;
    private String CreateTime;
    private String orderBy;
    private String colorCodeShow;

    public String getSid() {
        return Sid;
    }

    public void setSid(String Sid) {
        this.Sid = Sid;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String Uid) {
        this.Uid = Uid;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getGroupSid() {
        return GroupSid;
    }

    public void setGroupSid(String GroupSid) {
        this.GroupSid = GroupSid;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String Price) {
        this.Price = Price;
    }

    public String getCostPrice() {
        return CostPrice;
    }

    public void setCostPrice(String CostPrice) {
        this.CostPrice = CostPrice;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getColorCode() {
        return colorCode;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String CreateTime) {
        this.CreateTime = CreateTime;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getColorCodeShow() {
        return colorCodeShow;
    }

    public void setColorCodeShow(String colorCodeShow) {
        this.colorCodeShow = colorCodeShow;
    }
}
