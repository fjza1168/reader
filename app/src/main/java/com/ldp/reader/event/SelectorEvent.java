package com.ldp.reader.event;

import com.ldp.reader.model.flag.BookDistillate;
import com.ldp.reader.model.flag.BookSort;
import com.ldp.reader.model.flag.BookType;

/**
 * Created by ldp on 17-4-21.
 */

public class SelectorEvent {

    public BookDistillate distillate;

    public BookType type;

    public BookSort sort;

    public SelectorEvent(BookDistillate distillate,
                         BookType type,
                         BookSort sort) {
        this.distillate = distillate;
        this.type = type;
        this.sort = sort;
    }
}
