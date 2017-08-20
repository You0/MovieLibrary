package me.com.movielibrary.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.graphics.Palette;

import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import me.com.movielibrary.MyApplication;
import me.com.movielibrary.R;
import me.com.movielibrary.bean.Movie;
import me.com.movielibrary.bean.MovieList;
import me.com.movielibrary.presenter.impl.FragmentMovieDetailPresenterImpl;
import me.com.movielibrary.ui.activity.ImageDisplay;
import me.com.movielibrary.ui.activity.SearchActivity;
import me.com.movielibrary.ui.adapter.MovieDetailGridViewAdapter;
import me.com.movielibrary.ui.customview.AutoChangeLineLayout;
import me.com.movielibrary.ui.customview.MyGriView;
import me.com.movielibrary.ui.iView.FragmentMovieDetailIView;
import me.com.movielibrary.ui.iView.FragmentMovieIView;
import me.com.movielibrary.ui.poupwindow.VideoPlayPouopWindow;
import me.com.movielibrary.utils.DatabaseHelper;

import static me.com.movielibrary.MyApplication.mImageloader;
import static me.com.movielibrary.MyApplication.options;

/**
 * Created by Me on 2017/8/10.
 */

public class FragmentMovieDetail extends BaseFragment implements FragmentMovieDetailIView, AppBarLayout.OnOffsetChangedListener {
    FragmentMovieDetailPresenterImpl presenter;
    private WeakReference<String> fh;
    private AppBarLayout appBarLayout;
    private Toolbar toolbar;
    private SwipeRefreshLayout detial_sr;
    private CollapsingToolbarLayout toolbar_container;
    private ImageView iv_cover;
    private Button bt_love;
    private TextView tv_title;
    private TextView tv_rating;
    private TextView tv_time;
    private TextView tv_descript;
    private AutoChangeLineLayout tags;
    private AutoChangeLineLayout actors;
    private MyGriView grv_album;
    private FloatingActionButton fb_play;
    private MovieDetailGridViewAdapter adapter;
    private View view;
    public static VideoPlayPouopWindow videoPlayPouopWindow ;
    private DatabaseHelper databaseHelper;
    private Movie mMovie;
    private Context context;

