package com.ldp.reader.model.bean.packages;

import com.ldp.reader.model.bean.BaseBean;
import com.ldp.reader.model.bean.BookReviewBean;

import java.util.List;

/**
 * Created by ldp on 17-4-21.
 */

public class BookReviewPackage extends BaseBean {

    private List<BookReviewBean> reviews;

    public List<BookReviewBean> getReviews() {
        return reviews;
    }

    public void setReviews(List<BookReviewBean> reviews) {
        this.reviews = reviews;
    }
}
