package com.ldp.reader.ui.adapter;

import android.content.Context;

import com.ldp.reader.model.bean.BookHelpsBean;
import com.ldp.reader.ui.adapter.view.DiscHelpsHolder;
import com.ldp.reader.ui.base.adapter.IViewHolder;
import com.ldp.reader.widget.adapter.WholeAdapter;

/**
 * Created by ldp on 17-4-21.
 */

public class DiscHelpsAdapter extends WholeAdapter<BookHelpsBean> {

    public DiscHelpsAdapter(Context context, Options options) {
        super(context, options);
    }

    @Override
    protected IViewHolder<BookHelpsBean> createViewHolder(int viewType) {
        return new DiscHelpsHolder();
    }
}
