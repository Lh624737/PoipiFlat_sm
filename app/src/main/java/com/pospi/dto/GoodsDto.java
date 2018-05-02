package com.pospi.dto;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;

import java.io.Serializable;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Qiyan on 2016/5/11.
 */
@Entity
public class GoodsDto implements Serializable {
    /**
     * Sid : 04e1da31a4784c938942f9195cc72e40
     * UId : f17851cbccb84ce0b55ac6f464d9e13f
     * category_sid : 81d7a171f123412e8aef109cdb76ba9a
     * colorCode : 0xFF646464
     * group_sid : null
     * image : null
     * name : test
     * price : 0.0
     * CostPrice : 0.0
     * unit : 个
     * mainPrinterSid : null
     * backPrinterSid : null
     * Code : null
     * createTime : 2015-08-31T15:08:02
     * orderBy : 9
     * isHide : false
     * IsDel : false
     * Code_bm :
     * specification : null
     * valuationType : 0
     * genre : 0
     * setFlag : false
     * setPids : null
     * setOldPrice : null
     * colorCodeShow : #646464
     */
    @Id(autoincrement = true)
    private Long id;
    @Transient
    private int num;//添加商品的数量
    @Transient
    private double discount;

    private String Sid;
    private String UId;
    private String category_sid;
    private String colorCode;
    private String group_sid;
    private String image;
    private String name;
    private double price;
    private double CostPrice;
    private String unit;
    private String mainPrinterSid;
    private String backPrinterSid;
    private String Code;
    private String createTime;
    private int orderBy;
    private boolean isHide;
    @Transient
    private boolean IsDel;
    @Transient
    private String Code_bm;
    @Transient
    private String specification;
    @Transient
    private int valuationType;
    @Transient
    private int genre;
    @Transient
    private boolean setFlag;
    @Transient
    private String setPids;
    @Transient
    private String setOldPrice;

    private String colorCodeShow;

