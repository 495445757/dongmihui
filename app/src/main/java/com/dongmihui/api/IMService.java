package com.dongmihui.api;

import com.dongmihui.bean.ApiMessage;
import com.dongmihui.bean.ContactListBean;
import com.dongmihui.bean.GroupBean;
import com.dongmihui.utils.ApiConstant;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2016/9/28.
 */

public interface IMService {

    @GET(ApiConstant.URL_SEARCHUSER)
    Call<ApiMessage<List<ContactListBean>>> getSearchContactList(@Query("name") String name);

    @GET(ApiConstant.URL_SEARCHGROUP)
    Call<ApiMessage<List<GroupBean>>> getGroupList(@Query("name") String groupName);

    @GET(ApiConstant.URL_GET_FRIEND)
    Call<ApiMessage<List<ContactListBean>>> getContactList(@Query("name") String imName);

    @GET(ApiConstant.URL_USER_INFO)
    Call<ApiMessage<ContactListBean>> getUserInfo(@Query("userName")String imName);
}
