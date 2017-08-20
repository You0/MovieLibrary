package me.com.movielibrary.ui.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.LinkedList;
import me.com.movielibrary.R;
import me.com.movielibrary.bean.MovieList;
import me.com.movielibrary.presenter.impl.FragmentMoviePresenterImpl;
import me.com.movielibrary.ui.adapter.FragmentMovieRecyclerViewAdapter;
import me.com.movielibrary.ui.adapter.SpacesItemDecoration;
import me.com.movielibrary.ui.iView.FragmentMovieIView;

import static me.com.movielibrary.MyApplication.mImageloader;

/**
 * Created by Me on 2017/8/6.
 */

public class FragmentMovie extends BaseFragment implements FragmentMovieIView{
    protected SwipeRefreshLayout mSwipeRefresh;
    protected RecyclerView mRecyclerView;
    protected FragmentMovieRecyclerViewAdapter mAdapter;
    protected FragmentMoviePresenterImpl presenter;
    protected LinkedList<MovieList> movieLists = new LinkedList<>();
    public static int index = 1 ;
    protected boolean isScrollDown;
    protected boolean isRefreshing = false;
    protected RecyclerView.OnScrollListener listener;

    @Override
    public View InitView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie,null);

        mSwipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.sr);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rl);
        mSwipeRefresh.setColorSchemeColors(getContext().getResources().getColor(R.color.colorAccent));
        presenter.DoInitRecyclerVeiw();
        presenter.DoGetData(1);
        presenter.DoShowProcess();
        presenter.DoSetRefreshListener();
        return view;
    }

    @Override
    public void Create(Bundle savedInstanceState) {
        presenter = new FragmentMoviePresenterImpl(this);
    }

    @Override
    public void InitRecyclerView() {
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
       // layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);//可防止Item切换

        mRecyclerView.setLayoutManager(layoutManager);



        mAdapter = new FragmentMovieRecyclerViewAdapter((getContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        SpacesItemDecoration decoration=new SpacesItemDecoration(16);
        mRecyclerView.addItemDecoration(decoration);
        mRecyclerView.setAdapter(mAdapter);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        //mRecyclerView.setHasFixedSize(true);
        //adapter.setOnItemClickListener(this);



        listener = new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                isScrollDown = dy > 0;
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {// RecyclerView已经停止滑动
                    int lastVisibleItem;
                    // 获取RecyclerView的LayoutManager
                    RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                    // 获取到最后一个可见的item
                    if (layoutManager instanceof LinearLayoutManager) {// 如果是LinearLayoutManager
                        lastVisibleItem = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                    } else if (layoutManager instanceof StaggeredGridLayoutManager) {// 如果是StaggeredGridLayoutManager
                        int[] into = new int[((StaggeredGridLayoutManager) layoutManager).getSpanCount()];
                        ((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(into);
                        lastVisibleItem = findMax(into);
                    } else {// 否则抛出异常
                        throw new RuntimeException("Unsupported LayoutManager used");
                    }
                    // 获取item的总数
                    int totalItemCount = layoutManager.getItemCount();
                    if (lastVisibleItem >= totalItemCount - 1 && isScrollDown && isRefreshing==false) {
                        index = index +1;
                        presenter.DoGetData(index);
                        isRefreshing = true;
                        presenter.DoShowProcess();
                    }
                }
            }
        };

        mRecyclerView.addOnScrollListener(listener);
    }

    @Override
    public void onSetProgress() {
        mSwipeRefresh.setRefreshing(true);

    }

    @Override
    public void onRefreshDone(boolean success) {
        if(!success){
            index--;
        }
        mSwipeRefresh.setRefreshing(false);
        isRefreshing = false;
    }

    @Override
    public void onSetRefreshListener() {
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                index = 1;
                presenter.DoGetData(index);
            }
        });
    }

    /**
     * 更新数据刷新UI
     * */
    @Override
    public void onRefreshUI(ArrayList<MovieList> movieLists) {
        if(index==1){
            this.movieLists.clear();
        }



        this.movieLists.addAll(movieLists);
        mAdapter.setMovieLists(this.movieLists);
    }

    private int findMax(int[] lastPositions) {
        int max = lastPositions[0];
        for (int value : lastPositions) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

}