    @Transient
    private String modified;
    //订单单个商品的sid
    @Transient
    private String goodsId;
    public String getColorCodeShow() {
        return this.colorCodeShow;
    }
    public void setColorCodeShow(String colorCodeShow) {
        this.colorCodeShow = colorCodeShow;
    }
    public boolean getIsHide() {
        return this.isHide;
    }
    public void setIsHide(boolean isHide) {
        this.isHide = isHide;
    }
    public int getOrderBy() {
        return this.orderBy;
    }
    public void setOrderBy(int orderBy) {
        this.orderBy = orderBy;
    }
    public String getCreateTime() {
        return this.createTime;
    }
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
    public String getCode() {
        return this.Code;
    }
    public void setCode(String Code) {
        this.Code = Code;
    }
    public String getBackPrinterSid() {
        return this.backPrinterSid;
    }
    public void setBackPrinterSid(String backPrinterSid) {
        this.backPrinterSid = backPrinterSid;
    }
    public String getMainPrinterSid() {
        return this.mainPrinterSid;
    }
    public void setMainPrinterSid(String mainPrinterSid) {
        this.mainPrinterSid = mainPrinterSid;
    }
    public String getUnit() {
        return this.unit;
    }
    public void setUnit(String unit) {
        this.unit = unit;
    }
    public double getCostPrice() {
        return this.CostPrice;
    }
    public void setCostPrice(double CostPrice) {
        this.CostPrice = CostPrice;
    }
    public double getPrice() {
        return this.price;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getImage() {
        return this.image;
    }
    public void setImage(String image) {
        this.image = image;
    }
    public String getGroup_sid() {
        return this.group_sid;
    }
    public void setGroup_sid(String group_sid) {
        this.group_sid = group_sid;
    }
    public String getColorCode() {
        return this.colorCode;
    }
    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }
    public String getCategory_sid() {
        return this.category_sid;
    }
    public void setCategory_sid(String category_sid) {
        this.category_sid = category_sid;
    }
    public String getUId() {
        return this.UId;
    }
    public void setUId(String UId) {
        this.UId = UId;
    }
    public String getSid() {
        return this.Sid;
    }
    public void setSid(String Sid) {
        this.Sid = Sid;
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    @Generated(hash = 11315792)
    public GoodsDto(Long id, String Sid, String UId, String category_sid,
            String colorCode, String group_sid, String image, String name,
            double price, double CostPrice, String unit, String mainPrinterSid,
            String backPrinterSid, String Code, String createTime, int orderBy,
            boolean isHide, String colorCodeShow) {
        this.id = id;
        this.Sid = Sid;
        this.UId = UId;
        this.category_sid = category_sid;
        this.colorCode = colorCode;
        this.group_sid = group_sid;
        this.image = image;
        this.name = name;
        this.price = price;
        this.CostPrice = CostPrice;
        this.unit = unit;
        this.mainPrinterSid = mainPrinterSid;
        this.backPrinterSid = backPrinterSid;
        this.Code = Code;
        this.createTime = createTime;
        this.orderBy = orderBy;
        this.isHide = isHide;
        this.colorCodeShow = colorCodeShow;
    }
    @Generated(hash = 323937848)
    public GoodsDto() {
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

//    public String getSid() {
//        return Sid;
//    }
//
//    public void setSid(String Sid) {
//        this.Sid = Sid;
//    }
//
//    public String getUId() {
//        return UId;
//    }
//
//    public void setUId(String UId) {
//        this.UId = UId;
//    }
//
//    public String getCategory_sid() {
//        return category_sid;
//    }
//
//    public void setCategory_sid(String category_sid) {
//        this.category_sid = category_sid;
//    }
//
//    public String getColorCode() {
//        return colorCode;
//    }
//
//    public void setColorCode(String colorCode) {
//        this.colorCode = colorCode;
//    }
//
//    public String getGroup_sid() {
//        return group_sid;
//    }
//
//    public void setGroup_sid(String group_sid) {
//        this.group_sid = group_sid;
//    }
//
//    public String getImage() {
//        return image;
//    }
//
//    public void setImage(String image) {
//        this.image = image;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public double getPrice() {
//        return price;
//    }
//
//    public void setPrice(double price) {
//        this.price = price;
//    }
//
//    public double getCostPrice() {
//        return CostPrice;
//    }
//
//    public void setCostPrice(double CostPrice) {
//        this.CostPrice = CostPrice;
//    }
//
//    public String getUnit() {
//        return unit;
//    }
//
//    public void setUnit(String unit) {
//        this.unit = unit;
//    }
//
//    public String getMainPrinterSid() {
//        return mainPrinterSid;
//    }
//
//    public void setMainPrinterSid(String mainPrinterSid) {
//        this.mainPrinterSid = mainPrinterSid;
//    }
//
//    public String getBackPrinterSid() {
//        return backPrinterSid;
//    }
//
//    public void setBackPrinterSid(String backPrinterSid) {
//        this.backPrinterSid = backPrinterSid;
//    }
//
//    public String getCode() {
//        return Code;
//    }
//
//    public void setCode(String Code) {
//        this.Code = Code;
//    }
//
//    public String getCreateTime() {
//        return createTime;
//    }
//
//    public void setCreateTime(String createTime) {
//        this.createTime = createTime;
//    }
//
//    public int getOrderBy() {
//        return orderBy;
//    }

//    public void setOrderBy(int orderBy) {
//        this.orderBy = orderBy;
//    }

    public boolean isIsHide() {
        return isHide;
    }

//    public void setIsHide(boolean isHide) {
//        this.isHide = isHide;
//    }

    public boolean isIsDel() {
        return IsDel;
    }

    public void setIsDel(boolean IsDel) {
        this.IsDel = IsDel;
    }

    public String getCode_bm() {
        return Code_bm;
    }

    public void setCode_bm(String Code_bm) {
        this.Code_bm = Code_bm;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public int getValuationType() {
        return valuationType;
    }

    public void setValuationType(int valuationType) {
        this.valuationType = valuationType;
    }

    public int getGenre() {
        return genre;
    }

    public void setGenre(int genre) {
        this.genre = genre;
    }

    public boolean isSetFlag() {
        return setFlag;
    }

    public void setSetFlag(boolean setFlag) {
        this.setFlag = setFlag;
    }

    public String getSetPids() {
        return setPids;
    }

    public void setSetPids(String setPids) {
        this.setPids = setPids;
    }

    public String getSetOldPrice() {
        return setOldPrice;
    }

    public void setSetOldPrice(String setOldPrice) {
        this.setOldPrice = setOldPrice;
    }

//    public String getColorCodeShow() {
//        return colorCodeShow;
//    }
//
//    public void setColorCodeShow(String colorCodeShow) {
//        this.colorCodeShow = colorCodeShow;
//    }
}
