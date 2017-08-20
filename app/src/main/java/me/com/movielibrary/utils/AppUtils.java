package me.com.movielibrary.utils;

import android.content.Context;
import android.util.DisplayMetrics;

import me.com.movielibrary.MyApplication;

/**
 * Created by Me on 2017/8/10.
 */

public class AppUtils {
    public static DisplayMetrics getDisplayMetrics(Context context){
        return context.getResources().getDisplayMetrics();
    }

    public static int dip2px(float dpValue) {
        final float scale = MyApplication.getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }




    public static String fixImag(String img){

        return img;
    }

}
