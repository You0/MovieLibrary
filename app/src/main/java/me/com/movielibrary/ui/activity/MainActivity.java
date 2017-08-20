package me.com.movielibrary.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import me.com.movielibrary.R;
import me.com.movielibrary.ui.fragment.FragmentMain;
import me.com.movielibrary.ui.iView.MainActivityIView;

public class MainActivity extends AppCompatActivity implements MainActivityIView{
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        InitToolBar();
        LoadContentFragment(R.id.content_main);

    }

    @Override
    public void InitToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if(null != toolbar){
            setSupportActionBar(toolbar);
        }
        getSupportActionBar().setTitle("Pocket");

    }

    @Override
    public void LoadContentFragment(int ResourseId) {
        getSupportFragmentManager().beginTransaction()
                .replace(ResourseId,
                        new FragmentMain(), "main_content").commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.search){
            Intent intent = new Intent(MainActivity.this,SearchActivity.class);
            startActivity(intent);
        }

        if(item.getItemId() == R.id.action_setting){
            Intent intent = new Intent(MainActivity.this,SettingActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.anim_enter_from_bottom,R.anim.anim_exit_from_bottom);
        }

        return true;
    }
}
