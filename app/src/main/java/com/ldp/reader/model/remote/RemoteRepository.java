package com.ldp.reader.model.remote;

import android.util.Log;

import com.ldp.reader.model.bean.BookChapterBean;
import com.ldp.reader.model.bean.BookChapterBeanByBiquge;
import com.ldp.reader.model.bean.BookDetailBean;
import com.ldp.reader.model.bean.BookDetailBeanInOwn;
import com.ldp.reader.model.bean.BookIdBean;
import com.ldp.reader.model.bean.BookSearchResult;
import com.ldp.reader.model.bean.ChapterBean;
import com.ldp.reader.model.bean.ChapterInfoBean;
import com.ldp.reader.model.bean.CollBookBean;
import com.ldp.reader.model.bean.ContentBean;
import com.ldp.reader.model.bean.DirectLoginResultBean;
import com.ldp.reader.model.bean.LoginResultBean;
import com.ldp.reader.model.bean.SmsLoginBean;
import com.ldp.reader.model.bean.SyncBookShelfBean;
import com.ldp.reader.model.bean.packages.BillboardPackage;
import com.ldp.reader.model.bean.BillBookBean;
import com.ldp.reader.model.bean.BookHelpsBean;
import com.ldp.reader.model.bean.BookListBean;
import com.ldp.reader.model.bean.BookListDetailBean;
import com.ldp.reader.model.bean.BookReviewBean;
import com.ldp.reader.model.bean.BookCommentBean;
import com.ldp.reader.model.bean.packages.BookChapterPackage;
import com.ldp.reader.model.bean.packages.BookChapterPackageByBiquge;
import com.ldp.reader.model.bean.packages.BookCommentPackage;
import com.ldp.reader.model.bean.BookDetailBeanInBiquge;
import com.ldp.reader.model.bean.packages.BookSortPackage;
import com.ldp.reader.model.bean.packages.BookSubSortPackage;
import com.ldp.reader.model.bean.BookTagBean;
import com.ldp.reader.model.bean.CommentBean;
import com.ldp.reader.model.bean.CommentDetailBean;
import com.ldp.reader.model.bean.HelpsDetailBean;
import com.ldp.reader.model.bean.HotCommentBean;
import com.ldp.reader.model.bean.ReviewDetailBean;
import com.ldp.reader.model.bean.SortBookBean;
import com.ldp.reader.model.bean.packages.SearchBookPackage;
import com.ldp.reader.model.bean.packages.SearchBookPackageByBiquge;
import com.mob.secverify.datatype.VerifyResult;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.functions.Function;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.http.Query;

import static com.ldp.reader.utils.Constant.APP_KEY;
import static com.ldp.reader.utils.Constant.APP_SECRET;

/**
 * Created by ldp on 17-4-20.
 */

public class RemoteRepository {
    private static final String TAG = "RemoteRepository";

    private static RemoteRepository sInstance;
    private Retrofit mRetrofit, mRetrofitByOwn;
    private BookApi mBookApi;
    private BookApiOwn mBookApiOwn;


    private RemoteRepository() {
        mRetrofit = RemoteHelper.getInstance()
                .getRetrofit();
        mRetrofitByOwn = RemoteHelper.getInstance()
                .getRetrofitByOwn();
        mBookApi = mRetrofit.create(BookApi.class);
        mBookApiOwn = mRetrofitByOwn.create(BookApiOwn.class);
    }

    public static RemoteRepository getInstance() {
        if (sInstance == null) {
            synchronized (RemoteHelper.class) {
                if (sInstance == null) {
                    sInstance = new RemoteRepository();
                }
            }
        }
        return sInstance;
    }



    public Single<List<BookSearchResult>> getSearchResult(String bookName) {
        return mBookApiOwn.getSearchResult(bookName);

    }


    public Single<BookDetailBeanInOwn> getBookInfo(String bookId) {
        return mBookApiOwn.getBookInfo(bookId);
    }

    public Single<List<BookDetailBeanInOwn>> getBookInfoBatch( RequestBody body) {
        return mBookApiOwn.getBookInfoBatch(body);
    }



    public Single<List<ChapterBean>> getBookFolder(String bookId) {
        return mBookApiOwn.getBookFolder(bookId);
    }

