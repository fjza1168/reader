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
        for(CollBookBean bookBean: collBooks){
            Log.d("+书名",bookBean.getTitle());
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
                    public void accept(List<CollBookBean> collBooks) throws Exception{
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
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable e) throws Exception {
                                //提示没有网络
                                LogUtils.e(e);
                                mView.showErrorTip(e.toString());
                                mView.complete();
                            }
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
                            for (BookIdBean bookIdBean:bookIdBeans ) {
                                bookIdList.add(bookIdBean.getBookId()+"");
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
    public void getBookShelfByMobile(String mobile,String mobileToken) {
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
        for (String bookId:bookIdList ) {
            CollBookBean  mCollBookBean = BookRepository.getInstance().getCollBook(bookId);
            if(null == mCollBookBean){
                bookDetailSingleList.add(RemoteRepository.getInstance().getBookInfo(bookId))  ;
            }
        }
       Disposable disposable = Single.concat(bookDetailSingleList)
               .subscribeOn(Schedulers.io())
               .observeOn(AndroidSchedulers.mainThread() )
          .subscribe(new Consumer<BookDetailBeanInOwn>() {
              @Override
              public void accept(BookDetailBeanInOwn bookDetailBeanInOwn) throws Exception {
                  addToBookShelf(bookDetailBeanInOwn.getCollBookBean());
              }
          }, new Consumer<Throwable>() {
              @Override
              public void accept(Throwable throwable) throws Exception {
                  mView.showErrorTip(throwable.getMessage());
              }
          }, new Action() {
              @Override
              public void run() throws Exception {
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
              }
          });
        addDisposable(disposable);
    }



    private void addToBookShelf(CollBookBean collBookBean) {
        List<BookChapterBean> bookChapterBeans = new ArrayList<>();
        BookRepository.getInstance()
                .saveCollBookWithAsync(collBookBean);
        Disposable disposable= RemoteRepository.getInstance()
                .getBookFolder(collBookBean.get_id())
               .compose(RxUtils::toSimpleSingle)
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
                        BookRepository.getInstance().saveCollBookWithAsync(collBookBean);
                        CollBookBean collBookBeanResult = BookRepository.getInstance().getCollBook(collBookBean.get_id());
                        Log.d(TAG, "addToBookShelf:collBookBeanResult "+ collBookBeanResult);

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mView.showErrorTip(throwable.getMessage());
                    }
                });


        addDisposable(disposable);
    }




    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    @Override
    public void setBookShelf(List<String> bookIds) {
        RequestBody body = RequestBody.create(JSON, new Gson().toJson(bookIds));
        String token = SharedPreUtils.getInstance().getString("token");
        Disposable disposable = RemoteRepository.getInstance().setBookShelf(token, body)
                .compose(RxUtils::toSimpleSingle)
                .subscribe(new Consumer<SyncBookShelfBean>() {
                    @Override
                    public void accept(SyncBookShelfBean syncBookShelfBean) throws Exception {
                      mView.finishSyncBook();
                      mView.finishUpdate();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mView.showErrorTip(throwable.getMessage());
                    }
                });
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
                .subscribe(new Consumer<SyncBookShelfBean>() {
                    @Override
                    public void accept(SyncBookShelfBean syncBookShelfBean) throws Exception {
                        mView.finishSyncBook();
                        mView.finishUpdate();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mView.showErrorTip(throwable.getMessage());
                    }
                });
        addDisposable(disposable);
    }


    //需要修改
    @Override
    public void updateCollBooks(List<CollBookBean> collBookBeans) {
        Log.d(TAG, "+updateCollBooks ");

//        List<CollBookBean> collBookBeans = BookRepository
//                .getInstance().getCollBooks();
        if (collBookBeans == null || collBookBeans.isEmpty()) {
            return;
        }
        List<CollBookBean> collBooks = new ArrayList<>(collBookBeans);
        List<Single<BookDetailBeanInOwn>> observables = new ArrayList<>(collBooks.size());
        List<Single<BookDetailBeanInBiquge>> observablesInBiquge = new ArrayList<>();

        Iterator<CollBookBean> it = collBooks.iterator();
        while (it.hasNext()) {
            CollBookBean collBook = it.next();
            //删除本地文件
            if (collBook.isLocal()) {
                it.remove();
            } else {
                observables.add(RemoteRepository.getInstance().getBookInfo(collBook.get_id()));
//                if (null == collBook.getBookIdInBiquge()|| collBook.getBookIdInBiquge().isEmpty()) {
//
//                    continue;
//                }

                Log.d(TAG, "+笔趣阁ID " + collBook.getBookIdInBiquge());
//                observablesInBiquge.add(RemoteRepository.getInstance().getBookDetailByBiquge(collBook.get_id()));
            }
        }

        List<CollBookBean> newCollBooksMerge = new ArrayList<CollBookBean>(observables.size());
        Disposable chaptersDispoable = Single.concat(observables)
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<BookDetailBeanInOwn>() {
                    @Override
                    public void accept(BookDetailBeanInOwn bookDetailBeanInOwn) throws Exception {
                        CollBookBean oldCollBook = BookRepository.getInstance().getCollBook(String.valueOf(bookDetailBeanInOwn.getBookId()));

                        //如果是oldBook是update状态，或者newCollBook与oldBook章节数不同
//                        showNotification(oldCollBook);
//                        BookShelfPresenter.this.updateCategory(oldCollBook);
                        if (!oldCollBook.getLastChapter().equals(bookDetailBeanInOwn.getLastChapter())) {
                            oldCollBook.setLastChapter(bookDetailBeanInOwn.getLastChapter());
                            Log.d(TAG, "+更新书籍 " + oldCollBook.getTitle() + oldCollBook.getLastChapter());
                            oldCollBook.setUpdate(true);
//       n                      showNotification(oldCollBook);
//                            BookShelfPresenter.this.updateCategory(oldCollBook);
                            Vibrator vibrator = (Vibrator) App.getContext().getSystemService(App.getContext().VIBRATOR_SERVICE);
                            vibrator.vibrate(4000);
                        } else {
                            oldCollBook.setUpdate(false);
                        }
                        newCollBooksMerge.add(oldCollBook);
                        Log.d(TAG, "+检查更新");
                    }
                }, throwable -> {

                }, () -> {
//                        updateCategory(newCollBooksMerge);
//                                //异步存储到数据库中
                    BookRepository.getInstance().saveCollBooks(newCollBooksMerge);
//                                Log.d("+?5缓存", "运行");//
                    mView.complete();
                });
        addDisposable(chaptersDispoable);


