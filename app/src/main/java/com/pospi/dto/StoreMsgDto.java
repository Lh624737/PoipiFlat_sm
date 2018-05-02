package com.pospi.dto;

/**
 * Created by Qiyan on 2016/4/12.
 */
public class StoreMsgDto {
    private String Id;
    private String Name;
    private String Address;
    private String Phone;
    private String Contacts;
    /**
     * 储值卡打几折(0.95)
     */
    private Float Discount;

    public Float getDiscount() {
        return Discount;
    }

    public void setDiscount(Float discount) {
        Discount = discount;
    }


    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getContacts() {
        return Contacts;
    }

    public void setContacts(String contacts) {
        Contacts = contacts;
    }
}
