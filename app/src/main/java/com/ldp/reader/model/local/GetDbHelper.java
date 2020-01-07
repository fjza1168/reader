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

import io.reactivex.Single;

/**
 * Created by ldp on 17-4-28.
 */

public interface GetDbHelper {
    Single<List<BookCommentBean>> getBookComments(String block, String sort, int start, int limited, String distillate);
    Single<List<BookHelpsBean>> getBookHelps(String sort, int start, int limited, String distillate);
    Single<List<BookReviewBean>> getBookReviews(String sort, String bookType, int start, int limited, String distillate);
    BookSortPackage getBookSortPackage();
    BillboardPackage getBillboardPackage();

    AuthorBean getAuthor(String id);
    ReviewBookBean getReviewBook(String id);
    BookHelpfulBean getBookHelpful(String id);

    /******************************/
    List<DownloadTaskBean> getDownloadTaskList();
}