    public Single<ContentBean> getBookContent(String bookId,String chapterId ,int sourceIndex) {
        return mBookApiOwn.getBookContent(bookId,chapterId,sourceIndex);
    }

    public Single<LoginResultBean> userLogin(String userNameInput, String passwordInput) {
        return mBookApiOwn.userLogin(userNameInput,passwordInput);
    }

    public Single<SmsLoginBean> smsLogin(String phoneNumber , String smsCode , String registrationId) {
        return mBookApiOwn.smsLogin( phoneNumber , smsCode , registrationId) ;
    }

    public Single<DirectLoginResultBean> userDirectLogin(VerifyResult verifyResult,String registrationId) {
        return mBookApiOwn.userDirectLogin(APP_KEY,APP_SECRET,verifyResult.getToken() ,verifyResult.getOpToken(),verifyResult.getOperator(),registrationId) ;
    }

    public Single<List<BookIdBean>> getBookShelf(String header) {
        return mBookApiOwn.getBookShelf(header);
    }

    public Single<List<BookIdBean>> getBookShelfByMobile(String mobile ,String token) {
        return mBookApiOwn.getBookShelfByMobile(mobile,token);
    }

    public Single<SyncBookShelfBean> setBookShelf(String token, RequestBody body) {
        return mBookApiOwn.setBookShelf(token,body);
    }


    public Single<SyncBookShelfBean> setBookShelfByMobile(RequestBody body) {
        return mBookApiOwn.setBookShelfByMobile(body);
    }





    public Single<List<CollBookBean>> getRecommendBooks(String gender) {
        return mBookApi.getRecommendBookPackage(gender)
                .map(bean -> bean.getBooks());
    }

    public Single<List<BookChapterBean>> getBookChapters(String bookId) {
        return mBookApi.getBookChapterPackage(bookId, "chapter")
                .map(new Function<BookChapterPackage, List<BookChapterBean>>() {
                    @Override
                    public List<BookChapterBean> apply(BookChapterPackage bean) throws Exception {
                        if (bean.getMixToc() == null) {
                            return new ArrayList<BookChapterBean>(1);
                        } else {
                            return bean.getMixToc().getChapters();
                        }
                    }
                });
    }

    /**
     * ?????????????????????????????????
     *
     * @param url
     * @return
     */
    public Single<ChapterInfoBean> getChapterInfo(String url) {
        return mBookApi.getChapterInfoPackage(url)
                .map(bean -> bean.getChapter());
    }

    /***********************************************************************************/


    public Single<List<BookCommentBean>> getBookComment(String block, String sort, int start, int limit, String distillate) {
        Log.d(TAG + "??????", block + sort + start + limit + "distilate" + distillate);
        return mBookApi.getBookCommentList(block, "all", sort, "all", start + "", limit + "", distillate)
                .map(new Function<BookCommentPackage, List<BookCommentBean>>() {
                    @Override
                    public List<BookCommentBean> apply(BookCommentPackage listBean) throws Exception {
                        return listBean.getPosts();
                    }
                });
    }

    public Single<List<BookHelpsBean>> getBookHelps(String sort, int start, int limit, String distillate) {
        return mBookApi.getBookHelpList("all", sort, start + "", limit + "", distillate)
                .map((listBean) -> listBean.getHelps());
    }

    public Single<List<BookReviewBean>> getBookReviews(String sort, String bookType, int start, int limited, String distillate) {
        return mBookApi.getBookReviewList("all", sort, bookType, start + "", limited + "", distillate)
                .map(listBean -> listBean.getReviews());
    }

    public Single<CommentDetailBean> getCommentDetail(String detailId) {
        return mBookApi.getCommentDetailPackage(detailId)
                .map(bean -> bean.getPost());
    }

    public Single<ReviewDetailBean> getReviewDetail(String detailId) {
        return mBookApi.getReviewDetailPacakge(detailId)
                .map(bean -> bean.getReview());
    }

    public Single<HelpsDetailBean> getHelpsDetail(String detailId) {
        return mBookApi.getHelpsDetailPackage(detailId)
                .map(bean -> bean.getHelp());
    }

