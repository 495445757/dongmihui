package com.dongmihui.common;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.dongmihui.R;
import com.dongmihui.api.ApiHttpClient;
import com.dongmihui.im.DemoHelper;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.PersistentCookieStore;
import com.lzy.ninegrid.NineGridView;
import com.lzy.okhttputils.OkHttpUtils;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.picasso.Picasso;

public class AppContext extends BaseApplication {

    private static AppContext app;
    public static final int PAGE_SIZE = 20;// 默认分页大小
    public static final String FIRST_OPEN = "first_open";
    public AppContext() {
        app = this;
    }

    public static synchronized AppContext getInstance() {
        if (app == null) {
            app = new AppContext();
        }
        return app;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LeakCanary.install(this);
        registerUncaughtExceptionHandler();
        Init();
        
        InitIM();
    }

    private void InitIM() {
        DemoHelper.getInstance().init(getInstance());
    }


    void  Init()
    {
        // 初始化网络请求
        AsyncHttpClient client = new AsyncHttpClient();
        PersistentCookieStore myCookieStore = new PersistentCookieStore(this);
        client.setCookieStore(myCookieStore);
        ApiHttpClient.setHttpClient(client);
        ApiHttpClient.setCookie(ApiHttpClient.getCookie(this));
        OkHttpUtils.init(this);
        NineGridView.setImageLoader(new PicassoImageLoader());
        
    }
    public PackageInfo getPackageInfo() {
        PackageInfo info = null;
        try {
            info = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace(System.err);
        }
        if (info == null)
            info = new PackageInfo();
        return info;
    }
    /** Picasso 加载 */
    private class PicassoImageLoader implements NineGridView.ImageLoader {

        @Override
        public void onDisplayImage(Context context, ImageView imageView, String url) {
            Picasso.with(context).load(url)//
                    .placeholder(R.drawable.ic_default_image)//
                    .error(R.drawable.ic_default_image)//
                    .into(imageView);
        }

        @Override
        public Bitmap getCacheImage(String url) {
            return null;
        }
    }

    // 注册App异常崩溃处理器
    private void registerUncaughtExceptionHandler() {
        Thread.setDefaultUncaughtExceptionHandler(AppException.getAppExceptionHandler());
    }
    public static Gson createGson() {
        com.google.gson.GsonBuilder gsonBuilder = new com.google.gson.GsonBuilder();

        gsonBuilder.setDateFormat("yyyy-MM-dd HH:mm:ss");
        return gsonBuilder.create();
    }
}