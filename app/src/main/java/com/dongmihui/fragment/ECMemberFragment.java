package com.dongmihui.fragment;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dongmihui.R;
import com.dongmihui.activity.MemberEditorActivity;
import com.dongmihui.activity.LoginActivity;
import com.dongmihui.activity.SettingActivity;
import com.dongmihui.activity.UsAboutActivity;
import com.dongmihui.api.MyApi;
import com.dongmihui.bean.MemberBean;
import com.dongmihui.common.AppContext;
import com.dongmihui.utils.SpUtils;
import com.dongmihui.utils.TLog;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ECMemberFragment extends Fragment {


    @Bind(R.id.btnBack)
    Button btnBack;
    @Bind(R.id.textHeadTitle)
    TextView textHeadTitle;
    @Bind(R.id.layout_header)
    RelativeLayout layoutHeader;
    @Bind(R.id.im_avatar)
    ImageView imAvatar;
    @Bind(R.id.tv_user_name)
    TextView tvUserName;
    @Bind(R.id.tv_integral_balance)
    TextView tvIntegralBalance;
    @Bind(R.id.iv_next)
    ImageView ivNext;
    @Bind(R.id.ll_account_setting)
    LinearLayout llAccountSetting;
    @Bind(R.id.ll_my_collect)
    LinearLayout llMyCollect;
    @Bind(R.id.ll_my_order)
    LinearLayout llMyOrder;
    @Bind(R.id.ll_my_investment_and_financing)
    LinearLayout llMyInvestmentAndFinancing;
    @Bind(R.id.ll_my_business)
    LinearLayout llMyBusiness;
    @Bind(R.id.ll_release_road_show)
    LinearLayout llReleaseRoadShow;
    @Bind(R.id.ll_my_launch)
    LinearLayout llMyLaunch;
    @Bind(R.id.ll_version_upgrade)
    LinearLayout llVersionUpgrade;
    @Bind(R.id.ll_about_us)
    LinearLayout llAboutUs;
    @Bind(R.id.ll_feedback)
    LinearLayout llFeedback;
    @Bind(R.id.but_quit)
    Button butQuit;
    private MyApi api;
    boolean isFirst =true;
    private MemberBean body;

    public ECMemberFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ecmember, container, false);
        ButterKnife.bind(this, view);
        api = new MyApi();
        textHeadTitle.setText("我的");
        initView();
        return view;
    }
    //联网获取用户信息 初始化用户信息
    ProgressDialog pd;
    private void initView() {
        if(pd==null){
            pd= new ProgressDialog(getActivity());
        }
        pd.setCanceledOnTouchOutside(false);
        pd.setMessage("加载中。。。");
        pd.show();
        int anInt = SpUtils.getInt(getActivity(), SpUtils.USER_ID);
        api.getMember(3, new Callback<MemberBean>() {
            @Override
            public void onResponse(Call<MemberBean> call, Response<MemberBean> response) {
                body = response.body();
                if(body.getCode()==0){
                    TLog.log("MmberFragment", body.toString()+"00000000000000000000");
                    pd.dismiss();
                }else if(body.getCode() == 1){
                    tvUserName.setText(body.result.getUserName());
                    Glide.with(AppContext.getInstance())
                            .load(body.result.getAvatar())
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(imAvatar);
                    TLog.log("MmberFragment", body.toString()+"111111111111111111111111");
                    pd.dismiss();
                }
            }

            @Override
            public void onFailure(Call<MemberBean> call, Throwable t) {
                TLog.log("MmberFragment",t.toString());
                pd.dismiss();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.im_avatar, R.id.iv_next, R.id.ll_account_setting, R.id.ll_my_collect, R.id.ll_my_order, R.id.ll_my_investment_and_financing, R.id.ll_my_business, R.id.ll_release_road_show, R.id.ll_my_launch, R.id.ll_version_upgrade, R.id.ll_about_us, R.id.ll_feedback, R.id.but_quit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.im_avatar:

            case R.id.iv_next:
                MemberEditorActivity.startMemberEditorActivity(getActivity());
                break;
            case R.id.ll_account_setting:
                SettingActivity.startSettingActivity(getActivity());
                break;
            case R.id.ll_my_collect:
                toast("我的收藏");
                break;
            case R.id.ll_my_order:
                toast("我的订单");
                break;
            case R.id.ll_my_investment_and_financing:
                toast("我的投融资");
                break;
            case R.id.ll_my_business:
                toast("我的业务");
                break;
            case R.id.ll_release_road_show:
                toast("我的路演");
                break;
            case R.id.ll_my_launch:
                toast("我的发起");
                break;
            case R.id.ll_version_upgrade:
                toast("版本升级");
                break;
            case R.id.ll_about_us:
                toast("关于我们");
                UsAboutActivity.startUsAboutActivity(getActivity());
                break;
            case R.id.ll_feedback:
                toast("意见反馈");
                break;
            case R.id.but_quit:
                LoginActivity.startLoginActivity(getActivity());
                break;
        }
    }

    public void toast(String string) {
        Toast.makeText(getActivity(), string+"正在开发中", Toast.LENGTH_SHORT).show();
    }
}
