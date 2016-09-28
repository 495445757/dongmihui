package com.dongmihui.api;

import com.dongmihui.bean.ApiMessage;
import com.dongmihui.bean.ContactListBean;
import com.dongmihui.bean.GroupListBean;
import com.dongmihui.utils.ApiConstant;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by Administrator on 2016/9/28.
 */

public class IMApi {

    public IMService server;

    public IMApi(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiConstant.url)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        server = retrofit.create(IMService.class);
    }

    /**
     * 搜索好友
     * @param userName
     * @param callback
     */
    public void getContactList(String userName, Callback<ApiMessage<List<ContactListBean>>> callback) {
        Call<ApiMessage<List<ContactListBean>>> contactList = server.getSearchContactList(userName);
        contactList.enqueue(callback);
    }

    /**
     * 搜索群
     * @param groupName
     * @param callBack
     */
    public void getGroupList(String groupName, Callback<ApiMessage<GroupListBean>> callBack) {
        Call<ApiMessage<GroupListBean>> groupList = server.getGroupList(groupName);
        groupList.enqueue(callBack);
    }

    /**
     * 获取好友列表
     * @param imName
     * @param callback
     */
    public void getContact(String imName, Callback<ApiMessage<List<ContactListBean>>> callback) {
        Call<ApiMessage<List<ContactListBean>>> contactList = server.getContactList(imName);
        contactList.enqueue(callback);
    }

}
