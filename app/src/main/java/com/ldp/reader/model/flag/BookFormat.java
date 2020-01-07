package com.ldp.reader.model.flag;

/**
 * Created by ldp on 2018/1/14.
 */

public enum BookFormat {
    TXT("txt"), PDF("pdf"), EPUB("epub"), NB("nb"), NONE("none");

    private String name;

    private BookFormat(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
