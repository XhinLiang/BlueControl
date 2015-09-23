package com.xhin.xdu.bluecontrol.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xhin.xdu.bluecontrol.R;
import com.xhin.xdu.bluecontrol.adapter.base.AbstractAdapter;
import com.xhin.xdu.bluecontrol.bean.BluetoothMessage;

import java.util.List;

/**
 * Created by Xhin on 2015/8/11.
 * XhinLiang@gmail.com
 */
public class BlueMessageAdapter extends AbstractAdapter<BluetoothMessage> {

    public BlueMessageAdapter(List<BluetoothMessage> list, Context context) {
        super(list, context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        final View view = LayoutInflater.from(context).inflate(R.layout.recycler_item_blue_message, parent, false);
        return new AnalystItemViewHolder(view);
    }

    @Override
    public void setView(BluetoothMessage item, RecyclerView.ViewHolder viewHolder) {
        AnalystItemViewHolder holder = (AnalystItemViewHolder) viewHolder;
        if (item == null)
            return;
        holder.tvMessage.setText(item.getMessage());
    }

    public class AnalystItemViewHolder extends RecyclerView.ViewHolder {

        public TextView tvMessage;

        public AnalystItemViewHolder(View parent) {
            super(parent);
            tvMessage = (TextView) parent.findViewById(R.id.tv_message);
        }
    }
}
