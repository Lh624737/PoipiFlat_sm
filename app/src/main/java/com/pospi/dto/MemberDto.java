package com.pospi.dto;

/**
 * Created by Qiyan on 2016/6/7.
 */
public class MemberDto {
    private String name;//名字
    private String number;//卡号
    private String tel;//电话
    private String address;
    private double score;
    private String getcardtime;
    private int bornyear;
    private int bornmonth;
    private int bornday;


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

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getGetcardtime() {
        return getcardtime;
    }

    public void setGetcardtime(String getcardtime) {
        this.getcardtime = getcardtime;
    }

    public int getBornyear() {
        return bornyear;
    }

    public void setBornyear(int bornyear) {
        this.bornyear = bornyear;
    }

    public int getBornmonth() {
        return bornmonth;
    }

    public void setBornmonth(int bornmonth) {
        this.bornmonth = bornmonth;
    }

    public int getBornday() {
        return bornday;
    }

    public void setBornday(int bornday) {
        this.bornday = bornday;
    }
}
