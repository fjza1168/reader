package com.ldp.reader.presenter.contract;

import com.ldp.reader.model.bean.packages.BookSortPackage;
import com.ldp.reader.model.bean.packages.BookSubSortPackage;
import com.ldp.reader.ui.base.BaseContract;

/**
 * Created by ldp on 17-4-23.
 */

public interface BookSortContract {

    interface View extends BaseContract.BaseView{
        void finishRefresh(BookSortPackage sortPackage, BookSubSortPackage subSortPackage);
    }

    interface Presenter extends BaseContract.BasePresenter<View>{
        void refreshSortBean();
    }
}
