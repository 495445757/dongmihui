package com.dongmihui.api;

import com.bumptech.glide.Glide;
import com.dongmihui.adapter.base.BaseListAdapter;
import com.dongmihui.bean.ApiMessage;
import com.dongmihui.bean.LoginBean;
import com.dongmihui.utils.ApiConstant;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by Administrator on 2016/9/27.
 */

public class UserApi {

    public UserServer server;

    public UserApi(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiConstant.url)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        server = retrofit.create(UserServer.class);

    }

    /**
     * 登陆接口
     * @param account   用户手机号或邮箱
     * @param password  用户密码
     * @param callback  回调
     */
    public void login(String account, String password, Callback<ApiMessage<ApiMessage.LogingCode>> callback) {
        Call<ApiMessage<ApiMessage.LogingCode>> login = server.getLogin(account, password);
        login.enqueue(callback);
    }

    public void loginNew(String account, String password, Callback<LoginBean> callback) {
        Call<LoginBean> login = server.getLoingNew(account, password);
        login.enqueue(callback);
    }

    /**
     * 激活邮箱
     * @param email
     * @param activate
     * @param callback
     */
    public void activate(String email, String activate, Callback<ApiMessage<ApiMessage.UserID>> callback) {
        Call<ApiMessage<ApiMessage.UserID>> activateCall = server.getActivate(email, activate);
        activateCall.enqueue(callback);
    }
}
