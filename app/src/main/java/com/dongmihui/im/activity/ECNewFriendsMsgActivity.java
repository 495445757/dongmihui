package com.dongmihui.im.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.dongmihui.R;
import com.dongmihui.im.adapter.NewFriendsMsgAdapter;
import com.dongmihui.im.bean.InviteMessage;
import com.dongmihui.im.dao.InviteMessgeDao;
import com.hyphenate.easeui.ui.EaseBaseActivity;

import java.util.List;


import butterknife.Bind;
import butterknife.ButterKnife;


public class ECNewFriendsMsgActivity extends BaseActivity {

    @Bind(R.id.rv_list)
    RecyclerView recyclerView;
    @Bind(R.id.iv_back_contact)
    ImageView ivBackContact;
    @Bind(R.id.activity_ecnew_friends_msg)
    LinearLayout activityEcnewFriendsMsg;

    public static void startECNewFriendsMsgActivity(Activity activity) {
        if (activity != null) {
            Intent intent = new Intent(activity, ECNewFriendsMsgActivity.class);
            activity.startActivity(intent);

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ecnew_friends_msg);
        ButterKnife.bind(this);
        InviteMessgeDao dao = new InviteMessgeDao(this);
        List<InviteMessage> msgs = dao.getMessagesList();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new NewFriendsMsgAdapter(msgs, this));

        ivBackContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        dao.saveUnreadMessageCount(0);
    }




}
