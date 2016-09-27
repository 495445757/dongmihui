package com.dongmihui.api;

import com.dongmihui.utils.ApiConstant;


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



}
