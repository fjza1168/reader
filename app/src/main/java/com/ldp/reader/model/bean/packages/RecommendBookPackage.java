package com.ldp.reader.model.bean.packages;

import com.ldp.reader.model.bean.BaseBean;
import com.ldp.reader.model.bean.CollBookBean;

import java.util.List;

/**
 * Created by ldp on 17-5-8.
 */

public class RecommendBookPackage extends BaseBean {

    private List<CollBookBean> books;

    public List<CollBookBean> getBooks() {
        return books;
    }

    public void setBooks(List<CollBookBean> books) {
        this.books = books;
    }
}
