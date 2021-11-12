package com.ldp.reader.presenter;

import android.os.Vibrator;
import android.util.Log;

import com.ldp.reader.App;
import com.ldp.reader.RxBus;
import com.ldp.reader.model.bean.BookChapterBean;
import com.ldp.reader.model.bean.BookDetailBeanInBiquge;
import com.ldp.reader.model.bean.BookDetailBeanInOwn;
import com.ldp.reader.model.bean.BookIdBean;
import com.ldp.reader.model.bean.ChapterBean;
import com.ldp.reader.model.bean.CollBookBean;
import com.ldp.reader.model.bean.DirectLoginResultBean;
import com.ldp.reader.model.bean.DirectSycBookShelfBean;
import com.ldp.reader.model.bean.DownloadTaskBean;
import com.ldp.reader.model.bean.SyncBookShelfBean;
import com.ldp.reader.model.bean.packages.BookChapterPackageByBiquge;
import com.ldp.reader.model.local.BookRepository;
import com.ldp.reader.model.remote.RemoteRepository;
import com.ldp.reader.presenter.contract.BookShelfContract;
import com.ldp.reader.ui.base.RxPresenter;
import com.ldp.reader.utils.Constant;
import com.ldp.reader.utils.LogUtils;
import com.ldp.reader.utils.MD5Utils;
import com.ldp.reader.utils.RxUtils;
import com.ldp.reader.utils.SharedPreUtils;
import com.ldp.reader.utils.StringUtils;
import com.google.gson.Gson;
import com.mob.pushsdk.MobPush;
import com.mob.pushsdk.MobPushCallback;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by ldp on 17-5-8.
 */

