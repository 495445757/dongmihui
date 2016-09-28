package com.dongmihui.api;

import com.dongmihui.bean.ApiMessage;
import com.dongmihui.bean.NewBean;
import com.dongmihui.utils.ApiConstant;

import retrofit2.Call;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by Administrator on 2016/9/28.
 */

public interface NewServer {

    @GET(ApiConstant.URL_INDEX)
    Call<ApiMessage<NewBean>> getIndexData();
}
