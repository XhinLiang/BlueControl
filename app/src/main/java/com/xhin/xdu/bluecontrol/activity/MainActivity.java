package com.xhin.xdu.bluecontrol.activity;

import android.app.Fragment;

import com.xhin.xdu.bluecontrol.activity.base.SingleFragmentActivity;
import com.xhin.xdu.bluecontrol.bean.BlueMessageFragment;

public class MainActivity extends SingleFragmentActivity {


    @Override
    protected Fragment createFragment() {
        return new BlueMessageFragment();
    }

    @Override
    protected String Title() {
        return null;
    }

}
