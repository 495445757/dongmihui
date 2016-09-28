package com.dongmihui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.RequestManager;
import com.dongmihui.R;
import com.dongmihui.activity.NewDetailActivity;
import com.dongmihui.adapter.ECNewListAdapter;
import com.dongmihui.adapter.HomefragmentAdapter;
import com.dongmihui.adapter.HomefragmentViewPagerAdapter;
import com.dongmihui.adapter.base.BaseListAdapter;
import com.dongmihui.api.NewApi;
import com.dongmihui.bean.ApiMessage;
import com.dongmihui.bean.ChannelItem;
import com.dongmihui.bean.NewBean;
import com.dongmihui.bean.NewsEntity;
import com.dongmihui.fragment.base.BaseFragment;
import com.dongmihui.ui.empty.EmptyLayout;
import com.dongmihui.ui.viewpagerindicator.LinePageIndicator;
import com.dongmihui.utils.ToastUtil;
import com.dongmihui.widget.MyListView;
import com.dongmihui.widget.SuperRefreshLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bingoogolapple.bgabanner.BGABanner;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrator on 2016/9/28.
 */

public class ECHomeFragment extends BaseFragment implements
        BGABanner.OnItemClickListener,
        BGABanner.Adapter,
        BaseListAdapter.Callback,
        AdapterView.OnItemClickListener {
    @Bind(R.id.btnBack)
    Button btnBack;
    @Bind(R.id.textHeadTitle)
    TextView textHeadTitle;
    @Bind(R.id.layout_header)
    RelativeLayout layoutHeader;
    @Bind(R.id.banner_guide_content)
    BGABanner mBanner;
    @Bind(R.id.myviewpager)
    ViewPager myviewpager;
    @Bind(R.id.liner1)
    LinearLayout liner1;
    @Bind(R.id.indicator)
    LinePageIndicator indicator;
    @Bind(R.id.image_red)
    ImageView imageRed;
    @Bind(R.id.image_btn_ranking)
    RelativeLayout imageBtnRanking;
    @Bind(R.id.listView)
    MyListView listView;
    @Bind(R.id.NewsListRefresh_Layout)
    SuperRefreshLayout NewsListRefreshLayout;
    @Bind(R.id.error_layout)
    EmptyLayout errorLayout;

    NewApi api;
    Callback<ApiMessage<NewBean>> callback;
    private ArrayList<GridView> array;

    ECNewListAdapter mAdapter;

    private static String[] channellist = {"董秘助手", "董秘购", "投融资", "业务资讯", "路演中心", "朋友圈", "自选股", "发起"};
    private static Integer[] channeimg = {R.mipmap.channel1, R.mipmap.channel2, R.mipmap.channel3, R.mipmap.channel4, R.mipmap.channel5, R.mipmap.channel6, R.mipmap.channel7, R.mipmap.channel8};


    void initCh() {

        List<ChannelItem> mList = new ArrayList<ChannelItem>();
        for (int i = 0; i < channellist.length; i++) {
            ChannelItem channelItem = new ChannelItem();
            channelItem.setId(1);
            channelItem.setName(channellist[i]);
            channelItem.setIcon(channeimg[i]);
            mList.add(channelItem);
        }
        array = new ArrayList<GridView>();
        final int PageCount = 2;
        for (int i = 0; i < PageCount; i++) {
            GridView appPage = new GridView(getActivity());
            appPage.setAdapter(new HomefragmentAdapter(getActivity(), mList, i));
            appPage.setNumColumns(4);
            appPage.setVerticalSpacing(30);
            array.add(appPage);
        }

    }

    public void initBannerData(List<NewBean.BannerBean> bannerBean) {
        List<String> sts = new ArrayList<>();
        for (int i = 0; i < bannerBean.size(); i++) {
            sts.add("" + (i + 1));
        }
        mBanner.setData(bannerBean, sts);
        mBanner.setAdapter(new BGABanner.Adapter() {
            @Override
            public void fillBannerItem(BGABanner banner, View view, Object model, int position) {
                NewBean.BannerBean model1 = (NewBean.BannerBean) model;
                getImgLoader().load(model1.getUrl())
                        .placeholder(R.drawable.holder)
                        .error(R.drawable.holder)
                        .into((ImageView) view);
            }
        });
        mBanner.setOnItemClickListener(this);
    }

    public void initzixData(List<NewBean.ZixunBean> zixunBeen) {
        mAdapter = new ECNewListAdapter(this);
        listView.setAdapter(mAdapter);
        mAdapter.addItem(zixunBeen);
    }

    @Override
    protected void initData() {
        super.initData();
        api = new NewApi();
        callback = new Callback<ApiMessage<NewBean>>() {
            @Override
            public void onResponse(Call<ApiMessage<NewBean>> call, Response<ApiMessage<NewBean>> response) {
                ApiMessage<NewBean> body = response.body();
                switch (body.getCode()) {
                    case 0:
                        Toast.makeText(getActivity(), body.getMsg(), Toast.LENGTH_SHORT).show();
                        Log.d("ECHomeFragment", body.getMsg());
                        break;
                    case 1:
                        NewBean result = body.getResult();
                        initBannerData(result.getBanner());
                        initzixData(result.getZixun());
                        break;
                }
            }

            @Override
            public void onFailure(Call<ApiMessage<NewBean>> call, Throwable t) {
                Toast.makeText(getActivity(), "获取数据失败", Toast.LENGTH_SHORT).show();
                Log.d("ECHomeFragment", t.toString());
            }
        };
        api.getIndexData(callback);
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
        initCh();
        myviewpager.setAdapter(new HomefragmentViewPagerAdapter(getActivity(), array));
        indicator.setViewPager(myviewpager);

        mBanner.setAdapter(new BGABanner.Adapter() {
            @Override
            public void fillBannerItem(BGABanner banner, View view, Object model, int position) {
                ((ImageView) view).setImageResource((int) model);
            }
        });
        mBanner.setData(Arrays.asList(R.drawable.holder, R.drawable.holder, R.drawable.holder), Arrays.asList("1", "2", "3"));
        listView.setOnItemClickListener(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home_pager;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void fillBannerItem(BGABanner banner, View view, Object model, int position) {

    }

    @Override
    public void onBannerItemClick(BGABanner banner, View view, Object model, int position) {
        NewBean.BannerBean model1 = (NewBean.BannerBean) model;
        Toast.makeText(getActivity(), "model1:" + model1.getUrl(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public Date getSystemTime() {
        return new Date();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        NewBean.ZixunBean news = mAdapter.getItem(position);
        if (news != null) {

            //  TextView title = (TextView) view.findViewById(R.id.item_title);
            //   TextView content = (TextView) view.findViewById(R.id.);
            // updateTextColor(title, content);
            //  Intent intent = new Intent(view.getContext(), DetailActivity.class);
            // intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //  startActivity(intent);
            ToastUtil.ShortToast("新闻");
            NewDetailActivity.show(getActivity(), news.getId());
        }
    }

}
