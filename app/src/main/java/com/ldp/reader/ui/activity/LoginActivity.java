package com.ldp.reader.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ldp.reader.R;
import com.ldp.reader.RxBus;
import com.ldp.reader.event.BookSyncEvent;
import com.ldp.reader.model.bean.DirectLoginResultBean;
import com.ldp.reader.model.bean.LoginResultBean;
import com.ldp.reader.model.bean.SmsLoginBean;
import com.ldp.reader.presenter.LoginPresenter;
import com.ldp.reader.presenter.contract.LoginContract;
import com.ldp.reader.ui.base.BaseMVPActivity;
import com.ldp.reader.utils.SharedPreUtils;
import com.ldp.reader.utils.ToastUtils;
import com.mob.pushsdk.MobPush;
import com.mob.pushsdk.MobPushCallback;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.gui.RegisterPage;

/**
 * Created by ldp on 17-4-24.
 */

public class LoginActivity extends BaseMVPActivity<LoginContract.Presenter>
        implements LoginContract.View {
    private static final String TAG = LoginActivity.class.getSimpleName();
    @BindView(R.id.et_user_phone)
    EditText etUserPhone;
    @BindView(R.id.et_sms_code_input)
    EditText etSmsCodeInput;
    @BindView(R.id.btn_user_login)
    Button btnUserLogin;
    @BindView(R.id.iv_login_back)
    ImageView ivLoginBack;
    @BindView(R.id.ll_user_not_login)
    LinearLayout llUserNotLogin;
    @BindView(R.id.ll_user_login)
    LinearLayout llUserLogin;
    @BindView(R.id.iv_user_logo)
    ImageView ivUserLogo;
    @BindView(R.id.tv_user_name)
    TextView tvUserName;
    @BindView(R.id.btn_direct_login)
    Button btnDirectLogin;
    @BindView(R.id.btn_user_logout)
    Button btnUserLogout;
    @BindView(R.id.btn_get_sms_code)
    Button btnGetSmsCode;



    private String userName;

    @Override
    protected int getContentId() {
        return R.layout.activity_login;
    }

    @Override
    protected LoginContract.Presenter bindPresenter() {
        return new LoginPresenter();
    }

    @Override
    protected void initWidget() {
        super.initWidget();

    }

    private void setUpAdapter() {

    }

    @Override
    protected void initClick() {
        super.initClick();
    }


    private void toggleKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @Override
    protected void processLogic() {
        super.processLogic();
        if (TextUtils.isEmpty(SharedPreUtils.getInstance().getString("token"))) {
            Log.d(TAG, "processLogic: token??? ");
            llUserNotLogin.setVisibility(View.VISIBLE);
            llUserLogin.setVisibility(View.GONE);
            mPresenter.preDirectLogin();
        } else {
            Log.d(TAG, "processLogic: token " + SharedPreUtils.getInstance().getString("token"));
            llUserNotLogin.setVisibility(View.GONE);
            llUserLogin.setVisibility(View.VISIBLE);
            tvUserName.setText(SharedPreUtils.getInstance().getString("userName"));
        }
        getRegId();


        SMSSDK.registerEventHandler(new EventHandler(){
            @Override
            public void afterEvent(int i, int i1, Object o) {
                if (i == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE && i1 ==  SMSSDK.RESULT_COMPLETE) {
                    runOnUiThread(() -> {
                         mPresenter.smsLogin(phoneNumber,smsCode ,registrationId );
                    });

                }
                super.afterEvent(i, i1, o);
            }
        });
    }


    @Override
    public void finishLogin(LoginResultBean loginResultBean) {
        if (200 == loginResultBean.getCode()) {
            SharedPreUtils.getInstance().putString("loginType", "password");
            SharedPreUtils.getInstance().putString("token", loginResultBean.getData());
            SharedPreUtils.getInstance().putString("userName", userName);
            ToastUtils.show("????????????");
            finish();
        } else {
            ToastUtils.show(loginResultBean.getMessage());
        }
    }

    @Override
    public void finishDirectLogin(DirectLoginResultBean loginResultBean) {
         if(200 ==loginResultBean.getStatus()){
             SharedPreUtils.getInstance().putString("loginType", "telecom");
             SharedPreUtils.getInstance().putString("token", loginResultBean.getRes().getMobileToken());
            SharedPreUtils.getInstance().putString("userName", loginResultBean.getRes().getPhone());
            ToastUtils.show("????????????");
             RxBus.getInstance().post(new BookSyncEvent());

             finish();
        }else {
             ToastUtils.show("????????????" + loginResultBean.getError());
         }
    }

    @Override
    public void finishSmsLogin(SmsLoginBean smsLoginBean) {
        SharedPreUtils.getInstance().putString("loginType", "telecom");
        SharedPreUtils.getInstance().putString("token", smsLoginBean.getSmsCode());
        SharedPreUtils.getInstance().putString("userName", smsLoginBean.getPhoneNumber());
        ToastUtils.show("????????????");
        RxBus.getInstance().post(new BookSyncEvent());

        finish();
    }

    @Override
    public void showError() {
        ToastUtils.show("????????????");

    }

    @Override
    public void complete() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    String phoneNumber = "" ,smsCode = "";
    @OnClick({R.id.iv_login_back, R.id.btn_user_login,R.id.btn_direct_login,R.id.btn_user_logout ,R.id.btn_get_sms_code})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_login_back:
                finish();
                break;
            case R.id.btn_user_login:
//                userName = etUserNameInput.getText().toString();
//                String password = etPasswordInput.getText().toString();
//                if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(password)) {
//                    ToastUtils.show("?????????????????????????????????");
//                    return;
//                }
//                mPresenter.userLogin(userName, password);

                phoneNumber = etUserPhone.getText().toString().trim();
                smsCode = etSmsCodeInput.getText().toString().trim();

                SMSSDK.submitVerificationCode("86" ,phoneNumber,smsCode);

                break;
            case  R.id.btn_get_sms_code:
                SMSSDK.getVerificationCode("86", etUserPhone.getText().toString().trim(), null, null);
                break;
            case R.id.btn_direct_login:
                mPresenter.directLogin();
                break;
            case R.id.btn_user_logout:
                userLogout();
                break;
            default:
                break;
        }
    }

    private void userLogout() {
        llUserNotLogin.setVisibility(View.VISIBLE);
        llUserLogin.setVisibility(View.GONE);
        SharedPreUtils.getInstance().putString("token", "");
        SharedPreUtils.getInstance().putString("userName", "");
    }

    String registrationId = "";
    private void getRegId(){
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
    }


}
