package com.ldp.reader.presenter.contract;

import com.ldp.reader.model.bean.LoginResultBean;
import com.ldp.reader.ui.base.BaseContract;

/**
 * Created by ldp on 17-6-2.
 */

public interface LoginContract extends BaseContract {

    interface View extends BaseView{
        void finishLogin(LoginResultBean loginResultBean);

    }

    interface Presenter extends BasePresenter<View>{
        void userLogin(String userName,String passWord);

    }
}
