package me.com.movielibrary.ui.iView;

import android.support.v4.app.Fragment;

import java.util.List;

/**
 * Created by Me on 2017/8/5.
 */

public interface FragmentMainIView{
    void OnInitTabLayout(String[] titles);
    void OnInitFragmentLayout(List<Fragment> fragments);
}

