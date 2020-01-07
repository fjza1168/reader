package com.ldp.reader.model.bean;

import java.util.List;

public class SyncBookShelfBean {


    /**
     * status : true
     * message : 同步书架成功
     * code : 200
     * data : {"userId":2,"username":"李东平","userBookList":[{"userId":2,"bookId":51515},{"userId":2,"bookId":515155},{"userId":2,"bookId":5415155},{"userId":2,"bookId":544445515}]}
     */

    private boolean status;
    private String message;
    private int code;
    private DataBean data;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * userId : 2
         * username : 李东平
         * userBookList : [{"userId":2,"bookId":51515},{"userId":2,"bookId":515155},{"userId":2,"bookId":5415155},{"userId":2,"bookId":544445515}]
         */

        private int userId;
        private String username;
        private List<UserBookListBean> userBookList;

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public List<UserBookListBean> getUserBookList() {
            return userBookList;
        }

        public void setUserBookList(List<UserBookListBean> userBookList) {
            this.userBookList = userBookList;
        }

        public static class UserBookListBean {
            /**
             * userId : 2
             * bookId : 51515
             */

            private int userId;
            private int bookId;

            public int getUserId() {
                return userId;
            }

            public void setUserId(int userId) {
                this.userId = userId;
            }

            public int getBookId() {
                return bookId;
            }

            public void setBookId(int bookId) {
                this.bookId = bookId;
            }
        }
    }
}
