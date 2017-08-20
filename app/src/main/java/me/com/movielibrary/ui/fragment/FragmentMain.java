package me.com.movielibrary.ui.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import me.com.movielibrary.R;
import me.com.movielibrary.presenter.impl.FragmentMainPresenterImpl;
import me.com.movielibrary.ui.adapter.FragmentMainViewpagerAdapter;
import me.com.movielibrary.ui.iView.FragmentMainIView;

/**
 * Created by Me on 2017/8/5.
 */

public class FragmentMain extends BaseFragment implements FragmentMainIView {
    FragmentMainPresenterImpl presenter;
    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    public void Create(Bundle savedInstanceState) {
        presenter = new FragmentMainPresenterImpl(this);
    }

    @Override
    public View InitView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, null);

        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        viewPager = (ViewPager) view.findViewById(R.id.vp_view);

        presenter.DoInitTabLayout(new String[]{"最新", "类别", "喜爱"});
        presenter.DoInitFragmentLayout();
        presenter.DoBindTab2ViewPager(viewPager,tabLayout);

        return view;
    }


    @Override
    public void OnInitTabLayout(String[] titles) {
        if (tabLayout.getChildCount() > 1) {
            return;
        }
        for (int i = 0; i < titles.length; i++) {
            tabLayout.addTab(tabLayout.newTab().setText(titles[i]));
        }
    }

    @Override
    public void OnInitFragmentLayout(List<Fragment> fragments) {
        FragmentMainViewpagerAdapter adapter = new FragmentMainViewpagerAdapter(getFragmentManager());
        adapter.SetFragments(fragments);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(3);
    }
}
