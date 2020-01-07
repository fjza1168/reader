package com.ldp.reader.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ldp.reader.R;
import com.ldp.reader.model.bean.BillBookBean;
import com.ldp.reader.presenter.BillBookPresenter;
import com.ldp.reader.presenter.contract.BillBookContract;
import com.ldp.reader.ui.activity.BookDetailActivity;
import com.ldp.reader.ui.adapter.BillBookAdapter;
import com.ldp.reader.ui.base.BaseMVPFragment;
import com.ldp.reader.widget.RefreshLayout;
import com.ldp.reader.widget.itemdecoration.DividerItemDecoration;

import java.util.List;

import butterknife.BindView;

/**
 * Created by ldp on 17-5-3.
 */

public class BillBookFragment extends BaseMVPFragment<BillBookContract.Presenter>
        implements BillBookContract.View{
    private static final String EXTRA_BILL_ID = "extra_bill_id";

    /********************/
    @BindView(R.id.refresh_layout)
    RefreshLayout mRefreshLayout;
    @BindView(R.id.refresh_rv_content)
    RecyclerView mRvContent;
    /*******************/
    private BillBookAdapter mBillBookAdapter;
    /*****************/
    private String mBillId;

    public static Fragment newInstance(String billId){
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_BILL_ID,billId);
        Fragment fragment = new BillBookFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getContentId() {
        return R.layout.fragment_refresh_list;
    }

    @Override
    protected BillBookContract.Presenter bindPresenter() {
        return new BillBookPresenter();
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        mBillId = getArguments().getString(EXTRA_BILL_ID);
    }

    @Override
    protected void initClick() {
        super.initClick();
        mBillBookAdapter.setOnItemClickListener(
                (view, pos) -> {
                    String bookId = mBillBookAdapter.getItem(pos).get_id();
                    BookDetailActivity.startActivity(getContext(),bookId);
                }
        );
    }

    @Override
    protected void initWidget(Bundle savedInstanceState) {
        super.initWidget(savedInstanceState);
        setUpAdapter();
    }

    @Override
    protected void processLogic() {
        super.processLogic();
        mRefreshLayout.showLoading();
        mPresenter.refreshBookBrief(mBillId);
    }

    private void setUpAdapter(){
        mRvContent.setLayoutManager(new LinearLayoutManager(getContext()));
        mRvContent.addItemDecoration(new DividerItemDecoration(getContext()));
        mBillBookAdapter = new BillBookAdapter();
        mRvContent.setAdapter(mBillBookAdapter);
    }

    @Override
    public void finishRefresh(List<BillBookBean> beans) {
        mBillBookAdapter.refreshItems(beans);
    }

    @Override
    public void showError() {
        mRefreshLayout.showError();
    }

    @Override
    public void complete() {
        mRefreshLayout.showFinish();
    }
}
