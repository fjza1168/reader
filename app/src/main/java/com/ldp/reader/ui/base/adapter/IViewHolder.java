package com.ldp.reader.ui.base.adapter;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created by ldp on 17-5-17.
 */

public interface IViewHolder<T> {
    View createItemView(ViewGroup parent);
    void initView();
    void onBind(T data,int pos);
    void onClick();
}
