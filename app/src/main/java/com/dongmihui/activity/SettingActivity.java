package com.dongmihui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dongmihui.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingActivity extends Activity {

    @Bind(R.id.textHeadTitle)
    TextView textHeadTitle;
    @Bind(R.id.layout_header)
    RelativeLayout layoutHeader;
    @Bind(R.id.tv_account_info)
    TextView tvAccountInfo;
    @Bind(R.id.tv_privacy_settings)
    TextView tvPrivacySettings;
    @Bind(R.id.tv_account_binding)
    TextView tvAccountBinding;
    @Bind(R.id.tv_message_setting)
    TextView tvMessageSetting;

    public static void startSettingActivity(Activity activity) {
        if (activity != null) {
            Intent intent = new Intent(activity, SettingActivity.class);
            activity.startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        textHeadTitle.setText("设置");
    }

    @OnClick({R.id.tv_account_info, R.id.tv_privacy_settings, R.id.tv_account_binding, R.id.tv_message_setting})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_account_info:
                break;
            case R.id.tv_privacy_settings:
                PrivacySettingsActivity.startPrivacySettingsActivity(this);
                break;
            case R.id.tv_account_binding:
                break;
            case R.id.tv_message_setting:
                break;
        }
    }
}
