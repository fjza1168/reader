package com.ldp.reader.presenter;

import android.text.TextUtils;
import android.util.Log;

import com.ldp.reader.RxBus;
import com.ldp.reader.event.BookSyncEvent;
import com.ldp.reader.model.bean.DirectLoginResultBean;
import com.ldp.reader.model.bean.LoginResultBean;
import com.ldp.reader.model.bean.SmsLoginBean;
import com.ldp.reader.model.remote.RemoteRepository;
import com.ldp.reader.presenter.contract.LoginContract;
import com.ldp.reader.ui.base.RxPresenter;
import com.ldp.reader.utils.RxUtils;
import com.ldp.reader.utils.SharedPreUtils;
import com.mob.pushsdk.MobPush;
import com.mob.pushsdk.MobPushCallback;
import com.mob.secverify.OperationCallback;
import com.mob.secverify.SecVerify;
import com.mob.secverify.VerifyCallback;
import com.mob.secverify.common.exception.VerifyException;
import com.mob.secverify.datatype.VerifyResult;



import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by ldp on 17-6-2.
 */

public class LoginPresenter extends RxPresenter<LoginContract.View>
        implements LoginContract.Presenter {
    private static final String TAG = LoginPresenter.class.getSimpleName();
    private String registrationId;
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
        Log.d(TAG, "preDirectLogin: registrationId");
        registrationId = SharedPreUtils.getInstance().getString("registrationId");
        Log.d(TAG, "onCallback: registrationId  " + registrationId);
        if (TextUtils.isEmpty(registrationId)){
            MobPush.getRegistrationId(new MobPushCallback<String>() {
                @Override
                public void onCallback(String s) {
                    Log.d(TAG, "onCallback: registrationId  " + s);
                    registrationId = s;
                    SharedPreUtils.getInstance().putString("registrationId",registrationId);
                }
            });
        }
        //???????????????????????????????????????????????????????????????????????????????????????
        SecVerify.preVerify(new OperationCallback<Void>() {
            @Override
            public void onComplete(Void data) {
                Log.d(TAG, "onComplete: " + data);
                //TODO?????????????????????
            }
            @Override
            public void onFailure(VerifyException e) {
                Log.d(TAG, "onFailure: " + e);
                //TODO?????????????????????
            }
        });
    }

    @Override
    public void smsLogin(String phoneNumber, String smsCode, String registrationId){
       Disposable disposable = RemoteRepository.getInstance().smsLogin( phoneNumber,  smsCode,  registrationId)
                .compose(RxUtils::toSimpleSingle)
               .subscribe(new Consumer<SmsLoginBean>() {
                   @Override
                   public void accept(SmsLoginBean smsLoginBean) throws Exception {
                       mView.finishSmsLogin(smsLoginBean);
                   }
               }, Throwable::printStackTrace);
       addDisposable(disposable);

    }

    @Override
    public void directLogin() {
        Log.d(TAG, "directLogin: ");
        registrationId = SharedPreUtils.getInstance().getString("registrationId");
        SecVerify.verify(new VerifyCallback() {
            @Override
            public void onOtherLogin() {
                // ????????????????????????????????????????????????????????????
            }
            @Override
            public void onUserCanceled() {
                // ??????????????????????????????????????????????????????????????????????????????????????????
            }
            @Override
            public void onComplete(VerifyResult data) {
                Log.e(TAG, "onComplete: ");
                // ???????????????????????????token????????????????????????????????????????????????????????????????????????????????????????????????????????????
                Log.e(TAG, "onComplete: "+ data);
                Log.e(TAG, "onComplete: "+ data.getToken());
                Log.e(TAG, "onComplete: registrationId " + registrationId);

                Disposable disposable =  RemoteRepository.getInstance().userDirectLogin(data,registrationId)
                        .compose(RxUtils::toSimpleSingle)
                        .subscribe(new Consumer<DirectLoginResultBean>() {
                            @Override
                            public void accept(DirectLoginResultBean directLoginResultBean) throws Exception {
                                mView.finishDirectLogin(directLoginResultBean);
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {

                                Log.e(TAG, "accept: " +throwable.getMessage() + throwable.getCause());
                                mView.showError();
                            }
                        });
               addDisposable(disposable);

            }
            @Override
            public void onFailure(VerifyException e) {
                Log.d(TAG, "onFailure: " +e.toString());
                //TODO?????????????????????
            }
        });
    }


}
