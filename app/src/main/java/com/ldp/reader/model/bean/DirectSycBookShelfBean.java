package com.ldp.reader.model.bean;

import java.util.List;

/**
 * Created by ldp on 17-5-3.
 * 排行榜的书籍 (不被公用的直接作为内部类)
 */

public class DirectSycBookShelfBean {
    private List<String> bookIds;
    private String mobile;
    private String mobileToken;

    public List<String> getBookIds() {
        return bookIds;
    }

    public void setBookIds(List<String> bookIds) {
        this.bookIds = bookIds;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getMobileToken() {
        return mobileToken;
    }

    public void setMobileToken(String mobileToken) {
        this.mobileToken = mobileToken;
    }
}
