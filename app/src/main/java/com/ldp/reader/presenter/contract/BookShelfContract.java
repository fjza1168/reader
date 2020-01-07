package com.ldp.reader.presenter.contract;

import com.ldp.reader.model.bean.CollBookBean;
import com.ldp.reader.ui.base.BaseContract;

import java.util.List;

/**
 * Created by ldp on 17-5-8.
 */

public interface BookShelfContract {

    interface View extends BaseContract.BaseView{
        void finishRefresh(List<CollBookBean> collBookBeans);
        void finishUpdate();
        void finishSyncBook();
        void showErrorTip(String error);
    }

    interface Presenter extends BaseContract.BasePresenter<View>{
        void refreshCollBooks();
        void createDownloadTask(CollBookBean collBookBean);
        void updateCollBooks(List<CollBookBean> collBookBeans);
        void loadRecommendBooks(String gender);
        void getBookShelf(String token);
        void getBookInfo(List<String> bookId);
        void setBookShelf(List<String> bookIds);
    }
}
