package com.pospi.dto;

import java.io.Serializable;

/**
 * Created by acer on 2018/1/26.
 */

public class Remark implements Serializable{
    private String name;
    private boolean isChoose;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isChoose() {
        return isChoose;
    }

    public void setChoose(boolean choose) {
        isChoose = choose;
    }
}
