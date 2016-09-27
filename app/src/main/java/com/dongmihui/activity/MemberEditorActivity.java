package com.dongmihui.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dongmihui.R;
import com.dongmihui.im.DemoModel;
import com.dongmihui.im.activity.BaseActivity;
import com.dongmihui.utils.ToastUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 编辑个人信息的界面
 */
public class MemberEditorActivity extends BaseActivity {

    @Bind(R.id.im_back)
    ImageView imBack;
    @Bind(R.id.im_avatar)
    ImageView imAvatar;
    @Bind(R.id.layout_user_name)
    RelativeLayout layoutUserName;
    @Bind(R.id.layout_user_sex)
    RelativeLayout layoutUserSex;
    @Bind(R.id.layout_user_job)
    RelativeLayout layoutUserJob;
    @Bind(R.id.layout_user_city)
    RelativeLayout layoutUserCity;
    @Bind(R.id.layout_user_brief)
    RelativeLayout layoutUserBrief;
    @Bind(R.id.tv_user_name)
    TextView tvUserName;
    @Bind(R.id.tv_user_sex)
    TextView tvUserSex;
    @Bind(R.id.tv_user_job)
    TextView tvUserJob;
    @Bind(R.id.im_user_job)
    ImageView imUserJob;
    @Bind(R.id.tv_user_city)
    TextView tvUserCity;
    @Bind(R.id.tv_user_brief)
    TextView tvUserBrief;



    public static void startMemberEditorActivity(Activity activity, String name) {
        if (activity != null) {
            Intent intent = new Intent(activity, MemberEditorActivity.class);
            intent.putExtra("newName", name);
            activity.startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_editor);
        ButterKnife.bind(this);
        //initView();

    }



    private void initView() {
//        Intent intent;
//
//        startActivityForResult(intent,4000);
        String newName = getIntent().getExtras().getString("newName");
        DemoModel model = new DemoModel(this);
        tvUserName.setText(newName);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            switch (resultCode){
                case 1000:
                    String newName = data.getExtras().getString("newName");
                    ToastUtil.showShort(this,newName);
                    tvUserName.setText(newName);
                    break;
            }
        }
    }

    @OnClick({R.id.im_back, R.id.im_avatar, R.id.layout_user_name, R.id.layout_user_sex, R.id.layout_user_job, R.id.layout_user_city, R.id.layout_user_brief})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.im_back:
                finish();
                break;
            case R.id.im_avatar:
                break;
            case R.id.layout_user_name:
                startActivity(new Intent(this, ChangeNameActivity.class));
                break;
            case R.id.layout_user_sex:
               sexDialog();
                break;
            case R.id.layout_user_job:
                break;
            case R.id.layout_user_city:
                break;
            case R.id.layout_user_brief:
                startActivity(new Intent(this, BriefInputActivity.class));
                break;
        }
    }
    private void sexDialog(){
        final String items[]={"男","女"};
        AlertDialog.Builder builder=new AlertDialog.Builder(this);  //先得到构造器
        builder.setTitle("请选择性别"); //设置标题
        builder.setSingleChoiceItems(items,0,new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //dialog.dismiss();
                Toast.makeText(MemberEditorActivity.this, items[which], Toast.LENGTH_SHORT).show();
            }
        });
        builder.setPositiveButton("确定",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Toast.makeText(MemberEditorActivity.this, "确定", Toast.LENGTH_SHORT).show();
            }
        });
        builder.create().show();
    }
}
