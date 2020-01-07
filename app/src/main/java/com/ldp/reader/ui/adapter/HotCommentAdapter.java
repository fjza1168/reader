package com.ldp.reader.ui.adapter;

import com.ldp.reader.model.bean.HotCommentBean;
import com.ldp.reader.ui.adapter.view.HotCommentHolder;
import com.ldp.reader.ui.base.adapter.BaseListAdapter;
import com.ldp.reader.ui.base.adapter.IViewHolder;

/**
 * Created by ldp on 17-5-4.
 */

public class HotCommentAdapter extends BaseListAdapter<HotCommentBean>{
    @Override
    protected IViewHolder<HotCommentBean> createViewHolder(int viewType) {
        return new HotCommentHolder();
    }
}
