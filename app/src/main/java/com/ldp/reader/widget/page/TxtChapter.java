package com.ldp.reader.widget.page;

/**
 * Created by ldp on 17-7-1.
 */

public class TxtChapter{

    //章节所属的小说(网络)
    String bookId;
    //章节的链接(网络)
    String link;

    public int getChapterIdInBiquge() {
        return chapterIdInBiquge;
    }

    public void setChapterIdInBiquge(int chapterIdInBiquge) {
        this.chapterIdInBiquge = chapterIdInBiquge;
    }

    //章节的ID(笔趣阁)
    int chapterIdInBiquge;

    //章节名(共用)
    String title;

    //章节内容在文章中的起始位置(本地)
    long start;
    //章节内容在文章中的终止位置(本地)
    long end;

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String id) {
        this.bookId = id;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    @Override
    public String toString() {
        return "TxtChapter{" +
                "title='" + title + '\'' +
                ", start=" + start +
                ", end=" + end +
                '}';
    }
}
