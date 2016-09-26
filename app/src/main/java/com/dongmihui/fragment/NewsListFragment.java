package com.dongmihui.fragment;


    import android.os.Bundle;
    import android.os.Handler;
    import android.text.TextUtils;
    import android.view.View;
    import android.widget.AdapterView;

    import com.dongmihui.activity.NewDetailActivity;
    import com.dongmihui.adapter.NewsListAdapter;
    import com.dongmihui.adapter.base.BaseListAdapter;
    import com.dongmihui.api.DongmihuiApi;
    import com.dongmihui.bean.NewsEntity;
    import com.dongmihui.bean.PageBean;
    import com.dongmihui.bean.ResultBean;
    import com.dongmihui.common.AppContext;
    import com.dongmihui.fragment.base.BaseListFragment;

    import java.lang.reflect.Type;

    import com.google.gson.reflect.TypeToken;

public class NewsListFragment extends BaseListFragment<NewsEntity>  {
        public static final String NEWS_SYSTEM_TIME = "news_system_time";

        private Handler handler = new Handler();

        protected int mCatalog = 1;
        private boolean isFirst = true;

        private String mSystemTime;

        //   private ListView mlistView;
        @Override
        protected void onRestartInstance(Bundle bundle) {
            super.onRestartInstance(bundle);
            mIsRefresh = false;
            mSystemTime = bundle.getString("system_time", "");
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

        }


        @Override
        protected BaseListAdapter<NewsEntity> getListAdapter() {
            return new NewsListAdapter(this);
        }
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            NewsEntity news = mAdapter.getItem(position - 1);
            if (news != null) {

             //   TextView title = (TextView) view.findViewById(R.id.item_title);
                //   TextView content = (TextView) view.findViewById(R.id.);
                // updateTextColor(title, content);
                //  Intent intent = new Intent(view.getContext(), DetailActivity.class);
                // intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                //  startActivity(intent);

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


