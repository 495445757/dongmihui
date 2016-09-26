package com.dongmihui.adapter;

import android.view.View;

import com.dongmihui.R;
import com.dongmihui.adapter.base.BaseListAdapter;
import com.dongmihui.bean.NewsEntity;

import java.util.List;

/**
 * Created by administrator on 2016-09-19.
 */
public class NewsListAdapter extends BaseListAdapter<NewsEntity> {
    private String systemTime;
    public NewsListAdapter(Callback callback) {
        super(callback);
    }
    @Override
    protected void convert(ViewHolder vh, NewsEntity item, int position) {
      //  vh.setText(R.id.item_title, item.getTitle());
       // vh.setText(R.id.item_content, item.getSummary());
        List<String> imgUrlList= item.getPicList();
if(imgUrlList.size()==3)
//{
//    vh.setVisibility(R.id.article_layout);
//    vh.setImageForNet(R.id.left_image, item.getPicOne());
//    vh.setGone(R.id.layout_image);
//}
//        else
{

   // vh.setGone(R.id.left_image);
  vh.setGone(R.id.article_top_layout);
    vh.setVisibility(R.id.layout_image);

    vh.setText(R.id.item_abstract, item.getTitle());

   vh.setImageForNet(R.id.item_image_0, imgUrlList.get(0));
   vh.setImageForNet(R.id.item_image_1, imgUrlList.get(1));
   vh.setImageForNet(R.id.item_image_2, imgUrlList.get(2));
}
  else if(imgUrlList.size()==1)
{
     vh.setText(R.id.item_title, item.getTitle());
    vh.setText(R.id.item_content, item.getSummary());
    vh.setVisibility(R.id.article_top_layout);
    vh.setGone(R.id.layout_image);
}else
{
    vh.setVisibility(R.id.article_top_layout);
    vh.setGone(R.id.layout_image);
}


    }
    @Override
    protected int getLayoutId(int position, NewsEntity item) {
        return R.layout.item_list_news;
    }

    public void setSystemTime(String systemTime) {
        this.systemTime = systemTime;
    }
}
