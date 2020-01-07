package com.ldp.reader.ui.base.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by ldp on 17-5-17.
 */

public class BaseViewHolder<T> extends RecyclerView.ViewHolder{
    public IViewHolder<T> holder;

    public BaseViewHolder(View itemView, IViewHolder<T> holder) {
        super(itemView);
        this.holder = holder;
        holder.initView();
    }
}
