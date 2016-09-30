package com.dongmihui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dongmihui.R;
import com.dongmihui.api.MyApi;
import com.dongmihui.bean.ApiMessage;
import com.dongmihui.common.AppContext;
import com.dongmihui.utils.SpUtils;
import com.dongmihui.utils.ToastUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingAccountActivity extends Activity {

    @Bind(R.id.tv_modfiyphone)
    Button tvModfiyphone;
    @Bind(R.id.tv_modfiypwd)
    Button tvModfiypwd;
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
    @Bind(R.id.modfiypwd)
    TextView modfiypwd;
    @Bind(R.id.old_pwd)
    EditText oldPwd;
    @Bind(R.id.layout_modfiy_pwd)
    RelativeLayout layoutModfiyPwd;
    @Bind(R.id.newpwd)
    TextView newpwd;
    @Bind(R.id.put_newpwd)
    EditText putNewpwd;
    @Bind(R.id.once_newpwd)
    TextView onceNewpwd;
    @Bind(R.id.put_once_newpwd)
    EditText putOnceNewpwd;
    @Bind(R.id.btn_ok)
    Button btnOk;
    @Bind(R.id.layoutPhone1)
    RelativeLayout layoutPhone1;
    @Bind(R.id.layout_modfiy_pwd1)
    RelativeLayout layoutModfiyPwd1;
    @Bind(R.id.layout_modfiy_pwd2)
    RelativeLayout layoutModfiyPwd2;
    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.btn_ok_pwd)
    Button btnOkPwd;
    private MyApi api;
    private ApiMessage verifyBody;
    private String phoneNumber;
    int i =120;
    Handler handler =new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if(i==0){
                imGetCode.setText("获取验证码");
                imGetCode.setTextColor(SettingAccountActivity.this.getResources().getColor(R.color.bar_bg));
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


    public static void startSettingAccountActivity(Activity activity) {
        if (activity != null) {
            Intent intent = new Intent(activity, SettingAccountActivity.class);
            activity.startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_info);
        api = new MyApi();
        ButterKnife.bind(this);
    }



    @OnClick({R.id.tv_modfiyphone, R.id.tv_modfiypwd, R.id.im_GetCode, R.id.btn_ok, R.id.back, R.id.btn_ok_pwd})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_modfiyphone:
                setViewPhone();
                break;
            case R.id.tv_modfiypwd:
                setViewPwd();
                break;
            case R.id.im_GetCode:
                getVerifyCode();

                break;
            case R.id.back:
                finish();
                break;
            case R.id.btn_ok:
                modifyPhone();
                break;
            case R.id.btn_ok_pwd:
                modfiyPwd();
                break;
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

    public void getVerifyCode() {
        phoneNumber = phone.getText().toString().trim();
        if(!TextUtils.isEmpty(phoneNumber)){
            api.getVerifyCode(phoneNumber,3, new Callback<ApiMessage>() {
                @Override
                public void onResponse(Call<ApiMessage> call, Response<ApiMessage> response) {

                    verifyBody = response.body();
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

    private void modifyPhone() {
        String verify = code.getText().toString().trim();
        if(!TextUtils.isEmpty(verify)){
          int  anInt = SpUtils.getInt(this, SpUtils.USER_ID);
            api.setModifyPhone(anInt, phoneNumber, verify, new Callback<ApiMessage>() {
                @Override
                public void onResponse(Call<ApiMessage> call, Response<ApiMessage> response) {
                    ApiMessage phoneBody = response.body();
                    if(phoneBody.getCode()==0){
                        ToastUtil.showShort(AppContext.getInstance(), phoneBody.getMsg().toString());
                    }else if(phoneBody.getCode()==1){
                        ToastUtil.showShort(AppContext.getInstance(), phoneBody.getMsg().toString());
                    }
                }

                @Override
                public void onFailure(Call<ApiMessage> call, Throwable t) {

                }
            });
        }



    }

    private void modfiyPwd() {
        String oldpwd = oldPwd.getText().toString().trim();
        String newPwd = putNewpwd.getText().toString().trim();
        String rePwd = putOnceNewpwd.getText().toString().trim();
        if (!TextUtils.isEmpty(oldpwd) && !TextUtils.isEmpty(newPwd) && !TextUtils.isEmpty(rePwd)) {
            int  anInt = SpUtils.getInt(this, SpUtils.USER_ID);
            api.modifyPwd(anInt, oldpwd, newPwd, rePwd, new Callback<ApiMessage>() {
                @Override
                public void onResponse(Call<ApiMessage> call, Response<ApiMessage> response) {
                    ApiMessage body = response.body();
                    if(body.getCode()==0){
                        ToastUtil.showShort(AppContext.getInstance(), body.getMsg().toString());
                    }else if(body.getCode()==1){
                        ToastUtil.showShort(AppContext.getInstance(), body.getMsg().toString());
                    }
                }

                @Override
                public void onFailure(Call<ApiMessage> call, Throwable t) {
                    ToastUtil.showShort(AppContext.getInstance(), t.toString());
                }
            });
        }else {
            ToastUtil.showShort(AppContext.getInstance(), "密码不能为空");
        }
    }

    private void setViewPwd() {
        tvModfiyphone.setTextColor(this.getResources().getColor(R.color.bar_bg));
        tvModfiypwd.setTextColor(this.getResources().getColor(R.color.white));
        tvModfiypwd.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.im_modifypwd));
        tvModfiyphone.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.modifypwd));
        layoutPhone.setVisibility(View.GONE);
        layoutPhone1.setVisibility(View.GONE);
        layoutModfiyPwd.setVisibility(View.VISIBLE);
        layoutModfiyPwd1.setVisibility(View.VISIBLE);
        layoutModfiyPwd2.setVisibility(View.VISIBLE);
        btnOkPwd.setVisibility(View.VISIBLE);
        btnOk.setVisibility(View.GONE);
    }

    private void setViewPhone() {
        tvModfiypwd.setTextColor(this.getResources().getColor(R.color.bar_bg));
        tvModfiyphone.setTextColor(this.getResources().getColor(R.color.white));
        tvModfiyphone.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.modif_phone_pressed));
        tvModfiypwd.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.modifypwd));
        layoutPhone.setVisibility(View.VISIBLE);
        layoutPhone1.setVisibility(View.VISIBLE);
        layoutModfiyPwd.setVisibility(View.GONE);
        layoutModfiyPwd1.setVisibility(View.GONE);
        layoutModfiyPwd2.setVisibility(View.GONE);
        btnOkPwd.setVisibility(View.GONE);
        btnOk.setVisibility(View.VISIBLE);

    }



}
