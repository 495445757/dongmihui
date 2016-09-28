package com.dongmihui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dongmihui.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
    @Bind(R.id.btnGetCode)
    ImageView btnGetCode;
    @Bind(R.id.imgPhone2)
    TextView imgPhone2;
    @Bind(R.id.edt_password)
    EditText edtPassword;
    @Bind(R.id.layout)
    RelativeLayout layout;
    @Bind(R.id.but_regist)
    Button butRegist;

    public static void startRegisterActivity(Activity activity) {
        if (activity != null) {
            Intent intent = new Intent(activity, RegisterActivity.class);
            activity.startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_username_phone);
        ButterKnife.bind(this);

    }

    @OnClick({R.id.btnGetCode, R.id.but_regist})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnGetCode:
                break;
            case R.id.but_regist:
                break;
        }
    }
}
