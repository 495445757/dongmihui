package com.dongmihui.api;

import com.dongmihui.bean.MemberBean;
import com.dongmihui.utils.ApiConstant;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Android on 2016/9/27.
 */
public interface MyServer {

    @GET(ApiConstant.URL_MEMBER)
    Call<MemberBean> getMemberInfo(@Query("id") int id);
}
