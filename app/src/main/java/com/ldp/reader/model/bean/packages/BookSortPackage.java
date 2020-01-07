package com.ldp.reader.model.bean.packages;

import com.ldp.reader.model.bean.BaseBean;
import com.ldp.reader.model.bean.BookSortBean;

import java.util.List;

/**
 * Created by ldp on 17-4-23.
 */

public class BookSortPackage extends BaseBean {

    private List<BookSortBean> male;
    private List<BookSortBean> female;

    public List<BookSortBean> getMale() {
        return male;
    }

    public void setMale(List<BookSortBean> male) {
        this.male = male;
    }

    public List<BookSortBean> getFemale() {
        return female;
    }

    public void setFemale(List<BookSortBean> female) {
        this.female = female;
    }
}
