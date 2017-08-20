package me.com.movielibrary.presenter.impl;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;


import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import http.body.Request;
import http.body.Response;
import me.com.movielibrary.bean.Movie;

import me.com.movielibrary.presenter.FragmentMovieDetailPresenter;
import me.com.movielibrary.ui.iView.FragmentMovieDetailIView;
import task.AnsyCall;
import task.Dispatcher;



/**
 * Created by Me on 2017/8/11.
 */

public class FragmentMovieDetailPresenterImpl implements FragmentMovieDetailPresenter {
    private final int DATA_ERROR = 1;
    private final  int DATA_COMPLETE = 0;
    FragmentMovieDetailIView iView;
    private Handler handler;
    private List<View> tags;
    private List<View> actors;



    public void setActorsClick()
    {
        iView.setActorsClickListener(actors);
    }

    public void setTagsClick()
    {
        iView.setTagsClickListener(tags);
    }

    public void clearTags()
    {
        if (tags!=null){
            tags.clear();
        }
    }

    public void clearActors()
    {
        if(actors!=null){
            actors.clear();
        }
    }



    public void addTags(View view){
        if(tags==null){
            tags = new ArrayList<>();
        }
        tags.add(view);
    }

    public void addActors(View view){
        if(actors==null){
            actors = new ArrayList<>();
        }
        actors.add(view);
    }

    public void SetTagsListener(){
        iView.setTagsClickListener(tags);
    }

    public void SetActorsListener(){
        iView.setTagsClickListener(actors);
    }


    public FragmentMovieDetailPresenterImpl(final FragmentMovieDetailIView iView){
        this.iView = iView;
        handler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case DATA_ERROR:{
                        iView.setRefreshDone(false);
                        break;
                    }
                    case DATA_COMPLETE:{
                        iView.InitUI((Movie) msg.obj);
                        iView.setRefreshDone(true);
                    }
                }
            }
        };

    }

    @Override
    public void doGetData(String query) {
        final String realUrl = "http://115.159.159.65:8080/douban/movie/detail?fh=" + query;
        //Log.e("impl",realUrl);
        try {
            Request request = new Request.Builder().get().url(realUrl).build();
            AnsyCall call = new AnsyCall(request);
            call.enqueue(new Dispatcher.CallBack() {
                @Override
                public void Error(Response response) {
                    Log.e("FragmentMoviePresenter", "网站访问错误");
                    handler.sendEmptyMessage(DATA_ERROR);
                }

                @Override
                public void Success(Response response) {
                    try {
                        String result = response.string("utf-8");
                        Log.e("impl",result);
                        GsonBuilder builder = new GsonBuilder();
                        builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                            public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                                return new Date(json.getAsJsonPrimitive().getAsLong());
                            }
                        });
                        Gson gson = builder.create();
                        Movie movie;
                        movie = gson.fromJson(result, Movie.class);
                        Message message = handler.obtainMessage();
                        message.obj = movie;
                        message.what = DATA_COMPLETE;
                        handler.sendMessage(message);
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
    public void doSetData() {

    }

    @Override
    public void doPlay() {

    }

    @Override
    public void doLove() {

    }

    @Override
    public void doInitView() {
        iView.InitToolbar();
        iView.setRefresh();
        iView.setRefreshListener();
    }
}
