package com.ldp.reader.ui.adapter;

import com.ldp.reader.model.bean.CollBookBean;
import com.ldp.reader.ui.adapter.view.CollBookHolder;
import com.ldp.reader.ui.base.adapter.IViewHolder;
import com.ldp.reader.widget.adapter.WholeAdapter;

/**
 * Created by ldp on 17-5-8.
 */

public class CollBookAdapter extends WholeAdapter<CollBookBean> {

    @Override
    protected IViewHolder<CollBookBean> createViewHolder(int viewType) {
        return new CollBookHolder();
    }

}
