package com.ldp.reader.presenter;

import android.util.Log;

import com.ldp.reader.model.bean.BookSearchResult;
import com.ldp.reader.model.remote.RemoteRepository;
import com.ldp.reader.presenter.contract.SearchContract;
import com.ldp.reader.ui.base.RxPresenter;
import com.ldp.reader.utils.LogUtils;
import com.ldp.reader.utils.RxUtils;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ldp on 17-6-2.
 */

public class SearchPresenter extends RxPresenter<SearchContract.View>
        implements SearchContract.Presenter {
    private static final String TAG  = SearchPresenter.class.getSimpleName();
    @Override
    public void searchHotWord() {
        Disposable disp = RemoteRepository.getInstance()
                .getHotWords()
                .compose(RxUtils::toSimpleSingle)
                .subscribe(
                        bean -> {
                            mView.finishHotWords(bean);
                            Log.d("+bean", bean.toString());
                            LogUtils.e(bean);

                        },
                        e -> {
                            LogUtils.e(e);
                        }
                );
        addDisposable(disp);
    }

    @Override
    public void searchKeyWord(String query) {
        Disposable disp = RemoteRepository.getInstance()
                .getKeyWords(query)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Consumer<List<String>>() {
                            @Override
                            public void accept(List<String> bean) throws Exception {
                                Log.d("+bean", bean.toString());

                                mView.finishKeyWords(bean);
                                LogUtils.d("+bean", bean);

                            }
                        },
                        e -> {
                            LogUtils.e(e);
                        }
                );
        addDisposable(disp);
    }


//    @Override
//    public void searchBook(String query) {
//        Disposable disp = RemoteRepository.getInstance()
//                .getSearchBooks(query)
//                .compose(RxUtils::toSimpleSingle)
//                .subscribe(
//                        (List<SearchBookPackage.BooksBean> bean) -> {
//                            Log.d("+bean", bean.toString());
//                            mView.finishBooks(bean);
//                            LogUtils.d("+bean", bean);
//
//                        },
//                        e -> {
//                            LogUtils.e(e);
//                            mView.errorBooks();
//                        }
//                );
//        addDisposable(disp);
//    }


    @Override
    public void searchBook(String query) {
        Log.d(TAG, "searchBook: " + query);
//        Disposable disp = RemoteRepository.getInstance()
//                .getSearchBooksByBiqugeSearch(query)
//                .compose(RxUtils::toSimpleSingle)
//                .subscribe(new Consumer<List<SearchBookPackageByBiquge.DataBean>>() {
//                    @Override
//                    public void accept(List<SearchBookPackageByBiquge.DataBean> dataBeans) throws Exception {
//                        Log.d("+bean", dataBeans.toString());
//                            mView.finishBooks(dataBeans);
//                            LogUtils.d("+bean", dataBeans);
//                    }
//                }, throwable -> {
//                    LogUtils.e(throwable);
//                        mView.errorBooks();
//                });

        Disposable disp = RemoteRepository.getInstance()
                .getSearchResult(query)
                .compose(RxUtils::toSimpleSingle)
                .subscribe(new Consumer<List<BookSearchResult>>() {
                    @Override
                    public void accept(List<BookSearchResult> bookSearchResults) throws Exception {
                        mView.finishBooks(bookSearchResults);
                    }
                }, throwable -> {
                    LogUtils.e(throwable);
                    mView.errorBooks();
                });
        addDisposable(disp);
    }

}
