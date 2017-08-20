package me.com.movielibrary.ui.adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import me.com.movielibrary.ui.fragment.FragmentMovie;
import me.com.movielibrary.ui.fragment.FragmentMovieDetail;

/**
 * Created by Me on 2017/8/13.
 */

public class ActivityMovieDetailAdapter extends FragmentStatePagerAdapter {
    String[] fhs;
    FragmentManager fm;


    public ActivityMovieDetailAdapter(FragmentManager fm) {
        super(fm);
        this.fm = fm;
    }

    public void SetFragments(String[] f) {
        this.fhs = f;
        notifyDataSetChanged();
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }

    @Override
    public Fragment getItem(int position) {
        return FragmentMovieDetail.newInstance(fhs[position]);
    }


    @Override
    public int getCount() {
        if(fhs == null){
            return 0;
        }
        return fhs.length;
    }


}
