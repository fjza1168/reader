package com.ldp.reader.model.bean.packages;

import com.ldp.reader.model.bean.BaseBean;
import com.ldp.reader.model.bean.BookTagBean;

import java.util.List;

/**
 * Created by ldp on 17-5-1.
 */

public class BookTagPackage extends BaseBean {

    private List<BookTagBean> data;

    public List<BookTagBean> getData() {
        return data;
    }

    public void setData(List<BookTagBean> data) {
        this.data = data;
    }


}
