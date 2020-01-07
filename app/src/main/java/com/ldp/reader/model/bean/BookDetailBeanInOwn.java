package com.ldp.reader.model.bean;

import java.util.List;

public class BookDetailBeanInOwn {


    /**
     * bookId : 21771508
     * cover : https://www.liewen.la/files/article/image/24/24342/24342s.jpg
     * title : 仙都
     * author : 陈猿
     * desc : “进山采药去了？”“没，俺爹不让去，说山里有狼，到夜里就叫唤。”
     * lastChapter: "064 剑山（加更）更新检测加上去的",
     * sources : [{"bookId":21771508,"link":"https://www.liewen.la/b/24/24342/","source":{"id":1,"name":"猎文网","searchURL":"https://www.liewen.cc/search.php?keyword=%s","minKeywords":1}}]
     */

    private int bookId;
    private String cover;
    private String title;
    private String author;
    private String lastChapter;
    private String desc;
    private List<SourcesBean> sources;

    public CollBookBean getCollBookBean() {
        if (collBookBean == null){
            collBookBean = createCollBookBean();
        }
        return collBookBean;    }

    private CollBookBean collBookBean;

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<SourcesBean> getSources() {
        return sources;
    }

    public void setSources(List<SourcesBean> sources) {
        this.sources = sources;
    }

    public String getLastChapter() {
        return lastChapter;
    }

    public void setLastChapter(String lastChapter) {
        this.lastChapter = lastChapter;
    }

    public static class SourcesBean {
        /**
         * bookId : 21771508
         * link : https://www.liewen.la/b/24/24342/
         * source : {"id":1,"name":"猎文网","searchURL":"https://www.liewen.cc/search.php?keyword=%s","minKeywords":1}
         */

        private int bookId;
        private String link;
        private SourceBean source;

        public int getBookId() {
            return bookId;
        }

        public void setBookId(int bookId) {
            this.bookId = bookId;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public SourceBean getSource() {
            return source;
        }

        public void setSource(SourceBean source) {
            this.source = source;
        }

        public static class SourceBean {
            /**
             * id : 1
             * name : 猎文网
             * searchURL : https://www.liewen.cc/search.php?keyword=%s
             * minKeywords : 1
             */

            private int id;
            private String name;
            private String searchURL;
            private int minKeywords;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getSearchURL() {
                return searchURL;
            }

            public void setSearchURL(String searchURL) {
                this.searchURL = searchURL;
            }

            public int getMinKeywords() {
                return minKeywords;
            }

            public void setMinKeywords(int minKeywords) {
                this.minKeywords = minKeywords;
            }
        }
    }


    public CollBookBean createCollBookBean(){
        CollBookBean bean = new CollBookBean();
        bean.set_id(bookId+"");
        bean.setTitle(title);
        bean.setAuthor(author);
        bean.setShortIntro(desc);
        bean.setCover(cover);
        bean.setBookStatus("连载中");
//        bean.setLatelyFollower(getLatelyFollower());
//        bean.setRetentionRatio(Double.parseDouble(getRetentionRatio()));
        bean.setUpdated("2019年11月16日19:34:08");
        bean.setChaptersCount(100);
        bean.setLastChapter(lastChapter);
        return bean;
    }
}
