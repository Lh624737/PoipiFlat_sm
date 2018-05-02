package com.pospi.dto;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by acer on 2018/4/13.
 */
@Entity
public class Tablebeen {
    @Id(autoincrement = true)
    private Long id;
    private String tId;
    private String number;//桌号
    private int status;//状态
    private String eatingNumber;//就餐人数
    private String personName;//客户姓名

    @Generated(hash = 1222778747)
    public Tablebeen(Long id, String tId, String number, int status,
            String eatingNumber, String personName) {
        this.id = id;
        this.tId = tId;
        this.number = number;
        this.status = status;
        this.eatingNumber = eatingNumber;
        this.personName = personName;
    }

    @Generated(hash = 1640189935)
    public Tablebeen() {
    }

    public String getEatingNumber() {
        return eatingNumber;
    }

    public void setEatingNumber(String eatingNumber) {
        this.eatingNumber = eatingNumber;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTId() {
        return this.tId;
    }

    public void setTId(String tId) {
        this.tId = tId;
    }
}
