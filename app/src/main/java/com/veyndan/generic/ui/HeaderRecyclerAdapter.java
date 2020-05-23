package com.veyndan.generic.ui;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.veyndan.generic.LogUtils;

public abstract class HeaderRecyclerAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {
    @SuppressWarnings("unused")
    private static final String TAG = LogUtils.makeLogTag(HeaderRecyclerAdapter.class);

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    protected static final int HEADER_SIZE = 1;

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_HEADER:
                return onCreateHeaderItemViewHolder(parent);
            case TYPE_ITEM:
                return onCreateContentItemViewHolder(parent);
            default:
                throw new IllegalStateException();
        }
    }

    protected abstract VH onCreateHeaderItemViewHolder(ViewGroup parent);

    protected abstract VH onCreateContentItemViewHolder(ViewGroup parent);

    @Override
    public void onBindViewHolder(VH holder, int position) {
        if (position == 0) {
            onBindHeaderItemViewHolder(holder, position);
        } else {
            onBindContentItemViewHolder(holder, position - HEADER_SIZE);
        }
    }

    protected abstract void onBindHeaderItemViewHolder(VH holder, int position);

    protected abstract void onBindContentItemViewHolder(VH holder, int position);

    @Override
    public int getItemCount() {
        return getContentItemCount() + HEADER_SIZE;
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? TYPE_HEADER : TYPE_ITEM;
    }

    protected abstract int getContentItemCount();
}
