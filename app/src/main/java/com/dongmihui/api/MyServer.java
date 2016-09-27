package com.dongmihui.api;

import com.dongmihui.utils.ApiConstant;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Android on 2016/9/27.
 */
public interface MyServer {

    @POST(ApiConstant.URL_ACTIVATE)
    @FormUrlEncoded
    Call<String> getLog(@Field("name") String name);
}
