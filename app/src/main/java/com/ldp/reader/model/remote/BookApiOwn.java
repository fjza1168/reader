package com.ldp.reader.model.remote;

import com.ldp.reader.model.bean.BookDetailBeanInOwn;
import com.ldp.reader.model.bean.BookIdBean;
import com.ldp.reader.model.bean.BookSearchResult;
import com.ldp.reader.model.bean.ChapterBean;
import com.ldp.reader.model.bean.ContentBean;
import com.ldp.reader.model.bean.LoginResultBean;
import com.ldp.reader.model.bean.SyncBookShelfBean;

import java.util.List;

import io.reactivex.Single;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by ldp on 18-10-19.
 */

public interface BookApiOwn {


    /**
     * 书籍查询
     * @param bookName:作者名或者书名
     * @return
     */
    @GET("/search")
    Single<List<BookSearchResult>> getSearchResult(@Query("bookName") String bookName);

    /**
     * 获取书籍详情
     *
     *
     * @param bookId:书籍ID
     * @return
     */
    @GET("/getBookInfo")
    Single<BookDetailBeanInOwn> getBookInfo(@Query("bookId") String bookId);
    /**
     * 获取书籍目录
     *
     *
     * @param bookId:书籍ID
     * @return
     */
    @GET("/getBookFolder")
    Single<List<ChapterBean>> getBookFolder(@Query("bookId") String bookId);

    /**
     * 获取书籍详情
     *
     *
     * @param bookId:书籍ID
     * @return
     */
    @GET("/getBookContent")
    Single<ContentBean> getBookContent(@Query("bookId") String bookId, @Query("chapterId") String chapterId);

    /**
     * 登录
     *
     * @return
     */
    @POST("/login")
    Single<LoginResultBean> userLogin(@Query("username") String username, @Query("password") String password);

    /**
     * 获取所有书籍ID
     *
     * @return
     */
    @POST("/getBookShelf")
    Single<List<BookIdBean>> getBookShelf(@Header("Authorization") String header);

    /**
     * 上传所有书籍ID
     *
     * @return
     */
    @POST("/synBookShelf")
    Single<SyncBookShelfBean> setBookShelf(@Header("Authorization") String header, @Body RequestBody body);
}
