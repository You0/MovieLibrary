package me.com.movielibrary.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import me.com.movielibrary.R;
import me.com.movielibrary.bean.MovieList;
import me.com.movielibrary.ui.activity.MovieDetaillActivity;
import me.com.movielibrary.ui.customview.AutoChangeLineLayout;
import me.com.movielibrary.utils.AppUtils;

import static me.com.movielibrary.MyApplication.mImageloader;
import static me.com.movielibrary.MyApplication.options;

/**
 * Created by Me on 2017/8/6.
 */

public class FragmentMovieRecyclerViewAdapter extends RecyclerView.Adapter<FragmentMovieRecyclerViewAdapter.ViewHolder> {
    private List<MovieList> movieLists;
    private Context context;
    private int mItemWidth;

    public void setMovieLists(List<MovieList> movieLists) {
        this.movieLists = movieLists;
        notifyDataSetChanged();
    }

    public FragmentMovieRecyclerViewAdapter(Context context) {
        this.context = context;

        mItemWidth = AppUtils.getDisplayMetrics(context).widthPixels / 2 - 16;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_movie_item, null);
        return new ViewHolder(item);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //TODO 测试用例
        //holder.iv_movie_cover.setImageResource(movieLists.get(position).getCover());
        //图片加载

        holder.itemView.setTag(position);
        holder.title.setText(movieLists.get(position).getTitle());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        holder.time.setText(sdf.format(movieLists.get(position).getC_date()));
        String[] actors = movieLists.get(position).getActor().split(" ");
        holder.autoLine.removeAllViews();
        //加载演员名单。
        for(int i=0;i<actors.length;i++){
            if(actors[i].equals("")){
                continue;
            }
            TextView tv = new TextView(context);
            tv.setTextSize(10);
            tv.setBackground(context.getResources().getDrawable(R.drawable.tag));
            tv.setText(actors[i]);
            tv.setPadding(2,2,2,2);
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.leftMargin=2;
            layoutParams.topMargin = 2;
            tv.setLayoutParams(layoutParams);
            holder.autoLine.addView(tv);
            //使布局重绘
            holder.autoLine.invalidate();
        }

        String imgs = movieLists.get(position).getCover();

        //加载图片
       String uri = (String) holder.iv_movie_cover.getTag();

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(mItemWidth, getHeight(movieLists.get(position),mItemWidth));
        holder.iv_movie_cover.setLayoutParams(params);
        if(uri == null || !uri.equals(imgs)){
            holder.iv_movie_cover.setImageDrawable(context.getResources().getDrawable(R.drawable.timg));
        }
        if(!imgs.equals("")){
            mImageloader.displayImage(imgs,holder.iv_movie_cover,options);
        }
        holder.iv_movie_cover.setTag(imgs);



    }

    @Override
    public int getItemCount() {
        if(movieLists==null){
            return 0;
        }
        return movieLists.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView iv_movie_cover;
        AutoChangeLineLayout autoLine;
        TextView title;
        TextView time;


        public ViewHolder(View itemView) {
            super(itemView);
            iv_movie_cover = (ImageView) itemView.findViewById(R.id.iv_movie_cover);
            autoLine = (AutoChangeLineLayout) itemView.findViewById(R.id.autoLine);
            title = (TextView) itemView.findViewById(R.id.title);
            time = (TextView) itemView.findViewById(R.id.time);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, MovieDetaillActivity.class);
            String fhs = "";
            for(int i=0;i<movieLists.size();i++){
                fhs += movieLists.get(i).getFh() +" ";
            }

            intent.putExtra("fhs",fhs);
            intent.putExtra("position",(Integer) v.getTag());
            context.startActivity(intent);
        }
    }



    public static  int getHeight(MovieList bean,int newWidth){
        float btmW= bean.getWidth();
        float btmH= bean.getHeight();

        if(btmW==0){
            return (int)(newWidth*1.4);
        }
        float scale= btmH/btmW;
        return (int) (newWidth*scale);
    }
}
