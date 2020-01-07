package com.ldp.reader.ui.adapter;

import android.content.Context;

import com.ldp.reader.model.bean.BookListBean;
import com.ldp.reader.ui.adapter.view.BookListHolder;
import com.ldp.reader.ui.base.adapter.IViewHolder;
import com.ldp.reader.widget.adapter.WholeAdapter;

/**
 * Created by ldp on 17-5-1.
 */

public class BookListAdapter extends WholeAdapter<BookListBean> {
    public BookListAdapter() {
    }

    public BookListAdapter(Context context, Options options) {
        super(context, options);
    }

    @Override
    protected IViewHolder<BookListBean> createViewHolder(int viewType) {
        return new BookListHolder();
    }
}
