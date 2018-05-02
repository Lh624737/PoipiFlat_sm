package com.pospi.dto;

/**
 * Created by Qiyan on 2016/5/30.
 */
public class CashierCashDto {
    private String cashierName;
    /**
     * 销售笔数
     */
    private int num;
    /**
     * 销售额
     */
    private double sale;
    /**
     * 折扣额
     */
    private double discount;
    /**
     * 销售收入
     */
    private double in;
    /**
     * 收银缴款
     */
    private double payment;
    /**
     * 差额
     */
    private double gap;

    public String getCashierName() {
        return cashierName;
    }

    public void setCashierName(String cashierName) {
        this.cashierName = cashierName;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public double getSale() {
        return sale;
    }

    public void setSale(double sale) {
        this.sale = sale;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getIn() {
        return in;
    }

    public void setIn(double in) {
        this.in = in;
    }

    public double getPayment() {
        return payment;
    }

    public void setPayment(double payment) {
        this.payment = payment;
    }

    public double getGap() {
        return gap;
    }

    public void setGap(double gap) {
        this.gap = gap;
    }
}
