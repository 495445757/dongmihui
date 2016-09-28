package com.dongmihui.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.dongmihui.common.AppContext;
import com.dongmihui.im.DemoHelper;
import com.dongmihui.im.db.DemoDBManager;
import com.dongmihui.ui.swipebacklayout.SwipeBackActivity;
import com.dongmihui.utils.ApiConstant;
import com.dongmihui.utils.SpUtils;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;

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

    ProgressDialog pd;
    public void login() {
        String phoneNum = phone.getText().toString().trim();
        final String password = code.getText().toString().trim();

        if (TextUtils.isEmpty(phoneNum)) {
            Toast.makeText(this, "请输入手机或邮箱", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "密码不可为空", Toast.LENGTH_SHORT).show();
        }
        if (pd == null) {
            pd = new ProgressDialog(LoginActivity.this);
        }
        pd.setCanceledOnTouchOutside(false);
        pd.setMessage("登陆中。。。");
        pd.show();

        DemoDBManager.getInstance().closeDB();

        api.login(phoneNum, password, new Callback<ApiMessage<ApiMessage.LogingCode>>() {
            @Override
            public void onResponse(Call<ApiMessage<ApiMessage.LogingCode>> call, Response<ApiMessage<ApiMessage.LogingCode>> response) {
                ApiMessage<ApiMessage.LogingCode> body = response.body();
                if (body.getCode() == 0) {
                    pd.dismiss();
                    Toast.makeText(LoginActivity.this, body.getMsg(), Toast.LENGTH_SHORT).show();

                } else if (body.getCode() == 1) {
                    ApiMessage.LogingCode result = body.getResult();
                    if (result != null) {
                        SpUtils.putInt(getBaseContext(),SpUtils.USER_ID,result.getId());
                        SpUtils.putString(getBaseContext(),SpUtils.USER_HXNAME,result.getUserName());
                        SpUtils.putString(getBaseContext(),SpUtils.CORPOR_NAME,result.getCorporName());
                        String hxName = result.getHxName();
                        SpUtils.putString(getBaseContext(),SpUtils.USER_HXNAME,hxName);
                        hxLogin(hxName,password);
                    }
                }

            }

            @Override
            public void onFailure(Call<ApiMessage<ApiMessage.LogingCode>> call, Throwable t) {
                Log.d("LoginActivity", t.toString());
            }
        });
    }

    private void hxLogin(final String hxName, String passwd) {
        EMClient.getInstance().login(hxName, passwd, new EMCallBack() {
            @Override
            public void onSuccess() {
                DemoHelper.getInstance().setCurrentUserName(hxName);

                Log.d("LoginActivity", "环信登陆成功");
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();

                boolean updatenick = EMClient.getInstance().updateCurrentUserNick(AppContext.currentUserNick.trim());

                if (!updatenick) {
                    Log.d("LoginActivity", "更新当前用户NICK失败");
                }

                if (!LoginActivity.this.isFinishing() && pd.isShowing()) {
                    pd.dismiss();
                }
                MainActivity.startMainActivity(LoginActivity.this);
                finish();
            }

            @Override
            public void onError(int i, final String s) {
                Log.d("LoginActivity", "登陆失败：code：" + i + " message：" + s);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pd.dismiss();
                        Toast.makeText(LoginActivity.this, "登陆失败：" + s, Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onProgress(int i, String s) {
                Log.d("LoginActivity", "登陆中：进度 " + i);
            }
        });
    }



}
