package me.com.movielibrary.presenter;

/**
 * Created by Me on 2017/8/8.
 */

public interface FragmentMoviePresenter {
    void DoInitRecyclerVeiw();
    void DoGetData(int index);
    void DoShowProcess();
    void DoSetRefreshListener();

}
