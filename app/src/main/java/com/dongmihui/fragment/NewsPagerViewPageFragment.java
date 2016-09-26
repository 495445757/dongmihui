package com.dongmihui.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dongmihui.adapter.ViewPageFragmentAdapter;
import com.dongmihui.adapter.ViewPageInfo;
import com.dongmihui.bean.ChannelItem;
import com.dongmihui.fragment.base.BaseViewPagerViewPageFragment;
import com.dongmihui.listen.OnTabReselectListener;
import com.dongmihui.ui.TipDataModel;
import com.dongmihui.ui.easytagdragview.EasyTipDragView;
import com.dongmihui.ui.easytagdragview.widget.TipItemView;
import com.dongmihui.R;
import com.dongmihui.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;


public class NewsPagerViewPageFragment extends BaseViewPagerViewPageFragment implements
        OnTabReselectListener {
    private ArrayList<ChannelItem> userChannelList=new ArrayList<ChannelItem>();
    private ImageView button_more_columns;
    private EasyTipDragView easyTipDragView;
    public final static int CHANNELREQUEST = 0;
    public final static int CHANNELRESULT = 10;
    private  ArrayList<ViewPageInfo> mTabs=new  ArrayList<ViewPageInfo>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    //   private String mTextArray[] = { "新闻1", "新闻2", "新闻3", "新闻4"};
    @Override
    protected void onSetupTabAdapter(ViewPageFragmentAdapter adapter) {
//            adapter.addTab(mTextArray[0], "news", NewsContentFragment.class,
//                    getBundle(NewsList.CATALOG_ALL));
//            adapter.addTab(mTextArray[1], "news1", NewsContentFragment.class,
//                    null);
//            adapter.addTab(mTextArray[2], "news2", NewsContentFragment.class,
//                    null);
//            adapter.addTab(mTextArray[3], "news3", NewsContentFragment.class,
//                    null);
        initColumnData();
//
//            int count = userChannelList.size();
//
//            for (int i = 0; i < count; i++) {
//
//                adapter.addTab(userChannelList.get(i).getName(), "news"+userChannelList.get(i).getId(), NewsContentFragment.class,
//                        getBundle(userChannelList.get(i).getId()));
//
//
//              //  userChannelList.get(i).getName()
//            }

        adapter.addAllTab(mTabs);





    }
    private Bundle getBundle(int newType) {
        Bundle bundle = new Bundle();
        bundle.putInt("BUNDLE_KEY", newType);
        return bundle;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater,container,savedInstanceState)  ;

        easyTipDragView =(EasyTipDragView)mRoot.findViewById(R.id.easy_tip_drag_view);


        button_more_columns = (ImageView) mRoot.findViewById(R.id.button_more_columns);
        button_more_columns.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//                 Intent intent_channel = new  Intent(getActivity(),ChannelActivity.class);
//                 startActivityForResult(intent_channel,CHANNELREQUEST);

                easyTipDragView.open();
                //     btn.setVisibility(View.GONE);


            }
        });
        easyTipDragView.setAddData(TipDataModel.getAddTips());
        //设置可以添加的标签数据
        easyTipDragView.setDragData(TipDataModel.getDragTips());
        //在easyTipDragView处于非编辑模式下点击item的回调（编辑模式下点击item作用为删除item）
        easyTipDragView.setSelectedListener(new TipItemView.OnSelectedListener() {
            @Override
            public void onTileSelected(ChannelItem entity, int position, View view) {

                ToastUtil.ShortToast(((ChannelItem) entity).getName());
            }
        });
        //设置每次数据改变后的回调（例如每次拖拽排序了标签或者增删了标签都会回调）
        easyTipDragView.setDataResultCallback(new EasyTipDragView.OnDataChangeResultCallback() {
            @Override
            public void onDataChangeResult(ArrayList<ChannelItem> tips) {
                Log.i("heheda", tips.toString());
            }
        });
        //设置点击“确定”按钮后最终数据的回调
        easyTipDragView.setOnCompleteCallback(new EasyTipDragView.OnCompleteCallback() {
            @Override
            public void onComplete(ArrayList<ChannelItem> tips) {

                ResTabAdapter();
                ToastUtil.ShortToast("最终数据：" + tips.toString());
                button_more_columns.setVisibility(View.VISIBLE);
            }
        });
        return   mRoot;
    }



    @Override
    protected void ResTabAdapter() {
        //  initColumnData();
        super.ResTabAdapter();
       //  initColumnData();
        //  mTabsAdapter.addAllTab(mTabs);
    }


    /** 获取Column栏目 数据*/
    private void initColumnData() {


        List<ChannelItem> tipslist =TipDataModel.getDragTips();

        int count = tipslist.size();
        ViewPageInfo viewPageInfo;
        for (int i = 0; i < count; i++) {


                viewPageInfo = new ViewPageInfo(tipslist.get(i).getName(), "news" + tipslist.get(i).getId(), NewsListFragment.class,
                        getBundle(tipslist.get(i).getId()));


            mTabs.add(viewPageInfo);
        }


    }
    @Override
    protected void setScreenPageLimit() {
        mViewPager.setOffscreenPageLimit(3);
    }
    @Override
    public void onClick(View v) {

    }

    @Override
    public void initView(View view) {


    }

    @Override
    public void initData() {

    }
    @Override
    public void onTabReselect() {
    }


}
