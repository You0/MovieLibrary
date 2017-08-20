package me.com.movielibrary.ui.activity;


import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import java.io.File;
import me.com.movielibrary.R;
import static me.com.movielibrary.MyApplication.mImageloader;

/**
 * Created by Me on 2017/8/16.
 */

public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new PrefsFragement()).commit();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
        }
        return true;
    }

    public static class PrefsFragement extends PreferenceFragment {
        PreferenceScreen clean;
        PreferenceScreen update;
        PreferenceScreen developer;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);


            clean = (PreferenceScreen)findPreference("clean");
            update = (PreferenceScreen)findPreference("update");
            developer = (PreferenceScreen)findPreference("developer");

            File cache = mImageloader.getDiskCache().getDirectory();
            File[] files = cache.listFiles();
            long cacheSize =0;
            for(int i=0;i<files.length;i++){
                cacheSize += files[i].getTotalSpace();
            }


            Log.e("cacheSize", String.valueOf(cacheSize));
            clean.setSummary("图片缓存大小为"+cacheSize/1024/1024/1024/1024 + "MB");

            clean.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    mImageloader.getDiskCache().clear();
                    clean.setSummary("图片缓存大小为 0 MB");
                    return true;
                }
            });
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.anim_enter_from_bottom,R.anim.anim_exit_from_bottom);
    }
}
