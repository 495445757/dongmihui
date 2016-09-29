package com.dongmihui.bean;


/**
 * Created by Android on 2016/9/28.
 */
public class MemberBean  {


    public int code;
    public String msg;
    /**
     * id : 3
     * jifen : 122
     * userName : 张飞飞
     * sex : 保密
     * jobs :
     * avatar : http://192.168.2.3/uploads/avatar/57e897e21b944.jpeg
     * province : 2
     * city : 223
     * district : 22222
     * desc : dsfs
     */

    public ResultBean result;

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public ResultBean getResult() {
        return result;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "MemberBean{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", result=" + result +
                '}';
    }


    public static class ResultBean {
        public int id;
        public int jifen;
        public String userName;
        public String sex;
        public String jobs;
        public String avatar;

        public String home;

        public String province;
        public String city;
        public String district;

        public String desc;

        public String getAvatar() {
            return avatar;
        }

        public String getCity() {
            return city;
        }

        public String getDesc() {
            return desc;
        }

        public String getDistrict() {
            return district;
        }

        public int getId() {
            return id;
        }

        public int getJifen() {
            return jifen;
        }

        public String getJobs() {
            return jobs;
        }


        public String getProvince() {
            return province;
        }

        public String getSex() {
            return sex;
        }

        public String getUserName() {
            return userName;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }


        public void setCity(String city) {
            this.city = city;
        }


        public void setDesc(String desc) {
            this.desc = desc;
        }

        public void setDistrict(String district) {
            this.district = district;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setJifen(int jifen) {
            this.jifen = jifen;
        }

        public void setJobs(String jobs) {
            this.jobs = jobs;
        }


        public void setProvince(String province) {
            this.province = province;
        }


        public void setSex(String sex) {
            this.sex = sex;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getHome() {
            return home;
        }

        public void setHome(String home) {
            this.home = home;
        }
    }
}

