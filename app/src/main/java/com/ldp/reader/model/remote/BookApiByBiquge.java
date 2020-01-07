package com.ldp.reader.model.remote;

import com.ldp.reader.model.bean.BookChapterBeanByBiquge;
import com.ldp.reader.model.bean.packages.BookChapterPackageByBiquge;
import com.ldp.reader.model.bean.BookDetailBeanInBiquge;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by ldp on 18-10-19.
 */

public interface BookApiByBiquge {


    /**
     * 获取书籍详情
     * 如 http://shuapi.jiaston.com/info/248872.html
     *
     * @param bookId:书籍ID
     * @return
     */
    @GET("/info/{bookId}.html")
    Single<BookDetailBeanInBiquge> geBookDetailByBiquge(@Path("bookId") String bookId);

    /**
     * 获取目录
     * 如 http://shuapi.jiaston.com/book/199175/
     *
     * @param bookId:书籍ID
     * @return
     */
    @GET("/book/{bookId}/")
    Single<BookChapterPackageByBiquge> getChapterListByBiquge(@Path("bookId") String bookId);

    /**
     * 获取章节
     * 如 https://shuapi.jiaston.com/book/199175/1389236.html
     *
     * @param bookId:书籍ID
     * @param chapterId:章节名
     * @return
     */
    @GET("/book/{bookId}/{chapterId}.html")
    Single<BookChapterBeanByBiquge> getChapterByBiquge(@Path("bookId") String bookId, @Path("chapterId") String chapterId);

}
