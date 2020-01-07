package com.ldp.reader.ui.adapter;

import android.content.Context;

import com.ldp.reader.model.bean.BookReviewBean;
import com.ldp.reader.ui.adapter.view.DiscReviewHolder;
import com.ldp.reader.ui.base.adapter.IViewHolder;
import com.ldp.reader.widget.adapter.WholeAdapter;

/**
 * Created by ldp on 17-4-21.
 */

public class DiscReviewAdapter extends WholeAdapter<BookReviewBean> {

    public DiscReviewAdapter(Context context, Options options) {
        super(context, options);
    }

    @Override
    protected IViewHolder<BookReviewBean> createViewHolder(int viewType) {
        return new DiscReviewHolder();
    }
}
