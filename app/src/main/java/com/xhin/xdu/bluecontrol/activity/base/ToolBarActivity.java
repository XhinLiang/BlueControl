package com.xhin.xdu.bluecontrol.activity.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.ViewConfiguration;

import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.xhin.xdu.bluecontrol.R;
import com.xhin.xdu.bluecontrol.application.ManagerApplication;

import java.lang.reflect.Field;

import de.greenrobot.event.EventBus;

/**
 * Created by Xhin on 2015/7/25.
 * XhinLiang@gmail.com
 * 类似BaseActivity,只是把ActionBar换成了ToolBar
 * 注意,使用此抽象类时主题必须设定为NoTitleBar或者NoActionBar
 */
public abstract class ToolBarActivity extends AppCompatActivity {

    protected Activity context;

    protected abstract String setTitle();

    protected abstract int setContentViewID();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ManagerApplication.getInstance().addActivity(this);
        setContentView(setContentViewID());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(setTitle() == null ? getString(R.string.app_name) : setTitle());
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
        EventBus.getDefault().register(this);
        context = this;
        try {
            ViewConfiguration mconfig = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            menuKeyField.setAccessible(true);
            menuKeyField.setBoolean(mconfig, false);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ManagerApplication.getInstance().removeActivity(this);
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
                        .text(massage).swipeToDismiss(true).duration(500), ToolBarActivity.this);
    }


    @SuppressWarnings("unused")
    public void onEventMainThread(String event) {
        showMassage(event);
    }
}