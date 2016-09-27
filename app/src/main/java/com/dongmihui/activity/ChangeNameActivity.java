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

/**
 * 用户修改名字的界面
 */
public class ChangeNameActivity extends Activity {

    @Bind(R.id.im_back)
    ImageView imBack;
    @Bind(R.id.btn_keep)
    Button btnKeep;
    @Bind(R.id.et_input_name)
    EditText etInputName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_name);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.im_back, R.id.btn_keep})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.im_back:
                finish();
                break;
            case R.id.btn_keep:

                String newName = etInputName.getText().toString().trim();
                if(TextUtils.isEmpty(newName)){
                    ToastUtil.showShort(this,"名字不能为空！");
                }else{
                    //TODO 向服务器发送更改的昵称
                    Intent intent =new Intent(this,MemberEditorActivity.class);
                    intent.putExtra("newName",newName);
//                    startActivityForResult(intent,1000);
                    finishActivity(1000);
                   finish();
                }

                break;
        }
    }
}
