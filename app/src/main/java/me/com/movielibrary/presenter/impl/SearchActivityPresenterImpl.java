package me.com.movielibrary.presenter.impl;

import android.graphics.Bitmap;
import android.os.Message;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import http.body.Request;
import http.body.Response;
import me.com.movielibrary.bean.MovieList;
import me.com.movielibrary.presenter.SearchActivityPresenter;
import me.com.movielibrary.ui.fragment.FragmentMovie;
import me.com.movielibrary.ui.iView.FragmentMovieIView;
import me.com.movielibrary.ui.iView.SearchActivityIView;
import me.com.movielibrary.utils.AppUtils;
import task.AnsyCall;
import task.Dispatcher;

import static me.com.movielibrary.MyApplication.mImageloader;
import static me.com.movielibrary.MyApplication.options;

/**
 * Created by Me on 2017/8/14.
 */

public class SearchActivityPresenterImpl extends FragmentMoviePresenterImpl implements SearchActivityPresenter{
    public SearchActivityIView iView;
    public SearchActivityPresenterImpl(FragmentMovieIView iView) {
        super(iView);
        this.iView = (SearchActivityIView) iView;
    }

    @Override
    public void DoInitToolBar() {
        iView.InitToolBar();

    }

    public void DoGetData(HashMap<String,String> params, int index, int order){
        String type = params.get("type");
        String actor = params.get("actor");
        String key = params.get("key");
        String director = params.get("director");
        String time = params.get("time");
        String rating = params.get("rating");
        String series = params.get("series");
        String company = params.get("company");

        String realUrl = "http://115.159.159.65:8080/douban/search/group?";
        realUrl+= "index="+index+"&";
        realUrl+= "order="+order+"&";
        if(type!=null){
            realUrl+= "type="+type+"&";
        }
        if(actor!=null){
            realUrl+= "actor="+actor+"&";
        }
        if(key!=null){
            String[] keys = key.split(" ");
            StringBuilder sb = new StringBuilder();
            for(int i=0;i<keys.length-1;i++){
                sb.append(keys[i] + "%20");
            }
            sb.append(keys[keys.length-1]);
            realUrl+= "key="+sb.toString()+"&";
        }
        if(director!=null){
            realUrl+= "director="+director+"&";
        }
        if(company!=null){
            realUrl+= "company="+company+"&";
        }
        if(rating!=null){
            realUrl+= "rating="+rating+"&";
        }


        realUrl = realUrl.substring(0,realUrl.length()-1);


        Log.e("realurl",realUrl);
        try {
            Request request = new Request.Builder().get().url(realUrl).build();
            AnsyCall call = new AnsyCall(request);
            call.enqueue(new Dispatcher.CallBack() {
                @Override
                public void Error(Response response) {
                    Log.e("SearchActivity", "网站访问错误");
                    handler.sendEmptyMessage(DATA_ERROR);
                }

                @Override
                public void Success(Response response) {
                    try {
                        System.out.println("respone" + response.string("utf-8"));
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
