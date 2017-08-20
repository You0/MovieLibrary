package me.com.movielibrary.ui.poupwindow;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import java.lang.ref.WeakReference;
import java.util.zip.Inflater;

import me.com.movielibrary.R;
import me.com.movielibrary.ui.customview.VideoPlayView;
import me.com.movielibrary.utils.AppUtils;

/**
 * Created by Me on 2017/8/14.
 */

public class VideoPlayPouopWindow extends PopupWindow {
    private WeakReference<Context> mContext;
    private VideoPlayView videoPlayView;
    private FloatingActionButton fb_play;

    public VideoPlayPouopWindow(Context context,String url)
    {
        mContext = new WeakReference<Context>(context);
        View view = LayoutInflater.from(mContext.get()).inflate(R.layout.poupwindow_layout,null);
        setContentView(view);
        videoPlayView = (VideoPlayView) view.findViewById(R.id.video_play_view);
        setHeight(AppUtils.dip2px(220));

        setTouchable(true);

        setFocusable(false);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xFF000000);
        // 设置SelectPicPopupWindow弹出窗体的背景
        setBackgroundDrawable(dw);
        setOutsideTouchable(false);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        videoPlayView.setUrl(url);
        videoPlayView.openVideo();
    }

    public void setFb_play(FloatingActionButton fb_play) {
        this.fb_play = fb_play;
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if(fb_play!=null){
            fb_play.setVisibility(View.VISIBLE);
        }
        videoPlayView = null;

    }
}
