package com.ldp.reader.presenter.contract;

import com.ldp.reader.model.bean.BookListDetailBean;
import com.ldp.reader.ui.base.BaseContract;

/**
 * Created by ldp on 17-5-1.
 */

public interface BookListDetailContract {

    interface View extends BaseContract.BaseView{
        void finishRefresh(BookListDetailBean bean);
    }

    interface Presenter extends BaseContract.BasePresenter<View>{
        void refreshBookListDetail(String detailId);
    }
}
