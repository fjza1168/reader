package com.ldp.reader.presenter.contract;

import com.ldp.reader.model.bean.BookDetailBeanInOwn;
import com.ldp.reader.model.bean.BookListBean;
import com.ldp.reader.model.bean.CollBookBean;
import com.ldp.reader.model.bean.HotCommentBean;
import com.ldp.reader.ui.base.BaseContract;

import java.util.List;

/**
 * Created by ldp on 17-5-4.
 */

public interface BookDetailContract {
    interface View extends BaseContract.BaseView{
        /**
         * @param bean
         */
//        void finishRefresh(BookDetailBean bean);
      void finishRefresh(BookDetailBeanInOwn bean);

        void finishHotComment(List<HotCommentBean> beans);
        void finishRecommendBookList(List<BookListBean> beans);

        void waitToBookShelf();
        void errorToBookShelf();
        void succeedToBookShelf();
    }

    interface Presenter extends BaseContract.BasePresenter<View>{
        void refreshBookDetail(String bookId);
        //添加到书架上
        void addToBookShelf(CollBookBean collBook);
    }
}
