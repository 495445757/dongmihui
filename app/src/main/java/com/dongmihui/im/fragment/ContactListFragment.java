package com.dongmihui.im.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.dongmihui.R;
import com.dongmihui.im.DemoHelper;
import com.dongmihui.im.activity.ECChatActivity;
import com.dongmihui.im.activity.ECNewFriendsMsgActivity;
import com.dongmihui.im.activity.GroupsActivity;
import com.dongmihui.im.dao.InviteMessgeDao;
import com.dongmihui.im.dao.UserDao;
import com.dongmihui.widget.ContactItemView;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.ui.EaseBaseFragment;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.hyphenate.easeui.widget.EaseContactList;
import com.hyphenate.exceptions.HyphenateException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;



/**
 * Created by Administrator on 2016/9/19.
 */
public class ContactListFragment extends EaseBaseFragment {
    private static final String TAG = "ContactListFragment";

    protected Handler handler = new Handler();
    protected EaseUser toBeProcessUser;
    protected String toBeProcessUsername;
    protected EaseContactList contactListLayout;
    protected boolean isConflict;

    protected boolean hidden;

    //新的朋友条目
    ContactItemView mCivNewFriends;
    //联系人条目
    protected List<EaseUser> contactList;
    //清楚搜索框按钮
    protected ImageButton clearSearch;
    //搜索框
    protected EditText query;
    //联系人FrameLayout
    protected FrameLayout contentContainer;
    //联系人ListView
    protected ListView listView;
    //返回按钮
    private ImageView ivBack;
    //外部传入好友列表
    private Map<String, EaseUser> contactsMap;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contact_list, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        //to avoid crash when open app after long time stay in background after user logged into another device
        if(savedInstanceState != null && savedInstanceState.getBoolean("isConflict", false))
            return;
        super.onActivityCreated(savedInstanceState);
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initView() {
        contentContainer = (FrameLayout) getView().findViewById(R.id.content_container);
        contactListLayout = (EaseContactList) getView().findViewById(R.id.contact_list);

        listView = contactListLayout.getListView();
        //好友列表的头布局，用到自定义View ContactItemView 文件
        View headView = getLayoutInflater(getArguments()).inflate(R.layout.head_list, listView,false);
        listView.addHeaderView(headView);
        mCivNewFriends = (ContactItemView) headView.findViewById(R.id.civ_new_friends);
        mCivNewFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ECNewFriendsMsgActivity.startECNewFriendsMsgActivity(getActivity());
            }
        });
        showUnreaView();
        headView.findViewById(R.id.civ_groups).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 头布局中监听事件
                getActivity().startActivity(new Intent(getActivity(),GroupsActivity.class));
            }
        });
        //search
        query = (EditText) getView().findViewById(R.id.query);
        clearSearch = (ImageButton) getView().findViewById(R.id.search_clear);
        ivBack = (ImageView) getView().findViewById(R.id.iv_back_contact);
        ivBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
        registerForContextMenu(listView);
    }

    /**
     *数据适配添加监听
     */
    @Override
    protected void setUpView() {
        EMClient.getInstance().addConnectionListener(connectionListener);

        contactList = new ArrayList<EaseUser>();
        getContactList();
        //init list
        contactListLayout.init(contactList);

        if(listItemClickListener != null){
            Log.i("TAG","！为空");
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (position == 0) {
                        return;
                    }
                    Log.i("TAG", "item"+position);
                    EaseUser user = (EaseUser)listView.getItemAtPosition(position);
                    Log.i("TAG", "item"+position + user.getUsername());
                    listItemClickListener.onListItemClicked(user);
                    Intent intent = new Intent(getActivity(), ECChatActivity.class);
                    intent.putExtra("userId", user.getUsername());
                    intent.putExtra("chatType", EMMessage.ChatType.Chat);
                    startActivity(intent);
                }
            });
        }

        query.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                contactListLayout.filter(s);
                if (s.length() > 0) {
                    clearSearch.setVisibility(View.VISIBLE);
                } else {
                    clearSearch.setVisibility(View.INVISIBLE);

                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
            }
        });
        clearSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                query.getText().clear();
                hideSoftKeyboard();
            }
        });

        listView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideSoftKeyboard();
                return false;
            }
        });

    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if(((AdapterView.AdapterContextMenuInfo) menuInfo).position == 0){
            return;
        }
        toBeProcessUser = (EaseUser) listView.getItemAtPosition(((AdapterView.AdapterContextMenuInfo) menuInfo).position);
        toBeProcessUsername = toBeProcessUser.getUsername();
        getActivity().getMenuInflater().inflate(R.menu.em_context_contact_list, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.delete_contact) {
            try {
                // delete contact
                deleteContact(toBeProcessUser);
                // remove invitation message
                InviteMessgeDao dao = new InviteMessgeDao(getActivity());
                dao.deleteMessage(toBeProcessUser.getUsername());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }else if(item.getItemId() == R.id.add_to_blacklist){
            moveToBlacklist(toBeProcessUsername);
            return true;
        }
        return super.onContextItemSelected(item);
    }

    /**
     * 删除联系人
     * @param tobeDeleteUser
     */
    public void deleteContact(final EaseUser tobeDeleteUser) {
        String st1 = getResources().getString(R.string.deleting);
        final String st2 = getResources().getString(R.string.Delete_failed);
        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setMessage(st1);
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        new Thread(new Runnable() {
            public void run() {
                try {
                    EMClient.getInstance().contactManager().deleteContact(tobeDeleteUser.getUsername());
                    // remove user from memory and database
                    UserDao dao = new UserDao(getActivity());
                    dao.deleteContact(tobeDeleteUser.getUsername());
                    DemoHelper.getInstance().getContactList().remove(tobeDeleteUser.getUsername());
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            pd.dismiss();
                            contactList.remove(tobeDeleteUser);
                            contactListLayout.refresh();

                        }
                    });
                } catch (final Exception e) {
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            pd.dismiss();
                            Toast.makeText(getActivity(), st2 + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });

                }

            }
        }).start();

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        this.hidden = hidden;
        if (!hidden) {
            refresh();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!hidden) {
            refresh();
        }
    }


    /**
     * 移入黑名单
     */
    protected void moveToBlacklist(final String username){
        final ProgressDialog pd = new ProgressDialog(getActivity());
        String st1 = getResources().getString(com.hyphenate.easeui.R.string.Is_moved_into_blacklist);
        final String st2 = getResources().getString(com.hyphenate.easeui.R.string.Move_into_blacklist_success);
        final String st3 = getResources().getString(com.hyphenate.easeui.R.string.Move_into_blacklist_failure);
        pd.setMessage(st1);
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        new Thread(new Runnable() {
            public void run() {
                try {
                    //move to blacklist
                    EMClient.getInstance().contactManager().addUserToBlackList(username,false);
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            pd.dismiss();
                            Toast.makeText(getActivity(), st2, Toast.LENGTH_SHORT).show();
                            refresh();
                        }
                    });
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            pd.dismiss();
                            Toast.makeText(getActivity(), st3, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();

    }
    InviteMessgeDao inviteMessgeDao;
    // refresh ui
    public void refresh() {
        getContactList();
        contactListLayout.refresh();
        showUnreaView();
    }

    public void showUnreaView(){
        //判断是否有好友添加的请求，有责显示红色点
        if (inviteMessgeDao == null) {
            inviteMessgeDao = new InviteMessgeDao(getActivity());
        }
        if (inviteMessgeDao.getUnreadMessagesCount() > 0) {
            mCivNewFriends.showUnreadMsgView();
        }else{
            mCivNewFriends.hideUnreadMsgView();
        }
    }

    @Override
    public void onDestroy() {

        EMClient.getInstance().removeConnectionListener(connectionListener);

        super.onDestroy();
    }


    /**
     * get contact list and sort, will filter out users in blacklist
     */
    protected void getContactList() {
        contactList.clear();
        if(contactsMap == null){
            return;
        }
        synchronized (this.contactsMap) {
            Iterator<Map.Entry<String, EaseUser>> iterator = contactsMap.entrySet().iterator();
            List<String> blackList = EMClient.getInstance().contactManager().getBlackListUsernames();
            while (iterator.hasNext()) {
                Map.Entry<String, EaseUser> entry = iterator.next();
                // to make it compatible with data in previous version, you can remove this check if this is new app
                if (!entry.getKey().equals("item_new_friends")
                        && !entry.getKey().equals("item_groups")
                        && !entry.getKey().equals("item_chatroom")
                        && !entry.getKey().equals("item_robots")){
                    if(!blackList.contains(entry.getKey())){
                        //filter out users in blacklist
                        EaseUser user = entry.getValue();
                        EaseCommonUtils.setUserInitialLetter(user);
                        contactList.add(user);
                    }
                }
            }
        }

        // sorting
        Collections.sort(contactList, new Comparator<EaseUser>() {

            @Override
            public int compare(EaseUser lhs, EaseUser rhs) {
                if(lhs.getInitialLetter().equals(rhs.getInitialLetter())){
                    return lhs.getNick().compareTo(rhs.getNick());
                }else{
                    if("#".equals(lhs.getInitialLetter())){
                        return 1;
                    }else if("#".equals(rhs.getInitialLetter())){
                        return -1;
                    }
                    return lhs.getInitialLetter().compareTo(rhs.getInitialLetter());
                }

            }
        });

    }



    protected EMConnectionListener connectionListener = new EMConnectionListener() {

        @Override
        public void onDisconnected(int error) {
            if (error == EMError.USER_REMOVED || error == EMError.USER_LOGIN_ANOTHER_DEVICE) {
                isConflict = true;
            } else {
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        onConnectionDisconnected();
                    }

                });
            }
        }

        @Override
        public void onConnected() {
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    onConnectionConnected();
                }

            });
        }
    };
    private EaseContactListItemClickListener listItemClickListener;


    protected void onConnectionDisconnected() {

    }

    protected void onConnectionConnected() {

    }

    /**
     * set contacts map, key is the hyphenate id
     * @param contactsMap
     */
    public void setContactsMap(Map<String, EaseUser> contactsMap){
        this.contactsMap = contactsMap;
    }

    public interface EaseContactListItemClickListener {
        /**
         * on click event for item in contact list
         * @param user --the user of item
         */
        void onListItemClicked(EaseUser user);
    }

    /**
     * set contact list item click listener
     * @param listItemClickListener
     */
    public void setContactListItemClickListener(EaseContactListItemClickListener listItemClickListener){
        this.listItemClickListener = listItemClickListener;
    }
}