    public Single<List<CommentBean>> getBestComments(String detailId) {
        return mBookApi.getBestCommentPackage(detailId)
                .map(bean -> bean.getComments());
    }

    /**
     * ???????????? ?????????????????? ??????
     *
     * @param detailId
     * @param start
     * @param limit
     * @return
     */
    public Single<List<CommentBean>> getDetailComments(String detailId, int start, int limit) {
        return mBookApi.getCommentPackage(detailId, start + "", limit + "")
                .map(bean -> bean.getComments());
    }

    /**
     * ???????????? ???????????????????????? ??????
     *
     * @param detailId
     * @param start
     * @param limit
     * @return
     */
    public Single<List<CommentBean>> getDetailBookComments(String detailId, int start, int limit) {
        return mBookApi.getBookCommentPackage(detailId, start + "", limit + "")
                .map(bean -> bean.getComments());
    }

    /*****************************************************************************/
    /**
     * ?????????????????????
     *
     * @return
     */
    public Single<BookSortPackage> getBookSortPackage() {
        return mBookApi.getBookSortPackage();
    }

    /**
     * ????????????????????????
     *
     * @return
     */
    public Single<BookSubSortPackage> getBookSubSortPackage() {
        return mBookApi.getBookSubSortPackage();
    }

    /**
     * ??????????????????????????????
     *
     * @param gender
     * @param type
     * @param major
     * @param minor
     * @param start
     * @param limit
     * @return
     */
    public Single<List<SortBookBean>> getSortBooks(String gender, String type, String major, String minor, int start, int limit) {
        return mBookApi.getSortBookPackage(gender, type, major, minor, start, limit)
                .map(bean -> bean.getBooks());
    }

    /*******************************************************************************/

    /**
     * ??????????????????
     *
     * @return
     */
    public Single<BillboardPackage> getBillboardPackage() {
        return mBookApi.getBillboardPackage();
    }

    /**
     * ??????????????????
     *
     * @param billId
     * @return
     */
    public Single<List<BillBookBean>> getBillBooks(String billId) {
        return mBookApi.getBillBookPackage(billId)
                .map(bean -> bean.getRanking().getBooks());
    }

    /***********************************??????*************************************/

    /**
     * ??????????????????
     *
     * @param duration
     * @param sort
     * @param start
     * @param limit
     * @param tag
     * @param gender
     * @return
     */
    public Single<List<BookListBean>> getBookLists(String duration, String sort,
                                                   int start, int limit,
                                                   String tag, String gender) {
        return mBookApi.getBookListPackage(duration, sort, start + "", limit + "", tag, gender)
                .map(bean -> bean.getBookLists());
    }

    /**
     * ?????????????????????|??????
     *
     * @return
     */
    public Single<List<BookTagBean>> getBookTags() {
        return mBookApi.getBookTagPackage()
                .map(bean -> bean.getData());
    }

    /**
     * ?????????????????????
     *
     * @param detailId
     * @return
     */
    public Single<BookListDetailBean> getBookListDetail(String detailId) {
        return mBookApi.getBookListDetailPackage(detailId)
                .map(bean -> bean.getBookList());
    }

    /***************************************????????????**********************************************/
    public Single<BookDetailBean> getBookDetail(String bookId) {
        return mBookApi.getBookDetail(bookId);
    }

    public Single<List<HotCommentBean>> getHotComments(String bookId) {
        return mBookApi.getHotCommnentPackage(bookId)
                .map(bean -> bean.getReviews());
    }

    public Single<List<BookListBean>> getRecommendBookList(String bookId, int limit) {
        return mBookApi.getRecommendBookListPackage(bookId, limit + "")
                .map(bean -> bean.getBooklists());
    }
    /********************************????????????*********************************************/
    /**
     * ????????????
     *
     * @return
     */
    public Single<List<String>> getHotWords() {
        return mBookApi.getHotWordPackage()
                .map(bean -> bean.getHotWords());
    }

    /**
     * ???????????????
     *
     * @param query
     * @return
     */
    public Single<List<String>> getKeyWords(String query) {
        return mBookApi.getKeyWordPacakge(query)
                .map(bean -> bean.getKeywords());

    }



}
