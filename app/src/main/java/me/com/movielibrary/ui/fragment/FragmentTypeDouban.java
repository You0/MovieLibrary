package me.com.movielibrary.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import me.com.movielibrary.R;
import me.com.movielibrary.ui.activity.MainActivity;
import me.com.movielibrary.ui.activity.SearchActivity;

/**
 * Created by Me on 2017/8/20.
 */

public class FragmentTypeDouban extends BaseFragment {
    private int[] imageRes = {
            R.drawable.ic_category_game_center,
            R.drawable.ic_category_live,
            R.drawable.ic_category_promo,
            R.drawable.ic_category_t1,
            R.drawable.ic_category_t3,
            R.drawable.ic_category_t4,
            R.drawable.ic_category_t5,
            R.drawable.ic_category_t11,
            R.drawable.ic_category_t23};

    //定义图标下方的名称数组
    private String[] name = {
            "剧情","爱情","喜剧","科幻","动作","悬疑","犯罪","恐怖","青春","励志",
            "战争","文艺","黑色幽默","传记","情色","暴力","音乐","家庭"
    };


    @Override
    public View InitView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_type_douban,null);
        GridView gridview = (GridView) view.findViewById(R.id.gridview);
        int length = imageRes.length;

        //生成动态数组，并且转入数据
        ArrayList<HashMap<String, Object>> lstImageItem = new ArrayList<HashMap<String, Object>>();
        for (int i = 0; i < length; i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("ItemImage", imageRes[i]);//添加图像资源的ID
            map.put("ItemText", name[i]);//按序号做ItemText
            lstImageItem.add(map);
        }
        //生成适配器的ImageItem 与动态数组的元素相对应
        SimpleAdapter saImageItems = new SimpleAdapter(getContext(),
                lstImageItem,//数据来源
                R.layout.fragment_type_douban_item,//item的XML实现
                //动态数组与ImageItem对应的子项
                new String[]{"ItemImage", "ItemText"},

                //ImageItem的XML文件里面的一个ImageView,两个TextView ID
                new int[]{R.id.iv_img, R.id.tv_text});
        //添加并且显示
        gridview.setAdapter(saImageItems);
        //添加消息处理
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), SearchActivity.class);
                intent.putExtra("type",name[position]);
                getContext().startActivity(intent);
            }
        });



    return view;
    }

    @Override
    public void Create(Bundle savedInstanceState) {

    }
}
