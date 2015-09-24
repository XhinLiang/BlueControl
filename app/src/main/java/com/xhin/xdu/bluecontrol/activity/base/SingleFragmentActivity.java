package com.xhin.xdu.bluecontrol.activity.base;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.xhin.xdu.bluecontrol.R;
import com.xhin.xdu.bluecontrol.application.ManagerApplication;

import de.greenrobot.event.EventBus;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ManagerApplication.getInstance().addActivity(this);
        EventBus.getDefault().register(this);
        setContentView(getLayoutResId());
        //获取控件
        findViews();
        //设置Toolbar标题
        toolbar.setTitle(Title() == null ? getString(R.string.app_name) : Title());
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ManagerApplication.getInstance().removeActivity(this);
        EventBus.getDefault().unregister(this);
    }
    public void showMassage(String massage) {
        SnackbarManager.show(
                Snackbar.with(getApplicationContext())
                        .text(massage).swipeToDismiss(true).duration(500), SingleFragmentActivity.this);
    }

    private void findViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
    }

    @SuppressWarnings("unused")
    public void onEventMainThread(String event) {
        showMassage(event);
    }
}