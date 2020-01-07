package com.ldp.reader.model.bean;

/**
 * Created by ldp on 17-4-29.
 */

public class ChapterBean {

    /**
     * chapterId : -1070783028
     * title : 第一章 绯红
     * bookId : 470055429
     * lastUpdateTime : null
     * link : https://www.liewen.la/b/19/19578/8260453.html
     */

    private int chapterId;
    private String title;
    private int bookId;
    private Object lastUpdateTime;
    private String link;

    public int getChapterId() {
        return chapterId;
    }

    public void setChapterId(int chapterId) {
        this.chapterId = chapterId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public Object getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Object lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
