package com.ldp.reader.model.bean.packages;

import com.ldp.reader.model.bean.BaseBean;

import java.util.List;

/**
 * Created by ldp on 17-6-2.
 */

public class SearchBookPackageByBiquge extends BaseBean {

    /**
     * status : 1
     * info : success
     * data : [{"Id":"424","Name":"凡人修仙传","Author":"忘语","Img":"http://www.apporapp.cc/BookFiles/BookImages/424.jpg","Desc":"凡人修仙传最新章节列：小说《凡人修仙传》忘语/著,凡人修仙传全文阅读一个普通山村小子，偶然下进入到当地江湖小门派，成了一名记名弟子。他以这样身份，如何在门派中立足,如何以平庸的资质进入到修仙者的行列，从而......","BookStatus":"完结","LastChapterId":"4807039","LastChapter":"忘语新书《玄界之门》","CName":"武侠仙侠","UpdateTime":"2017-03-18 00:00:00"},{"Id":"55611","Name":"都市凡人修仙传","Author":"朝杨","Img":"http://www.apporapp.cc/BookFiles/BookImages/doushifanrenxiuxianchuan.jpg","Desc":"一个来自农村的少年，没有强大的背景，没有逆天的资质，在别人幻想着当富二代的时候，他却在为了成为富二代的爹努力奋斗，看一个平凡人物一步一步成长的故事","BookStatus":"连载","LastChapterId":"3090222","LastChapter":"关于本书","CName":"武侠仙侠","UpdateTime":"2017-03-01 00:00:00"},{"Id":"236188","Name":"小小凡人修仙传","Author":"至尊小宝","Img":"http://www.apporapp.cc/BookFiles/BookImages/xiaoxiaofanrenxiuxianchuan.jpg","Desc":"第629章大言明王\n　　 那个纨绔子弟以一副百事通的造型道：\u201c看起来朝中形势严峻，庄族拜相几百年，势力根深蒂固，长空豹又是王族正统，拥有得天独厚的影响力，暗中潜伏的东方一族也不容小视，而卫族、黄族隔岸观火。更传言，张夜虽然和大木族结亲，但也后院起火，不知道出了什么变故，张夜和妻子木昭君势同水火，放着那样的才女佳人却不碰，肯定有原因。如此，等于和木族对敌了。而张","BookStatus":"连载","LastChapterId":"1279344","LastChapter":"第629章 大言明王","CName":"武侠仙侠","UpdateTime":"2018-03-05 00:00:00"},{"Id":"41311","Name":"凡人修仙传之配角传奇","Author":"轻语江湖","Img":"http://www.apporapp.cc/BookFiles/BookImages/fanrenxiuxianchuanzhipeijiaochuanqi.jpg","Desc":"凡人结束，感动未断。\n　　\n　　 犹记得那人前装酷，人后逗比的厉师兄，\n　　\n　　 犹记得那惊才艳艳，亦师亦友的大衍神君，\n　　\n　　 犹记得那古林精怪，却不能修炼的墨彩环，\n　　\n　　 而那只出现过一次名字的韩家老三韩铸，抑或有着自己的人生。\n　　\n　　 那为老魔取名的老张叔，又何尝没有自己的传奇。\n　　\n　　 他们是凡人配角，但在这些故事之中，他们是自己人生的主角，因为他们也是传奇","BookStatus":"连载","LastChapterId":"2360333","LastChapter":"完结之后的一点话","CName":"小说同人","UpdateTime":"2015-12-19 00:00:00"}]
     */

    private int status;
    private String info;
    private List<DataBean> data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * Id : 424
         * Name : 凡人修仙传
         * Author : 忘语
         * Img : http://www.apporapp.cc/BookFiles/BookImages/424.jpg
         * Desc : 凡人修仙传最新章节列：小说《凡人修仙传》忘语/著,凡人修仙传全文阅读一个普通山村小子，偶然下进入到当地江湖小门派，成了一名记名弟子。他以这样身份，如何在门派中立足,如何以平庸的资质进入到修仙者的行列，从而......
         * BookStatus : 完结
         * LastChapterId : 4807039
         * LastChapter : 忘语新书《玄界之门》
         * CName : 武侠仙侠
         * UpdateTime : 2017-03-18 00:00:00
         */

        private String Id;
        private String Name;
        private String Author;
        private String Img;
        private String Desc;
        private String BookStatus;
        private String LastChapterId;
        private String LastChapter;
        private String CName;
        private String UpdateTime;

        public String getId() {
            return Id;
        }

        public void setId(String Id) {
            this.Id = Id;
        }

        public String getName() {
            return Name;
        }

        public void setName(String Name) {
            this.Name = Name;
        }

        public String getAuthor() {
            return Author;
        }

        public void setAuthor(String Author) {
            this.Author = Author;
        }

        public String getImg() {
            return Img;
        }

        public void setImg(String Img) {
            this.Img = Img;
        }

        public String getDesc() {
            return Desc;
        }

        public void setDesc(String Desc) {
            this.Desc = Desc;
        }

        public String getBookStatus() {
            return BookStatus;
        }

        public void setBookStatus(String BookStatus) {
            this.BookStatus = BookStatus;
        }

        public String getLastChapterId() {
            return LastChapterId;
        }

        public void setLastChapterId(String LastChapterId) {
            this.LastChapterId = LastChapterId;
        }

        public String getLastChapter() {
            return LastChapter;
        }

        public void setLastChapter(String LastChapter) {
            this.LastChapter = LastChapter;
        }

        public String getCName() {
            return CName;
        }

        public void setCName(String CName) {
            this.CName = CName;
        }

        public String getUpdateTime() {
            return UpdateTime;
        }

        public void setUpdateTime(String UpdateTime) {
            this.UpdateTime = UpdateTime;
        }
    }
}
