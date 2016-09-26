package com.dongmihui.fragment.base;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dongmihui.R;
import com.dongmihui.adapter.base.BaseListAdapter;
import com.dongmihui.bean.PageBean;
import com.dongmihui.bean.ResultBean;
import com.dongmihui.cache.CacheManager;
import com.dongmihui.common.AppContext;
import com.dongmihui.ui.empty.EmptyLayout;
import com.dongmihui.widget.SuperRefreshLayout;
import com.loopj.android.http.TextHttpResponseHandler;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


import cz.msebera.android.httpclient.Header;

/**
 * Created by Administrator on 2016/7/7.
 */
public abstract class BaseListFragment<T> extends BaseFragment implements
        SuperRefreshLayout.SuperRefreshLayoutListener,
        AdapterView.OnItemClickListener, BaseListAdapter.Callback,
        View.OnClickListener
{
    public static final int TYPE_NORMAL = 0;
    public static final int TYPE_LOADING = 1;
    public static final int TYPE_NO_MORE = 2;
    public static final int TYPE_ERROR = 3;
    public static final int TYPE_NET_ERROR = 4;
    protected static ExecutorService mExeService = Executors.newFixedThreadPool(3);
    protected PageBean<T> mBean;
    protected ListView mListView;
   protected SuperRefreshLayout mRefreshLayout;
protected EmptyLayout mErrorLayout;
    protected BaseListAdapter<T> mAdapter;
    private String mTime;
    protected boolean mIsRefresh;
    private View mFooterView;
    private ProgressBar mFooterProgressBar;
  private TextView mFooterText;
    protected TextHttpResponseHandler mHandler;
    protected String CACHE_NAME = getClass().getName();
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_base_list;
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
        mListView = (ListView) root.findViewById(R.id.listView);
      mRefreshLayout = (SuperRefreshLayout) root.findViewById(R.id.NewsListRefresh_Layout);
      mRefreshLayout.setColorSchemeResources(
            R.color.swiperefresh_color1, R.color.swiperefresh_color2,
             R.color.swiperefresh_color3, R.color.swiperefresh_color4);
        mErrorLayout = (EmptyLayout) root.findViewById(R.id.error_layout);
        mRefreshLayout.setSuperRefreshLayoutListener(this);
      //  mFooterView = LayoutInflater.from(getContext()).inflate(R.layout.layout_list_view_footer, null);
       // mFooterText = (TextView) mFooterView.findViewById(R.id.tv_footer);
      //  mFooterProgressBar = (ProgressBar) mFooterView.findViewById(R.id.pb_footer);
        mListView.setOnItemClickListener(this);
      //  setFooterType(TYPE_LOADING);
        mErrorLayout.setOnLayoutClickListener(this);
     //   if (isNeedFooter())
         //   mListView.addFooterView(mFooterView);




    }



    protected boolean isNeedFooter() {
        return true;
    }
//    protected void setFooterType(int type) {
//    switch (type) {
//        case TYPE_NORMAL:
//        case TYPE_LOADING:
//            mFooterText.setText(getResources().getString(R.string.footer_type_loading));
//            mFooterProgressBar.setVisibility(View.VISIBLE);
//            break;
//        case TYPE_NET_ERROR:
//            if(isAdded()) {
//                mFooterText.setText(getResources().getString(R.string.footer_type_net_error));
//            }
//            mFooterProgressBar.setVisibility(View.GONE);
//            break;
//        case TYPE_ERROR:
//           mFooterText.setText(getResources().getString(R.string.footer_type_error));
//            mFooterProgressBar.setVisibility(View.GONE);
//            break;
//        case TYPE_NO_MORE:
//            mFooterText.setText(getResources().getString(R.string.footer_type_not_more));
//            mFooterProgressBar.setVisibility(View.GONE);
//            break;
//    }
//}


    @Override
    protected void initData() {
        super.initData();
        //when open this fragment,read the obj

        mAdapter = getListAdapter();
        mListView.setAdapter(mAdapter);
        mHandler = new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                onRequestError(statusCode);
                onRequestFinish();
            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {





                    ResultBean<PageBean<T>> resultBean = AppContext.createGson().fromJson(responseString, getType());
                    if (resultBean != null && resultBean.isSuccess() && resultBean.getResult().getItems() != null) {
                        onRequestSuccess(resultBean.getCode());
                   setListData(resultBean);

                    } else {
                     // setFooterType(TYPE_NO_MORE);
                       mRefreshLayout.setNoMoreData();
                    }
                    onRequestFinish();
                } catch (Exception e) {
                    e.printStackTrace();
                    onFailure(statusCode, headers, responseString, e);
                }
            }



        };

        mExeService.execute(new Runnable() {
            @Override
            public void run() {
                mBean = (PageBean<T>) CacheManager.readObject(getActivity(), CACHE_NAME);
                //if is the first loading
                if (mBean == null) {
                    mBean = new PageBean<>();
                    mBean.setItems(new ArrayList<T>());
                    onRefreshing();
                } else {
                    mRoot.post(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.addItem(mBean.getItems());
                         mErrorLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
                       mRefreshLayout.setVisibility(View.VISIBLE);
                            onRefreshing();
                        }
                    });
                }
            }
        });


    }


    @Override
    public Date getSystemTime() {
        return new Date();
    }



    protected void setListData(ResultBean<PageBean<T>> resultBean) {
        //is refresh
        mBean.setNextPageToken(resultBean.getResult().getNextPageToken());
        if (mIsRefresh) {
            //cache the time
            mTime = resultBean.getTime();
            mBean.setItems(resultBean.getResult().getItems());
            mAdapter.clear();
            mAdapter.addItem(mBean.getItems());
            mBean.setPrevPageToken(resultBean.getResult().getPrevPageToken());
            mRefreshLayout.setCanLoadMore();
            mExeService.execute(new Runnable() {
                @Override
                public void run() {
                    CacheManager.saveObject(getActivity(), mBean, CACHE_NAME);
                }
            });
        } else {
            mAdapter.addItem(resultBean.getResult().getItems());
        }
        if (resultBean.getResult().getItems().size() < 20) {
             // setFooterType(TYPE_NO_MORE);

        }
        if (mAdapter.getDatas().size() > 0) {
       mErrorLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
          mRefreshLayout.setVisibility(View.VISIBLE);
        } else {
           mErrorLayout.setErrorType(EmptyLayout.NODATA);
        }
    }

    protected abstract Type getType();
    protected void onRequestError(int code) {
    // setFooterType(TYPE_NET_ERROR);
        if (mAdapter.getDatas().size() == 0){}
        mErrorLayout.setErrorType(EmptyLayout.NETWORK_ERROR);
    }

    protected void onRequestFinish() {
        onComplete();
    }
    protected void onRequestSuccess(int code) {

    }
    protected void onComplete() {
     mRefreshLayout.onLoadComplete();
        mIsRefresh = false;
    }
    protected abstract BaseListAdapter<T> getListAdapter();
    @Override
    public void onClick(View v) {
     mErrorLayout.setErrorType(EmptyLayout.NETWORK_LOADING);
        onRefreshing();
    }

    @Override
    public void onRefreshing() {
        mIsRefresh = true;
        requestData();
    }

    @Override
    public void onLoadMore() {
        requestData();
    }

    /**
     * request network data
     */
    protected void requestData() {
        onRequestStart();
   // setFooterType(TYPE_LOADING);
    }
    protected void onRequestStart() {

    }

}
