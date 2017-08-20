package me.com.movielibrary.presenter;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

/**
 * Created by Me on 2017/8/5.
 */

public interface FragmentMainPresenter {
    void DoInitTabLayout(String[] titles);
    void DoInitFragmentLayout();
    void DoBindTab2ViewPager(ViewPager viewPager, TabLayout tabLayout);


}
