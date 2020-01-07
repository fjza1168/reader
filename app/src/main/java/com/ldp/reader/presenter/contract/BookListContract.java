package com.ldp.reader.presenter.contract;

import com.ldp.reader.model.bean.BookListBean;
import com.ldp.reader.model.flag.BookListType;
import com.ldp.reader.ui.base.BaseContract;

import java.util.List;

/**
 * Created by ldp on 17-5-1.
 */

public interface BookListContract {
    interface View extends BaseContract.BaseView{
        void finishRefresh(List<BookListBean> beans);
        void finishLoading(List<BookListBean> beans);
        void showLoadError();
    }

    interface Presenter extends BaseContract.BasePresenter<View>{
        void refreshBookList(BookListType type, String tag,int start, int limited);
        void loadBookList(BookListType type, String tag,int start, int limited);
    }
}
