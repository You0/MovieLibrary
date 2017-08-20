package me.com.movielibrary.ui.iView;

import android.view.View;

import java.util.List;

import me.com.movielibrary.bean.Movie;

/**
 * Created by Me on 2017/8/10.
 */

public interface FragmentMovieDetailIView {
    void InitToolbar();
    void setRefreshListener();
    void setTags(String[] tags);
    void setActors(String[] actors);
    void setPhotos(String[] photos);
    void setRefreshDone(boolean success);
    void setRefresh();
    void OpenTextView();
    void setTagsClickListener(List<View> tags);
    void setActorsClickListener(List<View> actors);
    void InitUI(Movie movie);
}
