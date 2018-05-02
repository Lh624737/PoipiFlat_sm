package com.pospi.dto;

import java.io.Serializable;

public class ValueCardDto implements Serializable {


    /**
     * cardNo : 80000117
     * cardUid : -1127329884
     * cardType : 1
     * shop : null
     * cardStatus : 0
     * cardAmount : 0.0
     * lastChangeAmount : 0.0
     * lastChangeTime : 2016-08-09T09:50:45
     * enableTime : 2016-08-09T09:50:49
     * createTime : 2016-08-09T09:50:56
     * correctingTime : 2016-08-09T09:51:00
     * note : 11111
     * typeName : 普通卡
     * typeNote :
     * action : 1,1,1,1
     * mortgageAmount : 10.0
     */

    private String cardNo;
    private String cardUid;
    private int cardType;
    private Object shop;
    private String cardStatus;
    //卡余额
    private double cardAmount;
    private double lastChangeAmount;
    private String lastChangeTime;
    private String enableTime;
    private String createTime;
    private String correctingTime;
    private String note;
    private String typeName;
    private String typeNote;
    private String action;
    private double mortgageAmount;
    private boolean canConsume;
    private boolean canRefund;

    public boolean isCanConsume() {
        return canConsume;
    }

    public void setCanConsume(boolean canConsume) {
        this.canConsume = canConsume;
    }

    public boolean isCanRefund() {
        return canRefund;
    }

    public void setCanRefund(boolean canRefund) {
        this.canRefund = canRefund;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getCardUid() {
        return cardUid;
    }

    public void setCardUid(String cardUid) {
        this.cardUid = cardUid;
    }

    public int getCardType() {
        return cardType;
    }

    public void setCardType(int cardType) {
        this.cardType = cardType;
    }

    public Object getShop() {
        return shop;
    }

    public void setShop(Object shop) {
        this.shop = shop;
    }

    public String getCardStatus() {
        return cardStatus;
    }

    public void setCardStatus(String cardStatus) {
        this.cardStatus = cardStatus;
    }

    public double getCardAmount() {
        return cardAmount;
    }

    public void setCardAmount(double cardAmount) {
        this.cardAmount = cardAmount;
    }

    public double getLastChangeAmount() {
        return lastChangeAmount;
    }

    public void setLastChangeAmount(double lastChangeAmount) {
        this.lastChangeAmount = lastChangeAmount;
    }

    public String getLastChangeTime() {
        return lastChangeTime;
    }

    public void setLastChangeTime(String lastChangeTime) {
        this.lastChangeTime = lastChangeTime;
    }

    public String getEnableTime() {
        return enableTime;
    }

    public void setEnableTime(String enableTime) {
        this.enableTime = enableTime;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCorrectingTime() {
        return correctingTime;
    }

    public void setCorrectingTime(String correctingTime) {
        this.correctingTime = correctingTime;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeNote() {
        return typeNote;
    }

    public void setTypeNote(String typeNote) {
        this.typeNote = typeNote;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public double getMortgageAmount() {
        return mortgageAmount;
    }

    public void setMortgageAmount(double mortgageAmount) {
        this.mortgageAmount = mortgageAmount;
    }
}
