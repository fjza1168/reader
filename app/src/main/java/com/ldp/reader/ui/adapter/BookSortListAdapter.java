package com.ldp.reader.ui.adapter;

import android.content.Context;

import com.ldp.reader.model.bean.SortBookBean;
import com.ldp.reader.ui.adapter.view.BookSortListHolder;
import com.ldp.reader.ui.base.adapter.IViewHolder;
import com.ldp.reader.widget.adapter.WholeAdapter;

/**
 * Created by ldp on 17-5-3.
 */

public class BookSortListAdapter extends WholeAdapter<SortBookBean> {
    public BookSortListAdapter(Context context, Options options) {
        super(context, options);
    }

    @Override
    protected IViewHolder<SortBookBean> createViewHolder(int viewType) {
        return new BookSortListHolder();
    }
}
