package com.ldp.reader.model.bean;

import java.util.List;

/**
 * Created by ldp on 17-5-4.
 */

public class SmsLoginBean {

  String phoneNumber;
  String smsCode;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getSmsCode() {
        return smsCode;
    }

    public void setSmsCode(String smsCode) {
        this.smsCode = smsCode;
    }
}
