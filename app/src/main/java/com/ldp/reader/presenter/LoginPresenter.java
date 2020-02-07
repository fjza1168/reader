package com.ldp.reader.presenter;

import android.util.Log;

import com.ldp.reader.model.bean.DirectLoginResultBean;
import com.ldp.reader.model.bean.LoginResultBean;
import com.ldp.reader.model.remote.RemoteRepository;
import com.ldp.reader.presenter.contract.LoginContract;
import com.ldp.reader.ui.base.RxPresenter;
import com.ldp.reader.utils.RxUtils;
import com.mob.secverify.OperationCallback;
import com.mob.secverify.SecVerify;
import com.mob.secverify.VerifyCallback;
import com.mob.secverify.datatype.LoginResult;
import com.mob.secverify.datatype.VerifyResult;
import com.mob.secverify.exception.VerifyException;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by ldp on 17-6-2.
 */

public class LoginPresenter extends RxPresenter<LoginContract.View>
        implements LoginContract.Presenter {
    private static final String TAG = LoginPresenter.class.getSimpleName();
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

    @Override
    public void preDirectLogin() {
        //建议提前调用预登录接口，可以加快免密登录过程，提高用户体验
        SecVerify.preVerify(new OperationCallback<Void>() {
            @Override
            public void onComplete(Void data) {
                //TODO处理成功的结果
            }
            @Override
            public void onFailure(VerifyException e) {
                //TODO处理失败的结果
            }
        });
    }

    @Override
    public void directLogin() {
        SecVerify.verify(new VerifyCallback() {
            @Override
            public void onOtherLogin() {
                // 用户点击“其他登录方式”，处理自己的逻辑
            }
            @Override
            public void onUserCanceled() {
                // 用户点击“关闭按钮”或“物理返回键”取消登录，处理自己的逻辑
            }
            @Override
            public void onComplete(VerifyResult data) {
                Log.d(TAG, "onComplete: ");
                // 获取授权码成功，将token信息传给应用服务端，再由应用服务端进行登录验证，此功能需由开发者自行实现
                Log.d(TAG, "onComplete: "+ data);
                Log.d(TAG, "onComplete: "+ data.getToken());
               Disposable disposable =  RemoteRepository.getInstance().userDirectLogin(data)
                        .compose(RxUtils::toSimpleSingle)
                        .subscribe(new Consumer<DirectLoginResultBean>() {
                            @Override
                            public void accept(DirectLoginResultBean directLoginResultBean) throws Exception {
                               mView.finishDirectLogin(directLoginResultBean);
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                Log.e(TAG, "accept: " +throwable.getMessage());
                                mView.showError();
                            }
                        });
               addDisposable(disposable);

            }
            @Override
            public void onFailure(VerifyException e) {
                //TODO处理失败的结果
            }
        });
    }


}
