package com.dongmihui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.dongmihui.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class JobSetActivity extends Activity {

    @Bind(R.id.im_back)
    ImageView imBack;
    @Bind(R.id.btn_keep)
    Button btnKeep;
    @Bind(R.id.et_input_job)
    EditText etInputJob;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_set);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.im_back, R.id.btn_keep})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.im_back:
                finish();
                break;
            case R.id.btn_keep:

                break;
        }
    }
}
