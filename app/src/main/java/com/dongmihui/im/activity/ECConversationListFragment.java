package com.dongmihui.im.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import com.dongmihui.R;
import com.dongmihui.im.DemoHelper;
import com.dongmihui.im.constant.Constant;
import com.dongmihui.im.widget.AddPopWindow;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMConversationListener;
import com.hyphenate.EMError;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.ui.EaseConversationListFragment;
import com.hyphenate.easeui.widget.EaseConversationList;
import com.hyphenate.easeui.widget.EaseTitleBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;


/**
 *
 */
public class ECConversationListFragment extends EaseConversationListFragment {

    /**
     * 会话列表fragment
     */

    private final static int MSG_REFRESH = 2;
    protected EditText query;
    protected ImageButton clearSearch;
    protected boolean hidden;
    protected List<EMConversation> conversationList = new ArrayList<EMConversation>();
    protected EaseConversationList conversationListView;
    protected FrameLayout errorItemContainer;
    protected EaseTitleBar titleBar;
    protected boolean isConflict;

    protected EMConversationListener convListener = new EMConversationListener() {

        @Override
        public void onCoversationUpdate() {
            refresh();
        }

    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(com.hyphenate.easeui.R.layout.ease_fragment_conversation_list, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.getBoolean("isConflict", false))
            return;
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    protected void initView() {
        inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        conversationListView = (EaseConversationList) getView().findViewById(com.hyphenate.easeui.R.id.list);
        query = (EditText) getView().findViewById(com.hyphenate.easeui.R.id.query);
        // button to clear content in search bar
        clearSearch = (ImageButton) getView().findViewById(com.hyphenate.easeui.R.id.search_clear);
        errorItemContainer = (FrameLayout) getView().findViewById(com.hyphenate.easeui.R.id.fl_error_item);
        titleBar = (EaseTitleBar) getView().findViewById(R.id.title_bar);
        final Button occupation = (Button) titleBar.getOccupation();

        //设置标题的左图片
        titleBar.setLeftImageResource(R.drawable.em_contactlist_dmh);
        titleBar.setLeftLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(getContext(),"hehe",Toast.LENGTH_SHORT).show();
                //跳转到联系人列表
                startActivity(new Intent(getActivity(), ECContactListActivity.class));
            }
        });
        //弹出选择框 添加好友
        titleBar.setRightImageResource(R.drawable.em_add_dmh);
        titleBar.setRightLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(getContext(),"添加好友",Toast.LENGTH_SHORT).show();
                //自定义的popup
                AddPopWindow popu = new AddPopWindow(getActivity());
                popu.showPopupWindow(occupation);
            }
        });
    }

    EMMessageListener listen = new EMMessageListener() {
        @Override
        public void onMessageReceived(List<EMMessage> list) {
            for (EMMessage message : list) {
                DemoHelper.getInstance().getNotifier().onNewMsg(message);
            }
            refresh();
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> list) {

        }

        @Override
        public void onMessageReadAckReceived(List<EMMessage> list) {

        }

        @Override
        public void onMessageDeliveryAckReceived(List<EMMessage> list) {

        }

        @Override
        public void onMessageChanged(EMMessage emMessage, Object o) {

        }
    };



    @Override
    protected void setUpView() {
        conversationList.addAll(loadConversationList());
        conversationListView.init(conversationList);


        if (listItemClickListener != null) {
            conversationListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    EMConversation conversation = conversationListView.getItem(position);
                    String username = conversation.getUserName();
                    if (username.equals(EMClient.getInstance().getCurrentUser()))
                        Toast.makeText(getActivity(), R.string.Cant_chat_with_yourself, Toast.LENGTH_SHORT).show();
                    else {
                        // 开始聊天
                        Intent intent = new Intent(getActivity(), ECChatActivity.class);
                        if (conversation.isGroup()) {
                            if (conversation.getType() == EMConversation.EMConversationType.ChatRoom) {
                                // it's group chat
                                intent.putExtra(Constant.EXTRA_CHAT_TYPE, Constant.CHATTYPE_CHATROOM);
                            } else {
                                intent.putExtra(Constant.EXTRA_CHAT_TYPE, Constant.CHATTYPE_GROUP);
                            }

                        }
                        // 这是单人聊天
                        intent.putExtra(Constant.EXTRA_USER_ID, username);
                        startActivity(intent);
                    }
                }
            });
        }

        EMClient.getInstance().addConnectionListener(connectionListener);


        query.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                conversationListView.filter(s);
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

        conversationListView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideSoftKeyboard();
                return false;
            }
        });
    }


    protected EMConnectionListener connectionListener = new EMConnectionListener() {

        @Override
        public void onDisconnected(int error) {
            if (error == EMError.USER_REMOVED || error == EMError.USER_LOGIN_ANOTHER_DEVICE) {
                isConflict = true;
            } else {
                handler.sendEmptyMessage(0);
            }
        }

        @Override
        public void onConnected() {
            handler.sendEmptyMessage(1);
        }
    };
    private EaseConversationListItemClickListener listItemClickListener;

    protected Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    onConnectionDisconnected();
                    break;
                case 1:
                    onConnectionConnected();
                    break;

                case MSG_REFRESH: {
                    conversationList.clear();
                    conversationList.addAll(loadConversationList());
                    conversationListView.refresh();
                    break;
                }
                default:
                    break;
            }
        }
    };

    /**
     * 连接到服务器
     */
    protected void onConnectionConnected() {
        errorItemContainer.setVisibility(View.GONE);
    }

    /**
     * 与服务器断开连接
     */
    protected void onConnectionDisconnected() {
        errorItemContainer.setVisibility(View.VISIBLE);
    }


    /**
     * 刷新ui
     */
    public void refresh() {
        if (!handler.hasMessages(MSG_REFRESH)) {
            handler.sendEmptyMessage(MSG_REFRESH);
        }
    }

    /**
     * 加载会话列表
     *
     * @return +
     */
    protected List<EMConversation> loadConversationList() {
        // get all conversations
        Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();
        List<Pair<Long, EMConversation>> sortList = new ArrayList<Pair<Long, EMConversation>>();
        /**
         * 如果有新的消息在分类期间，lastMsgTime会改变所以使用同步来确保最后的时间戳信息不会改变。
         */
        synchronized (conversations) {
            for (EMConversation conversation : conversations.values()) {
                if (conversation.getAllMessages().size() != 0) {
                    sortList.add(new Pair<Long, EMConversation>(conversation.getLastMessage().getMsgTime(), conversation));
                }
            }
        }
        try {
            // Internal is TimSort algorithm, has bug
            sortConversationByLastChatTime(sortList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<EMConversation> list = new ArrayList<EMConversation>();
        for (Pair<Long, EMConversation> sortItem : sortList) {
            list.add(sortItem.second);
        }
        return list;
    }

    /**
     * 最后一条消息的谈话显示时间戳进行排序
     *
     * @param conversationList
     */
    private void sortConversationByLastChatTime(List<Pair<Long, EMConversation>> conversationList) {
        Collections.sort(conversationList, new Comparator<Pair<Long, EMConversation>>() {
            @Override
            public int compare(final Pair<Long, EMConversation> con1, final Pair<Long, EMConversation> con2) {

                if (con1.first.equals(con2.first)) {
                    return 0;
                } else if (con2.first.longValue() > con1.first.longValue()) {
                    return 1;
                } else {
                    return -1;
                }
            }

        });
    }

    protected void hideSoftKeyboard() {
        if (getActivity().getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getActivity().getCurrentFocus() != null)
                inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        this.hidden = hidden;
        if (!hidden && !isConflict) {
            refresh();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        EMClient.getInstance().chatManager().addMessageListener(listen);
        if (!hidden) {
            refresh();
        }
    }

    @Override
    public void onStop() {
        EMClient.getInstance().chatManager().removeMessageListener(listen);
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EMClient.getInstance().removeConnectionListener(connectionListener);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (isConflict) {
            outState.putBoolean("isConflict", true);
        }
    }

    public interface EaseConversationListItemClickListener {
        /**
         * 会话列表，请单击事件
         *
         * @param conversation -- clicked item
         */
        void onListItemClicked(EMConversation conversation);
    }

    /**
     * 会话列表项设置点击
     *
     * @param listItemClickListener
     */
    public void setConversationListItemClickListener(EaseConversationListItemClickListener listItemClickListener) {
        this.listItemClickListener = listItemClickListener;


    }


}


