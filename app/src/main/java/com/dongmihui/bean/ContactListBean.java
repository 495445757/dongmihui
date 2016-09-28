package com.dongmihui.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/9/28.
 */

public class ContactListBean {

    /**
     * id : 3
     * userName : dmh_227468872
     * nickName : 张飞
     * avatar : http://192.168.2.3/uploads/avatar/57e897e21b944.jpeg
     */
        private int id;
        private String userName;
        private String nickName;
        private String avatar;

        @Override
        public String toString() {
            return "USerBean{" +
                    "id=" + id +
                    ", userName='" + userName + '\'' +
                    ", nickName='" + nickName + '\'' +
                    ", avatar='" + avatar + '\'' +
                    '}';
        }

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

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

}
