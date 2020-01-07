package com.ldp.reader.presenter.contract;

import com.ldp.reader.model.bean.BillBookBean;
import com.ldp.reader.ui.base.BaseContract;

import java.util.List;

/**
 * Created by ldp on 17-5-3.
 */

public interface BillBookContract {
    interface View extends BaseContract.BaseView{
        void finishRefresh(List<BillBookBean> beans);
    }

    interface Presenter extends BaseContract.BasePresenter<View>{
        void refreshBookBrief(String billId);
    }
}
