
package com.dongmihui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;


import com.dongmihui.im.DemoHelper;
import com.dongmihui.listen.BaseViewInterface;
import com.dongmihui.listen.OnTabReselectListener;
import com.dongmihui.R;
import com.dongmihui.widget.MyFragmentTabHost;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMMessage;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class MainActivity extends BaseFragmentActivity  implements
        TabHost.OnTabChangeListener, BaseViewInterface, View.OnClickListener,
        View.OnTouchListener{

    private TextView tabMes;

    public static void startMainActivity(Activity activity) {
        if (activity != null) {
            Intent intent = new Intent(activity, MainActivity.class);
            activity.startActivity(intent);
        }
    }

    @Bind(android.R.id.tabhost)
    MyFragmentTabHost mTabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();

    }
    @Override
    public void onClick(View view) {

    }
    private long mExitTime;
    @Override
    public void onBackPressed()
    {
        long curTime = SystemClock.uptimeMillis();
        if ((curTime - mExitTime) < (3 * 1000)) {
            finish();
        } else {
            mExitTime = curTime;
            Toast.makeText(this,"再按一次退出董秘荟", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void onTabChanged(String tabId) {
        final int size = mTabHost.getTabWidget().getTabCount();
        for (int i = 0; i < size; i++) {
            View v = mTabHost.getTabWidget().getChildAt(i);
            if (i == mTabHost.getCurrentTab()) {
                v.setSelected(true);
            } else {
                v.setSelected(false);
            }
        }

        supportInvalidateOptionsMenu();
    }

    @Override
    public void initData() {

    }

    public void initView() {
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
        // mAddBt.setOnClickListener(this);
        initTabs();
        mTabHost.setCurrentTab(0);
        mTabHost.setOnTabChangedListener(this);


    }
    private void initTabs() {
        MainTab[] tabs = MainTab.values();
        int size = tabs.length;


        // 得到fragment的个数
        for (int i = 0; i < size; i++) {
            MainTab mainTab = tabs[i];


            View indicator = View.inflate(this,R.layout.tab_indicator, null);



            TabHost.TabSpec tab = mTabHost.newTabSpec(getString(mainTab.getResName()));
            TextView title = (TextView) indicator.findViewById(R.id.tab_title);
            ImageView icon = (ImageView) indicator.findViewById(R.id.iv_icon);

            Drawable drawable = this.getResources().getDrawable(mainTab.getResIcon());
            icon.setImageDrawable(drawable);
            if (i == 2) {
//                indicator.setVisibility(View.INVISIBLE);
//                mTabHost.setNoTabChangedTag(getString(mainTab.getResName()));
                tabMes = (TextView) indicator.findViewById(R.id.tab_mes);

            }
            title.setText(getString(mainTab.getResName()));
            tab.setIndicator(indicator);



            tab.setContent(new TabHost.TabContentFactory() {

                @Override
                public View createTabContent(String tag) {
                    return new View(MainActivity.this);
                }
            });
            mTabHost.addTab(tab, mainTab.getClz(), null);
            mTabHost.getTabWidget().getChildAt(i).setOnTouchListener(this);
        }
    }
    private Fragment getCurrentFragment() {
        return getSupportFragmentManager().findFragmentByTag(
                mTabHost.getCurrentTabTag());
    }

    @Override
    protected void onResume() {
        super.onResume();
        DemoHelper.getInstance().pushActivity(this);
        refreshUnreadMsgs();
        EMClient.getInstance().chatManager().addMessageListener(messageListener);
    }

    public void refreshUnreadMsgs() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int unreadMsgsCount = EMClient.getInstance().chatManager().getUnreadMsgsCount();
                if (unreadMsgsCount > 0) {
                    tabMes.setVisibility(View.VISIBLE);
                }else {
                    tabMes.setVisibility(View.GONE);
                }
            }
        });

    }

    @Override
    protected void onStop() {
        DemoHelper.getInstance().popActivity(this);
        super.onStop();
        EMClient.getInstance().chatManager().removeMessageListener(messageListener);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        super.onTouchEvent(event);
        boolean consumed = false;
        // use getTabHost().getCurrentTabView to decide if the current tab is
        // touched again
        if (event.getAction() == MotionEvent.ACTION_DOWN
                && v.equals(mTabHost.getCurrentTabView())) {
            // use getTabHost().getCurrentView() to get a handle to the view
            // which is displayed in the tab - and to get this views context
            Fragment currentFragment = getCurrentFragment();
            if (currentFragment != null
                    && currentFragment instanceof OnTabReselectListener) {
                OnTabReselectListener listener = (OnTabReselectListener) currentFragment;
                listener.onTabReselect();
                consumed = true;
            }
        }
        return consumed;
    }
    EMMessageListener messageListener = new EMMessageListener() {

        @Override
        public void onMessageReceived(List<EMMessage> messages) {
            // 通知新消息
            for (EMMessage message : messages) {
                DemoHelper.getInstance().getNotifier().onNewMsg(message);
            }
            refreshUnreadMsgs();
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> messages) {

        }

        @Override
        public void onMessageReadAckReceived(List<EMMessage> messages) {
        }

        @Override
        public void onMessageDeliveryAckReceived(List<EMMessage> message) {
        }

        @Override
        public void onMessageChanged(EMMessage message, Object change) {
        }
    };




}
