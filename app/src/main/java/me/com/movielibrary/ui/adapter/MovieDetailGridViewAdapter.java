package me.com.movielibrary.ui.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import me.com.movielibrary.R;

import static me.com.movielibrary.MyApplication.mImageloader;
import static me.com.movielibrary.MyApplication.options;

/**
 * Created by Me on 2017/8/11.
 */

public class MovieDetailGridViewAdapter extends ArrayAdapter {
    private int Resource;
    private ArrayList<String> arrayList = new ArrayList();
    private Context context;
    private ImageLoader imageLoader;
    private DisplayImageOptions options;

    public MovieDetailGridViewAdapter(Context context, int resource, List objects) {

            super(context, resource, objects);


        this.context = context;
        arrayList.addAll(objects);
        Resource = resource;
        imageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
    }



    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.fragment_movie_detial_gridview_item, null);
            holder.imageView = (ImageView) convertView.findViewById(R.id.image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if(!arrayList.get(position).equals("")){
            imageLoader.displayImage(arrayList.get(position),holder.imageView,options);
        }



        return convertView;
    }

    private final class ViewHolder {
        ImageView imageView;
    }
}
