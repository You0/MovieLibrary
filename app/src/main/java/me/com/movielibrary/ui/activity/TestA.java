package me.com.movielibrary.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import me.com.movielibrary.R;

/**
 * Created by Me on 2017/8/11.
 */

public class TestA extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_movie_detail);
    }
}
