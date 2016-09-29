package com.dongmihui.api;

import com.dongmihui.bean.ApiMessage;
import com.dongmihui.bean.NewBean;
import com.dongmihui.utils.ApiConstant;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by Administrator on 2016/9/28.
 */

public class NewApi {


    public NewServer server;

    public NewApi(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiConstant.url)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        server = retrofit.create(NewServer.class);
    }

    /**
     * 获取首页数据
     * @param callback
     */
    public void getIndexData(Callback<ApiMessage<NewBean>> callback) {
        Call<ApiMessage<NewBean>> indexData = server.getIndexData();
        indexData.enqueue(callback);
    }


}
