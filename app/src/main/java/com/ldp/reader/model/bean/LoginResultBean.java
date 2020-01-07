package com.ldp.reader.model.bean;

public class LoginResultBean {

    /**
     * status : true
     * message : 登录成功
     * code : 200
     * data : eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VybmFtZSI6IuadjuS4nOW5syIsImV4cCI6MTc5NDAwMjA2MCwiaWF0IjoxNTc3OTczMjYwfQ.vpK5l1sQ-wyWBpbf16_PImRv6n6huzgDi9XnHiXqHss
     */

    private boolean status;
    private String message;
    private int code;
    private String data;

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

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
