package com.ldp.reader.presenter.contract;

import com.ldp.reader.model.bean.BookCommentBean;
import com.ldp.reader.model.flag.BookDistillate;
import com.ldp.reader.model.flag.BookSort;
import com.ldp.reader.model.flag.CommunityType;
import com.ldp.reader.ui.base.BaseContract;

import java.util.List;

/**
 * Created by ldp on 17-4-20.
 */

public interface DiscCommentContact {

    interface View extends BaseContract.BaseView{
        void finishRefresh(List<BookCommentBean> beans);
        void finishLoading(List<BookCommentBean> beans);
        void showErrorTip();
    }

    interface Presenter extends BaseContract.BasePresenter<View>{
        void firstLoading(CommunityType block, BookSort sort, int start, int limited, BookDistillate distillate);
        void refreshComment(CommunityType block, BookSort sort, int start, int limited, BookDistillate distillate);
        void loadingComment(CommunityType block, BookSort sort, int start, int limited, BookDistillate distillate);
        void saveComment(List<BookCommentBean> beans);
    }
}