    private ArrayList<String> imgs = null;;
    protected boolean isVisible = false;
    protected boolean isCreateView = false;
    protected boolean isOnece = true;


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            isVisible = true;
        }
    }


    public static FragmentMovieDetail newInstance(String fh) {
        FragmentMovieDetail newFragment = new FragmentMovieDetail();
        Bundle bundle = new Bundle();

        bundle.putString("fh", fh);
        newFragment.setArguments(bundle);
        return newFragment;
    }


    @Override
    public View InitView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = getContext();
        view = inflater.inflate(R.layout.fragment_movie_detail, null);
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        detial_sr = (SwipeRefreshLayout) view.findViewById(R.id.detial_sr);
        toolbar_container = (CollapsingToolbarLayout) view.findViewById(R.id.toolbar_container);
        iv_cover = (ImageView) view.findViewById(R.id.iv_cover);
        bt_love = (Button) view.findViewById(R.id.bt_love);
        tv_title = (TextView) view.findViewById(R.id.tv_title);
        tv_rating = (TextView) view.findViewById(R.id.tv_rating);
        tv_time = (TextView) view.findViewById(R.id.tv_time);
        tv_descript = (TextView) view.findViewById(R.id.tv_descript);
        tags = (AutoChangeLineLayout) view.findViewById(R.id.tags);
        actors = (AutoChangeLineLayout) view.findViewById(R.id.actors);
        grv_album = (MyGriView) view.findViewById(R.id.grv_album);
        fb_play = (FloatingActionButton) view.findViewById(R.id.fb_play);
        appBarLayout = (AppBarLayout) view.findViewById(R.id.appbar);


        //初始化
        presenter.doInitView();
        //获取数据

        isCreateView = true;


         presenter.doGetData(fh.get());





        return view;
    }

    public FragmentMovieDetailPresenterImpl getPresenter() {
        return presenter;
    }

    public String getFh() {
        return fh.get();
    }

    public boolean getIsOnece()
    {
        return isOnece;
    }

    public void setFh(String fh) {
        this.fh = new WeakReference<String>(fh);
    }
    public void  setIsOnece(boolean is){
        isOnece = is;
    }


    @Override
    public void Create(Bundle savedInstanceState) {
        presenter = new FragmentMovieDetailPresenterImpl(this);
        Bundle args = getArguments();
        fh = new WeakReference<String>(args.getString("fh"));
        args = null;
    }

    @Override
    public void InitToolbar() {
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getSupportActionBar().setTitle(fh.get());
        appBarLayout.addOnOffsetChangedListener(this);

    }

    @Override
    public void setRefreshListener() {
        detial_sr.setColorSchemeColors(getContext().getResources().getColor(R.color.colorAccent));
        detial_sr.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.doGetData(fh.get());
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void setTags(String[] tags) {
        this.tags.removeAllViews();
        presenter.clearTags();
        for(int i=0;i<tags.length;i++){
            if(tags[i].equals("")){
                continue;
            }
            TextView tv = new TextView(context);
            presenter.addTags(tv);
            tv.setTextSize(15);
            tv.setBackground(context.getResources().getDrawable(R.drawable.tag));
            tv.setText(tags[i]);
            tv.setPadding(5,5,5,5);
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.leftMargin=20;
            layoutParams.topMargin = 10;
            tv.setLayoutParams(layoutParams);
            this.tags.addView(tv);
            //使布局重绘

        }
        this.tags.invalidate();
        presenter.setTagsClick();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void setActors(String[] actors) {
        this.actors.removeAllViews();
        presenter.clearActors();
        for (int i = 0; i < actors.length; i++) {
            if (actors[i].equals("")) {
                continue;
            }
            TextView tv = new TextView(context);
            presenter.addActors(tv);
            tv.setTextSize(15);

            tv.setBackground(context.getResources().getDrawable(R.drawable.tag));
            tv.setText(actors[i]);
            tv.setPadding(5, 5, 5, 5);
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.leftMargin = 20;
            layoutParams.topMargin = 10;
            tv.setLayoutParams(layoutParams);
            this.actors.addView(tv);
            //使布局重绘
        }
        this.actors.invalidate();
        presenter.setActorsClick();
    }

    @Override
    public void setPhotos(String[] photos) {

        if (adapter == null) {
            imgs = new ArrayList<>();
            for (int i = 0; i < photos.length; i++) {
                imgs.add(photos[i]);
            }
            Context c = getContext();
            if(c!=null){
                adapter = new MovieDetailGridViewAdapter(c, R.layout.fragment_movie_detial_gridview_item, imgs);
                grv_album.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        }

        grv_album.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), ImageDisplay.class);
                intent.putExtra("temp", position);
                intent.putStringArrayListExtra("imageUrl", imgs);
                getContext().startActivity(intent);
            }
        });
        
    }

    @Override
    public void setRefreshDone(boolean success) {
        if(!success){
            Toast.makeText(context, "网络错误", Toast.LENGTH_SHORT).show();
        }
        detial_sr.setRefreshing(false);
    }

    @Override
    public void setRefresh() {
        detial_sr.setRefreshing(true);
    }

    @Override
    public void OpenTextView() {
        tv_descript.setMaxLines(Integer.MAX_VALUE);
        tv_descript.invalidate();
    }

    @Override
    public void setTagsClickListener(List<View> tags) {
        if(tags==null){
            return;
        }
        for(int i=0;i<tags.size();i++){
            TextView tv = (TextView) tags.get(i);
            final String content = tv.getText().toString();
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), SearchActivity.class);
                    intent.putExtra("type",content);
                    getContext().startActivity(intent);
                }
            });
        }

    }

    @Override
    public void setActorsClickListener(List<View> actors) {
        if(actors==null){
            return;
        }
        for (int i = 0; i < actors.size(); i++) {
            TextView tv = (TextView) actors.get(i);
            final String content = tv.getText().toString();
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), SearchActivity.class);
                    intent.putExtra("actor",content);
                    getContext().startActivity(intent);
                }
            });
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void InitUI(final Movie movie) {
        this.mMovie = movie;
        //加载封面图片
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if(activity!=null){
            ActionBar actionBar = activity.getSupportActionBar();
            if(actionBar!=null){
                actionBar.setTitle(movie.getTitle());
            }
        }

        mImageloader.displayImage(movie.getCover(), iv_cover, options,new SimpleImageLoadingListener() {
            @Override
            public void onLoadingComplete(String imageUri, View view,
                                          final Bitmap loadedImage) {
                super.onLoadingComplete(imageUri, view, loadedImage);
                Palette.from(loadedImage).generate(new Palette.PaletteAsyncListener() {
                    @Override
                    public void onGenerated(Palette palette) {
                        int defaultBgColor = Color.parseColor("#009688");
                        int bgColor = palette.getDarkVibrantColor(defaultBgColor);
                        toolbar_container.setBackgroundColor(bgColor);
                    }
                });
            }
        });
        toolbar_container.setTitle(movie.getTitle());
        //toolbar.setTitle(movie.getTitle());
        tv_title.setText(movie.getTitle());
        tv_rating.setText("评分: " + movie.getRating());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        tv_time.setText(sdf.format(movie.getDate()));
        tv_descript.setText("简介:"+movie.getSeries());

        tv_descript.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenTextView();
            }
        });

        String[] images = movie.getImgs().split(" ");
        setPhotos(images);
        String[] actors = movie.getActor().split(" ");
        setActors(actors);
        String[] tags = movie.getType().split(" ");
        setTags(tags);

        bt_love.setTag(false);
        for(int i=0;i<FragmentLove.movieLists.size();i++){
            if(FragmentLove.movieLists.get(i).getFh().equals(mMovie.getFh())){
                bt_love.setText("已收藏");
                bt_love.setBackground(getResources().getDrawable(R.drawable.black_button));
                bt_love.setTag(true);
            }
        }

        bt_love.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(databaseHelper==null){
                    databaseHelper = new DatabaseHelper(getContext(),"love",null,1);
                }
                if(mMovie==null){
                    return;
                }
                boolean tag = (boolean) bt_love.getTag();
                SQLiteDatabase database = databaseHelper.getWritableDatabase();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                if(tag){
                    try {
                        database.execSQL("DELETE FROM love WHERE fh='"+mMovie.getFh()+"'");
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    bt_love.setTag(false);
                    bt_love.setText("+喜欢");
                    bt_love.setBackground(getResources().getDrawable(R.drawable.blue_button));
                    return;
                }

                String date  = sdf.format(mMovie.getDate());
                Log.e("date",date);
                try{
                    database.execSQL("INSERT INTO love (cover,title,actor,fh,c_date,rating)" +
                            "values(?,?,?,?,?,?)", new Object[]{mMovie.getCover(), mMovie.getTitle(), mMovie.getActor(),mMovie.getFh(), date, mMovie.getRating()});

                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(getContext(),"已经收藏过了！",Toast.LENGTH_SHORT).show();
                }

                bt_love.setText("已收藏");
                bt_love.setBackground(getResources().getDrawable(R.drawable.black_button));
                bt_love.setTag(true);

            }
        });


        fb_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int y = iv_cover.getHeight()/2;
                int x = iv_cover.getWidth() / 2;
                float currentY = fb_play.getY();

                TranslateAnimation transformation =
                        new TranslateAnimation(0,0 ,0, y-currentY);
                transformation.setDuration(200);
                fb_play.startAnimation(transformation);

                transformation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        fb_play.setVisibility(View.GONE);
                        String video = movie.getVideo();
                        Log.e("video",video);
                        videoPlayPouopWindow = new VideoPlayPouopWindow(MyApplication.getContext(),video);
                        videoPlayPouopWindow.showAtLocation(iv_cover, Gravity.NO_GRAVITY,0,0);
                        videoPlayPouopWindow.setFb_play(fb_play);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }
        });
    }

    public VideoPlayPouopWindow getVideoPlayPouopWindow() {
        return videoPlayPouopWindow;
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        detial_sr.setEnabled(verticalOffset == 0);
    }




}
