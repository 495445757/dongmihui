package com.dongmihui.activity;

import android.os.Bundle;
import android.view.View;

import com.dongmihui.ui.swipebacklayout.SwipeBackActivity;
import com.dongmihui.R;




public class LoginActivity extends SwipeBackActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findViewById(R.id.btnClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
