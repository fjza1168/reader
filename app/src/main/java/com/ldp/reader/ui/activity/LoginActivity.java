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
import com.ldp.reader.model.bean.LoginResultBean;
import com.ldp.reader.presenter.LoginPresenter;
import com.ldp.reader.presenter.contract.LoginContract;
import com.ldp.reader.ui.base.BaseMVPActivity;
import com.ldp.reader.utils.SharedPreUtils;
import com.ldp.reader.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ldp on 17-4-24.
 */

public class LoginActivity extends BaseMVPActivity<LoginContract.Presenter>
        implements LoginContract.View {
    private static final String  TAG  = LoginActivity.class.getSimpleName();
    @BindView(R.id.et_user_name_input)
    EditText etUserNameInput;
    @BindView(R.id.et_password_input)
    EditText etPasswordInput;
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
            Log.d(TAG, "processLogic: token空 ");
            llUserNotLogin.setVisibility(View.VISIBLE);
            llUserLogin.setVisibility(View.GONE);
        } else {
            Log.d(TAG, "processLogic: token "+SharedPreUtils.getInstance().getString("token"));
            llUserNotLogin.setVisibility(View.GONE);
            llUserLogin.setVisibility(View.VISIBLE);
            tvUserName.setText(SharedPreUtils.getInstance().getString("userName"));

        }
    }


    @Override
    public void finishLogin(LoginResultBean loginResultBean) {
        if (200 == loginResultBean.getCode()) {
            SharedPreUtils.getInstance().putString("token", loginResultBean.getData());
            SharedPreUtils.getInstance().putString("userName", userName);
            ToastUtils.show("登录成功");
            finish();
        } else {
            ToastUtils.show(loginResultBean.getMessage());
        }
    }

    @Override
    public void showError() {
        ToastUtils.show("登录失败");

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

    @OnClick({R.id.iv_login_back, R.id.btn_user_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_login_back:
                finish();
                break;
            case R.id.btn_user_login:
                userName = etUserNameInput.getText().toString();
                String password = etPasswordInput.getText().toString();
                if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(password)) {
                    ToastUtils.show("用户名或者密码不能为空");
                    return;
                }
                mPresenter.userLogin(userName, password);
                break;
        }
    }
}
