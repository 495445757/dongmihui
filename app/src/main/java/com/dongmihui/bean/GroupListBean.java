package com.dongmihui.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/9/28.
 */

public class GroupListBean {

    public List<Group> groups;

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    public static class Group{

        /**
         * id : 1
         * groupName : 第一个群
         * number : 246137203565003180
         * avatar : http://192.168.2.3/uploads/avatar/avatar.jpg
         */

        private int id;
        private String groupName;
        private String number;
        private String avatar;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getGroupName() {
            return groupName;
        }

        public void setGroupName(String groupName) {
            this.groupName = groupName;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }
    }

}
