package com.dongmihui.fragment.base;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dongmihui.R;
import com.dongmihui.adapter.ViewPageFragmentAdapter;
import com.dongmihui.bean.ChannelItem;
import com.dongmihui.ui.empty.EmptyLayout;
import com.dongmihui.widget.PagerSlidingTabStrip;


import java.util.ArrayList;




/**
 * Created by Administrator on 2016/7/6.
 */
public abstract class BaseViewPagerViewPageFragment extends BaseViewPageFragment {

    protected View mRoot;
    protected PagerSlidingTabStrip mTabStrip;
    protected ViewPager mViewPager;
    protected EmptyLayout mErrorLayout;
    protected ViewPageFragmentAdapter mTabsAdapter;
    private FragmentManager fm;
    private ArrayList<ChannelItem> userChannelList=new ArrayList<ChannelItem>();
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
    @Override
    protected int getLayoutId() {
        return R.layout.base_viewpage_fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mRoot == null) {
            View root = inflater.inflate(getLayoutId(), null);
            mTabStrip = (PagerSlidingTabStrip) root
                    .findViewById(R.id.pager_tabstrip);
            mViewPager = (ViewPager) root.findViewById(R.id.pager);

        // mErrorLayout = (EmptyLayout) root.findViewById(R.id.error_layout);
            mTabsAdapter = new ViewPageFragmentAdapter(getChildFragmentManager(),
                    mTabStrip, mViewPager);
            setScreenPageLimit();
            onSetupTabAdapter(mTabsAdapter);
            mRoot = root;
        }
        return  mRoot;
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        if (savedInstanceState != null) {
            int pos = savedInstanceState.getInt("position");
            mViewPager.setCurrentItem(pos, true);
        }
    }

    protected void setScreenPageLimit() {
    }




    protected abstract void onSetupTabAdapter(ViewPageFragmentAdapter adapter);

    protected void ResTabAdapter() {

      mTabsAdapter.removeAll();
      //  mTabStrip.removeAllTab();
     //  mViewPager.removeAllViews();
   mTabsAdapter = new ViewPageFragmentAdapter(getChildFragmentManager(),
             mTabStrip, mViewPager);
    onSetupTabAdapter(mTabsAdapter);
    }



}
