package com.pospi.dto;

/**
 *  * 做法组javabean
 *  * @FileName:com.pospi.dto.ModifiedGroupDto.java
 *  * @author: myName
 *  * @date: 2017-03-28 11:32
 *  * @version V1.0 <描述当前版本功能>
 *  
 */
public class ModifiedGroupDto {
    private static final String TAG = "ModifiedGroupDto";
    /**
     * Sid : 3a551ef3987b971850837590f54a6bda
     * Uid : 9eaf92ae8ac07a50ca9d46d30318cb66
     * Name : 辣椒
     * CreateTime : 2017-03-27 10:30:31
     * option : 0
     * orderBy : 0
     * option_bool : false
     */

    private String Sid;
    private String Uid;
    private String Name;
    private String CreateTime;
    private String option;
    private String orderBy;
    private boolean option_bool;

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

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String CreateTime) {
        this.CreateTime = CreateTime;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public boolean isOption_bool() {
        return option_bool;
    }

    public void setOption_bool(boolean option_bool) {
        this.option_bool = option_bool;
    }
}
