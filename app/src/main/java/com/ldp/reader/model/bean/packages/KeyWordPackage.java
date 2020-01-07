package com.ldp.reader.model.bean.packages;

import com.ldp.reader.model.bean.BaseBean;

import java.util.List;

/**
 * Created by ldp on 17-6-2.
 */

public class KeyWordPackage extends BaseBean {

    private List<String> keywords;

    public List<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }
}
