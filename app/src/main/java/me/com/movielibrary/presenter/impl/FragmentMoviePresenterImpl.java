package me.com.movielibrary.presenter.impl;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import http.body.Request;
import http.body.Response;
import me.com.movielibrary.bean.MovieList;
import me.com.movielibrary.presenter.FragmentMoviePresenter;
import me.com.movielibrary.ui.iView.FragmentMovieIView;
import me.com.movielibrary.utils.AppUtils;
import task.AnsyCall;
import task.Dispatcher;

import static me.com.movielibrary.MyApplication.mImageloader;
import static me.com.movielibrary.MyApplication.options;

/**
 * Created by Me on 2017/8/8.
 */

public class FragmentMoviePresenterImpl implements FragmentMoviePresenter {
    public final int DATA_COMPLETE = 0;
    public final int DATA_ERROR = 1;

    public FragmentMovieIView iView;
    public Handler handler;

    public FragmentMoviePresenterImpl(final FragmentMovieIView iView) {
        this.iView = iView;
        //初始化Handler
        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case DATA_COMPLETE: {
                        iView.onRefreshDone(true);
                        iView.onRefreshUI((ArrayList<MovieList>) msg.obj);
                        break;
                    }
                    case DATA_ERROR: {
                        iView.onRefreshDone(false);
                    }
                }
            }
        };
    }

    @Override
    public void DoInitRecyclerVeiw() {
        iView.InitRecyclerView();
    }


    @Override
    public void DoShowProcess() {
        iView.onSetProgress();
    }


    @Override
    public void DoSetRefreshListener() {
        iView.onSetRefreshListener();
    }

    public void DoGetData(int index) {
        final String realUrl = "http://115.159.159.65:8080/douban/search/last?index=" + index;

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
                        //System.out.println(response.string("utf-8"));
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
                            if (movieLists.get(i).getCover().equals("")) {
                                movieLists.get(i).setWidth(0);
                                movieLists.get(i).setHeight(0);
                            } else {
                                String imgs = movieLists.get(i).getCover();
                                Log.e("igs",imgs);
                                Bitmap bp = null;
                                try {
                                    bp = mImageloader.loadImageSync(imgs, options);
                                } catch (Exception e) {

                                }
                                if (bp == null) {
                                    movieLists.get(i).setWidth(0);
                                    movieLists.get(i).setHeight(0);
                                } else {
                                    movieLists.get(i).setWidth(bp.getWidth());
                                    movieLists.get(i).setHeight(bp.getHeight());
                                }

                            }

                        }
                        Message message = handler.obtainMessage();
                        message.obj = movieLists;
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


}
