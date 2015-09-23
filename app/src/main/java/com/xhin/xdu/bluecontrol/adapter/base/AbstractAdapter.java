package com.xhin.xdu.bluecontrol.adapter.base;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Xhin on 2015/7/26.
 * XhinLiang@gmail.com
 */
public abstract class AbstractAdapter<T> extends BaseRecyclerViewAdapter<T> implements Filterable {
    public final List<T> originalList;
    public int upDownFactor = 1;
    public boolean isShowScaleAnimate = true;
    public Context context;

    public AbstractAdapter(List<T> list, Context context) {
        super(list, context);
        this.context = context;
        originalList = new ArrayList<>(list);
    }

    @Override
    abstract public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType);

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        super.onBindViewHolder(viewHolder, position);
        setView(list.get(position),viewHolder);
        animate(viewHolder,position);
    }

    abstract public void setView(T item,RecyclerView.ViewHolder viewHolder);

    @Override
    public Filter getFilter() {
        return new ItemFilter(this, originalList);
    }

    @Override
    protected Animator[] getAnimators(View view) {
        if (view.getMeasuredHeight() <= 0 || isShowScaleAnimate) {
            ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 1.1f, 1f);
            ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 1.1f, 1f);
            return new ObjectAnimator[]{scaleX, scaleY};
        }
        return new Animator[]{
                ObjectAnimator.ofFloat(view, "scaleX", 1.1f, 1f),
                ObjectAnimator.ofFloat(view, "scaleY", 1.1f, 1f),
                ObjectAnimator.ofFloat(view, "translationY", upDownFactor * 1.5f * view.getMeasuredHeight(), 0)
        };
    }

    @Override
    public void setList(List<T> list) {
        super.setList(list);
        this.originalList.clear();
        this.originalList.addAll(list);
        setUpFactor();
        isShowScaleAnimate = true;
    }

    public void setDownFactor() {
        upDownFactor = -1;
        isShowScaleAnimate = false;
    }

    public void setUpFactor() {
        upDownFactor = 1;
        isShowScaleAnimate = false;
    }

    public interface RecyclerItem {
        String getUniqueID();
    }

    private class ItemFilter extends Filter {

        private final AbstractAdapter adapter;

        private final List<T> originalList;

        private final List<T> filteredList;

        private ItemFilter(AbstractAdapter adapter, List<T> originalList) {
            super();
            this.adapter = adapter;
            this.originalList = new LinkedList<>(originalList);
            this.filteredList = new ArrayList<>();
        }


        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            filteredList.clear();
            final FilterResults results = new FilterResults();
            if (constraint.length() == 0) {
                filteredList.addAll(originalList);
            } else {
                for (T e : originalList) {
                    if (((RecyclerItem) e).getUniqueID().contains(constraint)) {
                        filteredList.add(e);
                    }
                }
            }
            results.values = filteredList;
            results.count = filteredList.size();
            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            adapter.list.clear();
            adapter.list.addAll((ArrayList<T>) results.values);
            adapter.notifyDataSetChanged();
        }
    }


}