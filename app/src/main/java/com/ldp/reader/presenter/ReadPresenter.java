package com.ldp.reader.presenter;


import android.util.Log;

import com.ldp.reader.model.bean.BookChapterBean;
import com.ldp.reader.model.bean.ChapterBean;
import com.ldp.reader.model.bean.ChapterInfoBean;
import com.ldp.reader.model.bean.CollBookBean;
import com.ldp.reader.model.bean.ContentBean;
import com.ldp.reader.model.bean.packages.BookChapterPackageByBiquge;
import com.ldp.reader.model.local.BookRepository;
import com.ldp.reader.model.remote.RemoteRepository;
import com.ldp.reader.presenter.contract.ReadContract;
import com.ldp.reader.ui.base.RxPresenter;
import com.ldp.reader.utils.LogUtils;
import com.ldp.reader.utils.MD5Utils;
import com.ldp.reader.utils.RxUtils;
import com.ldp.reader.utils.SimilarityCharacterUtils;
import com.ldp.reader.widget.page.TxtChapter;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ldp on 17-5-16.
 */

public class ReadPresenter extends RxPresenter<ReadContract.View>
        implements ReadContract.Presenter {
    private static final String TAG = ReadPresenter.class.getSimpleName();

    private Subscription mChapterSub;
    String bookIdInBiquge = "";

    @Override
    public void loadCategory(String bookId) {
        List<BookChapterBean> bookChapterBeans = new ArrayList<>();
        CollBookBean collBookBean = BookRepository.getInstance().getCollBook(bookId);

        Log.d(TAG, "loadCategory: "+bookId+""+collBookBean);
       Disposable disposable= RemoteRepository.getInstance()
                .getBookFolder(bookId)
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
                    mView.showCategory(bookChapterBeans, bookId, true);

                    //存储收藏
                    BookRepository.getInstance()
                            .saveCollBookWithAsync(collBookBean);

                }, throwable -> mView.errorChapter());
        addDisposable(disposable);

    }



    @Override
    public synchronized void loadChapter(String bookId, List<TxtChapter> bookChapters) {

        int size = bookChapters.size();
        Log.e(TAG, "loadChapter  列表大小" + size + Arrays.asList(bookChapters).toString());

        //取消上次的任务，防止多次加载
        if (mChapterSub != null) {
            mChapterSub.cancel();
        }

        List<Single<ContentBean>> bookChapterSBeanByBiquge = new ArrayList<>(bookChapters.size());

        ArrayDeque<String> titlesInBiquge = new ArrayDeque<>();

        // 将要下载章节，转换成网络请求。
        for (int i = 0; i < size; ++i) {
            TxtChapter bookChapter = bookChapters.get(i);
            String pureLink = bookChapter.getLink();
            CollBookBean bean = BookRepository.getInstance().getCollBook(bookId);
            bookIdInBiquge = bean.get_id();
            Log.d("+收到的章节笔趣阁Id", bookIdInBiquge);
            Single<ContentBean> bookChapterBeanByBiqugeSingle = RemoteRepository.getInstance()
                    .getBookContent(bookIdInBiquge, pureLink ,0 );
            Log.d("+收到的章节ID", bookChapter.getLink());
            bookChapterSBeanByBiquge.add(bookChapterBeanByBiqugeSingle);
            titlesInBiquge.add(bookChapter.getTitle());
        }

        Single.concat(bookChapterSBeanByBiquge)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ContentBean>() {
                    String titleInBiquge = titlesInBiquge.poll();
                    @Override
                    public void onSubscribe(Subscription s) {
                        s.request(Integer.MAX_VALUE);
                        mChapterSub = s;
                    }

                    @Override
                    public void onNext(ContentBean contentBean) {
                        //存储数据
                        BookRepository.getInstance().saveChapterInfo(
                                bookId, titleInBiquge, contentBean.getContent()
                        );
                        Log.e("+chapterBody", "title" + titleInBiquge + titlesInBiquge + " " +contentBean.getContent());
                        mView.finishChapter(false);
                        //将获取到的数据进行存储
                        titleInBiquge = titlesInBiquge.poll();
                    }

                    @Override
                    public void onError(Throwable t) {
                        //只有第一个加载失败才会调用errorChapter
                        if (bookChapters.get(0).getTitle().equals(titleInBiquge)) {
                            mView.errorChapter();
                        }
                        LogUtils.e(t);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    @Override
    public synchronized void refreshChapter(String bookId, TxtChapter bookChapter , int sourceIndex) {
        String pureLink = bookChapter.getLink();
        CollBookBean bean = BookRepository.getInstance().getCollBook(bookId);
        bookIdInBiquge = bean.get_id();
        Disposable disposable = RemoteRepository.getInstance()
                .getBookContent(bookIdInBiquge, pureLink ,sourceIndex)
                .compose(RxUtils::toSimpleSingle)
                .subscribe(contentBean -> {
                    //存储数据
                    BookRepository.getInstance().saveChapterInfo(
                            bookId, bookChapter.getTitle(), contentBean.getContent());
                   mView.finishChapter(true);
                }, throwable -> mView.errorChapter());
        addDisposable(disposable);
    }


    @Override
    public void detachView() {
        super.detachView();
        if (mChapterSub != null) {
            mChapterSub.cancel();
        }
    }

}
