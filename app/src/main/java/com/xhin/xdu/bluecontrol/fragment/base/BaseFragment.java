package com.xhin.xdu.bluecontrol.fragment.base;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;

import com.nispok.snackbar.Snackbar;

import de.greenrobot.event.EventBus;

/**
 * Created by Xhin on 2015/8/21.
 * XhinLiang@gmail.com
 */
public abstract class BaseFragment extends Fragment {

    protected Activity context;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        context = getActivity();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    protected void showMassage(String massage) {
        Snackbar.with(context.getApplicationContext()).text(massage)// context
                .show(context); // activity where it is displayed
    }

    @SuppressWarnings("unused")
    public void onEventMainThread(String event) {
        showMassage(event);
    }

}
