package com.dongmihui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.dongmihui.R;
import com.dongmihui.utils.ToastUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BriefInputActivity extends Activity {

    @Bind(R.id.im_back)
    ImageView imBack;
    @Bind(R.id.btn_keep)
    Button btnKeep;
    @Bind(R.id.add_content)
    EditText addContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brief_input);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.im_back, R.id.btn_keep})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.im_back:
                finish();
                break;
            case R.id.btn_keep:

                String newDec = addContent.getText().toString().trim();
                if(TextUtils.isEmpty(newDec)){
                    ToastUtil.showShort(this,"名字不能为空！");
                }else{
                    Intent intent = new Intent(BriefInputActivity.this, MemberEditorActivity.class);
                    intent.putExtra("newDec",newDec);

                    setResult(1003,intent);
                    finish();
                }
                break;
        }
    }
}
