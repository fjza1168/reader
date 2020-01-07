package com.ldp.reader.ui.adapter;

import com.ldp.reader.model.bean.DownloadTaskBean;
import com.ldp.reader.ui.adapter.view.DownloadHolder;
import com.ldp.reader.ui.base.adapter.BaseListAdapter;
import com.ldp.reader.ui.base.adapter.IViewHolder;

/**
 * Created by ldp on 17-5-12.
 */

public class DownLoadAdapter extends BaseListAdapter<DownloadTaskBean> {

    @Override
    protected IViewHolder<DownloadTaskBean> createViewHolder(int viewType) {
        return new DownloadHolder();
    }
}
