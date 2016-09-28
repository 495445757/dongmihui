package com.dongmihui.api;

import com.dongmihui.bean.MemberBean;
import com.dongmihui.utils.ApiConstant;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by Android on 2016/9/27.
 */
public class MyApi {

    public MyServer server;

    public MyApi(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiConstant.url)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        server = retrofit.create(MyServer.class);


    }

    /**
     * 获取用户信息的接口
     * @param id
     * @param callback
     */
    public void getMember(int id, Callback<MemberBean> callback){
        Call<MemberBean> memberInfo = server.getMemberInfo(id);
        memberInfo.enqueue(callback);
    }


}
