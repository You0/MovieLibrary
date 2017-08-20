package me.com.movielibrary.presenter;

/**
 * Created by Me on 2017/8/11.
 */

public interface FragmentMovieDetailPresenter {
    //获取数据
    void doGetData(String query);
    //设置数据
    void doSetData();
    //播放视频
    void doPlay();
    //收藏
    void doLove();

    void doInitView();

}