public class BookShelfPresenter extends RxPresenter<BookShelfContract.View>
        implements BookShelfContract.Presenter {
    private static final String TAG = "BookShelfPresenter";

    @Override
    public void refreshCollBooks() {
        List<CollBookBean> collBooks = BookRepository
                .getInstance().getCollBooks();
        for (CollBookBean bookBean : collBooks) {
            Log.d("+书名", bookBean.getTitle());
        }
        mView.finishRefresh(collBooks);
    }


    @Override
    public void createDownloadTask(CollBookBean collBookBean) {
        DownloadTaskBean task = new DownloadTaskBean();
        task.setTaskName(collBookBean.getTitle());
        task.setBookId(collBookBean.get_id());
        task.setBookChapters(collBookBean.getBookChapters());
        task.setLastChapter(collBookBean.getBookChapters().size());

        RxBus.getInstance().post(task);
    }


    @Override
    public void loadRecommendBooks(String gender) {
        Disposable disposable = RemoteRepository.getInstance()
                .getRecommendBooks(gender)
                .doOnSuccess(new Consumer<List<CollBookBean>>() {
                    @Override
                    public void accept(List<CollBookBean> collBooks) throws Exception {
//                        //更新目录
//                        updateCategory(collBooks);
//                        //异步存储到数据库中
//                        BookRepository.getInstance()
//                                .saveCollBooksWithAsync(collBooks);
                    }
                })
                .compose(RxUtils::toSimpleSingle)
                .subscribe(
                        beans -> {
                            mView.finishRefresh(beans);
                            mView.complete();
                        },
                        e -> {
                            //提示没有网络
                            LogUtils.e(e);
                            mView.showErrorTip(e.toString());
                            mView.complete();
                        }
                );
        addDisposable(disposable);
    }


    @Override
    public void getBookShelf(String token) {
        Disposable disposable = RemoteRepository.getInstance()
                .getBookShelf(token)
                .compose(RxUtils::toSimpleSingle)
                .subscribe(
                        bookIdBeans -> {
                            List<String> bookIdList = new ArrayList<>();
                            for (BookIdBean bookIdBean : bookIdBeans) {
                                bookIdList.add(bookIdBean.getBookId() + "");
                            }
                            getBookInfo(bookIdList);
                        },
                        e -> {
                            //提示没有网络
                            LogUtils.e(e);
                            mView.showErrorTip(e.toString());
                            mView.complete();
                        }
                );
        addDisposable(disposable);
    }

    @Override
    public void getBookShelfByMobile(String mobile, String mobileToken) {
        Disposable disposable = RemoteRepository.getInstance()
                .getBookShelfByMobile(mobile, mobileToken)
                .compose(RxUtils::toSimpleSingle)
                .subscribe(
                        bookIdBeans -> {
                            List<String> bookIdList = new ArrayList<>();
                            for (BookIdBean bookIdBean : bookIdBeans) {
                                bookIdList.add(bookIdBean.getBookId() + "");
                            }
                            getBookInfo(bookIdList);
                        },
                        e -> {
                            //提示没有网络
                            LogUtils.e(e);
                            mView.showErrorTip(e.toString());
                            mView.complete();
                        }
                );
        addDisposable(disposable);
    }

    public void getBookInfo(List<String> bookIdList) {
        List<Single<BookDetailBeanInOwn>> bookDetailSingleList = new ArrayList<>();
        for (String bookId : bookIdList) {
            CollBookBean mCollBookBean = BookRepository.getInstance().getCollBook(bookId);
            if (null == mCollBookBean) {
                bookDetailSingleList.add(RemoteRepository.getInstance().getBookInfo(bookId));
            }
        }
        Disposable disposable = Single.concat(bookDetailSingleList)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bookDetailBeanInOwn -> addToBookShelf(bookDetailBeanInOwn.getCollBookBean()), throwable -> mView.showErrorTip(throwable.getMessage()), () -> {
                    List<CollBookBean> collBooks = BookRepository.getInstance().getCollBooks();
                    List<String> bookIds = new ArrayList<>();
                    for (CollBookBean collBookBean : collBooks) {
                        bookIds.add(collBookBean.get_id());
                    }
                    if ("password".equals(SharedPreUtils.getInstance().getString("loginType"))) {
                        setBookShelf(bookIds);
                    } else {
                        String mobile = SharedPreUtils.getInstance().getString("userName");
                        String mobileToken = SharedPreUtils.getInstance().getString("token");
                        setBookShelfByMobile(bookIds, mobile, mobileToken);
                    }
                });
        addDisposable(disposable);
    }


    private void addToBookShelf(CollBookBean collBookBean) {
        List<BookChapterBean> bookChapterBeans = new ArrayList<>();
        BookRepository.getInstance()
                .saveCollBookWithAsync(collBookBean);
        Disposable disposable = RemoteRepository.getInstance()
                .getBookFolder(collBookBean.get_id())
                .compose(RxUtils::toSimpleSingle)
                .subscribe(chapterBeans -> {
                    for (ChapterBean chapterBean : chapterBeans) {
                        BookChapterBean bookChapterBeanTemp = new BookChapterBean();
                        bookChapterBeanTemp.setLink(chapterBean.getChapterId() + "");
                        bookChapterBeanTemp.setTitle(chapterBean.getTitle());
                        bookChapterBeanTemp.setValidInZhuishu(false);
                        bookChapterBeanTemp.setId(MD5Utils.strToMd5By16(bookChapterBeanTemp.getLink()));
                        bookChapterBeanTemp.setBookId(collBookBean.get_id());
                        bookChapterBeans.add(bookChapterBeanTemp);
                    }
                    collBookBean.setBookChapters(bookChapterBeans);
                    //存储收藏
                    BookRepository.getInstance().saveCollBookWithAsync(collBookBean);
                    CollBookBean collBookBeanResult = BookRepository.getInstance().getCollBook(collBookBean.get_id());
                    Log.d(TAG, "addToBookShelf:collBookBeanResult " + collBookBeanResult);

                }, throwable -> mView.showErrorTip(throwable.getMessage()));


        addDisposable(disposable);
    }


    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @Override
    public void setBookShelf(List<String> bookIds) {
        RequestBody body = RequestBody.create(JSON, new Gson().toJson(bookIds));
        String token = SharedPreUtils.getInstance().getString("token");
        Disposable disposable = RemoteRepository.getInstance().setBookShelf(token, body)
                .compose(RxUtils::toSimpleSingle)
                .subscribe(syncBookShelfBean -> {
                    mView.finishSyncBook();
                    mView.finishUpdate();
                }, throwable -> mView.showErrorTip(throwable.getMessage()));
        addDisposable(disposable);
    }

    @Override
    public void setBookShelfByMobile(List<String> bookIds, String mobile, String mobileToken) {
        DirectSycBookShelfBean directSycBookShelfBean = new DirectSycBookShelfBean();
        directSycBookShelfBean.setBookIds(bookIds);
        directSycBookShelfBean.setMobile(mobile);
        directSycBookShelfBean.setMobileToken(mobileToken);
        RequestBody body = RequestBody.create(JSON, new Gson().toJson(directSycBookShelfBean));
        Disposable disposable = RemoteRepository.getInstance().setBookShelfByMobile(body)
                .compose(RxUtils::toSimpleSingle)
                .subscribe(syncBookShelfBean -> {
                    mView.finishSyncBook();
                    mView.finishUpdate();
                }, throwable -> mView.showErrorTip(throwable.getMessage()));
        addDisposable(disposable);
    }


