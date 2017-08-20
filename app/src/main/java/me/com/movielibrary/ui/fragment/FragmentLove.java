package me.com.movielibrary.ui.fragment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;

import me.com.movielibrary.R;
import me.com.movielibrary.bean.MovieList;
import me.com.movielibrary.utils.DatabaseHelper;

/**
 * Created by Me on 2017/8/15.
 */

public class FragmentLove extends FragmentMovie{
    private DatabaseHelper databaseHelper;
    public static LinkedList<MovieList> movieLists = new LinkedList<>();

    @Override
    public View InitView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie,null);
        mSwipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.sr);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rl);
        mSwipeRefresh.setColorSchemeColors(getContext().getResources().getColor(R.color.colorAccent));
        presenter.DoInitRecyclerVeiw();
        databaseHelper = new DatabaseHelper(getContext(),"love",null,1);
        LoadDataFromDB();
        onSetRefreshListener();

        mRecyclerView.removeOnScrollListener(listener);

        return view;
    }

    private void LoadDataFromDB() {
        SQLiteDatabase database = databaseHelper.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT cover,title,actor,fh,c_date,rating FROM love",null);
        movieLists.clear();
        if (cursor.moveToFirst()) {
            do {
                MovieList movieList = new MovieList();

                movieList.setCover(cursor.getString(0));
                movieList.setTitle(cursor.getString(1));
                movieList.setActor(cursor.getString(2));
                movieList.setFh(cursor.getString(3));
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    movieList.setC_date(sdf.parse(cursor.getString(4)));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                movieList.setRating(cursor.getFloat(5));

                Log.e("love",movieList.toString());
                movieLists.add(movieList);
            } while (cursor.moveToNext());
        }
        cursor.close();
        onRefreshUI(movieLists);
        onRefreshDone(true);

    }


    @Override
    public void onSetRefreshListener() {
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.e("xx","refresh");
                LoadDataFromDB();
            }
        });
    }

    public void onRefreshUI(LinkedList<MovieList> movieLists) {
        mAdapter.setMovieLists(this.movieLists);
    }

}
