package com.dongmihui.bean;

import java.io.Serializable;

///*用户头像信息*/
public class Avatar implements Serializable {
    public String mongoId;
    public String picUrl;
    public String smallPicUrl;

    public String getMongoId() {
        return mongoId;
    }

    public void setMongoId(String mongoId) {
        this.mongoId = mongoId;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getSmallPicUrl() {
        return smallPicUrl;
    }

    public void setSmallPicUrl(String smallPicUrl) {
        this.smallPicUrl = smallPicUrl;
    }
}