//    //需要修改
//    @Override
//    public void updateCollBooks(List<CollBookBean> collBookBeans) {
//        Log.d(TAG, "+updateCollBooks ");
//        if (collBookBeans == null || collBookBeans.isEmpty()) {
//            return;
//        }
//        List<CollBookBean> collBooks = new ArrayList<>(collBookBeans);
//        List<Single<BookDetailBeanInOwn>> observables = new ArrayList<>(collBooks.size());
//        Iterator<CollBookBean> it = collBooks.iterator();
//        while (it.hasNext()) {
//            CollBookBean collBook = it.next();
//            //删除本地文件
//            if (collBook.isLocal()) {
//                it.remove();
//            } else {
//                observables.add(RemoteRepository.getInstance().getBookInfo(collBook.get_id()));
//                Log.d(TAG, "+笔趣阁ID " + collBook.getBookIdInBiquge());
//            }
//        }
//
//        List<CollBookBean> newCollBooksMerge = new ArrayList<>(observables.size());
//        Disposable chaptersDispoable = Single.concat(observables)
//                .observeOn(Schedulers.io())
//                .subscribeOn(Schedulers.io())
//                .subscribe(bookDetailBeanInOwn -> {
//                    CollBookBean oldCollBook = BookRepository.getInstance().getCollBook(String.valueOf(bookDetailBeanInOwn.getBookId()));
//                    if (!oldCollBook.getLastChapter().equals(bookDetailBeanInOwn.getLastChapter())) {
//                        oldCollBook.setLastChapter(bookDetailBeanInOwn.getLastChapter());
//                        Log.d(TAG, "+更新书籍 " + oldCollBook.getTitle() + oldCollBook.getLastChapter());
//                        oldCollBook.setUpdate(true);
//                        updateCategory(oldCollBook);
//                        Vibrator vibrator = (Vibrator) App.getContext().getSystemService(App.getContext().VIBRATOR_SERVICE);
//                        vibrator.vibrate(400);
//                    } else {
//                        oldCollBook.setUpdate(false);
//                    }
//                    oldCollBook.setUpdated(StringUtils.dateConvert(StringUtils.dateConvert(bookDetailBeanInOwn.getUpdateTime(), Constant.FORMAT_BOOK_DATE), Constant.FORMAT_BOOK_DATE));
//                    newCollBooksMerge.add(oldCollBook);
//                    Log.d(TAG, "+检查更新");
//                }, throwable -> {
//
//                }, () -> {
////                                异步存储到数据库中
//                    BookRepository.getInstance().saveCollBooks(newCollBooksMerge);
//                    mView.complete();
//                    mView.finishUpdate();
//
//                });
//        addDisposable(chaptersDispoable);
//    }


    @Override
    public void updateCollBooks(List<CollBookBean> collBookBeans) {
        if (collBookBeans == null || collBookBeans.isEmpty()) {
            return;
        }
        List<Long> bookIdList = new ArrayList<>();
        for (CollBookBean collBook : collBookBeans) {
            //删除本地文件
            if (!collBook.isLocal()) {
                bookIdList.add(Long.valueOf(collBook.get_id()));
            }
        }
        List<CollBookBean> newCollBooksMerge = new ArrayList<>();
        RequestBody body = RequestBody.create(JSON, new Gson().toJson(bookIdList));
        Disposable disposable = RemoteRepository.getInstance().getBookInfoBatch(body)
                .compose(RxUtils::toSimpleSingle)
                .subscribe(bookDetailBeanInOwns -> {
                    for (BookDetailBeanInOwn bookDetailBeanInOwn : bookDetailBeanInOwns) {
                        CollBookBean oldCollBook = BookRepository.getInstance().getCollBook(String.valueOf(bookDetailBeanInOwn.getBookId()));
                        if (!oldCollBook.getLastChapter().equals(bookDetailBeanInOwn.getLastChapter())) {
                            updateBookInfo(bookDetailBeanInOwn, oldCollBook);
                            newCollBooksMerge.add(oldCollBook);
                        } else {
                            oldCollBook.setUpdate(false);
                        }
                        Log.d(TAG, "+检查更新");
                    }
                    BookRepository.getInstance().saveCollBooks(newCollBooksMerge);
                    mView.complete();
                    mView.finishUpdate();
                }, Throwable::printStackTrace);
        addDisposable(disposable);
    }

    private void updateBookInfo(BookDetailBeanInOwn bookDetailBeanInOwn, CollBookBean oldCollBook) {
        oldCollBook.setLastChapter(bookDetailBeanInOwn.getLastChapter());
        Log.d(TAG, "+更新书籍 " + oldCollBook.getTitle() + oldCollBook.getLastChapter());
        oldCollBook.setUpdate(true);
        updateCategory(oldCollBook);
        Vibrator vibrator = (Vibrator) App.getContext().getSystemService(App.getContext().VIBRATOR_SERVICE);
        vibrator.vibrate(400);
        oldCollBook.setUpdated(StringUtils.dateConvert(StringUtils.dateConvert(bookDetailBeanInOwn.getUpdateTime(), Constant.FORMAT_BOOK_DATE), Constant.FORMAT_BOOK_DATE));
    }


    //更新Book的目录
    private void updateCategory(CollBookBean collBookBean) {
        List<BookChapterBean> bookChapterBeans = new ArrayList<>();
        Log.d(TAG, "loadCategory: " + collBookBean.get_id() + "" + collBookBean);
        Disposable disposable = RemoteRepository.getInstance()
                .getBookFolder(collBookBean.get_id())
                .compose(RxUtils::toSimpleSingle)
                .subscribe(chapterBeans -> {
                    for (ChapterBean chapterBean : chapterBeans) {
                        BookChapterBean bookChapterBeanTemp = new BookChapterBean();
                        bookChapterBeanTemp.setLink(chapterBean.getChapterId() + "");
                        bookChapterBeanTemp.setTitle(chapterBean.getTitle());
                        bookChapterBeanTemp.setValidInZhuishu(false);
                        bookChapterBeanTemp.setId(MD5Utils.strToMd5By16(bookChapterBeanTemp.getLink()));
                        Log.d(TAG, "+章节名  " + chapterBean.getTitle());
                        bookChapterBeanTemp.setBookId(collBookBean.get_id());
                        bookChapterBeans.add(bookChapterBeanTemp);
                    }
                    collBookBean.setBookChapters(bookChapterBeans);
                    Log.d(TAG, "accept: " + bookChapterBeans);
                    //存储收藏
                    BookRepository.getInstance()
                            .saveCollBookWithAsync(collBookBean);

                }, Throwable::printStackTrace);
    }

}
