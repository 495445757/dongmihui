package com.dongmihui.adapter;

import com.dongmihui.R;
import com.dongmihui.adapter.base.BaseListAdapter;
import com.dongmihui.bean.NewBean;

import java.util.List;

/**
 * Created by Administrator on 2016/9/28.
 */

public class ECNewListAdapter extends BaseListAdapter<NewBean.ZixunBean>{
    public ECNewListAdapter(Callback callback) {
        super(callback);
    }

    @Override
    protected void convert(ViewHolder vh, NewBean.ZixunBean item, int position) {
        //  vh.setText(R.id.item_title, item.getTitle());
        // vh.setText(R.id.item_content, item.getSummary());
        List<NewBean.ZixunBean.LitpicBean> imgUrlList = item.getLitpic();
        if (imgUrlList.size() == 3)
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

            vh.setImageForNet(R.id.item_image_0, imgUrlList.get(0).getPic());
            vh.setImageForNet(R.id.item_image_1, imgUrlList.get(1).getPic());
            vh.setImageForNet(R.id.item_image_2, imgUrlList.get(2).getPic());
        } else if (imgUrlList.size() == 1) {
            vh.setText(R.id.item_title, item.getTitle());
            vh.setText(R.id.item_content, item.getContent());
            vh.setVisibility(R.id.article_top_layout);
            vh.setGone(R.id.layout_image);
        } else {
            vh.setVisibility(R.id.article_top_layout);
            vh.setGone(R.id.layout_image);
        }
    }

    @Override
    protected int getLayoutId(int position, NewBean.ZixunBean item) {
        return R.layout.item_list_news;
    }
}
