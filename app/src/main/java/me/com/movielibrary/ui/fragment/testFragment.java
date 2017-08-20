package me.com.movielibrary.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.com.movielibrary.R;

/**
 * Created by Me on 2017/8/5.
 */

public class testFragment extends BaseFragment {
    @Override
    public View InitView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.test,null);
    }

    @Override
    public void Create(Bundle savedInstanceState) {

    }
}
