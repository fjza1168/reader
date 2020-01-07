package com.ldp.reader.model.local;

import com.ldp.reader.model.bean.AuthorBean;
import com.ldp.reader.model.bean.ReviewBookBean;
import com.ldp.reader.model.bean.BookCommentBean;
import com.ldp.reader.model.bean.BookHelpfulBean;
import com.ldp.reader.model.bean.BookHelpsBean;
import com.ldp.reader.model.bean.BookReviewBean;

import java.util.List;

/**
 * Created by ldp on 17-4-28.
 */

public interface DeleteDbHelper {
    void deleteBookComments(List<BookCommentBean> beans);
    void deleteBookReviews(List<BookReviewBean> beans);
    void deleteBookHelps(List<BookHelpsBean> beans);
    void deleteAuthors(List<AuthorBean> beans);
    void deleteBooks(List<ReviewBookBean> beans);
    void deleteBookHelpful(List<BookHelpfulBean> beans);
    void deleteAll();
}
