package com.ldp.reader.ui.base;

/**
 * Created by ldp on 17-4-25.
 */

public abstract class BaseMVPFragment<T extends BaseContract.BasePresenter> extends BaseFragment implements BaseContract.BaseView{

    protected T mPresenter;

    protected abstract T bindPresenter();

    @Override
    protected void processLogic(){
        mPresenter = bindPresenter();
        mPresenter.attachView(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }
}
