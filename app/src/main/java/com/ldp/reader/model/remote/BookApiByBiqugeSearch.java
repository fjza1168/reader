package com.ldp.reader.model.remote;

import com.ldp.reader.model.bean.packages.SearchBookPackageByBiquge;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by ldp on 18-10-19.
 */

public interface BookApiByBiqugeSearch {


    /**
     * 书籍查询
     * 如 http://sou.jiaston.com/search.aspx?key=%E5%87%A1%E4%BA%BA%E4%BF%AE%E4%BB%99%E4%BC%A0&page=1&siteid=app
     *
     * @param key:作者名或者书名
     * @param page:默认为1
     * @param siteid:默认为app
     * @return
     */
    @GET("/search.aspx?")
    Single<SearchBookPackageByBiquge> getSearchBookPackageByBiquge(@Query("key") String key, @Query("page") String page, @Query("siteid") String siteid);
}
