package com.ldp.reader.ui.fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ldp.reader.R;
import com.ldp.reader.model.bean.CommentBean;
import com.ldp.reader.model.bean.HelpsDetailBean;
import com.ldp.reader.presenter.HelpsDetailPresenter;
import com.ldp.reader.presenter.contract.HelpsDetailContract;
import com.ldp.reader.ui.adapter.CommentAdapter;
import com.ldp.reader.ui.adapter.GodCommentAdapter;
import com.ldp.reader.ui.base.BaseMVPFragment;
import com.ldp.reader.utils.Constant;
import com.ldp.reader.utils.StringUtils;
import com.ldp.reader.widget.BookTextView;
import com.ldp.reader.widget.RefreshLayout;
import com.ldp.reader.widget.adapter.WholeAdapter;
import com.ldp.reader.widget.itemdecoration.DividerItemDecoration;
import com.ldp.reader.widget.transform.CircleTransform;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by ldp on 17-4-30.
 */

public class HelpsDetailFragment extends BaseMVPFragment<HelpsDetailContract.Presenter> implements HelpsDetailContract.View{
    private static final String TAG = "HelpsDetailFragment";
    private static final String EXTRA_DETAIL_ID = "extra_detail_id";

    @BindView(R.id.refresh_layout)
    RefreshLayout mRefreshLayout;
    @BindView(R.id.refresh_rv_content)
    RecyclerView mRvContent;
    /***********************************/
    private CommentAdapter mCommentAdapter;
    private DetailHeader mDetailHeader;
    /***********params****************/
    private String mDetailId;
    private int start = 0;
    private int limit = 30;

    public static Fragment newInstance(String detailId){
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_DETAIL_ID,detailId);
        Fragment fragment = new HelpsDetailFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getContentId() {
        return R.layout.fragment_refresh_list;
    }

    @Override
    protected HelpsDetailContract.Presenter bindPresenter() {
        return new HelpsDetailPresenter();
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        if (savedInstanceState != null){
            mDetailId = savedInstanceState.getString(EXTRA_DETAIL_ID);
        }else {
            mDetailId = getArguments().getString(EXTRA_DETAIL_ID);
        }
    }

    @Override
    protected void initWidget(Bundle savedInstanceState) {
        super.initWidget(savedInstanceState);
        initRecyclerView();
    }

    private void initRecyclerView(){

        mCommentAdapter = new CommentAdapter(getContext(),new WholeAdapter.Options());
        mDetailHeader = new DetailHeader();
        mCommentAdapter.addHeaderView(mDetailHeader);

        mRvContent.setLayoutManager(new LinearLayoutManager(getContext()));
        mRvContent.addItemDecoration(new DividerItemDecoration(getContext()));
        mRvContent.setAdapter(mCommentAdapter);
    }

    @Override
    protected void initClick() {
        super.initClick();
        mCommentAdapter.setOnLoadMoreListener(
                () -> mPresenter.loadComment(mDetailId, start, limit)
        );
    }

    @Override
    protected void processLogic() {
        super.processLogic();
        //???????????????
        mRefreshLayout.showLoading();
        mPresenter.refreshHelpsDetail(mDetailId,start,limit);
    }

    @Override
    public void finishRefresh(HelpsDetailBean helpsDetail,
                              List<CommentBean> bestComments,
                              List<CommentBean> comments) {
        //??????
        start = comments.size();
        mDetailHeader.setCommentDetail(helpsDetail);
        mDetailHeader.setGodCommentList(bestComments);
        mCommentAdapter.refreshItems(comments);
    }

    @Override
    public void finishLoad(List<CommentBean> comments) {
        start += comments.size();
        mCommentAdapter.addItems(comments);
    }

    @Override
    public void showError() {
        mRefreshLayout.showError();
    }

    @Override
    public void showLoadError() {
        mCommentAdapter.showLoadError();
    }

