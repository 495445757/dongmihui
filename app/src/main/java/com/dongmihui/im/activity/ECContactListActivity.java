package com.dongmihui.im.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.dongmihui.R;
import com.dongmihui.im.DemoHelper;
import com.dongmihui.im.fragment.ContactListFragment;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.exceptions.HyphenateException;

import java.util.List;
import java.util.Map;



/**
 * 联系人列表页
 */
public class ECContactListActivity extends BaseActivity {


    ContactListFragment fragment;

    public static void startECContactListActivity(Activity activity) {
        if (activity != null) {
            Intent intent = new Intent(activity, ECContactListActivity.class);
            activity.startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eccontact_list);

        Map<String, EaseUser> contactList = DemoHelper.getInstance().getContactList();
        for (EaseUser user : contactList.values()) {
            Log.d("TAG", user.getUsername());
        }

        fragment = new ContactListFragment();
        fragment.setContactListItemClickListener(new ContactListFragment.EaseContactListItemClickListener() {
            @Override
            public void onListItemClicked(EaseUser user) {
                Log.e("TAG", user.getUsername());
//                Intent intent = new Intent(ECContactListActivity.this, ECChatActivity.class);
//                intent.putExtra("userId", user.getUsername());
//                intent.putExtra("chatType", EMMessage.ChatType.Chat);
//                startActivity(intent);
            }
        });

        //需要获取联系人列表
        fragment.setContactsMap(contactList);



        fragment.setContactListItemClickListener(new ContactListFragment.EaseContactListItemClickListener() {
            @Override
            public void onListItemClicked(EaseUser user) {
                //点击事件
            }
        });
        getSupportFragmentManager().beginTransaction().add(R.id.fl_contact, fragment).commit();
    }

    public Map<String,EaseUser> getContacts(){


            Log.d("Contact", "获取联系人列表");
            new Thread(){
                @Override
                public void run() {
                    super.run();
                    try {
                    List<String> allContactsFromServer = EMClient.getInstance().contactManager().getAllContactsFromServer();
                    Log.d("allContact", allContactsFromServer.toString());
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
                }
            }.start();



        return null;
    }

}
