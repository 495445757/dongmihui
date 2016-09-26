package com.dongmihui.api;

import com.dongmihui.common.AppContext;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;



/**
 * Created by Administrator on 2016/7/7.
 */
public class DongmihuiApi {


    public static final int CATALOG_BANNER_NEWS = 1; // 资讯Banner
    /**
     * 请求Banner列表接口
     *
     * @param catalog Banner类别 {@link #CATALOG_BANNER_NEWS, #CATALOG_BANNER_BLOG, #CATALOG_BANNER_EVENT}
     * @param handler AsyncHttpResponseHandler
     */
    public static void getBannerList(int catalog, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
       // params.put("catalog", catalog);
        ApiHttpClient.get("banner.txt", params, handler);
    }
    public static void getNewsList(int catalog, int page,
                                   AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("catalog", catalog);
        params.put("pageIndex", page);
        params.put("pageSize", AppContext.PAGE_SIZE);
          ApiHttpClient.get("action/api/news_list", params, handler);
    }

    public static void getNewsDetail(long id, String type, AsyncHttpResponseHandler handler) {
        if (id <= 0) return;
        RequestParams params = new RequestParams();
       // params.put("id", id);
        ApiHttpClient.get("11.txt" , params, handler);
    }
    public static void getNewsList(String pageToken, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
//        if (!TextUtils.isEmpty(pageToken)) {
//            params.put("pageToken", pageToken);
//        }

        ApiHttpClient.get("test.txt", params, handler);
    }
    public static void getChatList(String pageToken, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
//        if (!TextUtils.isEmpty(pageToken)) {
//            params.put("pageToken", pageToken);
//        }

        ApiHttpClient.get("chat.txt", params, handler);
    }
    public static void getotherNewsList(String pageToken, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
//        if (!TextUtils.isEmpty(pageToken)) {
//            params.put("pageToken", pageToken);
//        }

        ApiHttpClient.get("othernews.txt", params, handler);
    }
    public static void getthingsList(String pageToken, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();

           // params.put("pageToken", pageToken);
           // params.put("goodsId", "98573");
          //  params.put("pageNo", 1);


        ApiHttpClient.getUrl("penyou.php", params, handler);
    }


    public static void getComments(long sourceId, int type, String parts, String pageToken, TextHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
//        params.put("sourceId", sourceId);
//        params.put("type", type);
//        if (!android.text.TextUtils.isEmpty(parts)) {
//            params.put("parts", parts);
//        }
//        if (!android.text.TextUtils.isEmpty(pageToken)) {
//            params.put("pageToken", pageToken);
//        }
        ApiHttpClient.get("comment.txt", params, handler);
    }



}
