package com.dongmihui.activity;


import com.dongmihui.R;
import com.dongmihui.fragment.ECHomeFragment;
import com.dongmihui.fragment.ECMemberFragment;
import com.dongmihui.fragment.EvaluationFragment;
import com.dongmihui.fragment.MemberFragment;
import com.dongmihui.fragment.NewsPagerViewPageFragment;
import com.dongmihui.fragment.HomeIndexFragment;
import com.dongmihui.im.fragment.ConversationListFragment;

/**
 * Created by Administrator on 2016/7/5.
 */
public enum MainTab {
    HOME(0, R.string.main_navigation_home, R.drawable.tab_icon_home,
            ECHomeFragment.class),

    NEWS(1, R.string.main_navigation_news, R.drawable.tab_icon_news,
            NewsPagerViewPageFragment.class),



    IM(2, R.string.main_navigation_im, R.drawable.tab_icon_im,
            ConversationListFragment.class),
    FOUND(3, R.string.main_navigation_found, R.drawable.tab_icon_circle,
            EvaluationFragment.class),

    USER(4, R.string.main_navigation_user, R.drawable.tab_icon_me,
            ECMemberFragment.class);






    private int idx;
    private int resName;
    private int resIcon;
    private Class<?> clz;

    private MainTab(int idx, int resName, int resIcon, Class<?> clz) {
        this.idx = idx;
        this.resName = resName;
        this.resIcon = resIcon;
        this.clz = clz;
    }
    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public int getResName() {
        return resName;
    }

    public void setResName(int resName) {
        this.resName = resName;
    }

    public int getResIcon() {
        return resIcon;
    }

    public void setResIcon(int resIcon) {
        this.resIcon = resIcon;
    }

    public Class<?> getClz() {
        return clz;
    }

    public void setClz(Class<?> clz) {
        this.clz = clz;
    }
}
