package com.dongmihui.api;

import android.util.Log;

import com.dongmihui.bean.ApiMessage;
import com.dongmihui.bean.MemberBean;
import com.dongmihui.utils.ApiConstant;


import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.Part;

/**
 * Created by Android on 2016/9/27.
 */
public class MyApi {

    public MyServer server;

    public MyApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiConstant.url)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        server = retrofit.create(MyServer.class);


    }

    /**
     * 获取用户信息的接口
     *
     * @param id
     * @param callback
     */
    public void getMember(int id, Callback<MemberBean> callback) {
        Call<MemberBean> memberInfo = server.getMemberInfo(id);
        memberInfo.enqueue(callback);
    }

    /**
     * 上传用户修改信息的接口
     * @param id
     * @param name
     * @param sex
     * @param jobs
     * @param desc
     * @param avatar
     * @param callback
     */
    public void setAboutMember(int id, String name, String sex, String jobs,String home,String desc,String avatar,
                               Callback<String> callback) {
        Call<String> memberBeanCall = server.setAboutMember(id, name, sex, jobs, home,desc, avatar);
        memberBeanCall.enqueue(callback);

    }

    /**
     * 上传文件
     * @param filename
     * @param callback
     */
    public void putFiel(int id,
                        String filename,
                        Callback<ApiMessage<List<String>>> callback){

        File file = new File(filename);
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("application/otcet-stream"), file);

        MultipartBody.Part body =
                MultipartBody.Part.createFormData("aFile", file.getName(), requestFile);
        String descriptionString = "This is a description";
        RequestBody description = RequestBody.create(
                MediaType.parse("multipart/form-data"), descriptionString);

        Call<ApiMessage<List<String>>> call = server.upload(id,description, body);
        call.enqueue(callback);
    }

    /**
     * 修改密码
     * @param id
     * @param oldPass
     * @param newPass
     * @param rePass
     * @param callback
     */
  public void modifyPwd(int id,String oldPass,String newPass,String rePass,Callback<ApiMessage> callback){
      Call<ApiMessage> stringCall = server.setModifyPwd(id, oldPass, newPass, rePass);
      stringCall.enqueue(callback);
  }

    /**
     * 修改手机号
     * @param id
     * @param phone
     * @param verifYcode
     * @param callback
     */
    public void setModifyPhone(int id,String phone,String verifYcode,Callback<ApiMessage> callback){
        Call<ApiMessage> apiMessageCall = server.setModifyPhone(id, phone, verifYcode);
        apiMessageCall.enqueue(callback);
    }

    /**
     * 获取验证码
     * @param phone
     * @param verify
     * @param callback
     */
    public void getVerifyCode(String phone,int verify,Callback<ApiMessage> callback){
        Call<ApiMessage> verifyCode = server.getVerifyCode(phone, verify);
        verifyCode.enqueue(callback);
    }
}
