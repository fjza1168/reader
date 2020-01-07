package com.ldp.reader.ui.adapter;

import com.ldp.reader.model.bean.BookSearchResult;
import com.ldp.reader.ui.adapter.view.SearchBookHolder;
import com.ldp.reader.ui.base.adapter.BaseListAdapter;
import com.ldp.reader.ui.base.adapter.IViewHolder;

/**
 * Created by ldp on 17-6-2.
 */

public class SearchBookAdapter extends BaseListAdapter<BookSearchResult>{
    @Override
    protected IViewHolder<BookSearchResult> createViewHolder(int viewType) {
        return new SearchBookHolder();
    }
}
