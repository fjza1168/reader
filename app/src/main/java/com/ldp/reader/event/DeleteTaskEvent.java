package com.ldp.reader.event;

import com.ldp.reader.model.bean.CollBookBean;

/**
 * Created by ldp on 17-5-27.
 */

public class DeleteTaskEvent {
    public CollBookBean collBook;

    public DeleteTaskEvent(CollBookBean collBook){
        this.collBook = collBook;
    }
}
