package com.dongmihui.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.GridView;


import com.dongmihui.ui.viewpagerindicator.IconPagerAdapter;

import java.util.List;





public class HomefragmentViewPagerAdapter extends PagerAdapter implements IconPagerAdapter
         {
    private List<GridView> array;

    /**
     * 供外部调用（new）的方法
     *
     * @param context
     *            上下文

     */
    public HomefragmentViewPagerAdapter(Context context, List<GridView> array) {
        this.array = array;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return array.size();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        // TODO Auto-generated method stub
        return arg0 == arg1;
    }

    @Override
    public Object instantiateItem(View arg0, int arg1) {
        View view = array.get(arg1);
        ((ViewPager) arg0).addView(array.get(arg1));
        return view;
    }

    @Override
    public void destroyItem(View arg0, int arg1, Object arg2) {
        ((ViewPager) arg0).removeView((View) arg2);
    }
             @Override
             public int getIconResId(int index) {
                 // TODO Auto-generated method stub
                 return 0;
             }

}