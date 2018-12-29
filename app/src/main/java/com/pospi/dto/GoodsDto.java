package com.pospi.dto;

import com.pospi.util.DoubleSave;

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

    @Id(autoincrement = true)
    private Long id;
    @Transient
    private double num;//添加商品的数量
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
    private double proPrice;//促销价
    private String proType;//促销类型
    private double proDiscout;//促销折扣
    private double oldPrice;//
    private int singnum;
    private int muchnum;
    private String logic;
    private String dzc;//0称重1几份
    private double hyzk;
    private double lszk;
    private double cxzk;
    private String bzts;//保质期

    private double hyj;
    private double hyj1;
    private double hyj2;
    private double hyj3;
    private double minzkl;//限制单个商品最小折扣率0.5
    private String usejf; //1会员可以积分
    private String usezk;//1会员可以折扣
    private String plu;

    public String getBzts() {
        return bzts;
    }

    public void setBzts(String bzts) {
        this.bzts = bzts;
    }

    public String getPlu() {
        return plu;
    }

    public void setPlu(String plu) {
        this.plu = plu;
    }

    public double getHyj() {
        return hyj;
    }

    public void setHyj(double hyj) {
        this.hyj = hyj;
    }

    public double getHyj1() {
        return hyj1;
    }

    public void setHyj1(double hyj1) {
        this.hyj1 = hyj1;
    }

    public double getHyj2() {
        return hyj2;
    }

    public void setHyj2(double hyj2) {
        this.hyj2 = hyj2;
    }

    public double getHyj3() {
        return hyj3;
    }

    public void setHyj3(double hyj3) {
        this.hyj3 = hyj3;
    }

    public double getMinzkl() {
        return minzkl;
    }

    public void setMinzkl(double minzkl) {
        this.minzkl = minzkl;
    }

    public String getUsejf() {
        return usejf;
    }

    public void setUsejf(String usejf) {
        this.usejf = usejf;
    }

    public String getUsezk() {
        return usezk;
    }

    public void setUsezk(String usezk) {
        this.usezk = usezk;
    }

    public double getHyzk() {
        return hyzk;
    }

    public void setHyzk(double hyzk) {
        this.hyzk = hyzk;
    }

    public double getLszk() {
        return lszk;
    }

    public void setLszk(double lszk) {
        this.lszk = lszk;
    }

    public double getCxzk() {
        return cxzk;
    }

    public void setCxzk(double cxzk) {
        this.cxzk = cxzk;
    }

    public String getDzc() {
        return dzc;
    }

    public void setDzc(String dzc) {
        this.dzc = dzc;
    }

    public double getProPrice() {
        return proPrice;
    }

    public void setProPrice(double proPrice) {
        this.proPrice = proPrice;
    }

    public String getProType() {
        return proType;
    }

    public void setProType(String proType) {
        this.proType = proType;
    }

    public double getProDiscout() {
        return proDiscout;
    }

    public void setProDiscout(double proDiscout) {
        this.proDiscout = proDiscout;
    }

    public double getOldPrice() {
        return oldPrice;
    }

    public void setOldPrice(double oldPrice) {
        this.oldPrice = oldPrice;
    }

    public int getSingnum() {
        return singnum;
    }

    public void setSingnum(int singnum) {
        this.singnum = singnum;
    }

    public int getMuchnum() {
        return muchnum;
    }

    public void setMuchnum(int muchnum) {
        this.muchnum = muchnum;
    }

    public String getLogic() {
        return logic;
    }

    public void setLogic(String logic) {
        this.logic = logic;
    }

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
    @Generated(hash = 1565335578)
    public GoodsDto(Long id, String Sid, String UId, String category_sid,
            String colorCode, String group_sid, String image, String name,
            double price, double CostPrice, String unit, String mainPrinterSid,
            String backPrinterSid, String Code, String createTime, int orderBy,
            boolean isHide, double proPrice, String proType, double proDiscout,
            double oldPrice, int singnum, int muchnum, String logic, String dzc,
            double hyzk, double lszk, double cxzk, String bzts, double hyj,
            double hyj1, double hyj2, double hyj3, double minzkl, String usejf,
            String usezk, String plu, String colorCodeShow) {
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
        this.proPrice = proPrice;
        this.proType = proType;
        this.proDiscout = proDiscout;
        this.oldPrice = oldPrice;
        this.singnum = singnum;
        this.muchnum = muchnum;
        this.logic = logic;
        this.dzc = dzc;
        this.hyzk = hyzk;
        this.lszk = lszk;
        this.cxzk = cxzk;
        this.bzts = bzts;
        this.hyj = hyj;
        this.hyj1 = hyj1;
        this.hyj2 = hyj2;
        this.hyj3 = hyj3;
        this.minzkl = minzkl;
        this.usejf = usejf;
        this.usezk = usezk;
        this.plu = plu;
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

    public double getNum() {
        return num;
    }

    public void setNum(double num) {
        this.num = num;
    }

    public double getDiscount() {
        return DoubleSave.doubleSaveTwo(lszk + cxzk + hyzk);
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
