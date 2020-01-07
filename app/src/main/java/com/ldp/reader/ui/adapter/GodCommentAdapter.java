package com.ldp.reader.ui.adapter;

import com.ldp.reader.model.bean.CommentBean;
import com.ldp.reader.ui.adapter.view.CommentHolder;
import com.ldp.reader.ui.base.adapter.BaseListAdapter;
import com.ldp.reader.ui.base.adapter.IViewHolder;

/**
 * Created by ldp on 17-4-29.
 */

public class GodCommentAdapter extends BaseListAdapter<CommentBean>{
    @Override
    protected IViewHolder<CommentBean> createViewHolder(int viewType) {
        return new CommentHolder(true);
    }
}
