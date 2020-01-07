package com.ldp.reader.model.bean;

import java.util.List;

public class BookDetailBeanInBiquge {

    /**
     * status : 1
     * info : success
     * data : {"Id":248872,"Name":"诡秘之主","Img":"guimizhizhu.jpg","Author":"爱潜水的乌贼","Desc":"蒸汽与机械的浪潮中，谁能触及非凡？历史和黑暗的迷雾里，又是谁在耳语？我从诡秘中醒来，睁眼看见这个世界：枪械，大炮，巨舰，飞空艇，差分机；魔药，占卜，诅咒，倒吊人，封印物\u2026\u2026光明依旧照耀，神秘从未远离，这是一段\u201c愚者\u201d的传说。","CId":95,"CName":"玄幻奇幻","LastTime":"12/31/2018 7:18:53 PM","FirstChapterId":1297567,"LastChapter":"第八十章 伊莲","LastChapterId":1428127,"BookStatus":"连载","SameUserBooks":[{"Id":2387,"Name":"奥术神座","Author":"爱潜水的乌贼","Img":"aoshushenzuo.jpg","LastChapterId":5167945,"LastChapter":"新书《诡秘之主》","Score":0},{"Id":23891,"Name":"成仙途","Author":"爱潜水的乌贼","Img":"chengxiantu.jpg","LastChapterId":1676712,"LastChapter":"新书《诡秘之主》","Score":0},{"Id":2740,"Name":"灭运图录","Author":"爱潜水的乌贼","Img":"mieyuntulu.jpg","LastChapterId":5168335,"LastChapter":"新书《诡秘之主》","Score":0},{"Id":87465,"Name":"武道宗师","Author":"爱潜水的乌贼","Img":"wudaozongshi.jpg","LastChapterId":5218986,"LastChapter":"新书上传了","Score":0},{"Id":3310,"Name":"一世之尊","Author":"爱潜水的乌贼","Img":"yishizhizun.jpg","LastChapterId":5169494,"LastChapter":"新书《诡秘之主》","Score":0}],"SameCategoryBooks":[{"Id":32,"Name":"大道争锋","Img":"32.jpg","Score":0},{"Id":277112,"Name":"仲裁与时钟","Img":"zhongcaiyushizhong.jpg","Score":0},{"Id":320873,"Name":"一卡在手","Img":"yikazaishou.jpg","Score":0},{"Id":318877,"Name":"无敌炎黄系统","Img":"wudiyanhuangxitong.jpg","Score":0},{"Id":95192,"Name":"王国血脉","Img":"wangguoxuemai.jpg","Score":0},{"Id":251087,"Name":"幻神","Img":"huanshen.jpg","Score":0},{"Id":141133,"Name":"天地霸体诀","Img":"tiandibatijue.jpg","Score":0},{"Id":359356,"Name":"天道至尊","Img":"tiandaozhizun.jpg","Score":0},{"Id":247421,"Name":"阴阳化天下","Img":"yinyanghuatianxia.jpg","Score":0},{"Id":297663,"Name":"冰龙记","Img":"binglongji.jpg","Score":0},{"Id":362481,"Name":"灵，香织","Img":"ling，xiangzhi.jpg","Score":0},{"Id":346561,"Name":"废土女王","Img":"feitunvwang.jpg","Score":0}],"BookVote":{"BookId":248872,"TotalScore":684,"VoterCount":73,"Score":9.4}}
     */

