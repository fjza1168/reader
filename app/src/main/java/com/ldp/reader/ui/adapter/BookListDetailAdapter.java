package com.ldp.reader.ui.adapter;

import android.content.Context;

import com.ldp.reader.model.bean.BookListDetailBean;
import com.ldp.reader.ui.adapter.view.BookListInfoHolder;
import com.ldp.reader.ui.base.adapter.IViewHolder;
import com.ldp.reader.widget.adapter.WholeAdapter;

/**
 * Created by ldp on 17-5-2.
 */

public class BookListDetailAdapter extends WholeAdapter<BookListDetailBean.BooksBean.BookBean> {
    public BookListDetailAdapter(Context context, Options options) {
        super(context, options);
    }

    @Override
    protected IViewHolder<BookListDetailBean.BooksBean.BookBean> createViewHolder(int viewType) {
        return new BookListInfoHolder();
    }
}
