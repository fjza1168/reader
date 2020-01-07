package com.ldp.reader.presenter;

import com.ldp.reader.model.bean.CommentBean;
import com.ldp.reader.model.bean.CommentDetailBean;
import com.ldp.reader.model.remote.RemoteRepository;
import com.ldp.reader.presenter.contract.CommentDetailContract;
import com.ldp.reader.ui.base.RxPresenter;
import com.ldp.reader.utils.LogUtils;
import com.ldp.reader.utils.RxUtils;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ldp on 17-4-29.
 */

public class CommentDetailPresenter extends RxPresenter<CommentDetailContract.View>
        implements CommentDetailContract.Presenter {

    @Override
    public void refreshCommentDetail(String detailId, int start, int limit) {
        Single<CommentDetailBean> detailSingle = RemoteRepository
                .getInstance().getCommentDetail(detailId);

        Single<List<CommentBean>> bestCommentsSingle = RemoteRepository
                .getInstance().getBestComments(detailId);

        Single<List<CommentBean>> commentsSingle = RemoteRepository
                .getInstance().getDetailComments(detailId, start, limit);

        Disposable detailDispo = RxUtils.toCommentDetail(detailSingle, bestCommentsSingle, commentsSingle)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        (bean) -> {
                            mView.finishRefresh(bean.getDetail(),
                                    bean.getBestComments(), bean.getComments());
                            mView.complete();
                        },
                        (e) -> {
                            mView.showError();
                            LogUtils.e(e);
                        }
                );
        addDisposable(detailDispo);
    }

    @Override
    public void loadComment(String detailId, int start, int limit) {
        Disposable loadDispo = RemoteRepository.getInstance()
                .getDetailComments(detailId, start, limit)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        (bean) -> {
                            mView.finishLoad(bean);
                        },
                        (e) -> {
                            mView.showLoadError();
                            LogUtils.e(e);
                        }
                );
        addDisposable(loadDispo);
    }
}
