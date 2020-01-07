package com.ldp.reader.presenter.contract;

import com.ldp.reader.model.bean.BookReviewBean;
import com.ldp.reader.model.flag.BookDistillate;
import com.ldp.reader.model.flag.BookSort;
import com.ldp.reader.model.flag.BookType;
import com.ldp.reader.ui.base.BaseContract;

import java.util.List;

/**
 * Created by ldp on 17-4-21.
 */

public interface DiscReviewContract {
    interface View extends BaseContract.BaseView {
        void finishRefresh(List<BookReviewBean> beans);
        void finishLoading(List<BookReviewBean> beans);
        void showErrorTip();
    }

    interface Presenter extends BaseContract.BasePresenter<View> {
        void firstLoading(BookSort sort, BookType bookType, int start, int limited, BookDistillate distillate);
        void refreshBookReview(BookSort sort, BookType bookType, int start, int limited, BookDistillate distillate);
        void loadingBookReview(BookSort sort, BookType bookType, int start, int limited, BookDistillate distillate);
        void saveBookReview(List<BookReviewBean> beans);
    }
}
