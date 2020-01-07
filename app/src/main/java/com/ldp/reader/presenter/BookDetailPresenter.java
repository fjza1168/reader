package com.ldp.reader.presenter;

import android.util.Log;

import com.ldp.reader.model.bean.BookChapterBean;
import com.ldp.reader.model.bean.BookDetailBeanInOwn;
import com.ldp.reader.model.bean.ChapterBean;
import com.ldp.reader.model.bean.CollBookBean;
import com.ldp.reader.model.local.BookRepository;
import com.ldp.reader.model.remote.RemoteRepository;
import com.ldp.reader.presenter.contract.BookDetailContract;
import com.ldp.reader.ui.base.RxPresenter;
import com.ldp.reader.utils.MD5Utils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ldp on 17-5-4.
 */

public class BookDetailPresenter extends RxPresenter<BookDetailContract.View>
        implements BookDetailContract.Presenter{
    private static final String TAG = "BookDetailPresenter";
    private String bookId;

    @Override
    public void refreshBookDetail(String bookId) {
        this.bookId = bookId;
        refreshBook();
//        refreshComment();
//        refreshRecommend();

    }
//原有
//    @Override
//    public void addToBookShelf(CollBookBean collBookBean)  {
//        Disposable disposable = RemoteRepository.getInstance()
//                .getBookChapters(collBookBean.get_id())
//                .subscribeOn(Schedulers.io())
//                .doOnSubscribe(
//                        (d) -> mView.waitToBookShelf() //等待加载
//                )
//                .subscribeOn(AndroidSchedulers.mainThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(
//                        new Consumer<List<BookChapterBean>>() {
//                            @Override
//                            public void accept(List<BookChapterBean> beans) throws Exception {
//
//                                //设置 id
//                                for (BookChapterBean bean : beans) {
//                                    bean.setId(MD5Utils.strToMd5By16(bean.getLink()));
//                                    Log.d("+加入书架", bean.getLink() + bean.getTitle());
//                                }
//
//                                //设置目录
//                                collBookBean.setBookChapters(beans);
//                                //存储收藏
//                                BookRepository.getInstance()
//                                        .saveCollBookWithAsync(collBookBean);
//
//                                mView.succeedToBookShelf();
//                            }
//                        }
//                        ,
//                        e -> {
//                            mView.errorToBookShelf();
//                            LogUtils.e(e);
//                        }
//                );
//        addDisposable(disposable);
//    }

//    @Override
//    public void addToBookShelf(CollBookBean collBookBean)  {
//        RemoteRepository.getInstance()
//                .getBookChapters(collBookBean.get_id())
//                .subscribeOn(Schedulers.io())
//                .doOnSubscribe(
//                        (d) -> mView.waitToBookShelf()
//                        //等待加载
//                )
//                .subscribeOn(AndroidSchedulers.mainThread())
//                .observeOn(Schedulers.io())
//                .flatMap(new Function<List<BookChapterBean>, SingleSource<List<SearchBookPackageByBiquge.DataBean>>>() {
//                    @Override
//                    public SingleSource<List<SearchBookPackageByBiquge.DataBean>> apply(List<BookChapterBean> bookChapterBeans) throws Exception {
//
//                        //设置 id
//                        for (BookChapterBean bean : bookChapterBeans) {
//                            bean.setId(MD5Utils.strToMd5By16(bean.getLink()));
//                        }
//                        Log.d(TAG,"+加入书架"+bookChapterBeans.size() + "   " + collBookBean.getTitle());
//
//                        //设置目录
//                        collBookBean.setBookChapters(bookChapterBeans);
//                        //存储收藏
//                        BookRepository.getInstance()
//                                .saveCollBookWithAsync(collBookBean);
//
//
//                        return RemoteRepository.getInstance().getSearchBooksByBiqugeSearch(collBookBean.getTitle());
//                    }})
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<List<SearchBookPackageByBiquge.DataBean>>() {
//                    @Override
//                    public void accept(List<SearchBookPackageByBiquge.DataBean> dataBeans) throws Exception {
//
//                        for(int i =0;i<dataBeans.size();i++){
//
//                            if (dataBeans.get(i).getAuthor().equals(collBookBean.getAuthor()) && dataBeans.get(i).getName().equals(collBookBean.getTitle())){
//                                collBookBean.setBookIdInBiquge(dataBeans.get(i).getId());
//                                BookRepository.getInstance().saveCollBook(collBookBean);
//                                break;
//                            }
//                        }
//
//                        mView.succeedToBookShelf();
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(Throwable throwable) throws Exception {
//                        Log.d(TAG,"+网络错误"+throwable.getMessage() +throwable.getCause());
//                    }
//                });
//
////        addDisposable(disposable);
//    }

    @Override
    public void addToBookShelf(CollBookBean collBookBean) {
        List<BookChapterBean> bookChapterBeans = new ArrayList<>();
        BookRepository.getInstance()
                .saveCollBookWithAsync(collBookBean);
        Log.d(TAG, "addToBookShelf: "+bookId);
        Disposable disposable= RemoteRepository.getInstance()
                .getBookFolder(bookId)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(
                        (d) -> mView.waitToBookShelf()
                        //等待加载
                )
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<ChapterBean>>() {
                    @Override
                    public void accept(List<ChapterBean> chapterBeans) throws Exception {
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
                        BookRepository.getInstance()
                                .saveCollBookWithAsync(collBookBean);
                        mView.succeedToBookShelf();


                        CollBookBean collBookBeanResult = BookRepository.getInstance().getCollBook(bookId);
                        Log.d(TAG, "addToBookShelf:collBookBeanResult "+ collBookBeanResult);


                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mView.errorToBookShelf();

                    }
                });


        addDisposable(disposable);
    }

//    private void refreshBook(){
//        RemoteRepository
//                .getInstance()
//                .getBookDetail(bookId)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new SingleObserver<BookDetailBean>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//                        addDisposable(d);
//                    }
//
//                    @Override
//                    public void onSuccess(BookDetailBean value){
//                        mView.finishRefresh(value);
//                        mView.complete();
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        mView.showError();
//                    }
//                });
//    }


    private void refreshBook() {
        Disposable disposable = RemoteRepository
                .getInstance()
                .getBookInfo(bookId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BookDetailBeanInOwn>() {
                    @Override
                    public void accept(BookDetailBeanInOwn bookDetailBeanInOwn) throws Exception {
                        mView.finishRefresh(bookDetailBeanInOwn);
                        mView.complete();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mView.showError();
                    }
                });

        addDisposable(disposable);


//        Disposable disposable = RemoteRepository
//                .getInstance()
//                .getBookDetailByBiquge(bookId)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<BookDetailBeanInBiquge>() {
//                    @Override
//                    public void accept(BookDetailBeanInBiquge bookDetailBeanInBiquge) throws Exception {
//                        mView.finishRefresh(bookDetailBeanInBiquge);
//                        mView.complete();
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(Throwable throwable) throws Exception {
//                        mView.showError();
//                    }
//                });
//        addDisposable(disposable);

    }

//    private void refreshBook() {
//        Disposable disposable = RemoteRepository
//                .getInstance()
//                .getBookDetailByBiquge(bookId)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<BookDetailBeanInBiquge>() {
//                    @Override
//                    public void accept(BookDetailBeanInBiquge bookDetailBeanInBiquge) throws Exception {
//                        mView.finishRefresh(bookDetailBeanInBiquge);
//                        mView.complete();
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(Throwable throwable) throws Exception {
//                        mView.showError();
//                    }
//                });
//        addDisposable(disposable);
//
//    }

    private void refreshComment(){
        Disposable disposable = RemoteRepository
                .getInstance()
                .getHotComments(bookId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        (value) -> mView.finishHotComment(value)
                );
        addDisposable(disposable);
    }

    private void refreshRecommend(){
        Disposable disposable = RemoteRepository
                .getInstance()
                .getRecommendBookList(bookId,3)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        (value) -> mView.finishRecommendBookList(value)
                );
        addDisposable(disposable);
    }
}
