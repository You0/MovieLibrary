package me.com.movielibrary.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Me on 2017/8/5.
 * 做一点点封装的工作
 */

public abstract class BaseFragment extends Fragment {
    private Context context;

    @Override
    public void onAttach(Context context) {
        this.context = context;
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Create(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return InitView(inflater,container,savedInstanceState);
    }

    public abstract View InitView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState);
    public abstract void Create(Bundle savedInstanceState);

}
