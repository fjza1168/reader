package com.ldp.reader.ui.activity;

import androidx.appcompat.widget.Toolbar;

import com.ldp.reader.R;
import com.ldp.reader.ui.base.BaseActivity;

/**
 * Created by ldp on 17-5-26.
 */

public class CommunityActivity extends BaseActivity{

    @Override
    protected int getContentId() {
        return R.layout.activity_community;
    }

    @Override
    protected void setUpToolbar(Toolbar toolbar) {
        super.setUpToolbar(toolbar);
        getSupportActionBar().setTitle("社区");
    }
}
