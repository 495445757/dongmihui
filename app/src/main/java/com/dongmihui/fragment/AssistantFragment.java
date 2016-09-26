package com.dongmihui.fragment;

        import android.os.Bundle;
        import android.os.Handler;
        import android.support.v4.view.ViewPager;
        import android.text.TextUtils;
        import android.view.View;
        import android.widget.AdapterView;
        import android.widget.GridView;
        import android.widget.ImageView;

        import com.dongmihui.R;
        import com.dongmihui.activity.NewDetailActivity;
        import com.dongmihui.adapter.HomefragmentAdapter;
        import com.dongmihui.adapter.HomefragmentViewPagerAdapter;
        import com.dongmihui.adapter.NewsListAdapter;
        import com.dongmihui.adapter.base.BaseListAdapter;
        import com.dongmihui.api.DongmihuiApi;
        import com.dongmihui.bean.ChannelItem;
        import com.dongmihui.bean.NewsEntity;
        import com.dongmihui.bean.PageBean;
        import com.dongmihui.bean.ResultBean;
        import com.dongmihui.common.AppContext;
        import com.dongmihui.fragment.base.BaseListFragment;
        import com.dongmihui.ui.viewpagerindicator.LinePageIndicator;

        import java.lang.reflect.Type;
        import java.util.ArrayList;
        import java.util.Arrays;
        import java.util.List;

        import com.dongmihui.utils.ToastUtil;
        import com.google.gson.reflect.TypeToken;


public class AssistantFragment extends BaseListFragment<NewsEntity>  {
    public static final String NEWS_SYSTEM_TIME = "news_system_time";

    private Handler handler = new Handler();
    private static final String NEWS_BANNER = "news_banner1";
    protected int mCatalog = 1;
    private boolean isFirst = true;
    private    ArrayList<GridView> array;
    private String mSystemTime;


    private LinePageIndicator mIndicator;
    private ViewPager viewPager;
    //   private ListView mlistView;
    @Override
    protected void onRestartInstance(Bundle bundle) {
        super.onRestartInstance(bundle);
        mIsRefresh = false;
        mSystemTime = bundle.getString("system_time", "");
    }

    protected int getLayoutId() {
        return R.layout.fragment_assistant_pager;
    }

    @Override
    protected Type getType() {

        return new TypeToken<ResultBean<PageBean<NewsEntity>>>() {
        }.getType();

    }
    @Override
    protected void onRequestFinish() {
        super.onRequestFinish();
        isFirst = false;
    }
    @Override
    protected void initData() {
        super.initData();
        if (!TextUtils.isEmpty(mSystemTime)) {
            ((NewsListAdapter) mAdapter).setSystemTime(mSystemTime);
        }

    }

    private static String[] channellist ={"董秘助手","董秘购","投融资","业务资讯","路演中心","朋友圈","自选股","发起"};
    private static Integer[] channeimg ={R.mipmap.channel1,R.mipmap.channel2,R.mipmap.channel3,R.mipmap.channel4,R.mipmap.channel5,R.mipmap.channel6,R.mipmap.channel7,R.mipmap.channel8};
    void initCh() {

        List<ChannelItem> mList = new ArrayList<ChannelItem>();
        for (int i = 0; i < channellist.length; i++) {
            ChannelItem channelItem = new ChannelItem();
            channelItem.setId(1);
            channelItem.setName(channellist[i]);
            channelItem.setIcon(channeimg[i]);
            mList.add(channelItem);
            mList.add(channelItem);
        }
        array = new ArrayList<GridView>();
        final int PageCount = 1;
        for (int i = 0; i < PageCount; i++) {
            GridView appPage = new GridView(getActivity());
            appPage.setAdapter(new HomefragmentAdapter(getActivity(), mList, i));
            appPage.setNumColumns(4);
            appPage.setVerticalSpacing(30);
            array.add(appPage);
        }

    }
    @Override
    protected void initBundle(Bundle bundle) {
        super.initBundle(bundle);

        if (bundle != null) {
            mCatalog = bundle.getInt("BUNDLE_KEY", 0);
        }

    }
    @Override
    public void onRefreshing() {
        super.onRefreshing();

    }


    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
        initCh();
        viewPager = (ViewPager)findView(R.id.myviewpager);


        mIndicator = (LinePageIndicator)findView(R.id.indicator);
        viewPager.setAdapter(new HomefragmentViewPagerAdapter(getActivity(),array));

        mIndicator.setViewPager(viewPager);

    }


    @Override
    protected BaseListAdapter<NewsEntity> getListAdapter() {
        return new NewsListAdapter(this);
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        NewsEntity news = mAdapter.getItem(position - 1);
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
    @Override
    protected void setListData(ResultBean<PageBean<NewsEntity>> resultBean) {


        mSystemTime = resultBean.getTime();
        ((NewsListAdapter) mAdapter).setSystemTime(mSystemTime);
        AppContext.set(NEWS_SYSTEM_TIME, mSystemTime);


        super.setListData(resultBean);




    }

    @Override
    protected void requestData() {
        super.requestData();

        DongmihuiApi.getNewsList(mIsRefresh ? mBean.getPrevPageToken() : mBean.getNextPageToken(), mHandler);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("system_time", mSystemTime);
        super.onSaveInstanceState(outState);
    }
}