    private int status;
    private String info;
    private DataBean data;
    private CollBookBean collBookBean;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * Id : 248872
         * Name : 诡秘之主
         * Img : guimizhizhu.jpg
         * Author : 爱潜水的乌贼
         * Desc : 蒸汽与机械的浪潮中，谁能触及非凡？历史和黑暗的迷雾里，又是谁在耳语？我从诡秘中醒来，睁眼看见这个世界：枪械，大炮，巨舰，飞空艇，差分机；魔药，占卜，诅咒，倒吊人，封印物……光明依旧照耀，神秘从未远离，这是一段“愚者”的传说。
         * CId : 95
         * CName : 玄幻奇幻
         * LastTime : 12/31/2018 7:18:53 PM
         * FirstChapterId : 1297567
         * LastChapter : 第八十章 伊莲
         * LastChapterId : 1428127
         * BookStatus : 连载
         * SameUserBooks : [{"Id":2387,"Name":"奥术神座","Author":"爱潜水的乌贼","Img":"aoshushenzuo.jpg","LastChapterId":5167945,"LastChapter":"新书《诡秘之主》","Score":0},{"Id":23891,"Name":"成仙途","Author":"爱潜水的乌贼","Img":"chengxiantu.jpg","LastChapterId":1676712,"LastChapter":"新书《诡秘之主》","Score":0},{"Id":2740,"Name":"灭运图录","Author":"爱潜水的乌贼","Img":"mieyuntulu.jpg","LastChapterId":5168335,"LastChapter":"新书《诡秘之主》","Score":0},{"Id":87465,"Name":"武道宗师","Author":"爱潜水的乌贼","Img":"wudaozongshi.jpg","LastChapterId":5218986,"LastChapter":"新书上传了","Score":0},{"Id":3310,"Name":"一世之尊","Author":"爱潜水的乌贼","Img":"yishizhizun.jpg","LastChapterId":5169494,"LastChapter":"新书《诡秘之主》","Score":0}]
         * SameCategoryBooks : [{"Id":32,"Name":"大道争锋","Img":"32.jpg","Score":0},{"Id":277112,"Name":"仲裁与时钟","Img":"zhongcaiyushizhong.jpg","Score":0},{"Id":320873,"Name":"一卡在手","Img":"yikazaishou.jpg","Score":0},{"Id":318877,"Name":"无敌炎黄系统","Img":"wudiyanhuangxitong.jpg","Score":0},{"Id":95192,"Name":"王国血脉","Img":"wangguoxuemai.jpg","Score":0},{"Id":251087,"Name":"幻神","Img":"huanshen.jpg","Score":0},{"Id":141133,"Name":"天地霸体诀","Img":"tiandibatijue.jpg","Score":0},{"Id":359356,"Name":"天道至尊","Img":"tiandaozhizun.jpg","Score":0},{"Id":247421,"Name":"阴阳化天下","Img":"yinyanghuatianxia.jpg","Score":0},{"Id":297663,"Name":"冰龙记","Img":"binglongji.jpg","Score":0},{"Id":362481,"Name":"灵，香织","Img":"ling，xiangzhi.jpg","Score":0},{"Id":346561,"Name":"废土女王","Img":"feitunvwang.jpg","Score":0}]
         * BookVote : {"BookId":248872,"TotalScore":684,"VoterCount":73,"Score":9.4}
         */

        private int Id;
        private String Name;
        private String Img;
        private String Author;
        private String Desc;
        private int CId;
        private String CName;
        private String LastTime;
        private int FirstChapterId;
        private String LastChapter;
        private int LastChapterId;
        private String BookStatus;
        private BookVoteBean BookVote;
        private List<SameUserBooksBean> SameUserBooks;
        private List<SameCategoryBooksBean> SameCategoryBooks;

        public int getId() {
            return Id;
        }

        public void setId(int Id) {
            this.Id = Id;
        }

        public String getName() {
            return Name;
        }

        public void setName(String Name) {
            this.Name = Name;
        }

        public String getImg() {
            return Img;
        }

        public void setImg(String Img) {
            this.Img = Img;
        }

        public String getAuthor() {
            return Author;
        }

        public void setAuthor(String Author) {
            this.Author = Author;
        }

        public String getDesc() {
            return Desc;
        }

        public void setDesc(String Desc) {
            this.Desc = Desc;
        }

        public int getCId() {
            return CId;
        }

        public void setCId(int CId) {
            this.CId = CId;
        }

        public String getCName() {
            return CName;
        }

        public void setCName(String CName) {
            this.CName = CName;
        }

        public String getLastTime() {
            return LastTime;
        }

        public void setLastTime(String LastTime) {
            this.LastTime = LastTime;
        }

        public int getFirstChapterId() {
            return FirstChapterId;
        }

        public void setFirstChapterId(int FirstChapterId) {
            this.FirstChapterId = FirstChapterId;
        }

        public String getLastChapter() {
            return LastChapter;
        }

        public void setLastChapter(String LastChapter) {
            this.LastChapter = LastChapter;
        }

        public int getLastChapterId() {
            return LastChapterId;
        }

