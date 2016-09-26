package com.dongmihui.callback;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.lzy.okhttputils.callback.AbsCallback;

import org.json.JSONObject;

import java.lang.reflect.Type;

import okhttp3.Response;


public abstract class JsonCallback<T> extends AbsCallback<T> {

    private Class<T> clazz;
    private Type type;

    public JsonCallback(Class<T> clazz) {
        this.clazz = clazz;
    }

    public JsonCallback(Type type) {
        this.type = type;
    }

    @Override
    public T parseNetworkResponse(Response response) {
        try {
            String responseData = response.body().string();
            if (TextUtils.isEmpty(responseData)) return null;

            JSONObject jsonObject = new JSONObject(responseData);
            String data = jsonObject.optString("data", "");
            if (clazz == String.class) return (T) data;
            if (clazz != null) return new Gson().fromJson(data, clazz);
            if (type != null) return new Gson().fromJson(data, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
