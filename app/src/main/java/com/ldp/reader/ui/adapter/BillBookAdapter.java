package com.ldp.reader.ui.adapter;

import com.ldp.reader.model.bean.BillBookBean;
import com.ldp.reader.ui.adapter.view.BillBookHolder;
import com.ldp.reader.ui.base.adapter.BaseListAdapter;
import com.ldp.reader.ui.base.adapter.IViewHolder;

/**
 * Created by ldp on 17-5-3.
 */

public class BillBookAdapter extends BaseListAdapter<BillBookBean> {
    @Override
    protected IViewHolder<BillBookBean> createViewHolder(int viewType) {
        return new BillBookHolder();
    }
}
