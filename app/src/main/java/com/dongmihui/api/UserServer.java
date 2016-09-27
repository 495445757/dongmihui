package com.dongmihui.api;

import com.dongmihui.bean.ApiMessage;
import com.dongmihui.utils.ApiConstant;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Administrator on 2016/9/27.
 */

public interface UserServer {

    @POST(ApiConstant.URL_ACTIVATE)
    @FormUrlEncoded
    Call<ApiMessage<ApiMessage.UserID>> getActivate(@Field("email") String email,
                                                    @Field("activate") String activate);

    @POST(ApiConstant.URL_VERIFY)
    @FormUrlEncoded
    Call<ApiMessage<ApiMessage.VerifyCode>> getVerify(@Field("phone") String phone,
                                                      @Field("symBol") int i);

    @POST(ApiConstant.URL_REGISTER)
    @FormUrlEncoded
    Call<ApiMessage> getRegister(@Field("userId") int i,
                                 @Field("phone") String phone,
                                 @Field("verifYcode") String verifYcode,
                                 @Field("passWord") String password);

    @POST(ApiConstant.URL_LOGIN)
    @FormUrlEncoded
    Call<ApiMessage<ApiMessage.LogingCode>> getLogin(@Field("account") String account,
                                                     @Field("passWord") String password);

    @POST(ApiConstant.URL_PASSVERIFY)
    @FormUrlEncoded
    Call<ApiMessage> getPassVerify(@Field("phone") String phone,
                                   @Field("verifYcode") String verifYcode);
}
