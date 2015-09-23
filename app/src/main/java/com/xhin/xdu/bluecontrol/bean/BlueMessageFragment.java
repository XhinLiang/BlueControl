package com.xhin.xdu.bluecontrol.bean;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
    protected FloatingActionButton fabS, fabX, fabK, fabG;
    protected AbstractAdapter<BluetoothMessage> recyclerAdapter;
    protected LoadToast loadToast;
    protected View fragmentView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.full_recycler_view_layout, container, false);
        initRecyclerView();
        initEvent();
        return fragmentView;
    }

    private void initEvent() {
        fabS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerAdapter.add(new BluetoothMessage(22,"wssdfsdf"));
            }
        });
        fabX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        fabK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        fabG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

}
