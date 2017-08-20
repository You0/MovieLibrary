package me.com.movielibrary.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import me.com.movielibrary.ui.fragment.FragmentMovie;
import me.com.movielibrary.ui.fragment.FragmentMovieDetail;

/**
 * Created by Me on 2017/8/5.
 */

public class FragmentMainViewpagerAdapter extends FragmentPagerAdapter {
    List<Fragment> fragments;
    FragmentManager fm;


    public FragmentMainViewpagerAdapter(FragmentManager fm) {
        super(fm);
        this.fm = fm;
    }

    public void SetFragments(List<Fragment> f){
        this.fragments = f;
        notifyDataSetChanged();
    }



    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }






    @Override
    public int getCount() {
        if(fragments==null){
            return 0;
        }
        return fragments.size();
    }
}
