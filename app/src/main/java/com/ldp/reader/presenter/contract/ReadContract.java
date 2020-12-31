package com.ldp.reader.presenter.contract;

import com.ldp.reader.model.bean.BookChapterBean;
import com.ldp.reader.ui.base.BaseContract;
import com.ldp.reader.widget.page.TxtChapter;

import java.util.List;

import hugo.weaving.DebugLog;

/**
 * Created by ldp on 17-5-16.
 */

public interface ReadContract extends BaseContract{
    interface View extends BaseContract.BaseView {
        void showCategory(List<BookChapterBean> bookChapterList,String bookId ,boolean isBiqugeLoaded);
        void finishChapter(boolean isRefresh);

        void errorChapter();
    }

    interface Presenter extends BaseContract.BasePresenter<View>{
        void loadCategory(String bookId);
        void loadChapter(String bookId,List<TxtChapter> bookChapterList);
        void refreshChapter(String bookId, TxtChapter bookChapter , int sourceIndex);


        void loadCategoryInBiquge(String bookId);

    }
}
