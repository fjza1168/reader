package com.ldp.reader.ui.adapter;

import com.ldp.reader.model.bean.BookSortBean;
import com.ldp.reader.ui.adapter.view.BookSortHolder;
import com.ldp.reader.ui.base.adapter.BaseListAdapter;
import com.ldp.reader.ui.base.adapter.IViewHolder;

/**
 * Created by ldp on 17-4-23.
 */

public class BookSortAdapter extends BaseListAdapter<BookSortBean>{

    @Override
    protected IViewHolder<BookSortBean> createViewHolder(int viewType) {
        return new BookSortHolder();
    }
}
