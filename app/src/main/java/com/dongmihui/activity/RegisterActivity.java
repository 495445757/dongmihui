package com.dongmihui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dongmihui.R;
import com.dongmihui.api.MyApi;
import com.dongmihui.api.UserApi;
import com.dongmihui.bean.ApiMessage;
import com.dongmihui.common.AppContext;
import com.dongmihui.utils.ToastUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends Activity {


    @Bind(R.id.imgPhone)
    TextView imgPhone;
    @Bind(R.id.phone)
    EditText phone;
    @Bind(R.id.imgCancel)
    ImageView imgCancel;
    @Bind(R.id.layoutPhone)
    RelativeLayout layoutPhone;
    @Bind(R.id.imgCode)
    TextView imgCode;
    @Bind(R.id.code)
    EditText code;
    @Bind(R.id.im_GetCode)
    TextView imGetCode;
    @Bind(R.id.imgPhone2)
    TextView imgPhone2;
    @Bind(R.id.edt_password)
    EditText edtPassword;
    @Bind(R.id.layout)
    RelativeLayout layout;
    @Bind(R.id.but_regist)
    Button butRegist;
    @Bind(R.id.im_back)
    ImageView imBack;
    private MyApi api;
    int i =120;
    int id;
    Handler handler =new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if(i==0){
                imGetCode.setText("获取验证码");
                imGetCode.setTextColor(RegisterActivity.this.getResources().getColor(R.color.bar_bg));
                imGetCode.setTextSize(16.0f);
                imGetCode.setEnabled(true);
                i=120;
            }else {
                i--;
                imGetCode.setText(i+"s");
                setCode();
            }


        }
    };
    private UserApi userApi;

    public static void startRegisterActivity(Activity activity,int id) {
        if (activity != null) {
            Intent intent = new Intent(activity, RegisterActivity.class);
            intent.putExtra("id",id);
            activity.startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        id=getIntent().getIntExtra("id",0);
        if(id==0){
            return;
        }
        setContentView(R.layout.activity_active_username_phone);
        api = new MyApi();
        userApi = new UserApi();
        ButterKnife.bind(this);
        }


    @OnClick({R.id.imgCode, R.id.im_GetCode, R.id.but_regist,R.id.im_back})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.im_GetCode:
                getVerifyCode();
                break;
            case R.id.but_regist:
                regist();
                break;
            case R.id.im_back:
                finish();
                break;
        }
    }



    public void getVerifyCode() {
       String phoneNumber = phone.getText().toString().trim();
        if(!TextUtils.isEmpty(phoneNumber)){
            api.getVerifyCode(phoneNumber,1, new Callback<ApiMessage>() {
                @Override
                public void onResponse(Call<ApiMessage> call, Response<ApiMessage> response) {

                    ApiMessage verifyBody = response.body();
                    if(verifyBody.getCode()==0){
                        ToastUtil.showShort(AppContext.getInstance(), verifyBody.getMsg().toString());
                    }else if(verifyBody.getCode()==1){
                        ToastUtil.showShort(AppContext.getInstance(), verifyBody.getMsg().toString());
                        setCode();
                        imGetCode.setEnabled(false);
                    }


//                    if(verifyBody.getCode()==0){
//                        ToastUtil.showShort(AppContext.getInstance(), "返回失败");
//                    }else if(verifyBody.getCode()==1){
//                        ToastUtil.showShort(AppContext.getInstance(), "返回成功");
//                    }

                }

                @Override
                public void onFailure(Call<ApiMessage> call, Throwable t) {
                    ToastUtil.showShort(AppContext.getInstance(),t.toString());
                }
            });
        }

    }

    private void setCode() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                handler.sendEmptyMessageDelayed(500,1000);
            }
        }).start();

    }
    private void regist() {
        String phoneNumber = phone.getText().toString().trim();
        String codestr = code.getText().toString().trim();
        String pwd = edtPassword.getText().toString().trim();
        userApi.register(id, phoneNumber, codestr, pwd, new Callback<ApiMessage>() {
            @Override
            public void onResponse(Call<ApiMessage> call, Response<ApiMessage> response) {
                ApiMessage body = response.body();
                if(body.getCode()==0){
                    ToastUtil.showShort(AppContext.getInstance(),body.getMsg().toString());
                }else if(body.getCode()==1){
                    ToastUtil.showShort(AppContext.getInstance(),body.getMsg().toString());
                    LoginActivity.startLoginActivity(RegisterActivity.this);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<ApiMessage> call, Throwable t) {
                ToastUtil.showShort(AppContext.getInstance(),t.toString());
                }
        });

        }

}
