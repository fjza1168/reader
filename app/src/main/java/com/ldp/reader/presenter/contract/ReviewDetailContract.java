package com.ldp.reader.presenter.contract;

import com.ldp.reader.model.bean.CommentBean;
import com.ldp.reader.model.bean.ReviewDetailBean;
import com.ldp.reader.ui.base.BaseContract;

import java.util.List;

/**
 * Created by ldp on 17-4-30.
 */

public interface ReviewDetailContract {

    interface View extends BaseContract.BaseView{
        //全部加载的时候显示
        void finishRefresh(ReviewDetailBean reviewDetail,
                           List<CommentBean> bestComments, List<CommentBean> comments);
        void finishLoad(List<CommentBean> comments);
        void showLoadError();
    }

    interface Presenter extends BaseContract.BasePresenter<View>{
        void refreshReviewDetail(String detailId,int start,int limit);
        void loadComment(String detailId,int start,int limit);
    }
}
