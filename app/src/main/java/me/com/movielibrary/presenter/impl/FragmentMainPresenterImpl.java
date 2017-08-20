package me.com.movielibrary.presenter.impl;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;
import java.util.List;

import me.com.movielibrary.presenter.FragmentMainPresenter;
import me.com.movielibrary.ui.fragment.FragmentLove;
import me.com.movielibrary.ui.fragment.FragmentMovie;
import me.com.movielibrary.ui.fragment.FragmentTypeDouban;
import me.com.movielibrary.ui.iView.FragmentMainIView;

/**
 * Created by Me on 2017/8/5.
 */

public class FragmentMainPresenterImpl implements FragmentMainPresenter {
    private FragmentMainIView iView;

    public FragmentMainPresenterImpl(FragmentMainIView iView) {
        this.iView = iView;
    }

    @Override
    public void DoInitTabLayout(String[] titles) {
        iView.OnInitTabLayout(titles);
    }

    @Override
    public void DoInitFragmentLayout() {
        List<Fragment> fragments = new ArrayList<>();
        //这里初始化需要载入的view
        Fragment fragmentMovie = new FragmentMovie();
        Fragment fragmentType = new FragmentTypeDouban();
        Fragment fragmentLove = new FragmentLove();
        fragments.add(fragmentMovie);
        fragments.add(fragmentType);
        fragments.add(fragmentLove);
        iView.OnInitFragmentLayout(fragments);
    }

    @Override
    public void DoBindTab2ViewPager(final ViewPager viewPager, final TabLayout tabLayout) {
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition(), true);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                tabLayout.setScrollPosition(position, positionOffset, true);
            }

            @Override
            public void onPageSelected(int position) {
                tabLayout.getTabAt(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }
}
