package com.dongmihui.api;

import com.dongmihui.bean.ApiMessage;
import com.dongmihui.bean.MemberBean;
import com.dongmihui.utils.ApiConstant;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by Android on 2016/9/27.
 */
public interface MyServer {
    /**
     * 获取用户信息
     * @param id
     * @return
     */
    @GET(ApiConstant.URL_MEMBER)
    Call<MemberBean> getMemberInfo(@Query("id") int id);

    /**
     * 用户信息修改提交服务器
     * @param id
     * @param name
     * @param sex
     * @param jobs
     * @param desc
     * @param avatar
     * @return
     */
    @POST(ApiConstant.URL_ABOUTMEMBER)
    @FormUrlEncoded
    Call<String> setAboutMember(@Field("id") int id,
                                @Field("userName") String name,
                                @Field("sex") String sex,
                                @Field("jobs") String jobs,
                                @Field("home") String home,

                                @Field("desc") String desc,
                                @Field("avatar") String avatar
    );



    /**
     * 上传头像
     * @param id
     * @param description
     * @param file
     * @return
     */
    @Multipart
    @POST(ApiConstant.URL_AVATAR)
    Call<ApiMessage<List<String>>> upload(@Part("id")int id, @Part("description") RequestBody description,
                                          @Part MultipartBody.Part file);

    /**
     * 修改密码
     * @param id
     * @param oldPass
     * @param newPass
     * @param rePass
     * @return
     */
    @POST(ApiConstant.URL_MODIFYPWD)
    @FormUrlEncoded
    Call<ApiMessage> setModifyPwd(@Field("userId") int id, @Field("oldPass") String oldPass, @Field("newPass") String newPass,
                              @Field("rePass") String rePass
                              );

    /**
     * 修改手机号
     * @param id
     * @param phone
     * @param verifYcode
     * @return
     */
    @POST(ApiConstant.URL_MODIFYPHONE)
    @FormUrlEncoded
    Call<ApiMessage> setModifyPhone(@Field("userId") int id,@Field("phone") String phone,@Field("verifYcode")String verifYcode);

    /**
     * 获取验证码
     * @param phone
     * @param i
     * @return
     */
    @POST(ApiConstant.URL_VERIFY)
    @FormUrlEncoded
    Call<ApiMessage> getVerifyCode(@Field("phone") String phone,@Field("symBol") int i);
}
