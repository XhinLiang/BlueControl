package com.xhin.xdu.bluecontrol.activity.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.xhin.xdu.bluecontrol.R;
import com.xhin.xdu.bluecontrol.application.ManagerApplication;

import de.greenrobot.event.EventBus;

/**
 * Created by Xhin on 2015/7/25.
 * XhinLiang@gmail.com
 * 基本的Activity,拥有返回按钮,注册EventBus
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected Activity context;

    protected abstract String setTitle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ManagerApplication.getInstance().addActivity(this);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(setTitle() == null ? getString(R.string.app_name) : setTitle());
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
        EventBus.getDefault().register(this);
        context = this;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ManagerApplication.getInstance().removeActivity(this);
        EventBus.getDefault().unregister(this);
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
                        .text(massage).swipeToDismiss(true).duration(500), BaseActivity.this);
    }

    @Override
    public void setTitle(CharSequence title){
        if (getSupportActionBar()!=null)
            getSupportActionBar().setTitle(title);
    }


    @SuppressWarnings("unused")
    public void onEventMainThread(String event) {
        showMassage(event);
    }
}
