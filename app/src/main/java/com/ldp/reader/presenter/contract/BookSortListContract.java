package com.ldp.reader.presenter.contract;

import com.ldp.reader.model.bean.SortBookBean;
import com.ldp.reader.model.flag.BookSortListType;
import com.ldp.reader.ui.base.BaseContract;

import java.util.List;

/**
 * Created by ldp on 17-5-3.
 */

public interface BookSortListContract {
    interface View extends BaseContract.BaseView{
        void finishRefresh(List<SortBookBean> beans);
        void finishLoad(List<SortBookBean> beans);
        void showLoadError();
    }

    interface Presenter extends BaseContract.BasePresenter<View>{
        void refreshSortBook(String gender, BookSortListType type, String major, String minor, int start, int limit);
        void loadSortBook(String gender,BookSortListType type,String major,String minor,int start,int limit);
    }
}
