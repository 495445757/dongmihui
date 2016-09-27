package com.dongmihui.bean;

/**
 * Created by Administrator on 2016/9/27.
 */

public class ApiMessage<T> {


    @Override
    public String toString() {
        return "ApiMessage{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", result=" + result +
                '}';
    }

    /**
     * code : 1
     * msg : success
     * result : {"userId":2}
     */

    private int code;
    private String msg;
    /**
     * userId : 2
     */

    private T result;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public static class UserID{
        private int userId;

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }
    }

    public static class LogingCode {

        @Override
        public String toString() {
            return "LogingCode{" +
                    "id=" + id +
                    ", userName='" + userName + '\'' +
                    ", corporName='" + corporName + '\'' +
                    '}';
        }

        /**
         * id : 2
         * userName : 254138
         * corporName : 董秘荟
         * sex : 男
         * avatar : http://192.168.2.3/data/avatar/avatar.jpg
         */

        private int id;
        private String userName;
        private String corporName;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
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

    }

    public static class VerifyCode{

        /**
         * phone : 15237164690
         * verifycode : 254138
         */

        private String phone;
        private String verifycode;

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getVerifycode() {
            return verifycode;
        }

        public void setVerifycode(String verifycode) {
            this.verifycode = verifycode;
        }
    }
}
