package com.dongmihui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dongmihui.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PrivacySettingsActivity extends Activity {

    @Bind(R.id.btnBack)
    Button btnBack;
    @Bind(R.id.textHeadTitle)
    TextView textHeadTitle;
    @Bind(R.id.layout_header)
    RelativeLayout layoutHeader;
    @Bind(R.id.iv_proving_open)
    ImageView ivProvingOpen;
    @Bind(R.id.iv_proving_close)
    ImageView ivProvingClose;
    @Bind(R.id.rl_switch_proving)
    RelativeLayout rlSwitchProving;
    @Bind(R.id.iv_search_open)
    ImageView ivSearchOpen;
    @Bind(R.id.iv_search_close)
    ImageView ivSearchClose;
    @Bind(R.id.rl_switch_search)
    RelativeLayout rlSwitchSearch;
    @Bind(R.id.iv_public_open)
    ImageView ivPublicOpen;
    @Bind(R.id.iv_public_close)
    ImageView ivPublicClose;
    @Bind(R.id.rl_switch_public)
    RelativeLayout rlSwitchPublic;
    @Bind(R.id.tv_blank_list)
    TextView tvBlankList;
    @Bind(R.id.activity_privacy_settings)
    LinearLayout activityPrivacySettings;

    public static void startPrivacySettingsActivity(Activity activity) {
        if (activity != null) {
            Intent intent = new Intent(activity, PrivacySettingsActivity.class);
            activity.startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_settings);
        ButterKnife.bind(this);
        textHeadTitle.setText("隐私设置");
        initView();
    }

    private void initView() {
        
    }

    @OnClick({R.id.textHeadTitle, R.id.iv_proving_open, R.id.iv_proving_close, R.id.iv_search_open, R.id.iv_search_close, R.id.iv_public_open, R.id.iv_public_close, R.id.tv_blank_list})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_proving_open:
                break;
            case R.id.iv_proving_close:
                break;
            case R.id.iv_search_open:
                break;
            case R.id.iv_search_close:
                break;
            case R.id.iv_public_open:
                break;
            case R.id.iv_public_close:
                break;
            case R.id.tv_blank_list:

                break;
        }
    }
}
