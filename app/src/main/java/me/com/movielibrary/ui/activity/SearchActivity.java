package me.com.movielibrary.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import me.com.movielibrary.R;
import me.com.movielibrary.bean.MovieList;
import me.com.movielibrary.presenter.impl.SearchActivityPresenterImpl;
import me.com.movielibrary.ui.adapter.FragmentMovieRecyclerViewAdapter;
import me.com.movielibrary.ui.adapter.SpacesItemDecoration;
import me.com.movielibrary.ui.iView.FragmentMovieIView;
import me.com.movielibrary.ui.iView.SearchActivityIView;



/**
 * Created by Me on 2017/8/14.
 */
//业务逻辑和视图逻辑与FragmentMovie十分相似大可以直接使用,稍加修改即可
public class SearchActivity extends AppCompatActivity implements SearchActivityIView {
    Toolbar mToolbar;
    RecyclerView mRecyclerView;
    FragmentMovieRecyclerViewAdapter adapter;
    SearchView mSearchView;
    SwipeRefreshLayout swipeRefreshLayout;
    SearchActivityPresenterImpl presenter;
    private boolean isScrollDown;
    private boolean isRefreshing;
    private String type;
    private String actor;
    public static LinkedList<MovieList> movieLists = new LinkedList<>();
    private HashMap<String,String> params = new HashMap<>();
    private static int index = 1;
    private int order = -1;
    private String title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        presenter = new SearchActivityPresenterImpl(this);
        Intent intent = getIntent();
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        type = intent.getStringExtra("type");
        actor = intent.getStringExtra("actor");
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.sr);
        mRecyclerView = (RecyclerView) findViewById(R.id.rl);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
        presenter.DoInitRecyclerVeiw();
        presenter.DoInitToolBar();
        movieLists.clear();
        params.clear();
        params.put("type",type);
        params.put("actor",actor);
        if(type!=null||actor!=null){
            presenter.DoGetData(params,index,order);
            presenter.DoShowProcess();
        }
        presenter.DoSetRefreshListener();

    }

    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        movieLists.clear();
        params.clear();
        type = intent.getStringExtra("type");
        actor = intent.getStringExtra("actor");
        params.put("type",type);
        params.put("actor",actor);
        if(type!=null||actor!=null){
            index = 1;
            presenter.DoGetData(params,index,order);
            presenter.DoShowProcess();
        }
        ActionBar actionBar = getSupportActionBar();
        if(type!=null){
            actionBar.setTitle(type);
            title= type;
        }
        if(actor!=null){
            actionBar.setTitle(actor);
            title = actor;
        }

    }
    @Override
    public void InitRecyclerView() {
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        // layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);//可防止Item切换

        mRecyclerView.setLayoutManager(layoutManager);



        adapter = new FragmentMovieRecyclerViewAdapter(this);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        SpacesItemDecoration decoration=new SpacesItemDecoration(16);
        mRecyclerView.addItemDecoration(decoration);
        mRecyclerView.setAdapter(adapter);


        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

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
                        presenter.DoGetData(params,index,order);
                        isRefreshing = true;
                        presenter.DoShowProcess();
                    }
                }
            }
        });
    }

    @Override
    public void onSetProgress() {
        swipeRefreshLayout.setRefreshing(true);

    }

    @Override
    public void onRefreshDone(boolean success) {
        if(!success){
            if(index>1){
                index--;
            }else{
                index = 1;
            }
            Toast.makeText(this,"获取失败",Toast.LENGTH_SHORT).show();
        }
        swipeRefreshLayout.setRefreshing(false);
        isRefreshing = false;
    }

    @Override
    public void onSetRefreshListener() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                index = 1;
                presenter.DoGetData(params,index,order);
            }
        });
    }

    /**
     * 更新数据刷新UI
     * */
    @Override
    public void onRefreshUI(ArrayList<MovieList> movieLists) {
        Log.e("searchActivity","xx");
        if(index==1){
            this.movieLists.clear();
        }

        this.movieLists.addAll(movieLists);
        adapter.setMovieLists(this.movieLists);
    }

    @Override
    public void InitToolBar() {
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if(type!=null){
            actionBar.setTitle(type);
            title = type;
        }
        if(actor!=null){
            actionBar.setTitle(actor);
            title = actor;
        }

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setDisplayShowCustomEnabled(true);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem item = menu.findItem(R.id.action_search_search);
        mSearchView = (SearchView) MenuItemCompat.getActionView(item);

        if(title==null){
            mSearchView.setIconified(false);
        }


        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                try {
                    movieLists.clear();
                    index  = 1;
                    params.clear();
                    params.put("key",query);
                    presenter.DoGetData(params,index,order);
                    getSupportActionBar().setTitle(query);
                    mSearchView.clearFocus();
                    presenter.DoShowProcess();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
        }

        if(item.getItemId() == R.id.time){
            if(order==1){
                order=-1;
            }else{
                order = 1;
            }
            index = 1;
            presenter.DoGetData(params,index,order);
            presenter.DoShowProcess();
        }

        if(item.getItemId() == R.id.rating){
            order = 0;
            index = 1;
            presenter.DoGetData(params,index,order);
            presenter.DoShowProcess();
        }

        return true;
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
