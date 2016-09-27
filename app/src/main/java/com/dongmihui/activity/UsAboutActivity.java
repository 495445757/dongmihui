package com.dongmihui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.dongmihui.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UsAboutActivity extends Activity {

    @Bind(R.id.im_back)
    ImageView imBack;
    public static void startUsAboutActivity(Activity activity){
        if(activity!=null){
            activity.startActivity(new Intent(activity,UsAboutActivity.class));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.im_back)
    public void onClick() {
        finish();
    }
}
