package com.ldp.reader.ui.adapter;

import android.content.Context;

import com.ldp.reader.model.bean.CommentBean;
import com.ldp.reader.ui.adapter.view.CommentHolder;
import com.ldp.reader.ui.base.adapter.IViewHolder;
import com.ldp.reader.widget.adapter.WholeAdapter;

/**
 * Created by ldp on 17-4-29.
 */

public class CommentAdapter extends WholeAdapter<CommentBean> {

    public CommentAdapter(Context context, Options options) {
        super(context, options);
    }

    @Override
    protected IViewHolder<CommentBean> createViewHolder(int viewType) {
        return new CommentHolder(false);
    }
}