    @Override
    public void complete() {
        mRefreshLayout.showFinish();
    }

    /***********************************************************************/
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(EXTRA_DETAIL_ID, mDetailId);
    }

    class DetailHeader implements WholeAdapter.ItemView{
        @BindView(R.id.disc_detail_iv_portrait)
        ImageView ivPortrait;
        @BindView(R.id.disc_detail_tv_name)
        TextView tvName;
        @BindView(R.id.disc_detail_tv_time)
        TextView tvTime;
        @BindView(R.id.disc_detail_tv_title)
        TextView tvTitle;
        @BindView(R.id.disc_detail_btv_content)
        BookTextView btvContent;
        @BindView(R.id.disc_detail_tv_best_comment)
        TextView tvBestComment;
        @BindView(R.id.disc_detail_rv_best_comments)
        RecyclerView rvBestComments;
        @BindView(R.id.disc_detail_tv_comment_count)
        TextView tvCommentCount;

        GodCommentAdapter godCommentAdapter;
        HelpsDetailBean helpsDetailBean;
        List<CommentBean> godCommentList;
        Unbinder detailUnbinder = null;
        @Override
        public View onCreateView(ViewGroup parent) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.header_disc_detail,parent,false);
            return view;
        }

        @Override
        public void onBindView(View view) {
            if (detailUnbinder == null){
                detailUnbinder = ButterKnife.bind(this,view);
            }
            //??????????????????????????????
            if (helpsDetailBean == null || godCommentList == null){
                return;
            }
            //??????
            Glide.with(getContext())
                    .load(Constant.IMG_BASE_URL+ helpsDetailBean.getAuthor().getAvatar())
                    .placeholder(R.drawable.ic_loadding)
                    .error(R.drawable.ic_load_error)
                    .transform(new CircleTransform())
                    .into(ivPortrait);
            //??????
            tvName.setText(helpsDetailBean.getAuthor().getNickname());
            //??????
            tvTime.setText(StringUtils.dateConvert(helpsDetailBean.getCreated(),Constant.FORMAT_BOOK_DATE));
            //??????
            tvTitle.setText(helpsDetailBean.getTitle());
            //??????
            btvContent.setText(helpsDetailBean.getContent());
            //???????????????????????????
            btvContent.setOnBookClickListener(
                    bookName -> {
                        Log.d(TAG, "onBindView: "+bookName);
                    }
            );
            //???????????????
            if (godCommentList.isEmpty()) {
                tvBestComment.setVisibility(View.GONE);
                rvBestComments.setVisibility(View.GONE);
            }
            else {
                tvBestComment.setVisibility(View.VISIBLE);
                rvBestComments.setVisibility(View.VISIBLE);
                //?????????RecyclerView
                initRecyclerView();
                godCommentAdapter.refreshItems(godCommentList);
            }

            if (mCommentAdapter.getItems().isEmpty()){
                tvCommentCount.setText(getResources().getString(R.string.nb_comment_empty_comment));
            }
            else {
                CommentBean firstComment = mCommentAdapter.getItems().get(0);
                //?????????
                tvCommentCount.setText(getResources()
                        .getString(R.string.nb_comment_comment_count,firstComment.getFloor()));
            }
        }

        private void initRecyclerView(){
            if (godCommentAdapter != null) return;
            godCommentAdapter = new GodCommentAdapter();
            rvBestComments.setLayoutManager(new LinearLayoutManager(getContext()));
            rvBestComments.addItemDecoration(new DividerItemDecoration(getContext()));
            rvBestComments.setAdapter(godCommentAdapter);
        }

        public void setCommentDetail(HelpsDetailBean bean){
            helpsDetailBean = bean;
        }

        public void setGodCommentList(List<CommentBean> beans){
            godCommentList = beans;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mDetailHeader.detailUnbinder != null){
            mDetailHeader.detailUnbinder.unbind();
        }
    }
}


