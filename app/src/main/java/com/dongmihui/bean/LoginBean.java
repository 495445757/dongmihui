package com.dongmihui.bean;

/**
 * Created by Administrator on 2016/9/29.
 */

public class LoginBean {


    /**
     * id : 3
     * account : 15237164692
     * gender : 张飞飞
     * userName : dmh_227468872
     * corporName : 测试公司
     * sex : 保密
     * pwd : e10adc3949ba59abbe56e057f20f883e
     * jobs :
     * desc : dsfs
     * jifen : 122
     * isRememberMe : true
     * province : 河南
     * city : 郑州
     * district : 金水
     * portrait : http://192.168.2.3/uploads/avatar/57e897e21b944.jpeg
     */

    private UserBean user;
    /**
     * errorMessage : 登陆成功
     * errorCode : 1
     */

    private MsgBean msg;
    /**
     * user : {"id":3,"account":"15237164692","gender":"张飞飞","userName":"dmh_227468872","corporName":"测试公司","sex":"保密","pwd":"e10adc3949ba59abbe56e057f20f883e","jobs":"","desc":"dsfs","jifen":122,"isRememberMe":true,"province":"河南","city":"郑州","district":"金水","portrait":"http://192.168.2.3/uploads/avatar/57e897e21b944.jpeg"}
     * msg : {"errorMessage":"登陆成功","errorCode":"1"}
     * code : 1
     */

    private int code;

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public MsgBean getMsg() {
        return msg;
    }

    public void setMsg(MsgBean msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public static class UserBean {
        private int id;
        private String account;
        private String gender;
        private String userName;
        private String corporName;
        private String sex;
        private String pwd;
        private String jobs;
        private String desc;
        private int jifen;
        private boolean isRememberMe;
        private String province;
        private String city;
        private String district;
        private String portrait;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getCorporName() {
            return corporName;
        }

        public void setCorporName(String corporName) {
            this.corporName = corporName;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getPwd() {
            return pwd;
        }

        public void setPwd(String pwd) {
            this.pwd = pwd;
        }

        public String getJobs() {
            return jobs;
        }

        public void setJobs(String jobs) {
            this.jobs = jobs;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public int getJifen() {
            return jifen;
        }

        public void setJifen(int jifen) {
            this.jifen = jifen;
        }

        public boolean isIsRememberMe() {
            return isRememberMe;
        }

        public void setIsRememberMe(boolean isRememberMe) {
            this.isRememberMe = isRememberMe;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getDistrict() {
            return district;
        }

        public void setDistrict(String district) {
            this.district = district;
        }

        public String getPortrait() {
            return portrait;
        }

        public void setPortrait(String portrait) {
            this.portrait = portrait;
        }
    }

    public static class MsgBean {
        private String errorMessage;
        private String errorCode;

        public String getErrorMessage() {
            return errorMessage;
        }

        public void setErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
        }

        public String getErrorCode() {
            return errorCode;
        }

        public void setErrorCode(String errorCode) {
            this.errorCode = errorCode;
        }
    }
}
