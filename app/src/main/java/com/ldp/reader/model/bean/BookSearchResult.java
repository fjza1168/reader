package com.ldp.reader.model.bean;

import java.util.List;
import java.util.Objects;

public class BookSearchResult {

    /**
     * cover : https://www.liewen.la/files/article/image/36/36823/36823s.jpg
     * title : 神探悍妻之老婆大人上上签
     * author : 逍遥游游
     * desc : 天空降下一道雷，“咔嚓”一声，竟将威名赫赫的大姐大直接劈进了刑警队长苏青的体内。身份转换，斗转星移。于是大家开眼了：花样百出的断案手段，层出不穷的审案风格，闻所未闻，见所未见，简直就是警界的明日之花，...
     * sources : [{"link":"https://www.liewen.la/b/36/36823/","source":{"id":1,"name":"猎文网","searchURL":"https://www.liewen.cc/search.php?keyword=%s","minKeywords":1}}]
     */

    private String cover;
    private String title;
    private String author;
    private String desc;
    private List<SourcesBean> sources;

    public String getId() {
        id = Objects.hash(title, author)+"";
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookSearchResult that = (BookSearchResult) o;
        return title.equals(that.title) &&
                author.equals(that.author);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, author);
    }

    public void setId(String id) {
        this.id = id;
    }

    private String id;
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

    public static class SourcesBean {
        /**
         * link : https://www.liewen.la/b/36/36823/
         * source : {"id":1,"name":"猎文网","searchURL":"https://www.liewen.cc/search.php?keyword=%s","minKeywords":1}
         */

        private String link;
        private SourceBean source;

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
}
