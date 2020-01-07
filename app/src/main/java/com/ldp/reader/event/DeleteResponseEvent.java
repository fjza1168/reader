package com.ldp.reader.event;

import com.ldp.reader.model.bean.CollBookBean;

/**
 * Created by ldp on 17-5-27.
 */

public class DeleteResponseEvent {
    public boolean isDelete;
    public CollBookBean collBook;
    public DeleteResponseEvent(boolean isDelete,CollBookBean collBook){
        this.isDelete = isDelete;
        this.collBook = collBook;
    }
}
