package com.pospi.dto;

/**
 * Created by Qiyan on 2016/4/8.
 */
public class LoginReturnDto {
    private int result;
    private String value;
    private String message;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
