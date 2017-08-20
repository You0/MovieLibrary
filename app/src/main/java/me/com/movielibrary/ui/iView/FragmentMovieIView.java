package me.com.movielibrary.ui.iView;

import java.util.ArrayList;
import java.util.LinkedList;

import me.com.movielibrary.bean.MovieList;

/**
 * Created by Me on 2017/8/6.
 */

public interface FragmentMovieIView{
    void InitRecyclerView();
    void onSetProgress();
    void onRefreshDone(boolean success);
    void onSetRefreshListener();
    void onRefreshUI(ArrayList<MovieList> movieLists);
}