        public void setLastChapterId(int LastChapterId) {
            this.LastChapterId = LastChapterId;
        }

        public String getBookStatus() {
            return BookStatus;
        }

        public void setBookStatus(String BookStatus) {
            this.BookStatus = BookStatus;
        }

        public BookVoteBean getBookVote() {
            return BookVote;
        }

        public void setBookVote(BookVoteBean BookVote) {
            this.BookVote = BookVote;
        }

        public List<SameUserBooksBean> getSameUserBooks() {
            return SameUserBooks;
        }

        public void setSameUserBooks(List<SameUserBooksBean> SameUserBooks) {
            this.SameUserBooks = SameUserBooks;
        }

        public List<SameCategoryBooksBean> getSameCategoryBooks() {
            return SameCategoryBooks;
        }

        public void setSameCategoryBooks(List<SameCategoryBooksBean> SameCategoryBooks) {
            this.SameCategoryBooks = SameCategoryBooks;
        }

        public static class BookVoteBean {
            /**
             * BookId : 248872
             * TotalScore : 684
             * VoterCount : 73
             * Score : 9.4
             */

            private int BookId;
            private int TotalScore;
            private int VoterCount;
            private double Score;

            public int getBookId() {
                return BookId;
            }

            public void setBookId(int BookId) {
                this.BookId = BookId;
            }

            public int getTotalScore() {
                return TotalScore;
            }

            public void setTotalScore(int TotalScore) {
                this.TotalScore = TotalScore;
            }

            public int getVoterCount() {
                return VoterCount;
            }

            public void setVoterCount(int VoterCount) {
                this.VoterCount = VoterCount;
            }

            public double getScore() {
                return Score;
            }

            public void setScore(double Score) {
                this.Score = Score;
            }
        }

        public static class SameUserBooksBean {
            /**
             * Id : 2387
             * Name : 奥术神座
             * Author : 爱潜水的乌贼
             * Img : aoshushenzuo.jpg
             * LastChapterId : 5167945
             * LastChapter : 新书《诡秘之主》
             * Score : 0.0
             */

            private int Id;
            private String Name;
            private String Author;
            private String Img;
            private int LastChapterId;
            private String LastChapter;
            private double Score;

            public int getId() {
                return Id;
            }

            public void setId(int Id) {
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

            public int getLastChapterId() {
                return LastChapterId;
            }

            public void setLastChapterId(int LastChapterId) {
                this.LastChapterId = LastChapterId;
            }

            public String getLastChapter() {
                return LastChapter;
            }

            public void setLastChapter(String LastChapter) {
                this.LastChapter = LastChapter;
            }

            public double getScore() {
                return Score;
            }

            public void setScore(double Score) {
                this.Score = Score;
            }
        }

        public static class SameCategoryBooksBean {
            /**
             * Id : 32
             * Name : 大道争锋
             * Img : 32.jpg
             * Score : 0.0
             */

            private int Id;
            private String Name;
            private String Img;
            private double Score;

            public int getId() {
                return Id;
            }

            public void setId(int Id) {
                this.Id = Id;
            }

            public String getName() {
                return Name;
            }

            public void setName(String Name) {
                this.Name = Name;
            }

            public String getImg() {
                return Img;
            }

            public void setImg(String Img) {
                this.Img = Img;
            }

            public double getScore() {
                return Score;
            }

            public void setScore(double Score) {
                this.Score = Score;
            }
        }
    }
    public CollBookBean getCollBookBean(){
        if (collBookBean == null){
            collBookBean = createCollBookBean();
        }
        return collBookBean;
    }

    public CollBookBean createCollBookBean(){
        CollBookBean bean = new CollBookBean();
        bean.set_id(data.getId()+"");
        bean.setTitle(data.getName());
        bean.setAuthor(data.getAuthor());
        bean.setShortIntro(data.getDesc());
        bean.setCover(data.getImg());
        bean.setBookStatus(data.getBookStatus());
//        bean.setLatelyFollower(getLatelyFollower());
//        bean.setRetentionRatio(Double.parseDouble(getRetentionRatio()));
        bean.setUpdated(data.getLastTime());
        bean.setChaptersCount(100);
        bean.setLastChapter(data.getLastChapter());
        return bean;
    }
}