//        if (collBookBeans == null || collBookBeans.isEmpty()) return;
//        List<CollBookBean> collBooks = new ArrayList<>(collBookBeans);
//        List<Single<BookDetailBean>> observables = new ArrayList<>(collBooks.size());
//        Iterator<CollBookBean> it = collBooks.iterator();
//        while (it.hasNext()){
//            CollBookBean collBook = it.next();
//            //删除本地文件
//            if (collBook.isLocal()) {
//                it.remove();
//            }
//            else {
//                observables.add(RemoteRepository.getInstance()
//                        .getBookDetail(collBook.get_id()));
//            }
//        }
//        //zip可能不是一个好方法。
//        Single.zip(observables, new Function<Object[], List<CollBookBean>>() {
//            @Override
//            public List<CollBookBean> apply(Object[] objects) throws Exception {
//                List<CollBookBean> newCollBooks = new ArrayList<CollBookBean>(objects.length);
//                for (int i=0; i<collBooks.size(); ++i){
//                    CollBookBean oldCollBook = collBooks.get(i);
//                    CollBookBean newCollBook = ((BookDetailBean)objects[i]).getCollBookBean();
//                    //如果是oldBook是update状态，或者newCollBook与oldBook章节数不同
//                    if (oldCollBook.isUpdate() ||
//                            !oldCollBook.getLastChapter().equals(newCollBook.getLastChapter())){
//                        newCollBook.setUpdate(true);
//                    }
//                    else {
//                        newCollBook.setUpdate(false);
//                    }
//                    newCollBook.setLastRead(oldCollBook.getLastRead());
//                    newCollBooks.add(newCollBook);
//                    //存储到数据库中
//                    BookRepository.getInstance().saveCollBooks(newCollBooks);
//                }
//                return newCollBooks;
//            }
//        })
//                .compose(RxUtils::toSimpleSingle)
//                .subscribe(new SingleObserver<List<CollBookBean>>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//                        addDisposable(d);
//                    }
//
//                    @Override
//                    public void onSuccess(List<CollBookBean> value) {
//                        //跟原先比较
//                        mView.finishUpdate();
//                        List<String> bookNames = new ArrayList<>();
//                        for (CollBookBean collBookBean: value) {
//                            bookNames.add(collBookBean.getTitle());
//                        }
//                        mView.complete();
//
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        //提示没有网络
//                        mView.showErrorTip(e.toString());
//                        mView.complete();
//                        LogUtils.e(e);
//                    }
//                });
    }

    //更新每个CollBook的目录
    private void updateCategory(List<CollBookBean> collBookBeans) {
        List<Single<List<BookChapterBean>>> observables = new ArrayList<>(collBookBeans.size());
        for (CollBookBean bean : collBookBeans) {
            observables.add(
                    RemoteRepository.getInstance().getBookChapters(bean.get_id())
            );
        }
        Iterator<CollBookBean> it = collBookBeans.iterator();
        //执行在上一个方法中的子线程中
        Single.concat(observables)
                .subscribe(
                        new Consumer<List<BookChapterBean>>() {
                            @Override
                            public void accept(List<BookChapterBean> chapterList) throws Exception {

                                for (BookChapterBean bean : chapterList) {
                                    bean.setId(MD5Utils.strToMd5By16(bean.getLink()));
                                }

                                CollBookBean bean = it.next();
                                bean.setLastRead(StringUtils.
                                        dateConvert(System.currentTimeMillis(), Constant.FORMAT_BOOK_DATE));
                                bean.setBookChapters(chapterList);
                            }
                        }
                );
    }

    //更新每个CollBook的目录
    private void updateCategory(CollBookBean collBookBean) {
        List<Single<BookDetailBeanInBiquge>> observablesInBiquge = new ArrayList<>();
        List<BookChapterBean> bookChapterBeans = new ArrayList<>();
        Disposable disposable = RemoteRepository.getInstance()
                .getChapterListByBiquge(collBookBean.get_id())
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Consumer<BookChapterPackageByBiquge>() {
                    @Override
                    public void accept(BookChapterPackageByBiquge bookChapterPackageByBiquge) throws Exception {
                        List<BookChapterPackageByBiquge.DataBean.ListBeanX.ListBean> listBeans = new ArrayList<>();
                        for (BookChapterPackageByBiquge.DataBean.ListBeanX mlistBeanX : bookChapterPackageByBiquge.getData().getList()) {
                            if (null != mlistBeanX && null != mlistBeanX.getList()) {
                                listBeans.addAll(mlistBeanX.getList());
                            }
                        }
                        for (int i = 0; i <=listBeans.size() - 1; i++) {
                            if (null != listBeans.get(i)) {
                                BookChapterBean bookChapterBeanTemp = new BookChapterBean();
                                bookChapterBeanTemp.setLink("BQG" + String.valueOf(listBeans.get(i).getId()));
                                bookChapterBeanTemp.setTitle(listBeans.get(i).getName());
                                bookChapterBeanTemp.setValidInZhuishu(false);
                                bookChapterBeanTemp.setId(MD5Utils.strToMd5By16(bookChapterBeanTemp.getLink()));
                                Log.d(TAG, "+章节名  " + i + " " + listBeans.get(i).getName());
                                bookChapterBeanTemp.setBookId(collBookBean.get_id());
                                bookChapterBeans.add(bookChapterBeanTemp);
                            }
                        }
//                        BookRepository.getInstance()
//                                .saveBookChaptersWithAsync(bookChapterBeans);
                        //设置目录
                        collBookBean.setBookChapters(bookChapterBeans);
                        //存储收藏
                        BookRepository.getInstance()
                                .saveCollBookWithAsync(collBookBean);
//                        mView.succeedToBookShelf();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e(TAG, throwable.getCause() + "   " + throwable.getMessage());
//                        mView.errorToBookShelf();
                    }
                });
        addDisposable(disposable);
    }

}
