/**
 * 添加联系人界面
 */
package com.dongmihui.im.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.dongmihui.R;
import com.dongmihui.adapter.ViewHolder;
import com.dongmihui.adapter.base.BaseListAdapter;
import com.dongmihui.api.IMApi;
import com.dongmihui.bean.ApiMessage;
import com.dongmihui.bean.ContactListBean;
import com.dongmihui.bean.GroupBean;
import com.dongmihui.im.DemoHelper;
import com.dongmihui.utils.ToastUtil;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMGroupInfo;
import com.hyphenate.easeui.widget.EaseAlertDialog;
import com.hyphenate.exceptions.HyphenateException;

import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AddContactActivity extends BaseActivity implements BaseListAdapter.Callback{
    @Bind(R.id.lv_contact_list)
    ListView lvContactList;
    private EditText editText;
    //	private RelativeLayout searchedUserLayout;
//	private TextView nameText;
    private Button searchBtn;
    private String toAddUsername;
    private ProgressDialog progressDialog;
    BaseListAdapter adapter;
    IMApi api;

    public boolean isContact;

    public static EMGroup searchedGroup;

    /**
     *
     * @param activity
     * @param from contact , group
     */
    public static void startAddContactActivity(Activity activity,String from) {
        if (activity != null) {
            Intent intent = new Intent(activity, AddContactActivity.class);
            intent.putExtra("from", from);
            activity.startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.em_activity_add_contact);
        ButterKnife.bind(this);
        String from = getIntent().getStringExtra("from");
        if (TextUtils.equals(from, "group")) {
            isContact = false;
        }else {
            isContact = true;
        }
        TextView mTextView = (TextView) findViewById(R.id.add_list_friends);
        api = new IMApi();
        editText = (EditText) findViewById(R.id.edit_note);
        String strUserName="";
        String strAdd ="";
        if (isContact) {
            strAdd = getResources().getString(R.string.add_friend);
            strUserName = getResources().getString(R.string.user_name);
        }else {
            strAdd = "添加群组";
            strUserName="群组名称";
        }
        mTextView.setText(strAdd);

        editText.setHint(strUserName);
//		searchedUserLayout = (RelativeLayout) findViewById(R.id.ll_user);
//		nameText = (TextView) findViewById(R.id.name);
        searchBtn = (Button) findViewById(R.id.search);
        searchedGroup = null;
    }

    public void initListView(List<ContactListBean> beans) {
        if (adapter == null) {
            adapter = new AddContactListAdapter(this);
            lvContactList.setAdapter(adapter);
        }
        if (beans == null) {
            new EaseAlertDialog(this,"无此人").show();
            return;
        }
        adapter.clear();
        adapter.addItem(beans);
    }

    public void initListViewFromGroup(List<GroupBean> beans) {
        if (adapter == null) {
            adapter = new AddGroupListAdapter(this);
            lvContactList.setAdapter(adapter);
        }
        if (beans == null) {
            new EaseAlertDialog(this,"无此人").show();
            return;
        }
        adapter.clear();
        adapter.addItem(beans);
    }
    /**
     * 搜索
     *
     * @param v
     */
    public void searchContact(View v) {
        final String name = editText.getText().toString();
        String saveText = searchBtn.getText().toString();

        if (getString(R.string.button_search).equals(saveText)) {
            toAddUsername = name;
            if (TextUtils.isEmpty(name)) {
                new EaseAlertDialog(this,"输入不可为空").show();
                return;
            }

            if (isContact) {
                searchContact(name);
            }else {
                searchGroup(name);
            }


//            //如果用户存在，显示userame并添加按钮
//            searchedUserLayout.setVisibility(View.VISIBLE);
//            nameText.setText(toAddUsername);

        }
    }

    public void searchContact(String name) {
        progressDialog = new ProgressDialog(this);
        String stri = "搜索中请稍候。。。";
        progressDialog.setMessage(stri);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        // TODO 你可以在这里搜索用户从您的应用程序服务器
        api.getContactList(name, new Callback<ApiMessage<List<ContactListBean>>>() {
            @Override
            public void onResponse(Call<ApiMessage<List<ContactListBean>>> call, Response<ApiMessage<List<ContactListBean>>> response) {
                ApiMessage<List<ContactListBean>> body = response.body();
                if (body.getCode() == 0) {
                    Toast.makeText(AddContactActivity.this, body.getMsg(), Toast.LENGTH_SHORT).show();
                } else if (body.getCode() == 1) {
                    initListView(body.getResult());
                }
                Log.d("AddContactActivity", "body.getResult():" + body.getResult());
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ApiMessage<List<ContactListBean>>> call, Throwable t) {
                Log.d("AddContactActivity", t.toString());
                progressDialog.dismiss();
            }
        });
    }

    /**
     * search group with group id
     * @param
     */
    public void searchGroup(final String  groupName){
        if(TextUtils.isEmpty(groupName.trim())){
            return;
        }

        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage(getResources().getString(R.string.searching));
        pd.setCancelable(false);
        pd.show();

        api.getGroupList(groupName.trim(), new Callback<ApiMessage<List<GroupBean>>>() {
            @Override
            public void onResponse(Call<ApiMessage<List<GroupBean>>> call, Response<ApiMessage<List<GroupBean>>> response) {
                ApiMessage<List<GroupBean>> body = response.body();
                if (body.getCode() == 0) {
                    Toast.makeText(AddContactActivity.this, body.getMsg(), Toast.LENGTH_SHORT).show();
                } else if (body.getCode() == 1) {
                    initListViewFromGroup(body.getResult());
                }
                pd.dismiss();
            }

            @Override
            public void onFailure(Call<ApiMessage<List<GroupBean>>> call, Throwable t) {
                Log.d("AddContactActivity", t.toString());
                pd.dismiss();
            }
        });


    }

    /**
     * 添加联系人
     *
     * @param imName
     */
    public void addContact(final String imName) {
        if (EMClient.getInstance().getCurrentUser().equals(imName)) {
            new EaseAlertDialog(this, R.string.not_add_myself).show();
            return;
        }
        ToastUtil.ShortToast(imName);
        if (DemoHelper.getInstance().getContactList().containsKey(imName)) {
            //let the user know the contact already in your contact list
            if (EMClient.getInstance().contactManager().getBlackListUsernames().contains(imName)) {
                new EaseAlertDialog(this, R.string.user_already_in_contactlist).show();
                return;
            }
            new EaseAlertDialog(this, R.string.This_user_is_already_your_friend).show();
            return;
        }

        progressDialog = new ProgressDialog(this);
        String stri = getResources().getString(R.string.Is_sending_a_request);
        progressDialog.setMessage(stri);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        new Thread(new Runnable() {
            public void run() {

                try {
                    //demo use a hardcode reason here, you need let user to input if you like
                    String s = getResources().getString(R.string.Add_a_friend);
                    EMClient.getInstance().contactManager().addContact(imName, s);
                    runOnUiThread(new Runnable() {
                        public void run() {
                            progressDialog.dismiss();
                            String s1 = getResources().getString(R.string.send_successful);
                            Toast.makeText(getApplicationContext(), s1, Toast.LENGTH_LONG).show();
                        }
                    });
                } catch (final Exception e) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            progressDialog.dismiss();
                            String s2 = getResources().getString(R.string.Request_add_buddy_failure);
                            Toast.makeText(getApplicationContext(), s2 + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        }).start();
    }

    public void back(View v) {
        finish();
    }

    @Override
    public synchronized RequestManager getImgLoader() {
        return Glide.with(this);
    }

    @Override
    public Context getContext() {
        return getBaseContext();
    }

    @Override
    public Date getSystemTime() {
        return new Date();
    }

    public class AddGroupListAdapter extends BaseListAdapter<GroupBean> {
        public AddGroupListAdapter(Callback callback) {
            super(callback);
        }

        @Override
        protected void convert(ViewHolder vh, final GroupBean item, int position) {
            vh.setText(R.id.name,item.getGroupName());
            vh.setImageForNet(R.id.avatar,item.getAvatar());
            vh.setGone(R.id.indicator);
            View.OnClickListener onClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EMGroupInfo info = new EMGroupInfo(item.getNumber(), item.getGroupName());
                    GroupSimpleDetailActivity.startGroupSimpleDetailActivity(AddContactActivity.this, info);
                    Log.d("AddGroupListAdapter", item.toString());
                }
            };
            vh.setOnClick(R.id.ll_user, onClickListener);
            vh.setOnClick(R.id.name,onClickListener);
            vh.setOnClick(R.id.avatar,onClickListener);
        }

        @Override
        protected int getLayoutId(int position, GroupBean item) {
            return R.layout.add_contact_list_item;
        }
    }


    public class AddContactListAdapter extends BaseListAdapter<ContactListBean>{

        public AddContactListAdapter(Callback callback) {
            super(callback);
        }

        @Override
        protected void convert(ViewHolder vh, final ContactListBean item, int position) {
            vh.setText(R.id.name,item.getNickName());
            vh.setImageForNet(R.id.avatar,item.getAvatar());
            vh.setOnClick(R.id.indicator, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addContact(item.getUserName());
                }
            });
        }

        @Override
        protected int getLayoutId(int position, ContactListBean item) {
            return R.layout.add_contact_list_item;
        }
    }

}
