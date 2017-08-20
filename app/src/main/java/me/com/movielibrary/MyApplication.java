package me.com.movielibrary;

import android.app.Application;
import android.content.Context;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;

/**
 * Created by Me on 2017/8/9.
 */

public class MyApplication extends Application {
    public static ImageLoader mImageloader;
    public static DisplayImageOptions options;
    private static Context context;



    public static Context getContext()
    {
        return context;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        initImageLoader(getApplicationContext());
    }

    private void initImageLoader(Context context) {
        File cacheDir = StorageUtils.getCacheDirectory(context);
        ImageLoaderConfiguration config = ImageLoaderConfiguration.createDefault(context);
        ImageLoader.getInstance().init(config);
        mImageloader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();

    }

}
