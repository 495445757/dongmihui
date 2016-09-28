package com.dongmihui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dongmihui.R;
import com.dongmihui.api.UserApi;
import com.dongmihui.bean.ApiMessage;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrator on 2016/9/27.
 */

public class ActivateActivity extends Activity {

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
    @Bind(R.id.but_next)
    Button butNext;

    UserApi api;

    public static void startActivateActivity(Activity activity) {
        if (activity != null) {
            Intent intent = new Intent(activity, ActivateActivity.class);
            activity.startActivity(intent);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activit_active_username_postbox);
        ButterKnife.bind(this);

        api = new UserApi();

    }


    @OnClick({R.id.iv_back_register, R.id.but_next})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back_register:
                finish();
                break;
            case R.id.but_next:
                register();
                break;
        }
    }

    private void register() {

        String useremail = phone.getText().toString();
        String userCode = code.getText().toString();

        api.activate(useremail, userCode, new Callback<ApiMessage<ApiMessage.UserID>>() {
            @Override
            public void onResponse(Call<ApiMessage<ApiMessage.UserID>> call, Response<ApiMessage<ApiMessage.UserID>> response) {
                ApiMessage<ApiMessage.UserID> body = response.body();
                if (body.getCode() == 0) {
                    Toast.makeText(ActivateActivity.this, "body.getCode():" + body.getCode(), Toast.LENGTH_SHORT).show();
                } else if (body.getCode() == 1) {
                    Toast.makeText(ActivateActivity.this, "body.getResult().getUserId():" + body.getResult().getUserId(), Toast.LENGTH_SHORT).show();
                    RegisterActivity.startRegisterActivity(ActivateActivity.this);
                }
            }

            @Override
            public void onFailure(Call<ApiMessage<ApiMessage.UserID>> call, Throwable t) {
                Toast.makeText(ActivateActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
                Log.d("ActivateActivity", t.toString());
            }
        });

    }
}
