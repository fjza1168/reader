package com.ldp.reader.presenter;

import com.ldp.reader.model.bean.LoginResultBean;
import com.ldp.reader.model.remote.RemoteRepository;
import com.ldp.reader.presenter.contract.LoginContract;
import com.ldp.reader.ui.base.RxPresenter;
import com.ldp.reader.utils.RxUtils;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by ldp on 17-6-2.
 */

public class LoginPresenter extends RxPresenter<LoginContract.View>
        implements LoginContract.Presenter {

    @Override
    public void userLogin(String userName, String passWord) {
      Disposable disposableLogin  =   RemoteRepository.getInstance().userLogin(userName,passWord)
              .compose(RxUtils::toSimpleSingle)
              .subscribe(new Consumer<LoginResultBean>() {
                  @Override
                  public void accept(LoginResultBean loginResultBean) throws Exception {
                      mView.finishLogin(loginResultBean);
                  }
              }, new Consumer<Throwable>() {
                  @Override
                  public void accept(Throwable throwable) throws Exception {
                      mView.showError();
                  }
              });
      addDisposable(disposableLogin);
    }
}
