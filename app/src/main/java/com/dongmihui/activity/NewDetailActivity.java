package com.dongmihui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.dongmihui.ui.loopviewpager.AutoLoopViewPager;
import com.dongmihui.ui.swipebacklayout.SwipeBackActivity;
import com.dongmihui.ui.viewpagerindicator.CirclePageIndicator;
import com.dongmihui.R;
import com.dongmihui.utils.ToastUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by xiao on 16/9/19.
 */
public class NewDetailActivity extends SwipeBackActivity {

    @Bind(R.id.btnBack)
    Button btnBack;
    @Bind(R.id.textHeadTitle)
    TextView textHeadTitle;

    @Bind(R.id.pager)
    AutoLoopViewPager pager;
    @Bind(R.id.indicator)
    CirclePageIndicator indicator;
    private GalleryPagerAdapter galleryAdapter;
    private int[] imageViewIds;
    private List<String> imageList = new ArrayList<String>(Arrays.asList(
            "http://pic.nipic.com/2008-07-11/20087119630716_2.jpg",
            "http://pic.nipic.com/2008-07-11/20087119630716_2.jpg",
            "http://pic.nipic.com/2008-07-11/20087119630716_2.jpg"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        ButterKnife.bind(this);
        initView();
        initEvent();
    }

    public static void show(Context context, long id) {
        Intent intent = new Intent(context, NewDetailActivity.class);
        intent.putExtra("id", id);
        context.startActivity(intent);
       // ToastUtil.ShortToast("闻");
    }

    void initView() {
        textHeadTitle.setText("新闻标题");
        btnBack.setVisibility(View.VISIBLE);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        imageViewIds = new int[] { R.drawable.house_background, R.drawable.house_background_1, R.drawable.house_background_2};

        galleryAdapter = new GalleryPagerAdapter();
        pager.setAdapter(galleryAdapter);
        indicator.setViewPager(pager);
        indicator.setPadding(5, 5, 10, 5);
    }

    private void initEvent() {
      //  Button btnShare = (Button) findViewById(R.id.btnShare);
     //   btnShare.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // 是否只有已登录用户才能打开分享选择页
//            }
//        });

    }


    //轮播图适配器
    public class GalleryPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return imageViewIds.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView item = new ImageView(NewDetailActivity.this);
            item.setImageResource(imageViewIds[position]);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(-1, -1);
            item.setLayoutParams(params);
            item.setScaleType(ImageView.ScaleType.FIT_XY);
            container.addView(item);

            final int pos = position;
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(NewDetailActivity.this, ImageGalleryActivity.class);
                    intent.putStringArrayListExtra("images", (ArrayList<String>) imageList);
                    intent.putExtra("position", pos);
                    startActivity(intent);
                }
            });

            return item;
        }

        @Override
        public void destroyItem(ViewGroup collection, int position, Object view) {
            collection.removeView((View) view);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        pager.startAutoScroll();
    }

    @Override
    protected void onPause() {
        super.onPause();
        pager.stopAutoScroll();
    }
}
