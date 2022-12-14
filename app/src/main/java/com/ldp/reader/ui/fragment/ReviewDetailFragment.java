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
import com.ldp.reader.model.bean.AuthorBean;
import com.ldp.reader.model.bean.ReviewBookBean;
import com.ldp.reader.model.bean.BookHelpfulBean;
import com.ldp.reader.model.bean.CommentBean;
import com.ldp.reader.model.bean.ReviewDetailBean;
import com.ldp.reader.presenter.ReviewDetailPresenter;
import com.ldp.reader.presenter.contract.ReviewDetailContract;
import com.ldp.reader.ui.adapter.CommentAdapter;
import com.ldp.reader.ui.adapter.GodCommentAdapter;
import com.ldp.reader.ui.base.BaseMVPFragment;
import com.ldp.reader.utils.Constant;
import com.ldp.reader.utils.StringUtils;
import com.ldp.reader.widget.BookTextView;
import com.ldp.reader.widget.EasyRatingBar;
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

public class ReviewDetailFragment extends BaseMVPFragment<ReviewDetailContract.Presenter>
        implements ReviewDetailContract.View{
    private static final String TAG = "ReviewDetailFragment";
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
        Fragment fragment = new ReviewDetailFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getContentId() {
        return R.layout.fragment_refresh_list;
    }

    @Override
    protected ReviewDetailContract.Presenter bindPresenter() {
        return new ReviewDetailPresenter();
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
                () ->   mPresenter.loadComment(mDetailId, start, limit)
        );
    }

    @Override
    protected void processLogic() {
        super.processLogic();
        //???????????????
        mRefreshLayout.showLoading();
        mPresenter.refreshReviewDetail(mDetailId,start,limit);
    }

    @Override
    public void finishRefresh(ReviewDetailBean reviewDetail,
                              List<CommentBean> bestComments, List<CommentBean> comments) {
        start = comments.size();
        mDetailHeader.setCommentDetail(reviewDetail);
        mDetailHeader.setGodCommentList(bestComments);
        mCommentAdapter.refreshItems(comments);
    }

    @Override
    public void finishLoad(List<CommentBean> comments) {
        mCommentAdapter.addItems(comments);
        start += comments.size();
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
        @BindView(R.id.review_detail_iv_book_cover)
        ImageView ivBookCover;
        @BindView(R.id.review_detail_tv_book_name)
        TextView tvBookName;
        @BindView(R.id.review_detail_erb_rate)
        EasyRatingBar erbBookRate;
        @BindView(R.id.review_detail_tv_helpful_count)
        TextView tvHelpfulCount;
        @BindView(R.id.review_detail_tv_unhelpful_count)
        TextView tvUnhelpfulCount;

        @BindView(R.id.disc_detail_tv_best_comment)
        TextView tvBestComment;
        @BindView(R.id.disc_detail_rv_best_comments)
        RecyclerView rvBestComments;
        @BindView(R.id.disc_detail_tv_comment_count)
        TextView tvCommentCount;

        GodCommentAdapter godCommentAdapter;
        ReviewDetailBean reviewDetailBean;
        List<CommentBean> godCommentList;
        Unbinder detailUnbinder = null;
        @Override
        public View onCreateView(ViewGroup parent) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.header_disc_review_detail,parent,false);
            return view;
        }

        @Override
        public void onBindView(View view) {
            if (detailUnbinder == null){
                detailUnbinder = ButterKnife.bind(this,view);
            }
            //??????????????????????????????
            if (reviewDetailBean == null || godCommentList == null){
                return;
            }

            AuthorBean authorBean = reviewDetailBean.getAuthor();
            //??????
            Glide.with(getContext())
                    .load(Constant.IMG_BASE_URL+ authorBean.getAvatar())
                    .placeholder(R.drawable.ic_loadding)
                    .error(R.drawable.ic_load_error)
                    .transform(new CircleTransform())
                    .into(ivPortrait);
            //??????
            tvName.setText(authorBean.getNickname());
            //??????
            tvTime.setText(StringUtils.dateConvert(reviewDetailBean.getCreated(),
                    Constant.FORMAT_BOOK_DATE));
            //??????
            tvTitle.setText(reviewDetailBean.getTitle());
            //??????
            btvContent.setText(reviewDetailBean.getContent());
            //???????????????????????????
            btvContent.setOnBookClickListener(
                    bookName -> {
                        Log.d(TAG, "onBindView: "+bookName);
                    }
            );
            ReviewBookBean bookBean = reviewDetailBean.getBook();
            //????????????
            Glide.with(getContext())
                    .load(Constant.IMG_BASE_URL+ bookBean.getCover())
                    .placeholder(R.drawable.ic_book_loading)
                    .error(R.drawable.ic_load_error)
                    .fitCenter()
                    .into(ivBookCover);
            //??????
            tvBookName.setText(bookBean.getTitle());
            //???????????????
            erbBookRate.setRating(reviewDetailBean.getRating());
            //?????????
            BookHelpfulBean bookHelpfulBean = reviewDetailBean.getHelpful();
            //??????
            tvHelpfulCount.setText(bookHelpfulBean.getYes()+"");
            //??????
            tvUnhelpfulCount.setText(bookHelpfulBean.getNo()+"");
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

            //???????????????
            if (mCommentAdapter.getItems().isEmpty()){
                tvCommentCount.setText(getResources().getString(R.string.nb_comment_empty_comment));
            }
            else {
                CommentBean firstComment = mCommentAdapter.getItems().get(0);
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

        public void setCommentDetail(ReviewDetailBean bean){
            reviewDetailBean = bean;
        }

        public void setGodCommentList(List<CommentBean> beans){
            godCommentList = beans;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDetailHeader.detailUnbinder.unbind();
    }
}
