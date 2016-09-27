package com.dongmihui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.dongmihui.R;
import com.dongmihui.api.UserApi;
import com.dongmihui.bean.ApiMessage;
import com.dongmihui.ui.swipebacklayout.SwipeBackActivity;
import com.dongmihui.utils.ApiConstant;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;


public class LoginActivity extends SwipeBackActivity {

    @Bind(R.id.imgPhone)
    ImageView imgPhone;
    @Bind(R.id.phone)
    EditText phone;
    @Bind(R.id.imgCancel)
    ImageView imgCancel;
    @Bind(R.id.layoutPhone)
    RelativeLayout layoutPhone;
    @Bind(R.id.imgCode)
    ImageView imgCode;
    @Bind(R.id.code)
    EditText code;
    @Bind(R.id.im_hlpe)
    ImageView imHlpe;
    @Bind(R.id.btn_login)
    Button btnLogin;

    UserApi api;

    public static void startLoginActivity(Activity activity) {
        if (activity != null) {
            Intent intent = new Intent(activity, LoginActivity.class);
            activity.startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_use);
        ButterKnife.bind(this);
        api = new UserApi();
    }

    @OnClick({R.id.btn_login,R.id.tv_to_editpassword, R.id.tv_to_activate, R.id.rl_to_help})
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btn_login:
                login();
                break;
            case R.id.tv_to_editpassword:
                break;
            case R.id.tv_to_activate:
                ActivateActivity.startActivateActivity(this);
                break;
            case R.id.rl_to_help:
                break;
        }





    }


    public void login() {
        String phoneNum = phone.getText().toString();
        String password = code.getText().toString();

        api.login(phoneNum, password, new Callback<ApiMessage<ApiMessage.LogingCode>>() {
            @Override
            public void onResponse(Call<ApiMessage<ApiMessage.LogingCode>> call, Response<ApiMessage<ApiMessage.LogingCode>> response) {
                ApiMessage<ApiMessage.LogingCode> body = response.body();
                if (body.getCode() == 0) {
                    Toast.makeText(LoginActivity.this, body.getMsg(), Toast.LENGTH_SHORT).show();

                } else if (body.getCode() == 1) {
                    Toast.makeText(LoginActivity.this, body.getMsg(), Toast.LENGTH_SHORT).show();
                    Log.d("LoginActivity", "msg：" + response.body().toString());
                }

            }

            @Override
            public void onFailure(Call<ApiMessage<ApiMessage.LogingCode>> call, Throwable t) {
                Log.d("LoginActivity", t.toString());
            }
        });
    }

    public interface ApiUser {

        @FormUrlEncoded
        @POST(ApiConstant.URL_ACTIVATE)
        Call<String> getActivate(@Field("email") String email, @Field("activate") String activate);

    }


}
