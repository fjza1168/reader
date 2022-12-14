package com.ldp.reader.ui.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.ldp.reader.R;
import com.ldp.reader.model.bean.BookDetailBeanInOwn;
import com.ldp.reader.model.bean.BookListBean;
import com.ldp.reader.model.bean.CollBookBean;
import com.ldp.reader.model.bean.HotCommentBean;
import com.ldp.reader.model.local.BookRepository;
import com.ldp.reader.presenter.BookDetailPresenter;
import com.ldp.reader.presenter.contract.BookDetailContract;
import com.ldp.reader.ui.adapter.BookListAdapter;
import com.ldp.reader.ui.adapter.HotCommentAdapter;
import com.ldp.reader.ui.base.BaseMVPActivity;
import com.ldp.reader.utils.ToastUtils;
import com.ldp.reader.widget.RefreshLayout;
import com.ldp.reader.widget.itemdecoration.DividerItemDecoration;

import java.util.List;

import butterknife.BindView;

/**
 * Created by ldp on 17-5-4.
 */

public class BookDetailActivity extends BaseMVPActivity<BookDetailContract.Presenter>
        implements BookDetailContract.View {
    public static final String RESULT_IS_COLLECTED = "result_is_collected";

    private static final String TAG = "BookDetailActivity";
    private static final String EXTRA_BOOK_ID = "extra_book_id";

    private static final int REQUEST_READ = 1;

    @BindView(R.id.refresh_layout)
    RefreshLayout mRefreshLayout;
    @BindView(R.id.book_detail_iv_cover)
    ImageView mIvCover;
    @BindView(R.id.book_detail_tv_title)
    TextView mTvTitle;
    @BindView(R.id.book_detail_tv_author)
    TextView mTvAuthor;
    @BindView(R.id.book_detail_tv_type)
    TextView mTvType;
    @BindView(R.id.book_detail_tv_word_count)
    TextView mTvWordCount;
    @BindView(R.id.book_detail_tv_lately_update)
    TextView mTvLatelyUpdate;
    @BindView(R.id.book_list_tv_chase)
    TextView mTvChase;
    @BindView(R.id.book_list_av_chase)
    LottieAnimationView mAvChase;
    @BindView(R.id.book_list_ll_chase)
    LinearLayout mLlChase;
    @BindView(R.id.book_detail_tv_read)
    TextView mTvRead;
    @BindView(R.id.book_detail_tv_follower_count)
    TextView mTvFollowerCount;
    @BindView(R.id.book_detail_tv_retention)
    TextView mTvRetention;
    @BindView(R.id.book_detail_tv_day_word_count)
    TextView mTvDayWordCount;
    @BindView(R.id.book_detail_tv_brief)
    TextView mTvBrief;
    @BindView(R.id.book_detail_tv_more_comment)
    TextView mTvMoreComment;
    @BindView(R.id.book_detail_rv_hot_comment)
    RecyclerView mRvHotComment;
    @BindView(R.id.book_detail_rv_community)
    RelativeLayout mRvCommunity;
    @BindView(R.id.book_detail_tv_community)
    TextView mTvCommunity;
    @BindView(R.id.book_detail_tv_posts_count)
    TextView mTvPostsCount;
    @BindView(R.id.book_list_tv_recommend_book_list)
    TextView mTvRecommendBookList;
    @BindView(R.id.book_detail_rv_recommend_book_list)
    RecyclerView mRvRecommendBookList;

    /************************************/
    private HotCommentAdapter mHotCommentAdapter;
    private BookListAdapter mBookListAdapter;
    private CollBookBean mCollBookBean;
    private ProgressDialog mProgressDialog;
    /*******************************************/
    private String mBookId;
    private boolean isBriefOpen = false;
    private boolean isCollected = false;

    public static void startActivity(Context context, String bookId) {
        Intent intent = new Intent(context, BookDetailActivity.class);
        intent.putExtra(EXTRA_BOOK_ID, bookId);
        context.startActivity(intent);
    }

    @Override
    protected int getContentId() {
        return R.layout.activity_book_detail;
    }

    @Override
    protected BookDetailContract.Presenter bindPresenter() {
        return new BookDetailPresenter();
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        if (savedInstanceState != null) {
            mBookId = savedInstanceState.getString(EXTRA_BOOK_ID);
        } else {
            mBookId = getIntent().getStringExtra(EXTRA_BOOK_ID);
        }
    }

    @Override
    protected void setUpToolbar(Toolbar toolbar) {
        super.setUpToolbar(toolbar);
        getSupportActionBar().setTitle("????????????");
    }

    @Override
    protected void initClick() {
        super.initClick();

        //????????????TextView
        mTvBrief.setOnClickListener(
                (view) -> {
                    if (isBriefOpen) {
                        mTvBrief.setMaxLines(4);
                        isBriefOpen = false;
                    } else {
                        mTvBrief.setMaxLines(8);
                        isBriefOpen = true;
                    }
                }
        );

//        mTvChase.setOnClickListener(
//                (V) -> {
//                    //????????????
//                    if (isCollected) {
//                        //????????????
//                        BookRepository.getInstance()
//                                .deleteCollBookInRx(mCollBookBean);
//
//                        mTvChase.setText(getResources().getString(R.string.nb_book_detail_chase_update));
//
//                        //????????????
//                        Drawable drawable = getResources().getDrawable(R.drawable.selector_btn_book_list);
//                        mTvChase.setBackground(drawable);
//                        //????????????
//                        mTvChase.setCompoundDrawables(ContextCompat.getDrawable(this, R.drawable.ic_book_list_add), null,
//                                null, null);
//
//                        isCollected = false;
//                    } else {
//                        mPresenter.addToBookShelf(mCollBookBean);
//                        mTvChase.setText(getResources().getString(R.string.nb_book_detail_give_up));
//
//                        //????????????
//                        Drawable drawable = getResources().getDrawable(R.drawable.shape_common_gray_corner);
//                        mTvChase.setBackground(drawable);
//                        //????????????
//                        mTvChase.setCompoundDrawables(ContextCompat.getDrawable(this, R.drawable.ic_book_list_delete), null,
//                                null, null);
//
//                        isCollected = true;
//                    }
//                }
//        );

        mAvChase.setOnClickListener(
                (V) -> {
                    //????????????
                    if (isCollected) {
                        //????????????
                        BookRepository.getInstance()
                                .deleteCollBookInRx(mCollBookBean);


                        mTvChase.setText(getResources().getString(R.string.nb_book_detail_chase_update));
                        Drawable drawable = getResources().getDrawable(R.drawable.selector_btn_book_list);
                        mLlChase.setBackground(drawable);
                        mTvChase.setBackground(drawable);
                        mAvChase.setSpeed(-1);
                        mAvChase.playAnimation();
                        isCollected = false;
                    } else {
                        mPresenter.addToBookShelf(mCollBookBean);
                        mTvChase.setText(getResources().getString(R.string.nb_book_detail_give_up));

                        //????????????
                        Drawable drawable = getResources().getDrawable(R.drawable.shape_common_gray_corner);
                        mLlChase.setBackground(drawable);
                        mTvChase.setBackground(drawable);
                        mAvChase.setSpeed(1);
                        mAvChase.playAnimation();
                        isCollected = true;
                    }
                }
        );

        mTvRead.setOnClickListener(
                (v) -> startActivityForResult(new Intent(this, ReadActivity.class)
                        .putExtra(ReadActivity.EXTRA_IS_COLLECTED, isCollected)
                        .putExtra(ReadActivity.EXTRA_COLL_BOOK, mCollBookBean), REQUEST_READ)
        );


    }

    @Override
    protected void processLogic() {
        super.processLogic();
        mRefreshLayout.showLoading();
        mPresenter.refreshBookDetail(mBookId);
    }
//
//    @Override
//    public void finishRefresh(BookDetailBean bean) {
//        //??????
//        Glide.with(this)
//                .load(Constant.IMG_BASE_URL + bean.getCover())
//                .placeholder(R.drawable.ic_book_loading)
//                .error(R.drawable.ic_load_error)
//                .centerCrop()
//                .into(mIvCover);
//        //??????
//        mTvTitle.setText(bean.getTitle());
//        //??????
//        mTvAuthor.setText(bean.getAuthor());
//        //??????
//        mTvType.setText(bean.getMajorCate());
//
//        //?????????
//        mTvWordCount.setText(getResources().getString(R.string.nb_book_word, bean.getWordCount() / 10000));
//        //????????????
//        mTvLatelyUpdate.setText(StringUtils.dateConvert(bean.getUpdated(), Constant.FORMAT_BOOK_DATE));
//        //????????????
//        mTvFollowerCount.setText(bean.getFollowerCount() + "");
//        //?????????
//        mTvRetention.setText(bean.getRetentionRatio() + "%");
//        //????????????
//        mTvDayWordCount.setText(bean.getSerializeWordCount() + "");
//        //??????
//        mTvBrief.setText(bean.getLongIntro());
//        //??????
//        mTvCommunity.setText(getResources().getString(R.string.nb_book_detail_community, bean.getTitle()));
//        //?????????
//        mTvPostsCount.setText(getResources().getString(R.string.nb_book_detail_posts_count, bean.getPostCount()));
//        mCollBookBean = BookRepository.getInstance().getCollBook(bean.get_id());
//
//        //??????????????????
//        if (mCollBookBean != null) {
//            isCollected = true;
//
//            mTvChase.setText(getResources().getString(R.string.nb_book_detail_give_up));
//            //????????????
//            Drawable drawable = getResources().getDrawable(R.drawable.shape_common_gray_corner);
//            mTvChase.setBackground(drawable);
//            mLlChase.setBackground(drawable);
//            mAvChase.setSpeed(1);
//            mAvChase.playAnimation();
//            //????????????
////            mTvChase.setCompoundDrawables(ContextCompat.getDrawable(this, R.drawable.ic_book_list_delete), null,
////                    null, null);
//            mTvRead.setText("????????????");
//        } else {
//            mCollBookBean = bean.getCollBookBean();
//        }
//    }


    @Override
    public void finishRefresh(BookDetailBeanInOwn bean) {

        //??????
        Glide.with(this)
                .load(bean.getCover())
                .placeholder(R.drawable.ic_book_loading)
                .error(R.drawable.ic_load_error)
                .centerCrop()
                .into(mIvCover);
        //??????
        mTvTitle.setText(bean.getTitle());
        //??????
        mTvAuthor.setText(bean.getAuthor());
        //??????
        mTvType.setVisibility(View.GONE);

        //?????????
            mTvWordCount.setVisibility(View.GONE);

        //????????????
//        mTvLatelyUpdate.setText(StringUtils.dateConvert(bookDetailBean.getLastTime(), Constant.FORMAT_BOOK_DATE));
        mTvLatelyUpdate.setVisibility(View.GONE);

//        //????????????
//        mTvFollowerCount.setText(bean.getFollowerCount() + "");
//        //?????????
//        mTvRetention.setText(bean.getRetentionRatio() + "%");
//        //????????????
//        mTvDayWordCount.setText(bean.getSerializeWordCount() + "");
        //????????????
        mTvFollowerCount.setVisibility(View.GONE);
        //?????????
        mTvRetention.setVisibility(View.GONE);
        //????????????
        mTvDayWordCount.setVisibility(View.GONE);
        //??????
        mTvBrief.setText(bean.getDesc());
        //??????
//        mTvCommunity.setText(getResources().getString(R.string.nb_book_detail_community, bean.getTitle()));
//        //?????????
//        mTvPostsCount.setText(getResources().getString(R.string.nb_book_detail_posts_count, bean.getPostCount()));
        mTvCommunity.setVisibility(View.GONE);
        //?????????
        mTvPostsCount.setVisibility(View.GONE);
        mCollBookBean = BookRepository.getInstance().getCollBook(bean.getBookId()+"");

//        mCollBookBean = BookRepository.getInstance().getCollBook(bean.get_id());

        //??????????????????
        if (mCollBookBean != null) {
            Log.d(TAG, "finishRefresh: " + "mCollBookBean != null");
            isCollected = true;

            mTvChase.setText(getResources().getString(R.string.nb_book_detail_give_up));
            //????????????
            Drawable drawable = getResources().getDrawable(R.drawable.shape_common_gray_corner);
            mTvChase.setBackground(drawable);
            mLlChase.setBackground(drawable);
            mAvChase.setSpeed(1);
            mAvChase.playAnimation();
            //????????????
//            mTvChase.setCompoundDrawables(ContextCompat.getDrawable(this, R.drawable.ic_book_list_delete), null,
//                    null, null);
            mTvRead.setText("????????????");
        } else {
            mCollBookBean = bean.getCollBookBean();
            Log.d(TAG, "finishRefresh: " + "mCollBookBean = bean.getCollBookBean()");

        }
    }


    @Override
    public void finishHotComment(List<HotCommentBean> beans) {
        if (beans.isEmpty()) {
            return;
        }
        mHotCommentAdapter = new HotCommentAdapter();
        mRvHotComment.setLayoutManager(new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                //?????????ScrollView????????????
                return false;
            }
        });
        mRvHotComment.addItemDecoration(new DividerItemDecoration(this));
        mRvHotComment.setAdapter(mHotCommentAdapter);
        mHotCommentAdapter.addItems(beans);
    }

    @Override
    public void finishRecommendBookList(List<BookListBean> beans) {
        if (beans.isEmpty()) {
            mTvRecommendBookList.setVisibility(View.GONE);
            return;
        }
        //??????????????????
        mBookListAdapter = new BookListAdapter();
        mRvRecommendBookList.setLayoutManager(new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                //?????????ScrollView????????????
                return false;
            }
        });
        mRvRecommendBookList.addItemDecoration(new DividerItemDecoration(this));
        mRvRecommendBookList.setAdapter(mBookListAdapter);
        mBookListAdapter.addItems(beans);
    }

    @Override
    public void waitToBookShelf() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setTitle("????????????????????????");
        }
        mProgressDialog.show();
    }

    @Override
    public void errorToBookShelf() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
        ToastUtils.show("????????????????????????????????????");
    }

    @Override
    public void succeedToBookShelf() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
        ToastUtils.show("??????????????????");
    }

    @Override
    public void showError() {
        mRefreshLayout.showError();
    }

    @Override
    public void complete() {
        mRefreshLayout.showFinish();
    }

    /*******************************************************/
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(EXTRA_BOOK_ID, mBookId);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //?????????????????????????????????????????????????????????????????????????????????????????????
        if (requestCode == REQUEST_READ) {
            if (data == null) {
                return;
            }

            isCollected = data.getBooleanExtra(RESULT_IS_COLLECTED, false);

            if (isCollected) {
                mTvChase.setText(getResources().getString(R.string.nb_book_detail_give_up));
                //????????????
                Drawable drawable = getResources().getDrawable(R.drawable.shape_common_gray_corner);
                mTvChase.setBackground(drawable);

                mLlChase.setBackground(drawable);

//                //????????????
//                mTvChase.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(this, R.drawable.ic_book_list_delete), null,
//                        null, null);
                mTvRead.setText("????????????");
            }
        }
    }
}
