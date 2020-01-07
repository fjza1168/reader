package com.ldp.reader.presenter.contract;

import com.ldp.reader.model.bean.packages.BillboardPackage;
import com.ldp.reader.ui.base.BaseContract;

/**
 * Created by ldp on 17-4-23.
 */

public interface BillboardContract {

    interface View extends BaseContract.BaseView{
        void finishRefresh(BillboardPackage beans);
    }

    interface Presenter extends BaseContract.BasePresenter<View>{
        void loadBillboardList();
    }
}
