package com.dongmihui.ui;



import com.dongmihui.bean.ChannelItem;
import com.dongmihui.ui.easytagdragview.bean.SimpleTitleTip;
import com.dongmihui.ui.easytagdragview.bean.Tip;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/5/30 0030.
 */
public class TipDataModel {
    private static String[] dragTips ={"头条","热点","娱乐","体育","财经","科技","段子","轻松一刻","军事","历史","游戏","时尚","NBA"
            ,"漫画","社会","中国足球","手机"};
    private static String[] addTips ={"数码","移动互联","云课堂","家居","旅游","健康","读书","跑步","情感","政务","艺术","博客"};
    public static List<ChannelItem>  getDragTips(){
        List<ChannelItem> result = new ArrayList<>();
        for(int i=0;i<dragTips.length;i++){
            String temp =dragTips[i];
            ChannelItem tip = new ChannelItem();
            tip.setName(temp);
            tip.setId(i);
            result.add(tip);
        }
        return result;
    }
    public static List<ChannelItem> getAddTips(){
        List<ChannelItem> result = new ArrayList<>();
        for(int i=0;i<addTips.length;i++){
            String temp =addTips[i];
            ChannelItem tip = new ChannelItem();
            tip.setName(temp);
            tip.setId(i+dragTips.length);
            result.add(tip);
        }
        return result;
    }
}
