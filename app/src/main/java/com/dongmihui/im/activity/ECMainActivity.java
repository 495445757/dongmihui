package com.dongmihui.im.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dongmihui.R;
import com.dongmihui.im.DemoHelper;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.ui.EaseBaseActivity;


/**
 * @deprecated instead of use {@link com.dongmihui.activity.MainActivity}
 *
 */
public class ECMainActivity extends EaseBaseActivity {

    // 发起聊天 username 输入框
    private EditText mChatIdEdit;
    // 发起聊天
    private Button mStartChatBtn;
    // 退出登录
    private Button mSignOutBtn;
    //联系人列表页
    private Button mToContactBtn;
    //会话列表
    private Button mToConversation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 判断sdk是否登录成功过，并没有退出和被踢，否则跳转到登陆界面
        if (!EMClient.getInstance().isLoggedInBefore()) {
            Intent intent = new Intent(ECMainActivity.this, ECLoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        setContentView(R.layout.activity_main);

        initView();
    }


    @Override
    protected void onResume() {
        super.onResume();
        //注册当前Activity，收到消息不发生通知
        DemoHelper.getInstance().pushActivity(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //注销当前Activity，收到消息以通知形式发送
        DemoHelper.getInstance().popActivity(this);
    }

    /**
     * 初始化界面
     */
    private void initView() {

//        mChatIdEdit = (EditText) findViewById(R.id.ec_edit_chat_id);
//
//        mStartChatBtn = (Button) findViewById(R.id.ec_btn_start_chat);
        mStartChatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 获取我们发起聊天的者的username
                String chatId = mChatIdEdit.getText().toString().trim();
                if (!TextUtils.isEmpty(chatId)) {
                    // 获取当前登录用户的 username
                    String currUsername = EMClient.getInstance().getCurrentUser();
                    if (chatId.equals(currUsername)) {
                        Toast.makeText(ECMainActivity.this, "不能和自己聊天", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    // 跳转到聊天界面，开始聊天
                    Intent intent = new Intent(ECMainActivity.this, ECChatActivity.class);
                    // EaseUI封装的聊天界面需要这两个参数，聊天者的username，以及聊天类型，单聊还是群聊
                    intent.putExtra("userId", chatId);
                    intent.putExtra("chatType", EMMessage.ChatType.Chat);
                    startActivity(intent);
                } else {
                    Toast.makeText(ECMainActivity.this, "Username 不能为空", Toast.LENGTH_LONG).show();
                }
            }
        });

//        mSignOutBtn = (Button) findViewById(R.id.ec_btn_sign_out);
//        mSignOutBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                signOut();
//            }
//        });
//        mToContactBtn = (Button) findViewById(R.id.ec_btn_to_contact);
//        mToContactBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(ECMainActivity.this,ECContactListActivity.class));
//            }
//        });
//        mToConversation = (Button) findViewById(R.id.ec_btn_conversation);
        mToConversation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ECMainActivity.this,ECConversationActivity.class));
            }
        });
    }

    /**
     * 退出登录
     */
    private void signOut() {
        // 调用sdk的退出登录方法，第一个参数表示是否解绑推送的token，没有使用推送或者被踢都要传false
        EMClient.getInstance().logout(false, new EMCallBack() {
            @Override
            public void onSuccess() {
                Log.i("lzan13", "logout success");
                // 调用退出成功，结束app
                finish();
            }

            @Override
            public void onError(int i, String s) {
                Log.i("lzan13", "logout error " + i + " - " + s);
            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
    }
}
