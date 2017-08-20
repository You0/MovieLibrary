package me.com.movielibrary.presenter;

/**
 * Created by Me on 2017/8/10.
 */

public interface MovieDetailActivityPresenter {
    //初始化viewpager
    void DoInitViewPager();
    //获取下一页的数据
    void DoNextPage();
    //设置viewpager的Listner
    void doSetViewPagerListener();
    //动态加载viewpager的item
    void doSetFragments(String[] fhs);
    //刚开始需要初始化的item
    void doInitFragments(String[] fhs);
}
