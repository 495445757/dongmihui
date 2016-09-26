package com.dongmihui.im.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dongmihui.R;
import com.dongmihui.common.AppContext;
import com.dongmihui.im.bean.InviteMessage;
import com.dongmihui.im.dao.InviteMessgeDao;
import com.hyphenate.chat.EMClient;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * Created by Administrator on 2016/9/21.
 */

public class NewFriendsMsgAdapter extends RecyclerView.Adapter<NewFriendsMsgAdapter.ViewHolder> {
    List<InviteMessage> msgs;
    Context context;
    InviteMessgeDao messgeDao;

    public NewFriendsMsgAdapter(List<InviteMessage> msgs, Context context) {
        this.msgs = msgs;
        this.context = context;
        messgeDao = new InviteMessgeDao(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.em_row_invite_msg, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setData(msgs.get(position));
    }


    @Override
    public int getItemCount() {
        return msgs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.avatar)
        ImageView avatar;
        @Bind(R.id.avatar_container)
        RelativeLayout avatarContainer;
        @Bind(R.id.name)
        TextView name;
        @Bind(R.id.msg_state)
        ImageView msgState;
        @Bind(R.id.message)
        TextView message;
        @Bind(R.id.agree)
        Button agree;
        @Bind(R.id.user_state)
        Button userState;
        @Bind(R.id.tv_groupName)
        TextView tvGroupName;
        @Bind(R.id.ll_group)
        LinearLayout llGroup;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        String str1 = AppContext.getInstance().getResources().getString(R.string.Has_agreed_to_your_friend_request);
        String str2 = AppContext.getInstance().getResources().getString(R.string.agree);

        String str3 = AppContext.getInstance().getResources().getString(R.string.Request_to_add_you_as_a_friend);
        String str4 = AppContext.getInstance().getResources().getString(R.string.Apply_to_the_group_of);
        String str5 = AppContext.getInstance().getResources().getString(R.string.Has_agreed_to);
        String str6 = AppContext.getInstance().getResources().getString(R.string.Has_refused_to);

        String str7 = AppContext.getInstance().getResources().getString(R.string.refuse);
        String str8 = AppContext.getInstance().getResources().getString(R.string.invite_join_group);
        String str9 = AppContext.getInstance().getResources().getString(R.string.accept_join_group);
        String str10 = AppContext.getInstance().getResources().getString(R.string.refuse_join_group);

        public void setData(final InviteMessage msg) {
            if (msg != null) {
                agree.setVisibility(View.INVISIBLE);
                if (msg.getGroupId() != null) {
                    llGroup.setVisibility(View.VISIBLE);
                    tvGroupName.setText(msg.getGroupName());
                } else {
                    llGroup.setVisibility(View.GONE);
                }

                message.setText(msg.getReason());
                name.setText(msg.getFrom());
                if (msg.getStatus() == InviteMessage.InviteMesageStatus.BEAGREED) {
                    userState.setVisibility(View.INVISIBLE);
                    message.setText(str1);
                } else if (msg.getStatus() == InviteMessage.InviteMesageStatus.BEINVITEED || msg.getStatus() == InviteMessage.InviteMesageStatus.BEAPPLYED
                        || msg.getStatus() == InviteMessage.InviteMesageStatus.GROUPINVITATION) {
                    agree.setVisibility(View.VISIBLE);
                    agree.setEnabled(true);
                    agree.setBackgroundResource(android.R.drawable.btn_default);
                    agree.setText(str2);

                    userState.setVisibility(View.VISIBLE);
                    userState.setEnabled(true);
                    userState.setBackgroundResource(android.R.drawable.btn_default);
                    userState.setText(str7);
                    if (msg.getStatus() == InviteMessage.InviteMesageStatus.BEINVITEED) {
                        if (msg.getReason() == null) {
                            message.setText(str3);
                        }
                    } else if (msg.getStatus() == InviteMessage.InviteMesageStatus.BEAPPLYED) {
                        if (TextUtils.isEmpty(msg.getReason())) {
                            message.setText(str4 + msg.getGroupName());
                        }
                    } else if (msg.getStatus() == InviteMessage.InviteMesageStatus.GROUPINVITATION) {
                        if (TextUtils.isEmpty(msg.getReason())) {
                            message.setText(str8 + msg.getGroupName());
                        }
                    }

                    agree.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            acceptInvitation(agree, userState, msg);
                        }
                    });
                    userState.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            refuseInvitation(agree, userState, msg);
                        }
                    });
                } else if (msg.getStatus() == InviteMessage.InviteMesageStatus.AGREED) {
                    userState.setText(str5);
                    userState.setBackgroundDrawable(null);
                    userState.setEnabled(false);
                } else if (msg.getStatus() == InviteMessage.InviteMesageStatus.REFUSED) {
                    userState.setText(str6);
                    userState.setBackgroundDrawable(null);
                    userState.setEnabled(false);
                } else if (msg.getStatus() == InviteMessage.InviteMesageStatus.GROUPINVITATION_ACCEPTED) {
                    String str = msg.getGroupInviter() + str9 + msg.getGroupName();
                    userState.setText(str);
                    userState.setBackgroundDrawable(null);
                    userState.setEnabled(false);
                } else if (msg.getStatus() == InviteMessage.InviteMesageStatus.GROUPINVITATION_DECLINED) {
                    String str = msg.getGroupInviter() + str10 + msg.getGroupName();
                    userState.setText(str);
                    userState.setBackgroundDrawable(null);
                    userState.setEnabled(false);
                }
            }
        }


        /**
         * accept invitation
         *
         * @param buttonRefuse
         * @param msg
         */
        private void acceptInvitation(final Button buttonAgree, final Button buttonRefuse, final InviteMessage msg) {
            final ProgressDialog pd = new ProgressDialog(context);
            String str1 = context.getResources().getString(R.string.Are_agree_with);
            final String str2 = context.getResources().getString(R.string.Has_agreed_to);
            final String str3 = context.getResources().getString(R.string.Agree_with_failure);
            pd.setMessage(str1);
            pd.setCanceledOnTouchOutside(false);
            pd.show();

            new Thread(new Runnable() {
                public void run() {
                    // call api
                    try {
                        if (msg.getStatus() == InviteMessage.InviteMesageStatus.BEINVITEED) {//accept be friends
                            EMClient.getInstance().contactManager().acceptInvitation(msg.getFrom());
                        } else if (msg.getStatus() == InviteMessage.InviteMesageStatus.BEAPPLYED) { //accept application to join group
                            EMClient.getInstance().groupManager().acceptApplication(msg.getFrom(), msg.getGroupId());
                        } else if (msg.getStatus() == InviteMessage.InviteMesageStatus.GROUPINVITATION) {
                            EMClient.getInstance().groupManager().acceptInvitation(msg.getGroupId(), msg.getGroupInviter());
                        }
                        msg.setStatus(InviteMessage.InviteMesageStatus.AGREED);
                        // update database
                        ContentValues values = new ContentValues();
                        values.put(InviteMessgeDao.COLUMN_NAME_STATUS, msg.getStatus().ordinal());
                        messgeDao.updateMessage(msg.getId(), values);
                        ((Activity) context).runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                pd.dismiss();
                                buttonAgree.setText(str2);
                                buttonAgree.setBackgroundDrawable(null);
                                buttonAgree.setEnabled(false);

                                buttonRefuse.setVisibility(View.INVISIBLE);
                            }
                        });
                    } catch (final Exception e) {
                        ((Activity) context).runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                pd.dismiss();
                                Toast.makeText(context, str3 + e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });

                    }
                }
            }).start();
        }

        /**
         * decline invitation
         *
         * @param buttonRefuse
         * @param msg
         */
        private void refuseInvitation(final Button buttonAgree, final Button buttonRefuse, final InviteMessage msg) {
            final ProgressDialog pd = new ProgressDialog(context);
            String str1 = context.getResources().getString(R.string.Are_refuse_with);
            final String str2 = context.getResources().getString(R.string.Has_refused_to);
            final String str3 = context.getResources().getString(R.string.Refuse_with_failure);
            pd.setMessage(str1);
            pd.setCanceledOnTouchOutside(false);
            pd.show();

            new Thread(new Runnable() {
                public void run() {
                    // call api
                    try {
                        if (msg.getStatus() == InviteMessage.InviteMesageStatus.BEINVITEED) {//decline the invitation
                            EMClient.getInstance().contactManager().declineInvitation(msg.getFrom());
                        } else if (msg.getStatus() == InviteMessage.InviteMesageStatus.BEAPPLYED) { //decline application to join group
                            EMClient.getInstance().groupManager().declineApplication(msg.getFrom(), msg.getGroupId(), "");
                        } else if (msg.getStatus() == InviteMessage.InviteMesageStatus.GROUPINVITATION) {
                            EMClient.getInstance().groupManager().declineInvitation(msg.getGroupId(), msg.getGroupInviter(), "");
                        }
                        msg.setStatus(InviteMessage.InviteMesageStatus.REFUSED);
                        // update database
                        ContentValues values = new ContentValues();
                        values.put(InviteMessgeDao.COLUMN_NAME_STATUS, msg.getStatus().ordinal());
                        messgeDao.updateMessage(msg.getId(), values);
                        ((Activity) context).runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                pd.dismiss();
                                buttonRefuse.setText(str2);
                                buttonRefuse.setBackgroundDrawable(null);
                                buttonRefuse.setEnabled(false);

                                buttonAgree.setVisibility(View.INVISIBLE);
                            }
                        });
                    } catch (final Exception e) {
                        ((Activity) context).runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                pd.dismiss();
                                Toast.makeText(context, str3 + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                }
            }).start();
        }

    }

}