package com.dongmihui.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.dongmihui.R;
import com.dongmihui.adapter.EvaluationAdapter;
import com.dongmihui.api.DongmihuiApi;
import com.dongmihui.bean.Evaluation;
import com.dongmihui.bean.EvaluationItem;
import com.dongmihui.common.AppContext;
import com.dongmihui.fragment.base.BaseFragment;
import com.dongmihui.ui.empty.EmptyLayout;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.TextHttpResponseHandler;

import java.lang.reflect.Type;
import java.util.ArrayList;


import cz.msebera.android.httpclient.Header;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;



/**
 * Created by administrator on 2016-08-16.
 */
public class EvaluationFragment extends BaseFragment implements View.OnClickListener  {
    private EvaluationAdapter mAdapter;
    private ListView mlistView;
    protected PtrClassicFrameLayout mRefreshLayout;
    protected TextHttpResponseHandler mHandler;
    protected EmptyLayout mErrorLayout;
    protected String CACHE_NAME = getClass().getName();
    protected boolean mIsRefresh;
    private ArrayList<EvaluationItem> Evdata;
    public static final int TYPE_NORMAL = 0;
    public static final int TYPE_LOADING = 1;
    public static final int TYPE_NO_MORE = 2;
    public static final int TYPE_ERROR = 3;
    public static final int TYPE_NET_ERROR = 4;
    private View mFooterView;
    private ProgressBar mFooterProgressBar;
    private TextView mFooterText;
    private int page = 1;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_evaluation;
    }


    @Override
    protected void initWidget(View root) {
        super.initWidget(root);


        View  emptyView = LayoutInflater.from(getContext()).inflate(R.layout.item_empty, null);

        mlistView = (ListView) root.findViewById(R.id.listView);
     mErrorLayout = (EmptyLayout) root.findViewById(R.id.error_layout);
      mlistView.setEmptyView(emptyView);
       mRefreshLayout = (PtrClassicFrameLayout) root.findViewById(R.id.Evaluntion_Layout);


        mFooterView = LayoutInflater.from(getContext()).inflate(R.layout.layout_list_view_footer, null);
        mFooterText = (TextView) mFooterView.findViewById(R.id.tv_footer);
        mFooterProgressBar = (ProgressBar) mFooterView.findViewById(R.id.pb_footer);
        mAdapter = new EvaluationAdapter(getContext(), new ArrayList<EvaluationItem>());
        setFooterType(TYPE_LOADING);
        mErrorLayout.setOnLayoutClickListener(this);
        if (isNeedFooter())
            mlistView.addFooterView(mFooterView);




        mRefreshLayout.setLastUpdateTimeRelateObject(this);
        mRefreshLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                initData(true);
            }
        });


        initData(false);
        mlistView.setAdapter(mAdapter);


    }

    @Override
    public void onClick(View v) {
        mErrorLayout.setErrorType(EmptyLayout.NETWORK_LOADING);
        onRefreshing();
    }

    public void onRefreshing() {
        mIsRefresh = true;
        requestData();
    }
    protected void requestData() {

        setFooterType(TYPE_LOADING);
    }

    protected Type getType() {
        return new TypeToken<Evaluation>() {
        }.getType();
    }
    protected boolean isNeedFooter() {
        return true;
    }
    private void initData(final boolean isMore) {



        mHandler = new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                onRequestError(statusCode);
                onRequestFinish();
            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    Evaluation resultBean = AppContext.createGson().fromJson(responseString, getType());
                    if (resultBean != null ) {

                        if (isMore) {
                            Evdata.addAll(0, resultBean.getEvaluataions());
                        } else {
                            if(resultBean.getEvaluataions()!=null)
                                Evdata =resultBean.getEvaluataions();
                        }
                        mAdapter.setData(Evdata);
                        if (resultBean.getEvaluataions().size() < 20) {
                            setFooterType(TYPE_NO_MORE);
                            //mRefreshLayout.setNoMoreData();
                        }
                        if (mAdapter.getDatas().size() > 0) {
                            mErrorLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
                            mRefreshLayout.setVisibility(View.VISIBLE);
                        } else {
                            mErrorLayout.setErrorType(EmptyLayout.NODATA);
                        }

                        mErrorLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
                        onRequestFinish();


                        page++;


                    } else {


                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    onFailure(statusCode, headers, responseString, e);
                }
            }



        };


        DongmihuiApi.getthingsList(mIsRefresh ? "" : "", mHandler);
//

//            HttpParams params = new HttpParams();
//           // params.put("goodsId", "98573");
//            //  params.put("pageNo", String.valueOf(1));
//            OkHttpUtils.post("http://www.henanxinlong.com/penyou.php") .tag(this)//
//
//                    .execute(new JsonCallback<Evaluation>(Evaluation.class) {
//                        @Override
//                        public void onResponse(boolean isFromCache, Evaluation evaluation, Request request, @Nullable Response response) {
//                            if (isMore) {
//                                Evdata.addAll(0, evaluation.getEvaluataions());
//                            } else {
//                                if(evaluation.getEvaluataions()!=null)
//                                    Evdata = evaluation.getEvaluataions();
//                            }
//                            mAdapter.setData(Evdata);
//                            page++;
//
//                        }
//                    });


        }
    protected void onRequestError(int code) {

        if (mAdapter.getDatas().size() == 0){}
        mErrorLayout.setErrorType(EmptyLayout.NETWORK_ERROR);
    }
    protected void onRequestFinish() {
        onComplete();
    }
    protected void onRequestSuccess(int code) {

    }
    protected void onComplete() {
        mRefreshLayout.refreshComplete();
        mIsRefresh = false;
    }
    protected void setFooterType(int type) {
        switch (type) {
            case TYPE_NORMAL:
            case TYPE_LOADING:
                mFooterText.setText(getResources().getString(R.string.footer_type_loading));
                mFooterProgressBar.setVisibility(View.VISIBLE);
                break;
            case TYPE_NET_ERROR:
                mFooterText.setText(getResources().getString(R.string.footer_type_net_error));
                mFooterProgressBar.setVisibility(View.GONE);
                break;
            case TYPE_ERROR:
                mFooterText.setText(getResources().getString(R.string.footer_type_error));
                mFooterProgressBar.setVisibility(View.GONE);
                break;
            case TYPE_NO_MORE:
                mFooterText.setText(getResources().getString(R.string.footer_type_not_more));
                mFooterProgressBar.setVisibility(View.GONE);
                break;
        }
    }
}