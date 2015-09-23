package com.xhin.xdu.bluecontrol.activity.base;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.ViewConfiguration;

import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.xhin.xdu.bluecontrol.R;

import java.lang.reflect.Field;

/**
 * Created by xhinliang on 15-8-30.
 * xhinliang@gmail.com
 * 托管单个Fragment的抽象Activity
 */
public abstract class SingleFragmentActivity extends AppCompatActivity {

    private Toolbar toolbar;

    protected abstract Fragment createFragment();

    protected abstract String Title();

    protected int getLayoutResId() {
        return R.layout.activity_toolbar;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
        findViews(); //获取控件
        toolbar.setTitle(Title() == null ? getString(R.string.app_name) : Title());//设置Toolbar标题
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));//设置标题颜色
        setSupportActionBar(toolbar);
        if (null != getSupportActionBar()) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        FragmentManager manager = getFragmentManager();
        Fragment fragment = manager.findFragmentById(R.id.fragment_main);
        if (fragment == null) {
            fragment = createFragment();
            manager.beginTransaction()
                    .add(R.id.fragment_main, fragment)
                    .commit();
        }
        try {
            ViewConfiguration mConfig = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            menuKeyField.setAccessible(true);
            menuKeyField.setBoolean(mConfig, false);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
    public void showMassage(String massage) {
        SnackbarManager.show(
                Snackbar.with(getApplicationContext())
                        .text(massage).swipeToDismiss(true).duration(500), SingleFragmentActivity.this);
    }

    private void findViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
    }
}