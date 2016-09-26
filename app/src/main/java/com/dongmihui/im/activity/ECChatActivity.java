package com.dongmihui.im.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.dongmihui.R;
import com.hyphenate.easeui.ui.EaseBaseActivity;
import com.hyphenate.util.EasyUtils;



public class ECChatActivity extends BaseActivity {

    // 当前聊天的 ID
    private String mChatId;
    private ChatFragment fragment;
    /**
     * 挑战聊天界面
     * @param activity
     * @param  id 群名或者用户名
     * @param type 是群聊还是单聊
     */
    public static void startECChatActivity(Activity activity, String id, int type) {
        if (activity != null) {
            Intent intent = new Intent(activity, ECChatActivity.class);
            // EaseUI封装的聊天界面需要这两个参数，聊天者的username，以及聊天类型，单聊还是群聊
            intent.putExtra("userId", id);
            intent.putExtra("chatType", type);
            activity.startActivity(intent);
        }
    }
    private ChatFragment chatFragment;
    public static ECChatActivity activityInstance;
    String toChatUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        toChatUsername =  getIntent().getExtras().getString("userId");
        activityInstance = this;
        // 这里直接使用EaseUI封装好的聊天界面
        chatFragment = new ChatFragment();
        // 将参数传递给聊天界面
        chatFragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().add(R.id.ec_layout_container, chatFragment).commit();

        initView();
    }

    /**
     * 初始化界面
     */
    private void initView() {

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        activityInstance = null;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        // 确保只有一个聊天界面打开
        String username = intent.getStringExtra("userId");
        if (toChatUsername.equals(username))
            super.onNewIntent(intent);
        else {
            finish();
            startActivity(intent);
        }

    }

    @Override
    public void onBackPressed() {
        chatFragment.onBackPressed();
        if (EasyUtils.isSingleActivity(this)) {
            Intent intent = new Intent(this, ECMainActivity.class);
            startActivity(intent);
        }
    }

    public String getToChatUsername(){
        return toChatUsername;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        //PermissionsManager.getInstance().notifyPermissionsChange(permissions, grantResults);
    }

}
