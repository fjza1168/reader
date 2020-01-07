package com.ldp.reader.presenter.contract;

import com.ldp.reader.model.bean.BookHelpsBean;
import com.ldp.reader.model.flag.BookDistillate;
import com.ldp.reader.model.flag.BookSort;
import com.ldp.reader.ui.base.BaseContract;

import java.util.List;

/**
 * Created by ldp on 17-4-21.
 */

public interface DiscHelpsContract {

    interface View extends BaseContract.BaseView{
        void finishRefresh(List<BookHelpsBean> beans);
        void finishLoading(List<BookHelpsBean> beans);
        void showErrorTip();
    }

    interface Presenter extends BaseContract.BasePresenter<View>{
        void firstLoading(BookSort sort, int start, int limited, BookDistillate distillate);
        void refreshBookHelps(BookSort sort, int start, int limited, BookDistillate distillate);
        void loadingBookHelps(BookSort sort, int start,int limited,BookDistillate distillate);
        void saveBookHelps(List<BookHelpsBean> beans);
    }
}
