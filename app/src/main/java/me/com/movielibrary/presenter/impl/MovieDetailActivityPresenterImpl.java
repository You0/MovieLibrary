package me.com.movielibrary.presenter.impl;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import http.body.Request;
import http.body.Response;
import me.com.movielibrary.bean.MovieList;
import me.com.movielibrary.presenter.MovieDetailActivityPresenter;
import me.com.movielibrary.ui.fragment.FragmentMovie;
import me.com.movielibrary.ui.fragment.FragmentMovieDetail;
import me.com.movielibrary.ui.iView.MovieDetailActivityIView;
import me.com.movielibrary.utils.AppUtils;
import task.AnsyCall;
import task.Dispatcher;

import static me.com.movielibrary.MyApplication.mImageloader;
import static me.com.movielibrary.MyApplication.options;

/**
 * Created by Me on 2017/8/11.
 */

public class MovieDetailActivityPresenterImpl implements MovieDetailActivityPresenter {
    private MovieDetailActivityIView iView;
    private List<Fragment> fragmentMovieDetails = new ArrayList<>();
    private Handler handler;


    public MovieDetailActivityPresenterImpl(MovieDetailActivityIView iView) {
        this.iView = iView;
        handler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message msg) {
                //doSetFragments(fragmentMovieDetails.size());
            }
        };

    }

    @Override
    public void DoInitViewPager() {
        iView.InitViewPager();

    }

    public List<Fragment> getFragmentMovieDetails() {
        return fragmentMovieDetails;
    }

    @Override
    public void DoNextPage() {
        int index = ++FragmentMovie.index;
        final String realUrl = "http://115.159.159.65:8080/douban/search/last?index=" + index;
        try {
            Request request = new Request.Builder().get().url(realUrl).build();
            AnsyCall call = new AnsyCall(request);
            call.enqueue(new Dispatcher.CallBack() {
                @Override
                public void Error(Response response) {
                    Log.e("MovieDetailActivity", "网站访问错误");

                }

                @Override
                public void Success(Response response) {
                    try {
                        String result = response.string("utf-8");
                        GsonBuilder builder = new GsonBuilder();
                        builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                            public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                                return new Date(json.getAsJsonPrimitive().getAsLong());
                            }
                        });
                        Gson gson = builder.create();
                        List<MovieList> movieLists;
                        Type type = new TypeToken<List<MovieList>>() {
                        }.getType();
                        movieLists = gson.fromJson(result, type);
                        for (int i = 0; i < movieLists.size(); i++) {
                            movieLists.get(i).setWidth(0);
                            movieLists.get(i).setHeight(0);
                        }
                        //把下一页的信息加入
                        //FragmentMovie.movieLists.addAll(movieLists);
                        handler.sendEmptyMessage(fragmentMovieDetails.size());

                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void doSetViewPagerListener() {
        iView.setViewPagerListener();
    }

    @Override
    public void doSetFragments(String[] fhs) {


        //更新fragments
       // iView.OnInitFragmentLayout(fragmentMovieDetails);
    }

    @Override
    public void doInitFragments(String[] fhs) {

        iView.OnInitFragmentLayout(fhs);
    }
}
