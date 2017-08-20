package me.com.movielibrary.ui.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

import me.com.movielibrary.R;
import me.com.movielibrary.presenter.impl.MovieDetailActivityPresenterImpl;
import me.com.movielibrary.ui.adapter.ActivityMovieDetailAdapter;
import me.com.movielibrary.ui.adapter.FragmentMainViewpagerAdapter;
import me.com.movielibrary.ui.fragment.FragmentMovieDetail;
import me.com.movielibrary.ui.iView.MovieDetailActivityIView;

import static me.com.movielibrary.ui.fragment.FragmentMovieDetail.videoPlayPouopWindow;


/**
 * Created by Me on 2017/8/10.
 */

public class MovieDetaillActivity extends AppCompatActivity implements MovieDetailActivityIView {
    private MovieDetailActivityPresenterImpl presenter;
    private ViewPager viewPager;
    private ActivityMovieDetailAdapter adapter;
    private static int CurrentPosition = 0;
    private String[] fhs;
    private int lastPosition = 0;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_movie_detail);
        Intent intent = getIntent();
        int position = intent.getIntExtra("position", 0);
        fhs = intent.getStringExtra("fhs").split(" ");

        Log.e("MovieDetailActivity", "position:" + position);
        presenter = new MovieDetailActivityPresenterImpl(this);
        presenter.DoInitViewPager();
        presenter.doInitFragments(fhs);
        if (position > 0) {
            viewPager.setCurrentItem(position);
            CurrentPosition = position;
        }


        presenter.doSetViewPagerListener();

    }


    @Override
    public void InitViewPager() {
        viewPager = (ViewPager) findViewById(R.id.vp_detail);
        if (adapter == null) {
            adapter = new ActivityMovieDetailAdapter(getSupportFragmentManager());
        }
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(1);
    }

    @Override
    public void setViewPagerListener() {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            /**
             * 页面滚动的时候
             * */
            @Override
            public void onPageSelected(int position) {
                //adapter.getItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if(videoPlayPouopWindow!=null){
                    videoPlayPouopWindow.dismiss();
                    videoPlayPouopWindow = null;
                }
            }
        });
    }

    @Override
    public void OnInitFragmentLayout(String[] fhs) {
        adapter.SetFragments(fhs);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter = null;
    }
}
