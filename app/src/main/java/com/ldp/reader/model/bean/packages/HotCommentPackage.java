package com.ldp.reader.model.bean.packages;

import com.ldp.reader.model.bean.BaseBean;
import com.ldp.reader.model.bean.HotCommentBean;

import java.util.List;

/**
 * Created by ldp on 17-5-4.
 */

public class HotCommentPackage extends BaseBean {

    private List<HotCommentBean> reviews;

    public List<HotCommentBean> getReviews() {
        return reviews;
    }

    public void setReviews(List<HotCommentBean> reviews) {
        this.reviews = reviews;
    }
}
