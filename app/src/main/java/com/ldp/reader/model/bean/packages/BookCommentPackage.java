package com.ldp.reader.model.bean.packages;

import com.ldp.reader.model.bean.BaseBean;
import com.ldp.reader.model.bean.BookCommentBean;

import java.util.List;

/**
 * Created by ldp on 17-4-20.
 */
public class BookCommentPackage extends BaseBean {

    private List<BookCommentBean> posts;

    public List<BookCommentBean> getPosts() {
        return posts;
    }

    public void setPosts(List<BookCommentBean> posts) {
        this.posts = posts;
    }
}
