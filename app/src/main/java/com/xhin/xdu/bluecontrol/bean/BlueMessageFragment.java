package com.xhin.xdu.bluecontrol.bean;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.MaterialDialog;
import com.github.clans.fab.FloatingActionButton;
import com.malinskiy.superrecyclerview.SuperRecyclerView;
import com.xhin.xdu.bluecontrol.R;
import com.xhin.xdu.bluecontrol.adapter.BlueMessageAdapter;
import com.xhin.xdu.bluecontrol.adapter.base.AbstractAdapter;
import com.xhin.xdu.bluecontrol.fragment.base.BaseFragment;

import net.steamcrafted.loadtoast.LoadToast;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Xhin on 2015/8/21.
 * XhinLiang@gmail.com
 */
public class BlueMessageFragment extends BaseFragment {

    protected SuperRecyclerView recyclerView;
    protected FloatingActionButton fabS, fabX, fabK, fabG, fabCustom;
    protected AbstractAdapter<BluetoothMessage> recyclerAdapter;
    protected LoadToast loadToast;
    protected View fragmentView;

    private Event event;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.full_recycler_view_layout, container, false);
        event = (Event) getActivity();
        initRecyclerView();
        initEvent();
        return fragmentView;
    }

    private void initEvent() {
        fabS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                event.sendMessage("s");
            }
        });

        fabX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                event.sendMessage("x");
            }
        });

        fabK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                event.sendMessage("k");
            }
        });

        fabG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                event.sendMessage("g");
            }
        });

        fabCustom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialDialog.Builder(context).title("Send").input(R.string.custom_text, R.string.nothing, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                    }
                }).positiveText("Confirm").callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        if (dialog.getInputEditText() != null) {
                            String custom = dialog.getInputEditText().getText().toString();
                            event.sendMessage(custom);
                        }
                    }
                }).show();
            }
        });
    }

    protected AbstractAdapter<BluetoothMessage> initAdapter(List<BluetoothMessage> dataSet, Context context) {
        return new BlueMessageAdapter(dataSet, context);
    }

    private void initRecyclerView() {
        recyclerView = (SuperRecyclerView) fragmentView.findViewById(R.id.recyclerView);
        fabS = (FloatingActionButton) fragmentView.findViewById(R.id.fab_s);
        fabX = (FloatingActionButton) fragmentView.findViewById(R.id.fab_x);
        fabK = (FloatingActionButton) fragmentView.findViewById(R.id.fab_k);
        fabG = (FloatingActionButton) fragmentView.findViewById(R.id.fab_g);
        fabCustom = (FloatingActionButton) fragmentView.findViewById(R.id.fab_custom);
        loadToast = new LoadToast(context);
        List<BluetoothMessage> dataSet = new LinkedList<>();
        final LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerAdapter = initAdapter(dataSet, context);
        recyclerView.getRecyclerView().setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerAdapter.setFirstOnly(false);
        recyclerAdapter.setDuration(300);
        recyclerView.setAdapter(recyclerAdapter);
    }

    public void onNewMessage(String message) {
        recyclerAdapter.add(new BluetoothMessage(0, message));
    }

    public void setConnectStatus(String status) {
        event.setTitle(status);
    }

    public interface Event {
        void sendMessage(String custom);

        void setTitle(String title);
    }

}
