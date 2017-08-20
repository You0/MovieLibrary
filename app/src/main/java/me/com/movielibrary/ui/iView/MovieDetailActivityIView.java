package me.com.movielibrary.ui.iView;

import android.support.v4.app.Fragment;

import java.util.List;

/**
 * Created by Me on 2017/8/11.
 */

public interface MovieDetailActivityIView {
    void InitViewPager();
    void setViewPagerListener();
    void OnInitFragmentLayout(String[] fhs);
}
