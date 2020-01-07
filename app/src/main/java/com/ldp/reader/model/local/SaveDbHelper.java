package com.ldp.reader.model.local;

import com.ldp.reader.model.bean.AuthorBean;
import com.ldp.reader.model.bean.DownloadTaskBean;
import com.ldp.reader.model.bean.packages.BillboardPackage;
import com.ldp.reader.model.bean.ReviewBookBean;
import com.ldp.reader.model.bean.BookCommentBean;
import com.ldp.reader.model.bean.BookHelpfulBean;
import com.ldp.reader.model.bean.BookHelpsBean;
import com.ldp.reader.model.bean.BookReviewBean;
import com.ldp.reader.model.bean.packages.BookSortPackage;

import java.util.List;

/**
 * Created by ldp on 17-4-28.
 */

public interface SaveDbHelper {
    void saveBookComments(List<BookCommentBean> beans);
    void saveBookHelps(List<BookHelpsBean> beans);
    void saveBookReviews(List<BookReviewBean> beans);
    void saveAuthors(List<AuthorBean> beans);
    void saveBooks(List<ReviewBookBean> beans);
    void saveBookHelpfuls(List<BookHelpfulBean> beans);

    void saveBookSortPackage(BookSortPackage bean);
    void saveBillboardPackage(BillboardPackage bean);
    /*************DownloadTask*********************/
    void saveDownloadTask(DownloadTaskBean bean);
}
