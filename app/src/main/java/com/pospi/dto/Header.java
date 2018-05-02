package com.pospi.dto;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import java.util.Hashtable;

public class Header implements KvmSerializable {

    public String licensekey;//许可证书
    public String username;//用户名
    public String password;//密码
    public String lang;//语言
    public String pagerecords;//每页记录数
    public String pageno;//页数
    public String updatecount;//每次更新记录数
    public String messagetype;//消息类型
    public String messageid;//消息id
    public String version;//版本编号

    @Override
    public Object getProperty(int i) {
        switch (i) {
            case 0:
                return licensekey;
            case 1:
                return username;
            case 2:
                return password;
            case 3:
                return lang;
            case 4:
                return pagerecords;
            case 5:
                return pageno;
            case 6:
                return updatecount;
            case 7:
                return messagetype;
            case 8:
                return messageid;
            case 9:
                return version;
        }
        return null;
    }

    @Override
    public int getPropertyCount() {
        return 10;
    }

    @Override
    public void getPropertyInfo(int i, Hashtable hashtable, PropertyInfo propertyInfo) {
        switch (i) {
            case 0:
                propertyInfo.type = PropertyInfo.STRING_CLASS;
                propertyInfo.name = "licensekey";
                break;
            case 1:
                propertyInfo.type = PropertyInfo.STRING_CLASS;
                propertyInfo.name = "username";
                break;
            case 2:
                propertyInfo.type = PropertyInfo.STRING_CLASS;
                propertyInfo.name = "password";
                break;
            case 3:
                propertyInfo.type = PropertyInfo.STRING_CLASS;
                propertyInfo.name = "lang";
                break;
            case 4:
                propertyInfo.type = PropertyInfo.STRING_CLASS;
                propertyInfo.name = "pagerecords";
                break;
            case 5:
                propertyInfo.type = PropertyInfo.STRING_CLASS;
                propertyInfo.name = "pageno";
                break;
            case 6:
                propertyInfo.type = PropertyInfo.STRING_CLASS;
                propertyInfo.name = "updatecount";
                break;
            case 7:
                propertyInfo.type = PropertyInfo.STRING_CLASS;
                propertyInfo.name = "messagetype";
                break;
            case 8:
                propertyInfo.type = PropertyInfo.STRING_CLASS;
                propertyInfo.name = "messageid";
                break;
            case 9:
                propertyInfo.type = PropertyInfo.STRING_CLASS;
                propertyInfo.name = "version";
                break;
        }
    }

    @Override
    public void setProperty(int i, Object o) {
        switch (i) {
            case 0:
                licensekey = o.toString();
                break;
            case 1:
                username = o.toString();
                break;
            case 2:
                password = o.toString();
                break;
            case 3:
                lang = o.toString();
                break;
            case 4:
                pagerecords = o.toString();
                break;
            case 5:
                pageno = o.toString();
                break;
            case 6:
                updatecount = o.toString();
                break;
            case 7:
                messagetype = o.toString();
                break;
            case 8:
                messageid = o.toString();
                break;
            case 9:
                version = o.toString();
                break;
        }
    }


}
